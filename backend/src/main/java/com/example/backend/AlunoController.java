package com.example.backend;

import com.example.backend.IdGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
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
    @GetMapping
    public ResponseEntity<List<Aluno>> buscarAlunos(
        @RequestParam(name = "nome", required = true) String nome,
        @RequestParam(name = "idade", required = true) Integer idade,
        @RequestParam(name="turmas", required = false) List<String> turmas,
        @RequestParam(name = "ordenarPor", defaultValue = "nome") String ordenarPor,

        @RequestParam(name = "ordem", defaultValue = "asc") String ordem,
        @RequestParam(name = "pagina", required = false) Integer pagina,
        @RequestParam(name = "tamanho", required = false) Integer tamanho){

        List<Aluno> alunosFiltrados = filtrarAlunos(nome, idade, turmas);


        if (pagina != null && tamanho != null) {
            return aplicarPaginacao(alunosFiltrados, pagina, tamanho);
        }

        return ResponseEntity.ok(alunosFiltrados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarAlunoPorId(@PathVariable Long id) {
        Aluno aluno = recuperarAlunoPorId(id);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping
    public ResponseEntity<Aluno> criarAluno(@Valid @RequestBody RequestAluno request) {
        Aluno aluno = new Aluno(
                IdGenerator.nextId(Aluno.class),
                request.nome(),
                request.idade(),
                request.turmas()
        );
        alunos.add(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(aluno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerAluno(@PathVariable Long id) {
        Aluno aluno = recuperarAlunoPorId(id);
        alunos.remove(aluno);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable Long id, @Valid @RequestBody RequestAluno request) {
        Aluno aluno = recuperarAlunoPorId(id);
        aluno.setNome(request.nome());
        aluno.setIdade(request.idade());
        aluno.setTurmas(request.turmas());
        return ResponseEntity.ok(aluno);

    }


    private Aluno recuperarAlunoPorId(Long id) {
        return alunos.stream().filter(aluno -> aluno.getId().equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException("Aluno não encontrado com o Id"));
    }

    private List<Aluno> filtrarAlunos(String nome, Integer idade, List<String> turmas) {
        return alunos.stream()
                .filter(p -> nome == null || p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(p -> idade == null || p.getIdade() <= idade)
                .filter(p -> turmas == null || turmas.stream()
                        .anyMatch(turma -> p.getTurmas().stream()
                                .anyMatch(prodTurma -> prodTurma.equalsIgnoreCase(turma))))
                .collect(Collectors.toList());
    }

    private ResponseEntity<List<Aluno>> aplicarPaginacao(List<Aluno> alunos, int pagina, int tamanho) {
        final String HEADER_TOTAL_COUNT = "X-Total-Count";
        final String HEADER_TOTAL_PAGES = "X-Total-Pages";
        final String HEADER_PAGE = "X-Page";
        final String HEADER_PAGE_SIZE = "X-Page-Size";

        int totalRegistros = alunos.size();
        int totalPaginas = (int) Math.ceil((double) totalRegistros / tamanho);
        int inicio = pagina * tamanho;
        int fim = Math.min(inicio + tamanho, totalRegistros);

        List<Aluno> paginaDeAlunos = inicio < totalRegistros
                ? alunos.subList(inicio, fim)
                : List.of();

        return ResponseEntity.ok()
                .header(HEADER_TOTAL_COUNT, String.valueOf(totalRegistros))
                .header(HEADER_TOTAL_PAGES, String.valueOf(totalPaginas))
                .header(HEADER_PAGE, String.valueOf(pagina))
                .header(HEADER_PAGE_SIZE, String.valueOf(tamanho))
                .body(paginaDeAlunos);
    }
}
