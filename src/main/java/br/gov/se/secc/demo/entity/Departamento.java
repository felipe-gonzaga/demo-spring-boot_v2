package br.gov.se.secc.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Departamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "dep_codigo")
	private Integer codigo;

	@Column(name = "dep_descricao")
	private String descricao;
	
}