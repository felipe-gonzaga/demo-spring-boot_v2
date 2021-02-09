package br.gov.se.secc.demo.controller.v1;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.se.secc.demo.entity.dto.DepartamentoDTO;
import br.gov.se.secc.demo.service.DepartamentoService;
import br.gov.se.sefaz.exception.ServiceException;
import br.gov.se.sefaz.util.result.EntidadeResult;

@Validated
@RestController
@RequestMapping("v1/departamento")
public class DepartamentoController {

	@Autowired
	private DepartamentoService departamentoService;
	
	@GetMapping("buscar")
	public ResponseEntity<EntidadeResult> buscar(@RequestParam(required = false) @Min(1) @Max(999999999) Integer codigo,
			@RequestParam(required = false) @Size(min = 3, max = 50) String descricao, @RequestParam(defaultValue = "0") @PositiveOrZero Integer pagina) {
		return departamentoService.buscar(codigo, descricao, pagina);	
	}

	@PostMapping("cadastrar")
	public ResponseEntity<EntidadeResult> cadastrar(@RequestBody @Valid DepartamentoDTO.Request.Cadastro cadastro) throws ServiceException {
		return departamentoService.cadastrar(cadastro);
	}

	@PutMapping("atualizar")
	public ResponseEntity<EntidadeResult> atualizar(@RequestBody @Valid DepartamentoDTO.Request.Atualizacao atualizacao) {
		return departamentoService.atualizar(atualizacao);
	}

	@DeleteMapping("remover")
	public ResponseEntity<EntidadeResult> remover(@RequestParam @NotNull @Min(1) @Max(999999999) Integer codigo) {
		return departamentoService.remover(codigo);
	}

}