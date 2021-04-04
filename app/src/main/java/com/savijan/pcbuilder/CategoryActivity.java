package com.savijan.pcbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnCpuCategory, btnMotherCategory, btnRamCategory, btnGpuCategory, btnHddCategory, btnSsdCategory, btnSupplyCategory, btnCaseCategory, btnCoolerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();
    }

    private void init() {

        btnCpuCategory = (Button) findViewById(R.id.btnCpuCategory);
        btnMotherCategory = (Button) findViewById(R.id.btnMotherCategory);
        btnRamCategory = (Button) findViewById(R.id.btnRamCategory);
        btnGpuCategory = (Button) findViewById(R.id.btnGpuCategory);
        btnHddCategory = (Button) findViewById(R.id.btnHddCategory);
        btnSsdCategory = (Button) findViewById(R.id.btnSsdCategory);
        btnSupplyCategory = (Button) findViewById(R.id.btnSupplyCategory);
        btnCaseCategory = (Button) findViewById(R.id.btnCaseCategory);
        btnCoolerCategory = (Button) findViewById(R.id.btnCoolerCategory);
        btnCpuCategory.setOnClickListener(this::onClick);
        btnMotherCategory.setOnClickListener(this::onClick);
        btnRamCategory.setOnClickListener(this::onClick);
        btnGpuCategory.setOnClickListener(this::onClick);
        btnHddCategory.setOnClickListener(this::onClick);
        btnSsdCategory.setOnClickListener(this::onClick);
        btnSupplyCategory.setOnClickListener(this::onClick);
        btnCaseCategory.setOnClickListener(this::onClick);
        btnCoolerCategory.setOnClickListener(this::onClick);

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ViewListActivity.class);
        switch (v.getId()){
            case R.id.btnCpuCategory:

                intent.putExtra("category", "Processors");
                startActivity(intent);
                break;
            case R.id.btnMotherCategory:
                intent.putExtra("category", "Motherboards");
                startActivity(intent);
                break;
            case R.id.btnRamCategory:
                intent.putExtra("category", "RAM");
                startActivity(intent);
                break;
            case R.id.btnGpuCategory:
                intent.putExtra("category", "Videocards");
                startActivity(intent);
                break;
            case R.id.btnHddCategory:
                intent.putExtra("category", "Harddisks");
                startActivity(intent);
                break;
            case R.id.btnSsdCategory:
                intent.putExtra("category", "Ssddisks");
                startActivity(intent);
                break;
            case R.id.btnSupplyCategory:
                intent.putExtra("category", "Supply");
                startActivity(intent);
                break;
            case R.id.btnCaseCategory:
                intent.putExtra("category", "Cases");
                startActivity(intent);
                break;
            case R.id.btnCoolerCategory:
                intent.putExtra("category", "Coolers");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}