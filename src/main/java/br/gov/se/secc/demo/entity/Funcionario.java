package br.gov.se.secc.demo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Funcionario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "fun_cpf")
	private String cpf;

	@Column(name = "fun_nome")
	private String nome;

	@Column(name = "fun_dtnascimento")
	private LocalDate dtNascimento;

	@Column(name = "fun_endereco")
	private String endereco;

	@Column(name = "fun_cep")
	private String cep;

	@Column(name = "fun_cidade")
	private String cidade;

	@Column(name = "fun_telefone")
	private String telefone;

	@Column(name = "fun_dsfuncao")
	private String dsFuncao;
	
	@Column(name = "fun_vlsalario")
	private BigDecimal vlSalario;
	
	@Column(name = "dep_codigo")
	private Integer codDepto;
	
}