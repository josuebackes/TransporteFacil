package com.example.myapplication.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.EmbarqueAdapter;
import com.example.myapplication.Model.PassengerUserModel;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegistroEmbarqueActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmbarqueAdapter embarqueAdapter;
    private List<PassengerUserModel> listaPassageiros;
    private static final String TAG = "RegistroEmbarqueActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_embarque);

        recyclerView = findViewById(R.id.rv_embarque);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaPassageiros = new ArrayList<>();
        embarqueAdapter = new EmbarqueAdapter(listaPassageiros);
        recyclerView.setAdapter(embarqueAdapter);

        carregarPassageiros();
    }

    private void carregarPassageiros() {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");
        usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPassageiros.clear();
                String hoje = obterDiaAtual();
                Log.d(TAG, "Hoje é: " + hoje); // Log do dia atual

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PassengerUserModel passageiro = dataSnapshot.getValue(PassengerUserModel.class);

                    if (passageiro != null) {
                        Log.d(TAG, "Passageiro carregado: " + passageiro.getNome() + ", UserType: " + passageiro.getUserType());

                        // Verificar o tipo de usuário e se está programado para hoje
                        if ("passageiro".equals(passageiro.getUserType())) {
                            Log.d(TAG, "UserType é passageiro para: " + passageiro.getNome());

                            if (passageiro.getCronograma() != null) {
                                Log.d(TAG, "Cronograma presente para: " + passageiro.getNome());

                                boolean scheduledToday = passageiro.getCronograma().containsDay(hoje);
                                Log.d(TAG, "Scheduled for today (" + hoje + ") para " + passageiro.getNome() + ": " + scheduledToday);

                                if (scheduledToday) {
                                    listaPassageiros.add(passageiro);
                                }
                            } else {
                                Log.d(TAG, "Cronograma está nulo para: " + passageiro.getNome());
                            }
                        }
                    } else {
                        Log.d(TAG, "Passageiro nulo no snapshot.");
                    }
                }

                embarqueAdapter.notifyDataSetChanged();
                if (listaPassageiros.isEmpty()) {
                    Toast.makeText(RegistroEmbarqueActivity.this, "Nenhum passageiro para hoje.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Erro ao carregar dados: " + error.getMessage());
                Toast.makeText(RegistroEmbarqueActivity.this, "Erro ao carregar dados.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String obterDiaAtual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", new Locale("pt", "BR"));
        String diaSemana = dateFormat.format(calendar.getTime());
        return diaSemana.substring(0, 1).toUpperCase() + diaSemana.substring(1); // "Segunda", "Terça", etc.
    }
}
