package br.com.zup.edu.petmanager.model;

public class PetResponseDTO {

    private String nome;
    private String raca;
    private TipoPet tipo;

    public PetResponseDTO() {}

    public PetResponseDTO(Pet pet) {
        this.nome = pet.getNome();
        this.raca = pet.getRaca();
        this.tipo = pet.getTipo();
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

}
