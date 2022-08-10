package com.digitalgoetz.education.api;

import static spark.Spark.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import com.digitalgoetz.education.math.OperationType;
import com.digitalgoetz.education.math.WorksheetGenerator;

public class WorksheetServer {
	public static void main(String[] args) {

		port(4222);

		get("/hello", (req, res) -> "Hello World");

		get("/worksheet/:operation/:digits", (req, res) -> {
			String operation = req.params("operation");
			int digits = Integer.parseInt(req.params("digits").trim());
			
			System.out.println(operation);
			System.out.println(digits);

			File worksheet = null;
			if (operation.equals("addition")) {
				worksheet = WorksheetGenerator.createWorksheet(OperationType.ADDITION, digits);
			} else if (operation.equals("subtraction")) {
				System.out.println("arewehere?");
				worksheet = WorksheetGenerator.createWorksheet(OperationType.SUBTRACTION, digits);
			}
			
			

			if (worksheet != null) {
				byte[] bytes = Files.readAllBytes(Paths.get(worksheet.getAbsolutePath()));
				HttpServletResponse raw = res.raw();

				raw.getOutputStream().write(bytes);
				raw.getOutputStream().flush();
				raw.getOutputStream().close();

				return raw;
			} else {
				System.out.println("FARK");
				res.status(500);
				return "Failed";
			}
		});
	}
}
