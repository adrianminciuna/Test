package test;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class CheckRefValidator 
implements ConstraintValidator<CheckRef, ReferenceData> {

	@Override
	public void initialize(CheckRef constraintAnnotation) {
		// TODO Auto-generated method stub
//		System.out.println(constraintAnnotation);
	}

	@Override
	public boolean isValid(ReferenceData value, ConstraintValidatorContext context) {
		if(value != null ) {
			return StringUtils.isNotBlank(value.getCode()) && value.getCode().startsWith("db");
		}
		return true;
	}

}
