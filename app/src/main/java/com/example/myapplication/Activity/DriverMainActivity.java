package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.DriverUserModel;
import com.example.myapplication.Model.PassengerUserModel;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DriverMainActivity extends AppCompatActivity {

    private EditText edt_nome_driver;
    private EditText edt_sobrenome_driver;
    private EditText edt_cpf_driver;
    private EditText edt_cnh_driver;
    private EditText edt_telefone_driver;
    private EditText edt_data_nascimento_driver;
    private EditText edt_email_driver;

    private TextView txt_nome;
    private TextView txt_sobrenome;
    private TextView txt_cpf;
    private TextView txt_cnh;
    private TextView txt_telefone;
    private TextView txt_email;
    private TextView txt_data_nascimento;

    private Button btn_atualizar;
    private Button btn_cancelar;

    private FirebaseAuth mAuth;
    private DatabaseReference driverRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            driverRef = FirebaseDatabase.getInstance().getReference("usuarios").child(mAuth.getCurrentUser().getUid());
        } else {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edt_nome_driver = findViewById(R.id.edt_nome_driver);
        edt_sobrenome_driver = findViewById(R.id.edt_sobrenome_driver);
        edt_cpf_driver = findViewById(R.id.edt_cpf_driver);
        edt_cnh_driver = findViewById(R.id.edt_cnh_driver);
        edt_telefone_driver = findViewById(R.id.edt_telefone_driver);
        edt_data_nascimento_driver = findViewById(R.id.edt_data_nascimento_driver);
        edt_email_driver = findViewById(R.id.edt_email_driver);

        txt_nome = findViewById(R.id.txt_nome);
        txt_sobrenome = findViewById(R.id.txt_sobrenome);
        txt_cpf = findViewById(R.id.txt_cpf);
        txt_cnh = findViewById(R.id.txt_cnh);
        txt_telefone = findViewById(R.id.txt_telefone);
        txt_data_nascimento = findViewById(R.id.txt_data_nascimento);
        txt_email = findViewById(R.id.txt_email);

        btn_atualizar = findViewById(R.id.btn_atualizar);
        btn_cancelar = findViewById(R.id.btn_cancelar);


        btn_atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarDadosMotorista();
                esconderDadosMotorista();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderDadosMotorista();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista_passageiros), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        esconderDadosMotorista();
    }

    public void atualizarDadosMotorista() {
        String userId = mAuth.getCurrentUser().getUid();

        DriverUserModel driverUserModel = new DriverUserModel();

        driverUserModel.setId(userId);
        driverUserModel.setNome(edt_nome_driver.getText().toString());
        driverUserModel.setSobrenome(edt_sobrenome_driver.getText().toString());
        driverUserModel.setCpf(edt_cpf_driver.getText().toString());
        driverUserModel.setCnh(edt_cnh_driver.getText().toString());
        driverUserModel.setPhone(edt_telefone_driver.getText().toString());
        driverUserModel.setBirth(edt_data_nascimento_driver.getText().toString());
        driverUserModel.setEmail(edt_email_driver.getText().toString());

        if (!TextUtils.isEmpty(driverUserModel.getNome()) && !TextUtils.isEmpty(driverUserModel.getSobrenome()) && !TextUtils.isEmpty(driverUserModel.getEmail())) {
            driverUserModel.Salvar();
            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.driver_action_show_driver) {
            Toast.makeText(this, "Atualizar dados do motorista", Toast.LENGTH_SHORT).show();
            carregarDadosMotorista();
            return true;

        } else if (id == R.id.driver_action_show_passenger) {
            Toast.makeText(this, "Exibir dados dos passageiros", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DriverMainActivity.this, ShowPassenger.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.driver_action_show_diary) {
            Toast.makeText(this, "Exibir agenda", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.driver_action_show_logout) {
            Intent intent = new Intent(DriverMainActivity.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void esconderDadosMotorista(){
        txt_email.setVisibility(View.GONE);
        txt_nome.setVisibility(View.GONE);
        txt_sobrenome.setVisibility(View.GONE);
        txt_cnh.setVisibility(View.GONE);
        txt_cpf.setVisibility(View.GONE);
        txt_data_nascimento.setVisibility(View.GONE);
        txt_telefone.setVisibility(View.GONE);

        edt_nome_driver.setVisibility(View.GONE);
        edt_sobrenome_driver.setVisibility(View.GONE);
        edt_cnh_driver.setVisibility(View.GONE);
        edt_cpf_driver.setVisibility(View.GONE);
        edt_telefone_driver.setVisibility(View.GONE);
        edt_email_driver.setVisibility(View.GONE);
        edt_data_nascimento_driver.setVisibility(View.GONE);

        btn_atualizar.setVisibility(View.GONE);
        btn_cancelar.setVisibility(View.GONE);
    }


    private void mostrarDadosMotorista(){
        txt_email.setVisibility(View.VISIBLE);
        txt_nome.setVisibility(View.VISIBLE);
        txt_sobrenome.setVisibility(View.VISIBLE);
        txt_cnh.setVisibility(View.VISIBLE);
        txt_cpf.setVisibility(View.VISIBLE);
        txt_data_nascimento.setVisibility(View.VISIBLE);
        txt_telefone.setVisibility(View.VISIBLE);

        edt_nome_driver.setVisibility(View.VISIBLE);
        edt_sobrenome_driver.setVisibility(View.VISIBLE);
        edt_cnh_driver.setVisibility(View.VISIBLE);
        edt_cpf_driver.setVisibility(View.VISIBLE);
        edt_telefone_driver.setVisibility(View.VISIBLE);
        edt_email_driver.setVisibility(View.VISIBLE);
        edt_data_nascimento_driver.setVisibility(View.VISIBLE);

        btn_atualizar.setVisibility(View.VISIBLE);
        btn_cancelar.setVisibility(View.VISIBLE);

    }

    private void carregarDadosMotorista() {
        mostrarDadosMotorista();
        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DriverUserModel driverUser = dataSnapshot.getValue(DriverUserModel.class);
                    if (driverUser != null) {
                        edt_nome_driver.setText(driverUser.getNome());
                        edt_sobrenome_driver.setText(driverUser.getSobrenome());
                        edt_cnh_driver.setText(driverUser.getCnh());
                        edt_cpf_driver.setText(driverUser.getCpf());
                        edt_telefone_driver.setText(driverUser.getPhone());
                        edt_email_driver.setText(driverUser.getEmail());
                        edt_data_nascimento_driver.setText(driverUser.getBirth());
                    }
                } else {
                    Toast.makeText(DriverMainActivity.this, "Dados não encontrados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DriverMainActivity.this, "Erro ao carregar dados: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
