package br.gov.se.secc.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.se.secc.demo.entity.Departamento;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {

	@Query(nativeQuery = true)
	Slice<Departamento> buscarPorId(Integer codigo);
		
	@Query(nativeQuery = true)
	Slice<Departamento> buscarPorDescricao(String descricao, Pageable pageable);

	@Query(nativeQuery = true)
	Slice<Departamento> buscarTodos(Pageable pageable);

	@Transactional
	@Modifying
	@Query(nativeQuery = true)
	int cadastrar(Integer codigo, String descricao);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true)
	int atualizar(Integer codigo, String descricao);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true)
	int remover(Integer codigo);
	
}