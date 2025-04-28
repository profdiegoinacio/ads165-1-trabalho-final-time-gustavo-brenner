package com.example.backend;

import com.example.backend.IdGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

record RequestAluno(
        @NotBlank(message = "O nome não pode ser vazio.")
        @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.")
         String nome,

        @NotBlank(message = "A idade não pode ser vazia.")
         Integer idade,


         List<String> turmas
) {


    public RequestAluno(String nome, Integer idade, List<String> turmas) {
        this.nome = nome;
        this.idade = idade;
        this.turmas = turmas != null ? turmas : List.of();
    }
}

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final List<Aluno> alunos = new ArrayList<>();

    @PostConstruct
    public void inicializarAlunos() {
        alunos.add(new Aluno(
               IdGenerator.nextId(Aluno.class),
                "João da Silva",
                14,
                List.of("Matemática 1","Geofrafia 1")

        ));
    }
}
