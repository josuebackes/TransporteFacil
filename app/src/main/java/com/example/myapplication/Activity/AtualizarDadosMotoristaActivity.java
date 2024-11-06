package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AtualizarDadosMotoristaActivity extends AppCompatActivity {

    private EditText edt_nome_driver, edt_sobrenome_driver, edt_cpf_driver, edt_cnh_driver, edt_telefone_driver, edt_data_nascimento_driver, edt_email_driver;
    private Button btn_atualizar, btn_cancelar;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_dados_motorista);

        edt_nome_driver = findViewById(R.id.edt_nome_driver);
        edt_sobrenome_driver = findViewById(R.id.edt_sobrenome_driver);
        edt_cpf_driver = findViewById(R.id.edt_cpf_driver);
        edt_cnh_driver = findViewById(R.id.edt_cnh_driver);
        edt_telefone_driver = findViewById(R.id.edt_telefone_driver);
        edt_data_nascimento_driver = findViewById(R.id.edt_data_nascimento_driver);
        edt_email_driver = findViewById(R.id.edt_email_driver);

        btn_atualizar = findViewById(R.id.btn_atualizar);
        btn_cancelar = findViewById(R.id.btn_cancelar);

        firebaseAuth = FirebaseAuth.getInstance();

        mostrarDadosUsuario();

        btn_atualizar.setOnClickListener(v -> atualizarDadosUsuario());

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AtualizarDadosMotoristaActivity.this, DriverMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void mostrarDadosUsuario() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String nome = dataSnapshot.child("nome").getValue(String.class);
                        String sobrenome = dataSnapshot.child("sobrenome").getValue(String.class);
                        String cpf = dataSnapshot.child("cpf").getValue(String.class);
                        String cnh = dataSnapshot.child("cnh").getValue(String.class);
                        String telefone = dataSnapshot.child("phone").getValue(String.class);
                        String dataNascimento = dataSnapshot.child("birth").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);

                        edt_nome_driver.setText(nome);
                        edt_sobrenome_driver.setText(sobrenome);
                        edt_cpf_driver.setText(cpf);
                        edt_cnh_driver.setText(cnh);
                        edt_telefone_driver.setText(telefone);
                        edt_data_nascimento_driver.setText(dataNascimento);
                        edt_email_driver.setText(email);
                    } else {
                        Log.d("AtualizarDados", "Usuário não encontrado no banco de dados");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("AtualizarDados", "Erro ao buscar dados: ", databaseError.toException());
                }
            });
        } else {
            Log.d("AtualizarDados", "Usuário não autenticado");
        }
    }

    private void atualizarDadosUsuario() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);

            String nome = edt_nome_driver.getText().toString().trim();
            String sobrenome = edt_sobrenome_driver.getText().toString().trim();
            String cpf = edt_cpf_driver.getText().toString().trim();
            String cnh = edt_cnh_driver.getText().toString().trim();
            String telefone = edt_telefone_driver.getText().toString().trim();
            String dataNascimento = edt_data_nascimento_driver.getText().toString().trim();
            String email = edt_email_driver.getText().toString().trim();

            Map<String, Object> updates = new HashMap<>();
            updates.put("nome", nome);
            updates.put("sobrenome", sobrenome);
            updates.put("cpf", cpf);
            updates.put("cnh", cnh);
            updates.put("phone", telefone);
            updates.put("birth", dataNascimento);
            updates.put("email", email);

            databaseReference.updateChildren(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AtualizarDadosMotoristaActivity.this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AtualizarDadosMotoristaActivity.this, "Erro ao atualizar dados.", Toast.LENGTH_SHORT).show();
                    Log.e("AtualizarDados", "Erro: ", task.getException());
                }
            });
        } else {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show();
        }
    }
}
