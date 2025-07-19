package model;

public class Place {
    private final String nome;
    private final String paese;

    public Place(String nome, String paese) {
        this.nome = nome;
        this.paese = paese;
    }

    public String getNome() {
        return nome;
    }

    public String getPaese() {
        return paese;
    }

    @Override
    public String toString() {
        return "Località: " + nome + ", Paese: " + paese;
    }
}