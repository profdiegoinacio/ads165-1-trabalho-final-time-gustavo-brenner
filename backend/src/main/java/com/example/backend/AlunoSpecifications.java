package com.example.backend;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AlunoSpecifications {

    public static Specification<Aluno> comFiltros(
            String nome, Integer idade, Long turmaId) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nome != null && !nome.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + nome.toLowerCase() + "%"
                ));
            }

            if (idade != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("idade"), idade
                ));
            }



            if (turmaId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("turma").get("id"), turmaId
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}