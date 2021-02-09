package br.gov.se.secc.demo.service;

import static br.gov.se.sefaz.util.result.ResultUtil.MENSAGEM_SUCESSO;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.gov.se.secc.demo.entity.Departamento;
import br.gov.se.secc.demo.entity.dto.DepartamentoDTO;
import br.gov.se.secc.demo.repository.DepartamentoRepository;
import br.gov.se.sefaz.util.result.EntidadeResult;
import br.gov.se.sefaz.util.result.ResultUtil;

@Service
public class DepartamentoService {
	
	@Autowired
	private DepartamentoRepository departamentoRepository;
	
	@Autowired
	private ResultUtil resultUtil;
	
	@Value("${spring.data.web.pageable.default-page-size}")
	private int PAGE_SIZE;

	public ResponseEntity<EntidadeResult> buscar(Integer codigo, String descricao, Integer pagina) {
		Slice<Departamento> dados = null;
		
		if (codigo != null) {
			dados = departamentoRepository.buscarPorId(codigo);
		} else if (descricao != null) {
			dados = departamentoRepository.buscarPorDescricao(descricao, PageRequest.of(pagina, PAGE_SIZE));
		} else {
			dados = departamentoRepository.buscarTodos(PageRequest.of(pagina, PAGE_SIZE));
		}
		
		return resultUtil.resultSucesso(OK, MENSAGEM_SUCESSO, dados);
	}
	
	public ResponseEntity<EntidadeResult> cadastrar(DepartamentoDTO.Request.Cadastro cadastro) {
		departamentoRepository.cadastrar(cadastro.getCodigo(), cadastro.getDescricao());
		return resultUtil.resultSucesso(CREATED, MENSAGEM_SUCESSO);
	}
	
	public ResponseEntity<EntidadeResult> atualizar(DepartamentoDTO.Request.Atualizacao atualizacao) {
		departamentoRepository.atualizar(atualizacao.getCodigo(), atualizacao.getDescricao());
		return resultUtil.resultSucesso(OK, MENSAGEM_SUCESSO);
	}

	public ResponseEntity<EntidadeResult> remover(Integer codigo) {
		departamentoRepository.remover(codigo);
		return resultUtil.resultSucesso(OK, MENSAGEM_SUCESSO);
	}
	
}