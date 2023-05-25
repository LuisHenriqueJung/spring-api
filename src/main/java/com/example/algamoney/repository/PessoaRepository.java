package com.example.algamoney.repository;

import com.example.algamoney.model.Pessoa;
import com.example.algamoney.repository.pessoa.PessoaRepositoryQuery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> ,PessoaRepositoryQuery{
    
}
