package br.com.luzrafaelf.desafio.controller.advice;

import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(BadRequest.class)
	public ResponseEntity<?> handleBadRequestException(BadRequest exception) {
		ErrorDTO error = new ErrorDTO(exception.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		ErrorDTO error = new ErrorDTO(exception.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException exception) {
		ErrorDTO error = new ErrorDTO(exception.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InternalServerError.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException exception) {
		ErrorDTO error = new ErrorDTO(exception.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException exception) {
		ErrorDTO error = new ErrorDTO(exception.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
