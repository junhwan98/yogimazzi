package goorm.deepdive.team1.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
	private static final String PHONE_NUMBER_PATTERN = "^010\\d{7,8}$";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && value.matches(PHONE_NUMBER_PATTERN);
	}
}
