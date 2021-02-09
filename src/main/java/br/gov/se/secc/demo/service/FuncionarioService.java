package br.gov.se.secc.demo.service;

import static br.gov.se.sefaz.util.result.ResultUtil.MENSAGEM_SUCESSO;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.gov.se.secc.demo.entity.dto.FuncionarioDTO;
import br.gov.se.secc.demo.repository.FuncionarioRepository;
import br.gov.se.sefaz.util.result.EntidadeResult;
import br.gov.se.sefaz.util.result.ResultUtil;

@Service
public class FuncionarioService {
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private ResultUtil resultUtil;
	
	@Value("${spring.data.web.pageable.default-page-size}")
	private int PAGE_SIZE;

	public ResponseEntity<EntidadeResult> buscar(String cpf, String nome, Integer codDepto, Integer pagina) {
		Slice<FuncionarioDTO.Response.Funcionario> dados = null;
		
		if (cpf != null) {
			dados = funcionarioRepository.buscarPorId(cpf);
		} else if (nome != null) {
			dados = funcionarioRepository.buscarPorNome(nome, PageRequest.of(pagina, PAGE_SIZE));
		} else if (codDepto != null) {
			dados = funcionarioRepository.buscarPorCodDepto(codDepto, PageRequest.of(pagina, PAGE_SIZE));
		} else {
			dados = funcionarioRepository.buscarTodos(PageRequest.of(pagina, PAGE_SIZE));
		}
		
		return resultUtil.resultSucesso(OK, MENSAGEM_SUCESSO, dados);
	}
	
}