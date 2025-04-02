import java.util.ArrayList;

/**
 * CalculatorLogic handles the core logic of a simple calculator. It supports
 * basic operations (+, -, *, /), sign toggling, answer recall, and delete.
 */
public class CalculatorLogic {
	private StringBuilder currentInput = new StringBuilder(); // Full expression being typed (e.g. "3+5*2")
	private String finalAnswer = "-1"; // Last calculated result
	private ArrayList<Double> numbers = new ArrayList<>();// Parsed numbers
	private ArrayList<Character> operators = new ArrayList<>(); // Parsed operations
	private StringBuilder currentNumber = new StringBuilder(); // currect number being typed
	private boolean afterEquals = false; // Flag to track whether last action was '='

	/**
	 * Processes user input from the UI and routes it to the appropriate logic
	 * handler.
	 *
	 * @param text the button label that was clicked
	 * @return the updated display string
	 */
	public String userInput(String text) {
		switch (text) {
		case "=":
			afterEquals = true;
			return doneInsertInput();
		case "C":
			finalAnswer = "-1";
			clearAll();
			return currentInput.toString();
		case "DEL":
			deleteLastChar();
			return currentInput.toString();
		case "+/-":
			return toggleLastNumberSign();

		case "Ans":
			return appendFinalAnswer();
		default:
			return appendToCurrentInput(text);

		}
	}

	/**
	 * Parses the full expression from currentInput into numbers and operators,
	 * performs the calculation, and returns the result or an error.
	 *
	 * @return formatted result string or error message
	 */
	private String doneInsertInput() {

		if (currentInput == null || currentInput.isEmpty()) {
			clearAll();
			return "";
		}

		numbers.clear();
		operators.clear();
		currentNumber.setLength(0);

		try {
			parseInput();
			if (!CalculatorValidator.hasValidOperatorNumberRatio(numbers, operators)) {
				clearAll();
				return CalculatorError.SYNTAX_ERROR.getMessage();
			}

		} catch (NumberFormatException e) {
			clearAll();
			return CalculatorError.SYNTAX_ERROR.getMessage();
		}


		String result = applyPrecedence(numbers, operators); 

		// If result is an error message (starts with known prefix), return only the
		// error
		if (result.equals(CalculatorError.MATH_ERROR.getMessage())) {
			return result;
		}

		// Otherwise, return the full expression and result
		return currentInput.toString() + " = " + result;

	}

	/**
	 * Parses the currentInput expression and fills the numbers and operators lists.
	 *
	 * @throws NumberFormatException if a number is invalid
	 */
	private void parseInput() {
		for (int i = 0; i < currentInput.length(); i++) {
			char c = currentInput.charAt(i);
			if (i == 0 && c == '-'
					|| (i > 0 && CalculatorValidator.isOperator(currentInput.charAt(i - 1)) && c == '-')) {
				currentNumber.append(c);
			} else if (Character.isDigit(c) || c == '.') {
				currentNumber.append(c);
			} else if (CalculatorValidator.isOperator(c)) {
				if (currentNumber.length() == 0 || currentNumber.toString().equals("-")) {
					throw new NumberFormatException();
				}
				numbers.add(Double.parseDouble(currentNumber.toString()));
				currentNumber.setLength(0);
				operators.add(c);
			}
		}
		if (currentNumber.length() > 0) {
			numbers.add(Double.parseDouble(currentNumber.toString()));
		}
	}



	/**
	 * Toggles the sign of the last number in the input.
	 *
	 * @return updated input string or error message
	 */
	private String toggleLastNumberSign() {
		if (afterEquals) {
			return toggleAfterEquals();
		}

		int lastOperator = findLastOperatorIndex();

		// Extract the number after the last operator
		String lastNumber = currentInput.substring(lastOperator + 1);
		String toggled = toggleSign(lastNumber);
		if (toggled == null) {
			return CalculatorError.SYNTAX_ERROR.getMessage();
		}

		// Replace it in the input
		currentInput.replace(lastOperator + 1, currentInput.length(), toggled);
		return currentInput.toString();
	}

	/**
	 * Handles toggling the sign of the finalAnswer after equals.
	 *
	 * @return toggled result or error message
	 */
	private String toggleAfterEquals() {
		String toggled = toggleSign(finalAnswer);
		if (toggled.isEmpty())
			return CalculatorError.SYNTAX_ERROR.getMessage();
		finalAnswer = toggled;
		currentInput = new StringBuilder(finalAnswer);
		afterEquals = false;
		return finalAnswer;
	}

	/**
	 * Finds the index of the last operator in the current expression.
	 *
	 * @return index of the last operator or -1 if not found
	 */
	private int findLastOperatorIndex() {
		for (int i = currentInput.length() - 1; i >= 0; i--) {
			char c = currentInput.charAt(i);
			if (CalculatorValidator.isOperator(c) && c != '-')
				return i;
		}
		return -1;
	}

	/**
	 * Toggles the sign of a string number.
	 *
	 * @param value numeric string to toggle
	 * @return toggled value or null if invalid
	 */
	private String toggleSign(String value) {
		if (value == null || value.isEmpty()) {
			return "";
		}

		try {
			double num = Double.parseDouble(value);
			if (num != 0) {
				num *= -1;
			}
			return String.format("%.3f", num);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Clears all internal state and resets flags.
	 */
	private void clearAll() {
		currentInput.setLength(0);
		currentNumber.setLength(0);
		numbers.clear();
		operators.clear();
		afterEquals = false;
	}

	/**
	 * Deletes the last character from the input.
	 */
	private void deleteLastChar() {
		if (currentInput.length() > 0) {
			currentInput.delete(currentInput.length() - 1, currentInput.length());
		}
	}

	/**
	 * Appends the final answer (Ans button) to the current input.
	 *
	 * @return updated input string
	 */
	private String appendFinalAnswer() {
		if (afterEquals) {
			currentInput.setLength(0);
			afterEquals = false;
		}
		currentInput.append(finalAnswer);
		return currentInput.toString();
	}

	/**
	 * Appends user input text to the current expression.
	 *
	 * @param text input string (number or operator)
	 * @return updated expression
	 */
	private String appendToCurrentInput(String text) {
		if (afterEquals) {
			currentInput.setLength(0);
			currentInput.append(finalAnswer);
			afterEquals = false;
		}
		currentInput.append(text);
		return currentInput.toString();
	}

	/**
	 * Applies operator precedence and computes the final result. First handles *
	 * and /, then handles + and -.
	 *
	 * @param nums list of parsed numbers (must have size = ops.size() + 1)
	 * @param ops  list of operators
	 * @return computed result
	 */
	public String applyPrecedence(ArrayList<Double> nums, ArrayList<Character> ops) {
		if (nums == null || ops == null || nums.size() == 0 || nums.size() != ops.size() + 1) {
			return CalculatorError.SYNTAX_ERROR.getMessage();
		}

		// Step 1: Apply * and /
		if( !applyMulDiv(nums, ops)) { 
			return CalculatorError.MATH_ERROR.getMessage();//Div with 0
		}

		// Step 2: Apply + and -
		double result = nums.get(0);
		for (int i = 0; i < ops.size(); i++) {
			char op = ops.get(i);
			double next = nums.get(i + 1);
			if (op == '+') {
				result += next;
			} else if (op == '-') {
				result -= next;
			}
		}
		
		// Final validation (Infinity / NaN)
		if (Double.isInfinite(result) || Double.isNaN(result)) {
			return CalculatorError.MATH_ERROR.getMessage(); 
		}

		finalAnswer = String.format("%.3f", result);

		return finalAnswer;
	}

	/**
	 * Applies multiplication and division operations first, modifying the input
	 * lists.
	 *
	 * @param nums list of numbers
	 * @param ops  list of operators
	 */
	private boolean applyMulDiv(ArrayList<Double> nums, ArrayList<Character> ops) {
		for (int i = 0; i < ops.size();) {
			char op = ops.get(i);
			if (op == '*' || op == '/') {
				double left = nums.get(i);
				double right = nums.get(i + 1);
				double result;
				
				if (op == '*') {
					result = left * right;
				} else {
					if (right == 0) {
						return false;
					}
					result = left / right;
				}

				// Replace left operand with result, and remove right operand & operator
				nums.set(i, result);
				nums.remove(i + 1);
				ops.remove(i); // Stay at same index for next iteration
			} else {
				i++;
			}
		}
		
		return true;

	}

}
