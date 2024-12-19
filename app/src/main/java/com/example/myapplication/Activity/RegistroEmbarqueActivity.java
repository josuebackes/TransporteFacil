package com.example.myapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistroEmbarqueActivity extends AppCompatActivity {

    private static final String TAG = "RegistroEmbarqueActivity";
    List<PassengerUserModel> allPassengers = new ArrayList<>();
    EmbarqueAdapter embarqueAdapter;
    Button btnSalvarEmbarque;
    Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_embarque);

        btnVoltar = findViewById(R.id.voltar_embarque);
        btnSalvarEmbarque = findViewById(R.id.salvar_embarque);

        RecyclerView recyclerView = findViewById(R.id.rv_embarque);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        embarqueAdapter = new EmbarqueAdapter(new ArrayList<>());
        recyclerView.setAdapter(embarqueAdapter);

        loadPassengersFromFirebase();

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroEmbarqueActivity.this, DriverMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSalvarEmbarque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarRotaNoGoogleMaps();
            }
        });
    }

    private void loadPassengersFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allPassengers.clear();
                Log.d(TAG, "Carregando passageiros do Firebase...");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PassengerUserModel passenger = snapshot.getValue(PassengerUserModel.class);
                    if (passenger != null) {
                        allPassengers.add(passenger);
                        Log.d(TAG, "Passageiro carregado: " + passenger.getNome());
                    } else {
                        Log.w(TAG, "Passageiro nulo encontrado no snapshot.");
                    }
                }
                Log.d(TAG, "Total de passageiros carregados: " + allPassengers.size());
                filterPassengers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Erro ao carregar passageiros: " + databaseError.getMessage());
            }
        });
    }

    private void filterPassengers() {
        List<PassengerUserModel> filteredPassengers = allPassengers.stream()
                .filter(p -> "passageiro".equals(p.getUserType()) && p.isScheduledForToday())
                .collect(Collectors.toList());

        Log.d(TAG, "Total de passageiros filtrados: " + filteredPassengers.size());

        embarqueAdapter.updateData(filteredPassengers);
    }

    private void gerarRotaNoGoogleMaps() {
        List<PassengerUserModel> filteredPassengers = embarqueAdapter.getFilteredPassengers();

        if (filteredPassengers == null || filteredPassengers.isEmpty()) {
            Log.d(TAG, "Nenhum passageiro filtrado disponível para gerar rota.");
            return;
        }

        // Endereço fixo do destino final
        String destinoFinalFixo = "R. Arlíndo Pasqualini, 580 - Vila Nova, Novo Hamburgo - RS, 93525-070, Brazil";

        // Montar a string com os endereços dos passageiros para o Google Maps
        StringBuilder waypoints = new StringBuilder();
        for (PassengerUserModel passenger : filteredPassengers) {
            if (passenger.getEndereco() != null && !passenger.getEndereco().isEmpty()) {
                waypoints.append(passenger.getEndereco()).append("|");
            }
        }

        if (waypoints.length() > 0) {
            waypoints.setLength(waypoints.length() - 1); // Remove o último "|"
        }

        // URL para Google Maps Directions
        String baseUrl = "https://www.google.com/maps/dir/?api=1";
        String mapUrl = baseUrl
                + "&origin=current+location" // Localização atual
                + "&destination=" + Uri.encode(destinoFinalFixo) // Destino fixo
                + "&waypoints=" + Uri.encode(waypoints.toString()); // Paradas intermediárias

        // Abrir o Google Maps com a rota
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl));
        intent.setPackage("com.google.android.apps.maps");

        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir Google Maps: " + e.getMessage());
        }
    }
}
