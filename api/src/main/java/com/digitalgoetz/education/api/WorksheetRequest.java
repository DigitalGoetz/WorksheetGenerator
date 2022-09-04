package com.digitalgoetz.education.api;

public class WorksheetRequest {

	Integer digits;
	String operation;

	public WorksheetRequest(String digits, String operation) {
		this.digits = Integer.parseInt(digits.trim());
		this.operation = operation;
	}

}
