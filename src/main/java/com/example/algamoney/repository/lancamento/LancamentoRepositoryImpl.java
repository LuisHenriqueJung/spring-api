package com.example.algamoney.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import com.example.algamoney.DTO.LancamentoEstatisticaCategoriaDia;
import com.example.algamoney.DTO.LancamentoEstatisticaCategoriaMes;
import com.example.algamoney.model.Lancamento;
import com.example.algamoney.model.Lancamento_;
import com.example.algamoney.repository.filter.LancamentoFilter;


public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();
				
		if(!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(
					builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(
					builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
		}
		
		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(
					builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

	@Override
	public List<LancamentoEstatisticaCategoriaDia> lancamentosPorDia(LocalDate dia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaCategoriaDia> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaCategoriaDia.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoriaDia.class, root.get(Lancamento_.TIPO),root.get(Lancamento_.DATA_VENCIMENTO),criteriaBuilder.sum(root.get(Lancamento_.VALOR))));
		
		LocalDate primeiroDia = dia.withDayOfMonth(1);
		LocalDate ultimoDia = dia.withDayOfMonth(dia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), 
						primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO),
						ultimoDia));
		System.out.println(primeiroDia + " " + ultimoDia);
		criteriaQuery.groupBy(root.get(Lancamento_.TIPO), root.get(Lancamento_.DATA_VENCIMENTO));
		
		TypedQuery<LancamentoEstatisticaCategoriaDia> typedQuery = manager.createQuery(criteriaQuery);
		
		
		return typedQuery.getResultList();
	}

	@Override
	public List<LancamentoEstatisticaCategoriaMes> lancamentosPorMes(LocalDate mes) {
CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaCategoriaMes> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaCategoriaMes.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoriaMes.class, root.get(Lancamento_.TIPO),root.get(Lancamento_.DATA_VENCIMENTO),criteriaBuilder.sum(root.get(Lancamento_.VALOR))));
		
		LocalDate primeiroDia = mes.withDayOfMonth(1);
		LocalDate ultimoDia = mes.withDayOfMonth(mes.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), 
						primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO),
						ultimoDia));
		criteriaQuery.groupBy(root.get(Lancamento_.TIPO), root.get(Lancamento_.DATA_VENCIMENTO));
		
		TypedQuery<LancamentoEstatisticaCategoriaMes> typedQuery = manager.createQuery(criteriaQuery);
		
		
		return typedQuery.getResultList();
	}
}
