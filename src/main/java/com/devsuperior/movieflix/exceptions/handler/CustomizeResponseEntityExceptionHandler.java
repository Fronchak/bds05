package com.devsuperior.movieflix.exceptions.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.devsuperior.movieflix.exceptions.DatabaseException;
import com.devsuperior.movieflix.exceptions.ExceptionResponse;
import com.devsuperior.movieflix.exceptions.ForbiddenException;
import com.devsuperior.movieflix.exceptions.OAuthCustomErrorResponse;
import com.devsuperior.movieflix.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.exceptions.UnauthorizedException;
import com.devsuperior.movieflix.exceptions.ValidationExceptionResponse;

@RestControllerAdvice
public class CustomizeResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ExceptionResponse response = makeResponse(
				new ExceptionResponse(), e, request, status, ResourceNotFoundException.getError());
		return ResponseEntity.status(status).body(response);		
	}
	
	private ExceptionResponse makeResponse(ExceptionResponse response,
			Exception e, WebRequest request, HttpStatus status, String error) {
		response.setTimestamp(Instant.now());
		response.setStatus(status.value());
		response.setMessage(e.getMessage());
		response.setError(error);
		response.setPath(request.getDescription(false));
		return response;
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ExceptionResponse> handleDatabaseException(DatabaseException e, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ExceptionResponse response = makeResponse(
				new ExceptionResponse(), e, request, status, DatabaseException.getError());
		return ResponseEntity.status(status).body(response);	
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationExceptionResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException e, WebRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationExceptionResponse response = (ValidationExceptionResponse) makeResponse(
				new ValidationExceptionResponse(), e, request, status, "Validation Error");
		
		for(FieldError error : e.getBindingResult().getFieldErrors()) {
			response.addError(error.getField(), error.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<OAuthCustomErrorResponse> handleUnhauthorizedException(UnauthorizedException e, WebRequest request) {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		OAuthCustomErrorResponse response = new OAuthCustomErrorResponse();
		response.setError(UnauthorizedException.getError());
		response.setErrorDescription(e.getMessage());
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<OAuthCustomErrorResponse> handleForbiddenException(ForbiddenException e, WebRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		OAuthCustomErrorResponse response = new OAuthCustomErrorResponse();
		response.setError(ForbiddenException.getError());
		response.setErrorDescription(e.getMessage());
		return ResponseEntity.status(status).body(response);
	}
}
