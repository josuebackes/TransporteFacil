package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class Register extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button btnContinue;
    private Button btn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista_passageiros), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radioGroup = findViewById(R.id.rg_option);
        btnContinue = findViewById(R.id.btn_continue);
        btn_return = findViewById(R.id.btn_return);

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                Intent intent = null;

                if (selectedId == R.id.rb_passenger) {
                    intent = new Intent(Register.this, PassengerRegisterActivity.class);
                } else if (selectedId == R.id.rb_responsible) {
                    intent = new Intent(Register.this, ResponsibleRegisterActivity.class);
                } else if (selectedId == R.id.rb_driver) {
                    intent = new Intent(Register.this, DriverRegisterActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(Register.this, "Por favor, selecione uma opção para continuar.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}