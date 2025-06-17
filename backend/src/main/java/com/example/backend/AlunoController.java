package com.example.backend;

import com.example.backend.Aluno;
import com.example.backend.PaginatedResponse;
import com.example.backend.services.AlunoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    @Autowired
    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    // Endpoint principal com suporte a filtragem, paginação e ordenação
    @GetMapping
    public ResponseEntity<PaginatedResponse<Aluno>> listarAlunos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer idade,

            @RequestParam(required = false) Long turmaId,
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Aluno> pageAlunos = alunoService.listarComFiltrosEPaginacao(
                nome, idade, turmaId, pageable);

        return ResponseEntity.ok(PaginatedResponse.of(pageAlunos));
    }

    // Endpoint para listar todos os produtos (sem paginação) - pode ficar lento com muitos registros
    @GetMapping("/todos")
    public ResponseEntity<List<Aluno>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(alunoService.buscarPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Aluno> criar(@Valid @RequestBody Aluno aluno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.salvar(aluno));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @Valid @RequestBody Aluno aluno) {
        try {
            return ResponseEntity.ok(alunoService.atualizar(id, aluno));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            Aluno aluno = alunoService.buscarPorId(id);
            alunoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}


    // Endpoints para gerenciar relacionamentos

    // Detalhes do Produto (OneToOne)
