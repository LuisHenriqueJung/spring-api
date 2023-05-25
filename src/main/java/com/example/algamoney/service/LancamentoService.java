package com.example.algamoney.service;

import java.util.Optional;

import javax.management.AttributeNotFoundException;

import com.example.algamoney.model.Lancamento;
import com.example.algamoney.model.Pessoa;
import com.example.algamoney.repository.LancamentoRepository;
import com.example.algamoney.repository.PessoaRepository;
import com.example.algamoney.service.exception.PessoaInexistenteOuInativaException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired 
	private LancamentoRepository lancamentoRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());
		if (pessoa.isEmpty() || pessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(lancamento);
	}
	public Lancamento atualizarLancamento(Lancamento lancamento)  {
		Lancamento lancamentoSalvo = buscarLancamentoSalvo(lancamento.getCodigo());
		/*if(!lancamentoSalvo.getPessoa().equals(lancamento.getPessoa())) {
			throw new EmptyResultDataAccessException(1);
		} 
		*/
		//TODO:Verificar a possibilidade de trocar a pessoa do lancamento
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		
		return lancamentoRepository.save(lancamentoSalvo);
	}
	public Lancamento buscarLancamentoSalvo(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.getById(codigo);
		if(lancamentoSalvo == null || lancamentoSalvo.getPessoa().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		return lancamentoSalvo;
		
	}
	
}
