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
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.myapplication.Model.PassengerUserModel;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;


public class PassengerRegisterActivity extends AppCompatActivity {

    private EditText edtNomePassageiro, edtSobrenomePassageiro, edtDataNascimentoPassageiro, edtCpfPassageiro, edtTelefonePassageiro, edtRua
            , edtNumeroCasa, edtBairro, edtCidade, edtEstado, edtCep, edtEmailPassageiro, edtSenhaPassageiro, edtConfirmarSenhaPassageiro;
    private CheckBox ckbMostrarSenha;
    private Button btnRegistrar, btnVoltar;
    private ProgressBar loginProgressbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_register);

        mAuth = FirebaseAuth.getInstance();
        initializeViews();

        ckbMostrarSenha.setOnCheckedChangeListener((buttonView, isChecked) -> togglePasswordVisibility(isChecked));
        btnRegistrar.setOnClickListener(v -> registerPassenger());
        btnVoltar.setOnClickListener(v -> navigateToRegister());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista_passageiros), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        edtNomePassageiro = findViewById(R.id.edt_nome_passenger_register);
        edtSobrenomePassageiro = findViewById(R.id.edt_sobrenome_passenger_register);
        edtDataNascimentoPassageiro = findViewById(R.id.edt_birth_date_passenger_register);
        edtCpfPassageiro = findViewById(R.id.edt_cpf_passenger_register);
        edtTelefonePassageiro = findViewById(R.id.edt_phone_passenger_register);
        edtRua = findViewById(R.id.edt_street_passenger_register);
        edtNumeroCasa = findViewById(R.id.edt_number_passenger_register);
        edtBairro = findViewById(R.id.edt_district_passenger_register);
        edtCidade = findViewById(R.id.edt_city_passenger_register);
        edtEstado = findViewById(R.id.edt_state_passenger_register);
        edtCep = findViewById(R.id.edt_cep_passenger_register);
        edtEmailPassageiro = findViewById(R.id.edt_email_passenger_register);
        edtSenhaPassageiro = findViewById(R.id.edt_senha_passenger_register);
        edtConfirmarSenhaPassageiro = findViewById(R.id.edt_confirmar_senha_passenger_register);

        ckbMostrarSenha = findViewById(R.id.ckb_mostrar_senha_passenger_register);
        btnRegistrar = findViewById(R.id.btn_registrar_passenger_register);
        btnVoltar = findViewById(R.id.btn_voltar_passenger_register);
        loginProgressbar = findViewById(R.id.loginProgressbar_passenger_register);
    }

    private void togglePasswordVisibility(boolean isChecked) {
        TransformationMethod transformation = isChecked
                ? HideReturnsTransformationMethod.getInstance()
                : PasswordTransformationMethod.getInstance();

        edtSenhaPassageiro.setTransformationMethod(transformation);
        edtConfirmarSenhaPassageiro.setTransformationMethod(transformation);
    }

    private void registerPassenger() {
        if (!areFieldsValid()) return;

        String email = edtEmailPassageiro.getText().toString();
        String senha = edtSenhaPassageiro.getText().toString();

        loginProgressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            loginProgressbar.setVisibility(View.INVISIBLE);
            if (task.isSuccessful()) {
                savePassengerUser();
                abrirTelaPrincipal();
            } else {
                showErrorMessage(task.getException().getMessage());
            }
        });
    }

    private boolean areFieldsValid() {
        if (isEmpty(edtNomePassageiro) || isEmpty(edtSobrenomePassageiro) || isEmpty(edtEmailPassageiro) || isEmpty(edtSenhaPassageiro) || isEmpty(edtConfirmarSenhaPassageiro)) {
            showErrorMessage("Todos os campos devem ser preenchidos");
            return false;
        }
        if (!edtSenhaPassageiro.getText().toString().equals(edtConfirmarSenhaPassageiro.getText().toString())) {
            showErrorMessage("As senhas não coincidem!");
            return false;
        }
        return true;
    }

    private boolean isEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private void savePassengerUser() {
        PassengerUserModel passanger = new PassengerUserModel();

        passanger.setId(mAuth.getUid());
        passanger.setNome(edtNomePassageiro.getText().toString());
        passanger.setSobrenome(edtSobrenomePassageiro.getText().toString());
        passanger.setCpf(edtCpfPassageiro.getText().toString());
        passanger.setTelefone(edtTelefonePassageiro.getText().toString());
        passanger.setDataNascimento(edtDataNascimentoPassageiro.getText().toString());
        passanger.setEmail(edtEmailPassageiro.getText().toString());
        passanger.setRua(edtRua.getText().toString());
        passanger.setNumeroCasa(edtNumeroCasa.getText().toString());
        passanger.setBairro(edtBairro.getText().toString());
        passanger.setCidade(edtCidade.getText().toString());
        passanger.setEstado(edtEstado.getText().toString());
        passanger.setCep(edtCep.getText().toString());
        passanger.setUserType("passageiro");
        passanger.Salvar();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(PassengerRegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void abrirTelaPrincipal() {
        startActivity(new Intent(this, PassengerMainActivity.class));
        finish();
    }

    private void navigateToRegister() {
        startActivity(new Intent(this, Register.class));
        finish();
    }

}