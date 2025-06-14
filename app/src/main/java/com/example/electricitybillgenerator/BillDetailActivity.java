package com.example.electricitybillgenerator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class BillDetailActivity extends AppCompatActivity {

    DatabaseHelper db;
    int billId;
    TextView textMonth, textUnits, textRebate, textTotal, textFinal;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        db = new DatabaseHelper(this);

        // Get ID passed from ListView item
        billId = getIntent().getIntExtra("bill_id", -1);

        // Bind views
        textMonth = findViewById(R.id.textMonth);
        textUnits = findViewById(R.id.textUnits);
        textRebate = findViewById(R.id.textRebate);
        textTotal = findViewById(R.id.textTotal);
        textFinal = findViewById(R.id.textFinal);
        btnDelete = findViewById(R.id.btnDeleteBill);

        loadBillDetails();

        btnDelete.setOnClickListener(v -> {
            db.deleteData(billId);
            Toast.makeText(this, "Bill deleted.", Toast.LENGTH_SHORT).show();
            finish(); // Close the detail view
        });

        Button btnBackToCalculate = findViewById(R.id.btnBackToCalculate);
        btnBackToCalculate.setOnClickListener(v -> {
            finish(); // ✅ Go back to the previous activity (if it's CalculateActivity)
        });


    }

    private void loadBillDetails() {
        Cursor cursor = db.getAllData();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == billId) {
                textMonth.setText("Month: " + cursor.getString(1));
                textUnits.setText("Units Used: " + cursor.getInt(2) + " kWh");
                textRebate.setText("Rebate: " + cursor.getInt(3) + "%");
                textTotal.setText(String.format("Total Charges: RM %.2f", cursor.getDouble(4)));
                textFinal.setText(String.format("Final Cost: RM %.2f", cursor.getDouble(5)));
                break;
            }
        }
    }
}
