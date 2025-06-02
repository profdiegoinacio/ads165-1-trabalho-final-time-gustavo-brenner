package com.example.backend;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    @Autowired
    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }
    @Transactional(readOnly = true)
    public List<Aluno> listarTodos(){
        return alunoRepository.findAll();
    }

    @Transactional(readOnly = true)
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
}
