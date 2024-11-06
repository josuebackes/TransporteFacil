package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverMainActivity extends AppCompatActivity {

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lista_passageiros), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
            Intent intent = new Intent(DriverMainActivity.this, AtualizarDadosMotoristaActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.driver_action_show_passenger) {
            Toast.makeText(this, "Exibir dados dos passageiros", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DriverMainActivity.this, ShowPassenger.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.driver_action_show_diary) {
            Toast.makeText(this, "Controle de passageiros", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DriverMainActivity.this, RegistroEmbarqueActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.driver_action_show_logout) {
            Intent intent = new Intent(DriverMainActivity.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
