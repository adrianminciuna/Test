package test;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class DummyCheckRefValidator 
implements ConstraintValidator<CheckRef, ReferenceData> {

	@Override
	public void initialize(CheckRef constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(ReferenceData value, ConstraintValidatorContext context) {
		return value == null || StringUtils.isNotBlank(value.getCode());
	}

}
