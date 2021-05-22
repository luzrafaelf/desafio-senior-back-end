package br.com.luzrafaelf.desafio.controller.advice;

public class ErrorDTO {

	private String error;

	public ErrorDTO(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

}