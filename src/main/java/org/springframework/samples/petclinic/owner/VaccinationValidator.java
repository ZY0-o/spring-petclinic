package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VaccinationValidator implements Validator {

	private static final String INVALID = "invalid";

	@Override
	public void validate(Object obj, Errors errors) {
		Vaccination vaccination = (Vaccination) obj;
		LocalDate date = vaccination.getDate();
		LocalDate nextDate = vaccination.getNextDate();
		// next date validation
		if (date != null && nextDate != null && nextDate.isBefore(date)) {
			errors.rejectValue("nextDate", INVALID, "Next vaccination date must be after the vaccination date.");
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Vaccination.class.isAssignableFrom(clazz);
	}

}
