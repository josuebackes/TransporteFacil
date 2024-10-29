package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Activity.PassengerMainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private EditText edt_email;
    private EditText edt_senha;
    private Button btn_login;
    private Button btn_registrar;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgressbar;
    private CheckBox ckb_mostrar_senha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        btn_login = findViewById(R.id.btn_login);
        btn_registrar = findViewById(R.id.btn_registrar);
        loginProgressbar = findViewById(R.id.loginProgressbar);
        ckb_mostrar_senha = findViewById(R.id.ckb_mostrar_senha);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = edt_email.getText().toString();
                String loginSenha = edt_senha.getText().toString();

                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginSenha)) {
                    loginProgressbar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail, loginSenha)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(user.getUid());
                                            userRef.child("userType").get().addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    String userType = task1.getResult().getValue(String.class);
                                                    if (userType != null) {
                                                        abrirTelaPrincipal(userType);
                                                    } else {
                                                        Toast.makeText(Login.this, "Tipo de usuário não encontrado.", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(Login.this, "Falha ao carregar o tipo de usuário.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Login.this, "" + error, Toast.LENGTH_SHORT).show();
                                        loginProgressbar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }
            }
        });


        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        ckb_mostrar_senha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edt_senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edt_senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista_passageiros), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void abrirTelaPrincipal(String userType) {
        Intent intent;
        switch (userType) {
            case "motorista":
                intent = new Intent(Login.this, DriverMainActivity.class);
                break;
            case "passageiro":
                intent = new Intent(this, PassengerMainActivity.class);
                break;
            case "responsável":
                intent = new Intent(this, ResponsibleMainActivity.class);
                break;
            default:
                intent = new Intent(this, DefaultMainActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }

}