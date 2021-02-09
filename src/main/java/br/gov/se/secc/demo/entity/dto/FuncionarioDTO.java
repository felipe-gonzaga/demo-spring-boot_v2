package br.gov.se.secc.demo.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public enum FuncionarioDTO {;
	
    public enum Response {;
    	
        public interface Funcionario {
        	String getCpf();
        	String getNome();
        	LocalDate getDtNascimento();
        	String getDsFuncao();
        	String getDsDepartamento();
        	BigDecimal getVlSalario();
        }
    }

}