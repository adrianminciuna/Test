package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.script.ScriptEngineManager;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.ScriptAssert;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import test.CheckRef;
import test.ReferenceData;
import test.ScriptAssertFieldError;

/**
 * Unit test for simple App.
 */
public class AppTest extends Assert {

	@Test
	@Disabled
	public void testGen() throws JsonProcessingException {
		Assignment assignment = new Assignment();
		assignment.name = "Name";
		assignment.description = "Description";
		assignment.sectors = Arrays.asList(new AsignmentSector(10L, new Sector(1L, "sector1")),
				new AsignmentSector(90L, new Sector(2L, "sector2")));
		assignment.cc1 = new Currency(1l, "EUR");
		assignment.cc2 = new Currency(2l, "USD");
		ObjectMapper mapper = new ObjectMapper();
//		mapper.setDefaultVisibility(vis)(feature)
		System.out.println(mapper.writeValueAsString(assignment));
	}

	@ParameterizedTest
	@ValueSource(strings = { /*"case1.json", "case2.json", */"case3.json" })
	public void test(String fileName) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.setSerializationInclusion(Include.NON_NULL);
		Assignment assignment = mapper.readValue(this.getClass().getResourceAsStream(fileName), Assignment.class);
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Assignment>> errors = validator.validate(assignment, Init.class);
		System.out.println("-------check -------" + fileName);
		
		Message root = new Message(null, "Assignment is invalid.");
		
		
		for (ConstraintViolation<Assignment> constraintViolation : errors) {
			Path propertyPath = constraintViolation.getPropertyPath();
			System.out.println(
					propertyPath + ": { message : " + constraintViolation.getMessage() + " }");
			Message target = root;
			for (Path.Node node : propertyPath) {
				Integer listIndex = node.getIndex();
				Object mapKey = node.getKey();
				String fieldName= node.getName();
				if(node.getKind().equals(ElementKind.PROPERTY)) {
					System.out.println(fieldName);
					target = target.findOrCreate(fieldName, listIndex, mapKey);
				}
			}
			target.setMessage(constraintViolation.getMessage());
		}
		
		System.out.println(mapper.writeValueAsString(root));
	}

	public static class Assignment {

		@NotNull(groups = Init.class)
		private String name;

		@NotNull
		private String description;

		@Valid
		@ScriptAssertFieldError(lang = "js", 
		script = "Java.from(_this).reduce( "
				+ "function(a, s) {return a + s.percentage || 0;}"
				+ ",0) == 100",
				groups = Init.class, message = "Sector procents sum should be 100.")
		private List<AsignmentSector> sectors;

		@NotNull
		@CheckRef
		@Valid
		private Currency cc1;

		@NotNull(groups = Init.class)
		@CheckRef(groups = Init.class)
		@Valid
		private Currency cc2;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public List<AsignmentSector> getSectors() {
			return sectors;
		}

		public void setSectors(List<AsignmentSector> sectors) {
			this.sectors = sectors;
		}

		public Currency getCc1() {
			return cc1;
		}

		public void setCc1(Currency cc1) {
			this.cc1 = cc1;
		}

		public Currency getCc2() {
			return cc2;
		}

		public void setCc2(Currency cc2) {
			this.cc2 = cc2;
		}

	}

	public static class Currency implements ReferenceData {
		@NotNull
		private Long id;
		@NotNull(groups = Ref.class)
		private String code;

		public Currency(Long id, String code) {
			super();
			this.id = id;
			this.code = code;
		}

		public Currency() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}

	public static class AsignmentSector {

		public AsignmentSector(Long percentage, Sector code) {
			super();
			this.percentage = percentage;
			this.sector = code;
		}

		public AsignmentSector() {
			super();
			// TODO Auto-generated constructor stub
		}

		@NotNull(groups = Init.class)
		@Min(value = 0, groups = Init.class)
		@Max(value = 100, groups = Init.class)
		public Long percentage;

		@NotNull(groups = Init.class)
		@CheckRef(groups = Init.class)
		@Valid
		private Sector sector;

		public Long getPercentage() {
			return percentage;
		}

		public void setPercentage(Long percentage) {
			this.percentage = percentage;
		}

		public Sector getSector() {
			return sector;
		}

		public void setSector(Sector sector) {
			this.sector = sector;
		}

	}

	public static class Sector implements ReferenceData {

		public Sector() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Sector(Long id, String code) {
			super();
			this.id = id;
			this.code = code;
		}

		@NotNull
		private Long id;
		@NotNull(groups = Ref.class)
		private String code;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}

	interface Init {

	}

	interface Ref {

	}
}
