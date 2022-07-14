package com.digitalgoetz.education.math;

import java.util.Random;

public class NumberGenerator {
	
	Random random;
	
	public NumberGenerator(long seed) {
		random = new Random(seed);
	}
	
	public String getValue(Integer numDigits) {
		String valueString = "";
		
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
		
		return valueString;
	}

}
