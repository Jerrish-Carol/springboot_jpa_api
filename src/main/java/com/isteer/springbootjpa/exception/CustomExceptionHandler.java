package com.isteer.springbootjpa.exception;


import java.util.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.isteer.springbootjpa.response.CustomErrorResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice // responsible to handle exception
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { DetailsNotFoundException.class })
	public ResponseEntity<Object> handleDetailsNotFoundException(DetailsNotFoundException exception) {
		long statusCode = exception.getHttpStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(statusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);

	}

	@Override
	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		List<String> exceptions = new ArrayList<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			exceptions.add(fieldName + " " + message);
		});
		CustomErrorResponse customResponse = new CustomErrorResponse(0, "BAD REQUEST", exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);

	}

	@Override
	@ExceptionHandler(value = { MethodNotAllowedException.class })
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatusCode status,
			WebRequest request) {

		List<String> exceptions = new ArrayList<>();

		exceptions.add(exception.getLocalizedMessage());

		CustomErrorResponse customResponse = new CustomErrorResponse(0, PAGE_NOT_FOUND_LOG_CATEGORY, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
	}

}


	
	
	 
	 
	 