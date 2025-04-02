
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

public class MyProjectController {

	private final int NUM_COLS = 4;
	private final int NUM_ROWS = 5;

	private final String[] OPERATIONS = { "7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "=",
			"+", "C", "DEL", "+/-", "Ans" };

	@FXML
	private GridPane buttonGrid;

	@FXML
	private TextField textField;

	private Button[] buttons;
	private CalculatorLogic logic = new CalculatorLogic();

	/**
	 * Initializes the calculator UI.
	 */
	public void initialize() {
		buttons = new Button[OPERATIONS.length];
		textField.setAlignment(Pos.CENTER_LEFT);

		setupGridConstraints();
		addButtons();
		setupResponsiveFontSizing();
		setupTextLengthListener();
	}

	/**
	 * Populates the GridPane with calculator buttons.
	 */
	private void addButtons() {
		for (int i = 0; i < OPERATIONS.length; i++) {
			final String label = OPERATIONS[i];
			Button btn = createButton(label);

			int col = i % NUM_COLS;
			int row = i / NUM_COLS;
			buttonGrid.add(btn, col, row);

			buttons[i] = btn;
		}
	}

	/* Set The button colors */
	private void setButtonColor(Button btn, String label) {
		switch (label) {
		case "+":
		case "-":
		case "*":
		case "/":
		case "=":
			btn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
			break;
		case "C":
		case "DEL":
		case "+/-":
		case "Ans":
			btn.setStyle("-fx-background-color: #9E9E9E;" + "-fx-text-fill: white;");
			break;
		default:
			break;

		}
	}

	/**
	 * Creates and configures a calculator button with the given label.
	 *
	 * @param label the text to display on the button
	 * @return a configured Button instance
	 */
	private Button createButton(final String label) {
		Button btn = new Button(label);
		btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setHgrow(btn, Priority.ALWAYS);
		GridPane.setVgrow(btn, Priority.ALWAYS);
		GridPane.setMargin(btn, new Insets(5));

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onButtonPressed(label);
			}
		});

		setButtonColor(btn, label);

		return btn;
	}

	/**
	 * Adds an empty button to balance the layout (optional aesthetic).
	 */
	/*
	 * private void addEmptyPlaceholder() { Button emptyButton = new Button(" ");
	 * emptyButton.setFocusTraversable(false); emptyButton.setFont(new Font("Arial",
	 * 18)); emptyButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	 * GridPane.setHgrow(emptyButton, Priority.ALWAYS);
	 * GridPane.setVgrow(emptyButton, Priority.ALWAYS);
	 * 
	 * // Last cell: column 3, row 4 buttonGrid.add(emptyButton, 3, 4); }
	 */

	/**
	 * Ensures that the GridPane has equal spacing in rows and columns.
	 */
	private void setupGridConstraints() {
		buttonGrid.getColumnConstraints().clear();
		buttonGrid.getRowConstraints().clear();

		for (int i = 0; i < NUM_COLS; i++) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(100.0 / NUM_COLS);
			buttonGrid.getColumnConstraints().add(col);
		}

		for (int i = 0; i < NUM_ROWS; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100.0 / NUM_ROWS);
			buttonGrid.getRowConstraints().add(row);
		}
	}

	/**
	 * Sets up dynamic resizing of font size based on scene width.
	 */
	private void setupResponsiveFontSizing() {
		textField.sceneProperty().addListener(new javafx.beans.value.ChangeListener<javafx.scene.Scene>() {
			@Override
			public void changed(javafx.beans.value.ObservableValue<? extends javafx.scene.Scene> obs,
					javafx.scene.Scene oldScene, javafx.scene.Scene newScene) {
				if (newScene != null) {
					newScene.widthProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
						@Override
						public void changed(javafx.beans.value.ObservableValue<? extends Number> o, Number oldVal,
								Number newVal) {
							double width = newVal.doubleValue();
							updateTextFieldFontSize(width);
							updateButtonFonts(width);
						}
					});
				}
			}
		});
	}

	/**
	 * Handles any button click — adds the text to the display.
	 *
	 * @param text the text of the clicked button
	 */
	private void onButtonPressed(String text) {
		String result = logic.userInput(text);
		textField.setText(result);
		textField.requestFocus(); // Set focus on the text field so the user can immediately start typing
		textField.positionCaret(textField.getText().length()); // Move the caret (cursor) to the end of the current text
																// for better UX
	}

	/**
	 * Dynamically adjusts the font size of the calculator display based on: 1. The
	 * current window width (to scale the UI responsively). 2. The length of the
	 * text in the display (to avoid text being cut off).
	 *
	 * Font size will shrink gradually if the text length exceeds 20 characters.
	 *
	 * @param width the current window width
	 */
	private void updateTextFieldFontSize(double width) {
		double baseWidth = 330.0;
		double scaleFactor = width / baseWidth;
		double baseFontSize = 24 * scaleFactor;

		double maxFontSize = 48;
		double minFontSize = 12;

		// Scale font size based on window width
		double newFontSize = Math.max(18, Math.min(maxFontSize, baseFontSize));

		// Reduce font size if the text is too long
		int textLength = textField.getText().length();
		if (textLength > 15) {
			double shrink = (textLength - 15) * 0.6;
			newFontSize = Math.max(minFontSize, newFontSize - shrink);
		}

		textField.setFont(new Font("Lucida Console", newFontSize));
	}

	/**
	 * Updates font size for all buttons based on window width.
	 *
	 * @param width current window width
	 */
	private void updateButtonFonts(double width) {
		double baseWidth = 440.0;
		double scaleFactor = width / baseWidth;
		double newFontSize = Math.max(18, Math.min(64, 18 * scaleFactor));

		for (Button btn : buttons) {
			btn.setFont(new Font("Lucida Console", newFontSize));
		}
	}

	/**
	 * Sets up a listener on the text field's text property, so the font size
	 * adjusts dynamically not only by window width but also when the length of the
	 * input text changes.
	 */
	private void setupTextLengthListener() {
		textField.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
			@Override
			public void changed(javafx.beans.value.ObservableValue<? extends String> obs, String oldText,
					String newText) {
				if (textField.getScene() != null) {
					updateTextFieldFontSize(textField.getScene().getWidth());
				} else {
					updateTextFieldFontSize(330.0); // fallback width
				}
			}
		});
	}
}
