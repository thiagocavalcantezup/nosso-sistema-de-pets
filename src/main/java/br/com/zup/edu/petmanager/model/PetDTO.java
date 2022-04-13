package br.com.zup.edu.petmanager.model;

import java.time.LocalDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PetDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String raca;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoPet tipo;

    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    public PetDTO() {}

    public PetDTO(@NotBlank String nome, @NotBlank String raca, @NotNull TipoPet tipo,
                  @NotNull @PastOrPresent LocalDate dataNascimento) {
        this.nome = nome;
        this.raca = raca;
        this.tipo = tipo;
        this.dataNascimento = dataNascimento;
    }

    public Pet toModel() {
        return new Pet(nome, raca, tipo, dataNascimento);
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
