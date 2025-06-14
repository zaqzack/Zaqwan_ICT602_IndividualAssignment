package com.example.electricitybillgenerator;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class CalculateActivity extends AppCompatActivity {

    private Spinner spinnerMonth;
    private EditText editUnits;
    private RadioGroup radioGroupRebate;
    private Button btnCalculate, btnReset;
    private TextView textTotal, textFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        // Link views with IDs
        spinnerMonth = findViewById(R.id.spinnerMonth);
        editUnits = findViewById(R.id.editTextUnits);
        radioGroupRebate = findViewById(R.id.radioGroupRebate);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        textTotal = findViewById(R.id.textTotal);
        textFinal = findViewById(R.id.textFinal);

        // Spinner setup for months
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.month_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        // Calculate button logic
        btnCalculate.setOnClickListener(v -> {
            String selectedMonth = spinnerMonth.getSelectedItem().toString();
            String unitStr = editUnits.getText().toString();

            if (selectedMonth.equals("Select Month")) {
                Toast.makeText(this, "Please select a valid month.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (unitStr.isEmpty() || radioGroupRebate.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please enter units and select rebate %", Toast.LENGTH_SHORT).show();
                return;
            }

            int units = Integer.parseInt(unitStr);
            int selectedRebateId = radioGroupRebate.getCheckedRadioButtonId();
            RadioButton selectedRebate = findViewById(selectedRebateId);
            int rebate = Integer.parseInt(selectedRebate.getText().toString().replace("%", ""));

            double totalCharges = calculateCharges(units);
            double finalCost = totalCharges - (totalCharges * rebate / 100.0);

            textTotal.setText(String.format("Total Charges: RM %.2f", totalCharges));
            textFinal.setText(String.format("Final Cost: RM %.2f", finalCost));
        });


        // Reset button logic
        btnReset.setOnClickListener(v -> {
            spinnerMonth.setSelection(0);
            editUnits.setText("");
            radioGroupRebate.clearCheck();
            textTotal.setText("Total Charges: RM0.00");
            textFinal.setText("Final Cost: RM0.00");
            Toast.makeText(this, "Form reset.", Toast.LENGTH_SHORT).show();
        });

        // OPTIONAL: Save & View Records
// Save Bill Button
        Button btnSave = findViewById(R.id.btnSaveBill);
        DatabaseHelper db = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {
            String selectedMonth = spinnerMonth.getSelectedItem().toString();
            String unitStr = editUnits.getText().toString();
            int selectedRebateId = radioGroupRebate.getCheckedRadioButtonId();

            if (selectedMonth.equals("Select Month") || unitStr.isEmpty() || selectedRebateId == -1) {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int units = Integer.parseInt(unitStr);
            RadioButton selectedRebate = findViewById(selectedRebateId);
            int rebate = Integer.parseInt(selectedRebate.getText().toString().replace("%", ""));
            double totalCharges = calculateCharges(units);
            double finalCost = totalCharges - (totalCharges * rebate / 100.0);

            boolean saved = db.insertData(selectedMonth, units, rebate, totalCharges, finalCost);

            if (saved) {
                Toast.makeText(this, "Bill saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save bill.", Toast.LENGTH_SHORT).show();
            }
        });

// View Saved Bills Button
        Button btnView = findViewById(R.id.btnViewRecords);
        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(CalculateActivity.this, ViewSavedBillsActivity.class);
            startActivity(intent);
        });

        Button btnBackToMain = findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(v -> {
            finish(); //  This sends user back to MainActivity
        });

    }

    private double calculateCharges(int units) {
        double charges = 0;
        if (units <= 200) {
            charges = units * 0.218;
        } else if (units <= 300) {
            charges = 200 * 0.218 + (units - 200) * 0.334;
        } else if (units <= 600) {
            charges = 200 * 0.218 + 100 * 0.334 + (units - 300) * 0.516;
        } else {
            charges = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (units - 600) * 0.546;
        }
        return charges;
    }
}
