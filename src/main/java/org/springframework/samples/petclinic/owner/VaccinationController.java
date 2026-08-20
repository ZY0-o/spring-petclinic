package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
class VaccinationController {

	private final OwnerRepository owners;

	public VaccinationController(OwnerRepository owners) {
		this.owners = owners;
	}

	@InitBinder("owner")
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "*.id");
	}

	@InitBinder("vaccination")
	public void setVaccinationAllowedFields(WebDataBinder dataBinder) {
		dataBinder.addValidators(new VaccinationValidator());
		dataBinder.setDisallowedFields("id", "*.id");
	}

	@ModelAttribute("vaccination")
	public Vaccination loadPetWithVaccination(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			Map<String, Object> model) {
		Optional<Owner> optionalOwner = owners.findById(ownerId);
		Owner owner = optionalOwner.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));

		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException(
					"Pet with id " + petId + " not found for owner with id " + ownerId + ".");
		}
		model.put("pet", pet);
		model.put("owner", owner);

		Vaccination vaccination = new Vaccination();
		pet.addVaccination(vaccination);
		return vaccination;
	}

	@ModelAttribute("defaultVaccinationDate")
	public LocalDate getDefaultVaccinationDate() {
		return LocalDate.now();
	}

	@GetMapping("owners/{ownerId}/pets/{petId}/vaccinations/new")
	public String initNewVaccinationForm() {
		return "pets/createOrUpdateVaccinationForm";
	}

	@PostMapping("owners/{ownerId}/pets/{petId}/vaccinations/new")
	public String processNewVaccinationForm(@ModelAttribute Owner owner, @PathVariable int petId,
			@Valid Vaccination vaccination, BindingResult result, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "pets/createOrUpdateVaccinationForm";
		}

		owner.addVaccination(petId, vaccination);
		this.owners.save(owner);
		redirectAttributes.addFlashAttribute("message", "Vaccination added successfully.");
		return "redirect:/owners/{ownerId}";
	}

}
