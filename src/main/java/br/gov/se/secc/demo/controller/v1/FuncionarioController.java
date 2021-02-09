package br.gov.se.secc.demo.controller.v1;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.se.secc.demo.service.FuncionarioService;
import br.gov.se.sefaz.util.result.EntidadeResult;

@Validated
@RestController
@RequestMapping("v1/funcionario")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;
	
	@GetMapping("buscar")
	public ResponseEntity<EntidadeResult> buscar(@RequestParam(required = false) @CPF String cpf, @RequestParam(required = false) @Size(min = 3, max = 50) String nome,
			@RequestParam(required = false) @Min(1) @Max(999999999) Integer codDepto, @RequestParam(defaultValue = "0") @PositiveOrZero Integer pagina) {
		return funcionarioService.buscar(cpf, nome, codDepto, pagina);	
	}

}