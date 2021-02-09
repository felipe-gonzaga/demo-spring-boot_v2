package br.gov.se.secc.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.gov.se.sefaz.util.WebClientUtil;
import br.gov.se.sefaz.util.result.ResultUtil;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public WebClientUtil webClientUtil() {
		return new WebClientUtil();
	}

	@Bean
	public ResultUtil resultUtil() {
		return new ResultUtil();
	}

}