package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vaccinations")
public class Vaccination extends BaseEntity {

	@NotBlank
	@Column(name = "vaccine_name")
	private String name;

	@NotNull
	@Column(name = "vaccination_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "next_vaccination_date")
	private LocalDate nextDate;

	private String notes;

	public Vaccination() {
		this.date = LocalDate.now();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getNextDate() {
		return this.nextDate;
	}

	public void setNextDate(LocalDate nextDate) {
		this.nextDate = nextDate;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
