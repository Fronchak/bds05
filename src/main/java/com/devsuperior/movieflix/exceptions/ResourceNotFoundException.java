package com.devsuperior.movieflix.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String msg) {
		super(msg);
	}
	
	public ResourceNotFoundException(String entity, String id) {
		super(entity + " not found by ID: " + id);
	}
	
	public static String getError() {
		return "Entity not found";
	}
}
