package com.example.calculatorapp;

import java.util.ArrayList;

/**
 * Enum representing different types of calculator errors.
 */
enum CalculatorError {
	/**
	 * Math Error – occurs when a mathematical operation is invalid. Includes: 1.
	 * Division by zero (e.g. 5 / 0) 2.Overflow Error – occurs when the calculation
	 * result exceeds the numerical limits
	 */
	MATH_ERROR("Math Error"),
	/**
	 * Syntax Error – occurs when the input expression is not valid. Includes: 1.
	 * Multiple decimal points in a number (e.g. 3.1.4) 2. Invalid operator usage
	 * (e.g. 5 ++ 3, 2*) 3. Expression starts or ends with an operator (e.g. +5, 2+)
	 * 4. Unrecognized characters in input (e.g. letters or symbols like $) 5. Empty
	 * input followed by "="
	 */
	SYNTAX_ERROR("Syntax Error");

	private final String message;

	CalculatorError(String message) {
		this.message = message;
	}

	// Return the message
	public String getMessage() {
		return message;
	}
}

/**
 * CalculatorValidator provides utility methods for validating calculator input
 * and output expressions.
 * 
 */
public class CalculatorValidator {

	/**
	 * Checks if the input expression contains two consecutive operators.
	 *
	 * @param input the full expression as a string
	 * @return true if two operators appear in a row, false otherwise
	 */
	public static boolean hasConsecutiveOperators(String input) {
		for (int i = 1; i < input.length(); i++) {
			if (isOperator(input.charAt(i - 1)) && isOperator(input.charAt(i))) {
				return false;
			}

		}
		return true;

	}

	/**
	 * Validates the operator-to-number ratio. A valid expression should have
	 * exactly one fewer operator than numbers.
	 *
	 * @param numbers   list of parsed numbers
	 * @param operators list of parsed operators
	 * @return true if the ratio is valid, false otherwise
	 */
	public static boolean hasValidOperatorNumberRatio(ArrayList<Double> numbers, ArrayList<Character> operators) {
		return operators.size() == numbers.size() - 1;
	}

	/**
	 * Checks if the result is invalid (either overflowed or NaN).
	 * 
	 * @param result the result to check
	 * @return true if overflow, underflow, or invalid value occurred; false if
	 *         result is valid
	 */
	public static boolean isResultInvalid(double result) {
		return Double.isInfinite(result) || Double.isNaN(result);
	}
	
	
	/**
	 * Checks if a character is an operator.
	 */
	public static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}

}
