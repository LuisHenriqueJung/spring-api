package com.example.algamoney.repository.lancamento;

import com.example.algamoney.DTO.LancamentoEstatisticaCategoriaDia;
import com.example.algamoney.DTO.LancamentoEstatisticaCategoriaMes;
import com.example.algamoney.model.Lancamento;
import com.example.algamoney.repository.filter.LancamentoFilter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface LancamentoRepositoryQuery {
	
	public List<LancamentoEstatisticaCategoriaDia> lancamentosPorDia(LocalDate dia);
	
	public List<LancamentoEstatisticaCategoriaMes> lancamentosPorMes(LocalDate dia);
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	
}
