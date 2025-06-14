package com.example.electricitybillgenerator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewSavedBillsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listData;
    ArrayList<Integer> billIds;
    DatabaseHelper db;


    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Refresh list every time you return
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_bills);

        listView = findViewById(R.id.listViewBills);
        db = new DatabaseHelper(this);
        listData = new ArrayList<>();
        billIds = new ArrayList<>();

        loadData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int billId = billIds.get(position);
            Intent intent = new Intent(ViewSavedBillsActivity.this, BillDetailActivity.class);
            intent.putExtra("bill_id", billId);
            startActivity(intent);
        });

        FloatingActionButton fabAdd = findViewById(R.id.fabAddBill);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ViewSavedBillsActivity.this, CalculateActivity.class);
            startActivity(intent);
        });


    }

    private void loadData() {
        listData.clear();
        billIds.clear();

        Cursor cursor = db.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String month = cursor.getString(1);
                int units = cursor.getInt(2);
                int rebate = cursor.getInt(3);
                double finalCost = cursor.getDouble(5);

                listData.add(month + ": " + units + " kWh | Rebate " + rebate + "% | RM" + String.format("%.2f", finalCost));
                billIds.add(id);
            } while (cursor.moveToNext());
        } else {
            listData.add("No bills saved yet.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item_bill,
                R.id.textItem,
                listData
        );
        listView.setAdapter(adapter);

    }
}
