package com.example.calculatorapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // GridLayout of all the buttons
    private GridLayout buttonGrid;
    //TextView to display the calculations and result
    private TextView display;
    // Calculator logic class that handles input and evaluation
    private CalculatorLogic logic = new CalculatorLogic();
    // List of button labels for the calculator
    private final String[] OPERATIONS = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "DEL", "+/-", "Ans"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the UI layout from XML
        setContentView(R.layout.activity_main); // זה הקובץ XML שלך
        // Reference the views from the layout
        buttonGrid = findViewById(R.id.buttonGrid);
        display = findViewById(R.id.displayExpression);
        // Dynamically add all calculator buttons
        addButtonsToGrid();
    }

    /**
     * Dynamically adds calculator buttons to the GridLayout.
     */
    private void addButtonsToGrid() {
        // Create a new Button for each operation
        for (String label : OPERATIONS) {
            Button btn = new Button(this);
            btn.setText(label);
            btn.setTextSize(20); // טקסט גדול יותר
            btn.setTextColor(getResources().getColor(R.color.buttonText)); // צבע מהקובץ colors.xml

            // Set backgroumd based on type
            if (label.matches("[0-9\\.]")) {
                btn.setBackgroundColor(getResources().getColor(R.color.buttonNumber));
            } else if (label.matches("[\\+\\-\\*/=]")) {
                btn.setBackgroundColor(getResources().getColor(R.color.buttonOperator));
            } else {
                btn.setBackgroundColor(getResources().getColor(R.color.buttonFunction));
            }

            // Grid layout parameters for sizing and positioning
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8); // מרווחים

            btn.setLayoutParams(params);

            // Define click behavior
            btn.setOnClickListener(v -> {
                String result = logic.userInput(label);
                display.setText(result);
            });

            // Add the button to the GridLayout
            buttonGrid.addView(btn);
        }
    }

}
