package com.digitalgoetz.education.math;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class WorksheetGenerator {
	
	private static final float leading = 20f;
	private static final int fontsize = 15;
	private static final int namesize = 18;
	private static final PDFont font = PDType1Font.COURIER;
	
	PDDocument document;
	PDPage page;
	PDPageContentStream contentStream;
	NumberGenerator numbers;
	
	private boolean noNegatives = true;
	private boolean displayHeader = true;

	public WorksheetGenerator(boolean noNegatives, boolean displayHeader) throws IOException {
		this.document = new PDDocument();
		this.page = new PDPage();
		this.document.addPage(page);
		
		this.numbers = new NumberGenerator(new Date().getTime());
		this.contentStream = new PDPageContentStream(document, page);
		
		this.noNegatives = noNegatives;
		this.displayHeader = displayHeader;
	}
	
	public WorksheetGenerator() throws IOException {
		this.document = new PDDocument();
		this.page = new PDPage();
		this.document.addPage(page);
		
		this.numbers = new NumberGenerator(new Date().getTime());
		this.contentStream = new PDPageContentStream(document, page);
	}

	private void addHeader() throws IOException {
		this.contentStream.beginText();
		this.contentStream.setFont(font, namesize);
		this.contentStream.setLeading(leading);
		this.contentStream.newLineAtOffset(20, 750);
		this.contentStream.showText("Name: ");
		this.contentStream.endText();

		this.contentStream.setNonStrokingColor(Color.BLACK);
		this.contentStream.addRect(20, 735, 575, 2);
		this.contentStream.fill();
	}
	
	private void addProblem(OperationType op, int digits, int leftMargin, int columnDepth) throws IOException{
		if(digits > 6) {
			digits = 6;
		}
		
		String firstNum = this.numbers.getNumber(digits);
		String secondNum = this.numbers.getNumber(digits);
		
		System.out.println(firstNum);
		System.out.println(secondNum);
		
		if(op == OperationType.SUBTRACTION && this.noNegatives) {
			if( Integer.valueOf(firstNum.trim()) < Integer.valueOf(secondNum.trim())) {
				String temp = secondNum;
				secondNum = firstNum;
				firstNum = temp;
			}
		}
		
		
		this.contentStream.beginText();
		this.contentStream.setFont(font, fontsize);
		this.contentStream.setLeading(leading);
		this.contentStream.newLineAtOffset(leftMargin, columnDepth);
		this.contentStream.showText("  " + firstNum);
		this.contentStream.newLine();
		
		
		String operator = "";
		if(op == OperationType.ADDITION) {
			operator = "+ ";
		}else {
			operator = "- ";
		}
		
		this.contentStream.showText(operator + secondNum);
		this.contentStream.endText();
		
		drawLine(operator + secondNum, 1, leftMargin, columnDepth - (int)leading, -2);
		
	}
	
	private File saveFile() throws IOException {
		this.contentStream.close();
		
		File saveFile = File.createTempFile("worksheet-", ".pdf");
		this.document.save(saveFile.getAbsolutePath());
		this.document.close();
		return saveFile;
	}
	
	private void drawLine(String text, float lineWidth, float sx, float sy, float linePosition) throws IOException {

	    float stringWidth = 15 * font.getStringWidth(text) / 1000;
	    float lineEndPoint = sx + stringWidth;

	    this.contentStream.setLineWidth(lineWidth);
	    this.contentStream.moveTo(sx, sy + linePosition);
	    this.contentStream.lineTo(lineEndPoint, sy + linePosition);
	    this.contentStream.stroke();
	}
	
	private void buildColumn(OperationType type, int digits, int leftSpace, int height, int spacing) throws IOException {
		while(height > 75) {
			this.addProblem(type, digits, leftSpace, height);
			height -= spacing;
		}
	}
	
	public static File createWorksheet(OperationType type, int numberOfDigits) throws IOException {
		WorksheetGenerator gen = new WorksheetGenerator();
		
		if(gen.displayHeader) {
			gen.addHeader();	
		}
		
		gen.buildColumn(type, numberOfDigits, 125, 675, 125);
		gen.buildColumn(type, numberOfDigits, 415, 675, 125);
		return gen.saveFile();
	}
	
}
