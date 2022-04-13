package br.com.zup.edu.petmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zup.edu.petmanager.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
