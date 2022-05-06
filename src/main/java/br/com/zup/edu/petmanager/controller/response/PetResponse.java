package br.com.zup.edu.petmanager.controller.response;

import br.com.zup.edu.petmanager.model.Pet;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

public class PetResponse {
    private String nome;

    private String raca;

    private String tipo;

    private Integer idade;


    public PetResponse(Pet pet) {
        this.nome = pet.getNome();
        this.raca = pet.getRaca();
        this.tipo = pet.getTipo().name().toLowerCase(Locale.ROOT);
        this.idade = Period.between(pet.getDataNascimento(), LocalDate.now()).getYears();
    }

    public PetResponse() {
    }

    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getIdade() {
        return idade;
    }
}
