package com.digitalgoetz.education.math;

import java.util.Random;

public class NumberGenerator {

	Random random;

	public NumberGenerator(long seed) {
		random = new Random(seed);
	}

	public String getNumber(Integer numDigits, boolean notZero) {

		if (numDigits < 1) {
			numDigits = 1;
		}

		if (numDigits > 6) {
			numDigits = 6;
		}

		int value = random.nextInt(getMaxValue(numDigits));
		if (notZero && value == 0) {
			while (value == 0) {
				value = random.nextInt(getMaxValue(numDigits));
			}
		}

		// Ensure returned Number string is correct length
		String valueString = String.valueOf(value);
		while (valueString.length() < numDigits) {
			valueString = " " + valueString;
		}

		return valueString;
	}

	public String getNumber(Integer numDigits) {
		return getNumber(numDigits, false);
	}

	private Integer getMaxValue(Integer numDigits) {
		String maxValueString = "";
		for (int i = 0; i < numDigits; i++) {
			maxValueString += "9";
		}
		return Integer.valueOf(maxValueString);
	}

}
