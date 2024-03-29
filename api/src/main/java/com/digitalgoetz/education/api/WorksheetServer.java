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

		staticFiles.externalLocation("/application/ui");

		options("/*", (request, response) -> {

			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}

			return "OK";
		});

		before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

		get("/health", (req, res) -> {
			res.status(200);
			return "OK";
		});

		get("/worksheet/:operation/:digits", (req, res) -> {
			String operation = req.params("operation");

			Boolean allowNegatives = false;
			if (operation.equals("subtraction")) {
				if (req.queryParams("negatives").toLowerCase().equals("true")) {
					allowNegatives = true;
				}
			}
			int digits = Integer.parseInt(req.params("digits").trim());

			File worksheet = null;
			if (operation.equals("addition")) {
				worksheet = WorksheetGenerator.createWorksheet(OperationType.ADDITION, digits);
			} else if (operation.equals("subtraction")) {
				worksheet = WorksheetGenerator.createWorksheet(OperationType.SUBTRACTION, digits, allowNegatives);
			}

			if (worksheet != null) {
				byte[] bytes = Files.readAllBytes(Paths.get(worksheet.getAbsolutePath()));
				HttpServletResponse raw = res.raw();

				raw.getOutputStream().write(bytes);
				raw.getOutputStream().flush();
				raw.getOutputStream().close();

				return raw;
			} else {
				res.status(500);
				return "Failed";
			}
		});
	}
}
