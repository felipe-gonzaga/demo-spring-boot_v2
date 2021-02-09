package br.gov.se.secc.demo.entity.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

public enum DepartamentoDTO {;
	
	private interface Codigo {
		@NotNull @Min(1) @Max(999999999)
		Integer getCodigo();
	}
	
	private interface Descricao {
		@NotEmpty @Size(min = 3, max = 50)
		String getDescricao();
	}
	
    public enum Request {;
    	
    	@Data
        private static class Base implements Codigo, Descricao  {
        	Integer codigo;
        	String descricao;
        }

        public static class Cadastro extends Base { }
        
        public static class Atualizacao extends Base { }
    }

}