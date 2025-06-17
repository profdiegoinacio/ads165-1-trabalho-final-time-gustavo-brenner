package com.example.backend;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "aluno_turma",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "turma_id")
    )
    private Set<Turma> turmas = new HashSet<>();


    public Aluno() {
    }

    public Aluno(Long id, String nome, Integer idade) {
        this(id, nome, idade, Collections.emptyList());
    }


    public Aluno(Long id, String nome, Integer idade, List<String> turmas) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;


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

    public Set<Turma> getTurma() { return turmas; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setTurmas(Set<Turma> turmas) { this.turmas = turmas; }
}




