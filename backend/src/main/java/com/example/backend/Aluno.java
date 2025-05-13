package com.example.backend;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    @NotBlank(message = "O nome não pode ser vazio.")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "A idade não pode ser vazia.")
    @Column(nullable = false, length = 2)
    private Integer idade;


    private List<String> turmas;


    public Aluno() {
    }

    public Aluno(Long id, String nome, Integer idade) {
        this(id, nome, idade, Collections.emptyList());
    }


    public Aluno(Long id, String nome, Integer idade, List<String> turmas) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.turmas = turmas;

    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public List<String> getTurmas() {
        return turmas;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setTurmas(List<String> turmas) {
        this.turmas = turmas;
    }
}




