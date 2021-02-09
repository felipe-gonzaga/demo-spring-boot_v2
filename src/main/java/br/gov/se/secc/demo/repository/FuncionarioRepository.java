package br.gov.se.secc.demo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.se.secc.demo.entity.Funcionario;
import br.gov.se.secc.demo.entity.dto.FuncionarioDTO;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {

	@Query(nativeQuery = true)
	Slice<FuncionarioDTO.Response.Funcionario> buscarPorId(String cpf);
		
	@Query(nativeQuery = true)
	Slice<FuncionarioDTO.Response.Funcionario> buscarPorNome(String nome, Pageable pageable);

	@Query(nativeQuery = true)
	Slice<FuncionarioDTO.Response.Funcionario> buscarPorCodDepto(Integer codDepto, Pageable pageable);

	@Query(nativeQuery = true)
	Slice<FuncionarioDTO.Response.Funcionario> buscarTodos(Pageable pageable);

}