package br.com.zup.edu.petmanager.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import br.com.zup.edu.petmanager.controller.response.PetResponse;
import br.com.zup.edu.petmanager.model.Pet;
import br.com.zup.edu.petmanager.model.TipoPet;
import br.com.zup.edu.petmanager.repository.PetRepository;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
public class DetalharPetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
    }

    @Test
    void deveDetalharUmPet() throws Exception {
        // cenário (given)
        //
        Pet pet = new Pet("Bichano", "Siamês", TipoPet.GATO, LocalDate.now().minusYears(2));
        pet = petRepository.save(pet);

        MockHttpServletRequestBuilder request = get("/pets/{id}", pet.getId()).contentType(
            APPLICATION_JSON
        );

        // ação (when) e corretude (then)
        //
        String responseString = mockMvc.perform(request)
                                       .andExpect(status().isOk())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString(UTF_8);

        PetResponse response = objectMapper.readValue(responseString, PetResponse.class);
        Integer idade = Period.between(pet.getDataNascimento(), LocalDate.now()).getYears();
        String tipo = pet.getTipo().name().toLowerCase(Locale.ROOT);

        assertThat(response).extracting("nome", "raca", "tipo", "idade")
                            .contains(pet.getNome(), pet.getRaca(), tipo, idade);
    }

    @Test
    void naoDeveDetalharUmPetQueNaoEstaCadastrado() throws Exception {
        // cenário (given)
        //
        MockHttpServletRequestBuilder request = get("/pets/{id}", Integer.MAX_VALUE).contentType(
            APPLICATION_JSON
        );

        // ação (when) e corretude (then)
        //
        Exception resolvedException = mockMvc.perform(request)
                                             .andExpect(status().isNotFound())
                                             .andReturn()
                                             .getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals(
            "Pet não cadastrado.", ((ResponseStatusException) resolvedException).getReason()
        );
    }

}
