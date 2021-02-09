package br.gov.se.secc.demo.exception;

import static br.gov.se.sefaz.util.result.ResultUtil.MENSAGEM_ERRO;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import java.lang.reflect.UndeclaredThrowableException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.gov.se.sefaz.exception.ServiceException;
import br.gov.se.sefaz.util.result.EntidadeResult;
import br.gov.se.sefaz.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class HandlerException {

	@Autowired
	private ResultUtil resultUtil;

	@ExceptionHandler(AsyncRequestTimeoutException.class)
	public ResponseEntity<EntidadeResult> handlerAsyncRequestTimeout(AsyncRequestTimeoutException arte) {
		log.error("handlerAsyncRequestTimeout", arte);
		return resultUtil.resultErro(SERVICE_UNAVAILABLE, arte, "O tempo limite de resposta do servidor foi excedido. Tente novamente mais tarde.");
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<EntidadeResult> handlerBind(BindException be) {
		log.error("handlerBind", be);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, be, MENSAGEM_ERRO);
	}

	// Captura exceções do RequestParam
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<EntidadeResult> handlerConstraintViolation(ConstraintViolationException cve) {
		StringBuilder msgUsuario = new StringBuilder();
		for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
			msgUsuario.append(violation.getPropertyPath().toString().split("[.]")[1]).append(": ").append(violation.getMessage());
			break;
		}
		return resultUtil.resultErro(BAD_REQUEST, msgUsuario.toString());
	}

	@ExceptionHandler(ConversionNotSupportedException.class)
	public ResponseEntity<EntidadeResult> handlerConversionNotSupported(ConversionNotSupportedException cnse) {
		log.error("handlerConversionNotSupported", cnse);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, cnse, MENSAGEM_ERRO);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<EntidadeResult> handlerDataAccess(DataAccessException dae) {
		return resultUtil.resultErro(BAD_REQUEST, dae.getRootCause().getMessage(), MENSAGEM_ERRO);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<EntidadeResult> handlerDataIntegrityViolation(DataIntegrityViolationException dive) {
		return resultUtil.resultErro(CONFLICT, dive.getRootCause().getMessage(), MENSAGEM_ERRO);
	}

	@ExceptionHandler(DateTimeException.class)
	public ResponseEntity<EntidadeResult> handlerDataTime(DateTimeException dte) {
		System.out.println(dte.getClass());
		return resultUtil.resultErro(BAD_REQUEST, dte.getMessage(), "Não foi possível realizar a formatação");
	}

	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<EntidadeResult> handlerDataTimeParse(DateTimeParseException dtpe) {
		return resultUtil.resultErro(BAD_REQUEST, dtpe.getMessage(), 
				new StringBuilder("Não foi possível realizar o parse do texto ").append(dtpe.getParsedString()).toString());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<EntidadeResult> handlerException(Exception ex) {
		log.error("handlerException", ex);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, ex, MENSAGEM_ERRO);
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public ResponseEntity<EntidadeResult> handlerHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException hmtnae) {
		log.error("handlerHttpMediaTypeNotAcceptable", hmtnae);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, hmtnae, MENSAGEM_ERRO);
	}
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<EntidadeResult> handlerHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException hmtnse) {
		StringBuilder msgUsuario = new StringBuilder("O tipo de mídia ").append(hmtnse.getContentType()).append(" não é suportado. Tipo de mídia suportado: ")
				.append(hmtnse.getSupportedMediaTypes().get(0));
		return resultUtil.resultErro(UNSUPPORTED_MEDIA_TYPE, msgUsuario.toString());
	}

	// Captura exceções do RequestBody
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<EntidadeResult> handlerHttpMessageNotReadable(HttpMessageNotReadableException hmnre, JsonMappingException jme) throws Throwable {
		StringBuilder msgUsuario = new StringBuilder(jme.getPath().get(0).getFieldName()).append(": ");
		
		try {
			throw hmnre.getRootCause();
		} catch (DateTimeException dte) {
			return resultUtil.resultErro(BAD_REQUEST, msgUsuario.append("data inválida").toString());
		} catch (Exception ex) {
			return resultUtil.resultErro(BAD_REQUEST, msgUsuario.append("valor inválido").toString());
		}
	}
	
	@ExceptionHandler(HttpMessageNotWritableException.class)
	public ResponseEntity<EntidadeResult> handlerHttpMessageNotWritable(HttpMessageNotWritableException hmnwe) {
		log.error("handlerHttpMessageNotWritable", hmnwe);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, hmnwe, MENSAGEM_ERRO);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<EntidadeResult> handlerHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException hrmnse) {
		StringBuilder msgUsuario = new StringBuilder("O método ").append(hrmnse.getMethod()).append(" não é suportado para esta solicitação. Método suportado: ")
				.append(hrmnse.getSupportedMethods()[0]);
		return resultUtil.resultErro(METHOD_NOT_ALLOWED, msgUsuario.toString());
	}
	
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<EntidadeResult> handlerJsonMapping(JsonMappingException jme) {
		log.error("handlerJsonMapping", jme);
		return resultUtil.resultErro(BAD_REQUEST, jme.getCause(), MENSAGEM_ERRO);
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<EntidadeResult> handlerJsonProcessing(JsonProcessingException jpe) {
		log.error("handlerJsonProcessing", jpe);
		return resultUtil.resultErro(BAD_REQUEST, jpe.getCause(), MENSAGEM_ERRO);
	}

	 // Captura exceções do RequestBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<EntidadeResult> handlerMethodArgumentNotValid(MethodArgumentNotValidException manve) {
		StringBuilder msgUsuario = new StringBuilder(manve.getBindingResult().getFieldError().getField()).append(": ")
				.append(manve.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
		return resultUtil.resultErro(BAD_REQUEST, msgUsuario.toString());
	}

	// Captura exceções do RequestParam
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<EntidadeResult> handlerMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException matme) throws Throwable {
		StringBuilder msgUsuario = new StringBuilder(matme.getName()).append(": ");
		
		try {
			throw matme.getRootCause();
		} catch (DateTimeException dte) {
			return resultUtil.resultErro(BAD_REQUEST, msgUsuario.append("data inválida").toString());
		} catch (Exception ex) {
			return resultUtil.resultErro(BAD_REQUEST, msgUsuario.append("deve ser do tipo ").append(matme.getRequiredType().getName()).toString());
		}
	}

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<EntidadeResult> handlerMissingPathVariable(MissingPathVariableException mpve) {
		log.error("handlerMissingPathVariable", mpve);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, mpve, MENSAGEM_ERRO);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<EntidadeResult> handlerMissingServletRequestParameter(MissingServletRequestParameterException msrpe) {
		return resultUtil.resultErro(BAD_REQUEST, new StringBuilder(msrpe.getParameterName()).append(": parâmetro não encontrado").toString());
	}
	
	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<EntidadeResult> handlerMissingServletRequestPart(MissingServletRequestPartException msrpe) {
		log.error("handlerMissingServletRequestPart", msrpe);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, msrpe, MENSAGEM_ERRO);
	}
	
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<EntidadeResult> handlerService(ServiceException se) {
		return resultUtil.resultErro(BAD_REQUEST, se.getMessage());
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	public ResponseEntity<EntidadeResult> handlerServletRequestBinding(ServletRequestBindingException srbe) {
		log.error("handlerServletRequestBinding", srbe);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, srbe, MENSAGEM_ERRO);
	}

	@ExceptionHandler(TypeMismatchException.class)
	public ResponseEntity<EntidadeResult> handlerTypeMismatch(TypeMismatchException tme) {
		log.error("handlerTypeMismatch", tme);
		return resultUtil.resultErro(INTERNAL_SERVER_ERROR, tme, MENSAGEM_ERRO);
	}

	@ExceptionHandler(UndeclaredThrowableException.class)
	public ResponseEntity<EntidadeResult> handlerUndeclaredThrowable(UndeclaredThrowableException ute) {
		return resultUtil.resultErro(BAD_REQUEST, ute.getCause(), MENSAGEM_ERRO);
	}

}