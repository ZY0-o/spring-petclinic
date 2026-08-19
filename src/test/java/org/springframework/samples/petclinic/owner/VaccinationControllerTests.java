
package org.springframework.samples.petclinic.owner;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@WebMvcTest(VaccinationController.class)
@DisabledInNativeImage
@DisabledInAotMode
class VaccinationControllerTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository owners;

	@BeforeEach
	void init() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		owner.addPet(pet);
		pet.setId(TEST_PET_ID);
		given(this.owners.findById(TEST_OWNER_ID)).willReturn(Optional.of(owner));
	}

	@Test
	void initNewVaccinationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/vaccinations/new", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVaccinationForm"));
	}

	@Test
	void processNewVaccinationFormSuccess() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/vaccinations/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("name", "Cats Vaccine")
				.param("date", "2013-01-01")
				.param("nextDate", "2014-01-01")
				.param("notes", "Vaccination Notes"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void processNewVaccinationFormHasErrors() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/vaccinations/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("nextDate", "2014-01-01")
				.param("notes", "Vaccination Notes"))
			.andExpect(model().attributeHasErrors("vaccination"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVaccinationForm"));
	}

}
