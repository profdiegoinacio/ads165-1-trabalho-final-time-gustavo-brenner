package com.example.backend.services;

import com.example.backend.Aluno;
import com.example.backend.AlunoRepository;
import com.example.backend.AlunoSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    @Autowired
    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    @Transactional()
    public List<Aluno> listarTodos(){
        return alunoRepository.findAll();
    }

    @Transactional()
    public Aluno buscarPorId(Long id ){
        return alunoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com id: " + id));
    }
    @Transactional
    public Aluno salvar(Aluno aluno){
        return alunoRepository.save(aluno);
    }
    @Transactional
    public Aluno atualizar(Long id , Aluno alunoAtualizado){
        Aluno alunoExistente = buscarPorId(id);
        alunoExistente.setNome(alunoAtualizado.getNome());
        alunoExistente.setIdade(alunoAtualizado.getIdade());

        return alunoRepository.save(alunoExistente);

    }

    @Transactional
    public void deletar(Long id ){
        buscarPorId(id);
        alunoRepository.deleteById(id);
    }

    @Transactional
    public List<Aluno> buscarPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome);
    }
    @Transactional()
    public Page<Aluno> listarComFiltrosEPaginacao(
            String nome, Integer idade, Long turmaId, Pageable pageable) {

        Specification<Aluno> spec = AlunoSpecifications.comFiltros(
                nome, idade, turmaId
        );

        return alunoRepository.findAll(pageable);
    }
}
