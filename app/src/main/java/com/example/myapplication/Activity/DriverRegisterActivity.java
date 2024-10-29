package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.DriverUserModel;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class DriverRegisterActivity extends AppCompatActivity {

    private EditText edtNome, edtSobrenome, edtCpf, edtCnh, edtPhone, edtBirthDate, edtEmail, edtSenha, edtConfirmarSenha;
    private CheckBox ckbMostrarSenha;
    private Button btnRegistrar, btnVoltar;
    private ProgressBar loginProgressbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        mAuth = FirebaseAuth.getInstance();
        initializeViews();

        ckbMostrarSenha.setOnCheckedChangeListener((buttonView, isChecked) -> togglePasswordVisibility(isChecked));
        btnRegistrar.setOnClickListener(v -> registerDriver());
        btnVoltar.setOnClickListener(v -> navigateToRegister());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista_passageiros), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        edtNome = findViewById(R.id.edt_nome_driver_register);
        edtSobrenome = findViewById(R.id.edt_sobrenome_driver_register);
        edtCpf = findViewById(R.id.edt_cpf_driver_register);
        edtCnh = findViewById(R.id.edt_cnh_driver_register);
        edtPhone = findViewById(R.id.edt_phone_driver_register);
        edtBirthDate = findViewById(R.id.edt_birth_date_driver_register);
        edtEmail = findViewById(R.id.edt_email_driver_register);
        edtSenha = findViewById(R.id.edt_senha_driver_register);
        edtConfirmarSenha = findViewById(R.id.edt_confirmar_senha_driver_register);
        ckbMostrarSenha = findViewById(R.id.ckb_mostrar_senha_driver_register);
        btnRegistrar = findViewById(R.id.btn_registrar_driver_register);
        btnVoltar = findViewById(R.id.btn_voltar_driver_register);
        loginProgressbar = findViewById(R.id.loginProgressbar_driver_register);
    }

    private void togglePasswordVisibility(boolean isChecked) {
        TransformationMethod transformation = isChecked
                ? HideReturnsTransformationMethod.getInstance()
                : PasswordTransformationMethod.getInstance();

        edtSenha.setTransformationMethod(transformation);
        edtConfirmarSenha.setTransformationMethod(transformation);
    }


    private void registerDriver() {
        if (!areFieldsValid()) return;

        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        loginProgressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            loginProgressbar.setVisibility(View.INVISIBLE);
            if (task.isSuccessful()) {
                saveDriverUser();
                abrirTelaPrincipal();
            } else {
                showErrorMessage(task.getException().getMessage());
            }
        });
    }

    private boolean areFieldsValid() {
        if (isEmpty(edtNome) || isEmpty(edtSobrenome) || isEmpty(edtEmail) || isEmpty(edtSenha) || isEmpty(edtConfirmarSenha)) {
            showErrorMessage("Todos os campos devem ser preenchidos");
            return false;
        }
        if (!edtSenha.getText().toString().equals(edtConfirmarSenha.getText().toString())) {
            showErrorMessage("As senhas não coincidem!");
            return false;
        }
        return true;
    }

    private boolean isEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private void saveDriverUser() {
        DriverUserModel driverUser = new DriverUserModel();
        driverUser.setEmail(edtEmail.getText().toString());
        driverUser.setNome(edtNome.getText().toString());
        driverUser.setSobrenome(edtSobrenome.getText().toString());
        driverUser.setCpf(edtCpf.getText().toString());
        driverUser.setCnh(edtCnh.getText().toString());
        driverUser.setPhone(edtPhone.getText().toString());
        driverUser.setBirth(edtBirthDate.getText().toString());
        driverUser.setId(mAuth.getUid());
        driverUser.setUserType("motorista");
        driverUser.Salvar();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(DriverRegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void abrirTelaPrincipal() {
        startActivity(new Intent(this, DriverMainActivity.class));
        finish();
    }

    private void navigateToRegister() {
        startActivity(new Intent(this, Register.class));
        finish();
    }
}
