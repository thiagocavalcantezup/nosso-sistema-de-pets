package br.com.zup.edu.petmanager.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PetIndexResponseDTO {

    private String nome;
    private String raca;
    private TipoPet tipo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    public PetIndexResponseDTO() {}

    public PetIndexResponseDTO(Pet pet) {
        this.nome = pet.getNome();
        this.raca = pet.getRaca();
        this.tipo = pet.getTipo();
        this.dataNascimento = pet.getDataNascimento();
    }

    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public TipoPet getTipo() {
        return tipo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

}
