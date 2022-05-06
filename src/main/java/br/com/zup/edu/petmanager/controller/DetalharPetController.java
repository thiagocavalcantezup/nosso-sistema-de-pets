package br.com.zup.edu.petmanager.controller;

import br.com.zup.edu.petmanager.controller.response.PetResponse;
import br.com.zup.edu.petmanager.model.Pet;
import br.com.zup.edu.petmanager.repository.PetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/pets")
public class DetalharPetController {
    private final PetRepository repository;

    public DetalharPetController(PetRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable Long id){
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,"Pet não cadastrado."));

        return ok(new PetResponse(pet));
    }
}
