package br.com.zup.edu.petmanager.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.zup.edu.petmanager.controller.request.PetRequest;
import br.com.zup.edu.petmanager.controller.request.TipoPetRequest;
import br.com.zup.edu.petmanager.model.Pet;
import br.com.zup.edu.petmanager.repository.PetRepository;
import br.com.zup.edu.petmanager.util.MensagemDeErro;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
public class CadastrarPetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetRepository petRepository;

    private String PETS_URI = "/pets";

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
    }

    @Test
    void deveCadastrarUmPetComDadosValidos() throws Exception {
        // cenário (given)
        //
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        PetRequest petRequest = new PetRequest(
            "Totó", "Poodle", TipoPetRequest.CAO, LocalDate.now().minusYears(5)
        );

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = post(PETS_URI).contentType(APPLICATION_JSON)
                                                              .content(payload)
                                                              .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        mockMvc.perform(request)
               .andExpect(status().isCreated())
               .andExpect(redirectedUrlPattern(baseUrl + PETS_URI + "/*"));

        List<Pet> pets = petRepository.findAll();

        assertEquals(1, pets.size());
    }

    @Test
    void naoDeveCadastrarUmPetComDadosInvalidos() throws Exception {
        // cenário (given)
        //
        PetRequest petRequest = new PetRequest(null, null, null, null);

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = post(PETS_URI).contentType(APPLICATION_JSON)
                                                              .content(payload)
                                                              .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        String response = mockMvc.perform(request)
                                 .andExpect(status().isBadRequest())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString(UTF_8);

        MensagemDeErro mensagemDeErro = objectMapper.readValue(response, MensagemDeErro.class);

        List<String> mensagens = mensagemDeErro.getMensagens();

        assertEquals(4, mensagens.size());
        assertThat(
            mensagens,
            containsInAnyOrder(
                "O campo nome não deve estar em branco", "O campo raca não deve estar em branco",
                "O campo tipo não deve ser nulo", "O campo dataNascimento não deve ser nulo"
            )
        );
    }

    @Test
    void naoDeveCadastrarUmPetComDataDeNascimentoQueNaoSejaNoPassado() throws Exception {
        // cenário (given)
        //
        PetRequest petRequest = new PetRequest(
            "Totó", "Poodle", TipoPetRequest.CAO, LocalDate.now()
        );

        String payload = objectMapper.writeValueAsString(petRequest);

        MockHttpServletRequestBuilder request = post(PETS_URI).contentType(APPLICATION_JSON)
                                                              .content(payload)
                                                              .header("Accept-Language", "pt-br");

        // ação (when) e corretude (then)
        //
        String response = mockMvc.perform(request)
                                 .andExpect(status().isBadRequest())
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString(UTF_8);

        MensagemDeErro mensagemDeErro = objectMapper.readValue(response, MensagemDeErro.class);

        List<String> mensagens = mensagemDeErro.getMensagens();

        assertEquals(1, mensagens.size());
        assertThat(
            mensagens, containsInAnyOrder("O campo dataNascimento deve ser uma data passada")
        );
    }

}
