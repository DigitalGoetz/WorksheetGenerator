package com.digitalgoetz.education.math;

import java.util.Random;

public class NumberGenerator {
	
	Random random;
	
	public NumberGenerator(long seed) {
		random = new Random(seed);
	}
	
	public String getNumber(Integer numDigits) {
		String valueString = "";
		
		if(numDigits < 1 ) {
			numDigits = 1;
		}
		
		if( numDigits > 6) {
			numDigits = 6;
		}
		
		for(int i = 0; i < numDigits; i++) {
			int value = random.nextInt(10);
			valueString += Integer.toString(value);
		}
		
		int length = valueString.length();
		for(int index = 0; index < length; index++) {
			
			if( valueString.charAt(index) == '0') {
				StringBuffer string = new StringBuffer(valueString);
		        string.setCharAt(index, ' ');
				valueString = string.toString();
			}else {
				break;
			}
		}
		
		if( valueString.trim().isEmpty()) {
			StringBuffer sb = new StringBuffer();
			int stringLength = numDigits - 1;
			for(int i =0; i < stringLength; i++) {
				sb.append(" ");
			}
			
			sb.append("0");
			valueString = sb.toString();
			System.out.println("Generated:");
			System.out.println(valueString);
		}
		
		return valueString;
	}

}
