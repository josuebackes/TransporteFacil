package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.PassengerUserModel;
import com.example.myapplication.Activity.PassengerAdapter;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowPassenger extends AppCompatActivity {

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference passageiros = referencia.child("usuarios");

    private RecyclerView recyclerViewPassageiros;
    private PassengerAdapter passengerAdapter;
    private ArrayList<PassengerUserModel> passengerList;

    private Spinner spinnerDia;
    private String diaSelecionado = "Todos";
    private Button btnFiltrar;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_passenger);

        recyclerViewPassageiros = findViewById(R.id.rv_passageiros);
        spinnerDia = findViewById(R.id.seu_spinner_dia_id);
        btnFiltrar = findViewById(R.id.seu_botao_filtrar_id);
        btnVoltar = findViewById(R.id.seu_botao_voltar_id);

        passengerList = new ArrayList<>();

        recyclerViewPassageiros.setLayoutManager(new LinearLayoutManager(this));
        passengerAdapter = new PassengerAdapter(this, passengerList);
        recyclerViewPassageiros.setAdapter(passengerAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dias_da_semana, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(adapter);

        spinnerDia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                diaSelecionado = parentView.getItemAtPosition(position).toString();
                Log.d("ShowPassenger", "Dia selecionado no Spinner: " + diaSelecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarPassageiros();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowPassenger.this, DriverMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        carregarPassageiros();
    }

    private void carregarPassageiros() {
        Query nomePassageirosQuery = passageiros.orderByChild("userType").equalTo("passageiro");

        nomePassageirosQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                passengerList.clear();

                Log.d("ShowPassenger", "Número de passageiros encontrados: " + snapshot.getChildrenCount());

                for (DataSnapshot dados : snapshot.getChildren()) {
                    PassengerUserModel passengerUserModel = dados.getValue(PassengerUserModel.class);

                    if (passengerUserModel != null) {
                        List<String> scheduleDays = passengerUserModel.getCronograma() != null ? passengerUserModel.getCronograma().getScheduleDays() : null;

                        if (diaSelecionado.equals("Todos") ||
                                (scheduleDays != null && scheduleDays.stream()
                                        .anyMatch(day -> day.equalsIgnoreCase(diaSelecionado)))) {

                            passengerList.add(passengerUserModel);
                            Log.d("ShowPassenger", "Passageiro adicionado: " + passengerUserModel.getNome());
                        }
                    }
                }

                passengerAdapter.notifyDataSetChanged();

                Log.d("ShowPassenger", "Número de passageiros filtrados: " + passengerList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Erro ao carregar dados: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
