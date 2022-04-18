package br.com.zup.edu.petmanager.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.edu.petmanager.model.Pet;
import br.com.zup.edu.petmanager.model.PetDTO;
import br.com.zup.edu.petmanager.model.PetIndexResponseDTO;
import br.com.zup.edu.petmanager.model.PetResponseDTO;
import br.com.zup.edu.petmanager.repository.PetRepository;

@RestController
@RequestMapping(PetController.BASE_URI)
public class PetController {

    public final static String BASE_URI = "/pets";

    private final PetRepository petRepository;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid PetDTO petDTO,
                                       UriComponentsBuilder ucb) {
        Pet pet = petRepository.save(petDTO.toModel());

        URI location = ucb.path(BASE_URI + "/{id}").buildAndExpand(pet.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Pet pet = petRepository.findById(id)
                               .orElseThrow(
                                   () -> new ResponseStatusException(
                                       HttpStatus.NOT_FOUND, "Não existe um pet com o id informado."
                                   )
                               );

        petRepository.delete(pet);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> show(@PathVariable Long id) {
        Pet pet = petRepository.findById(id)
                               .orElseThrow(
                                   () -> new ResponseStatusException(
                                       HttpStatus.NOT_FOUND, "Não existe um pet com o id informado."
                                   )
                               );

        return ResponseEntity.ok(new PetResponseDTO(pet));
    }

    @GetMapping
    public ResponseEntity<?> index(@PageableDefault(size = 2, page = 0, sort = "id", direction = Direction.ASC) Pageable paginacao) {
        Page<Pet> petsPage = petRepository.findAll(paginacao);
        List<PetIndexResponseDTO> petResponses = petsPage.map(PetIndexResponseDTO::new).toList();
        PageImpl<PetIndexResponseDTO> petsPageImpl = new PageImpl<>(
            petResponses, paginacao, petsPage.getTotalElements()
        );

        return ResponseEntity.ok(petsPageImpl);
    }

}
