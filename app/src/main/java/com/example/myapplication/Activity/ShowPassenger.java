package com.example.myapplication.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.PassengerUserModel;
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

    private ListView listaPassageiros;
    private ArrayList<String> pass;

    private Spinner spinnerDia;
    private String diaSelecionado = "Todos"; 
    private Button btnFiltrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_passenger);

        listaPassageiros = findViewById(R.id.lv_passageiros);
        spinnerDia = findViewById(R.id.seu_spinner_dia_id);
        btnFiltrar = findViewById(R.id.seu_botao_filtrar_id);
        pass = new ArrayList<>();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dias_da_semana, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(adapter);

        spinnerDia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                diaSelecionado = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Não faz nada
            }
        });

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarPassageiros();
            }
        });


        carregarPassageiros();
    }

    private void carregarPassageiros() {
        Query nomePassageirosQuery = passageiros.orderByChild("userType").equalTo("passageiro");
        nomePassageirosQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pass.clear();

                Log.d("ShowPassenger", "Número de passageiros encontrados: " + snapshot.getChildrenCount());

                for (DataSnapshot dados : snapshot.getChildren()) {
                    PassengerUserModel passengerUserModel = dados.getValue(PassengerUserModel.class);

                    if (passengerUserModel != null) {
                        Log.d("ShowPassenger", "Passageiro encontrado: " + passengerUserModel.getNome());

                        // Verificando o userType
                        if ("passageiro".equals(passengerUserModel.getUserType())) {
                            String nome = passengerUserModel.getNome();
                            String endereco = passengerUserModel.getEndereco();
                            List<String> scheduleDays = passengerUserModel.getCronograma() != null ? passengerUserModel.getCronograma().getScheduleDays() : null;

                            Log.d("ShowPassenger", "Dia selecionado: " + diaSelecionado);

                            Log.d("ShowPassenger", "Dias do cronograma: " + (scheduleDays != null ? scheduleDays.toString() : "Nenhum"));

                            if (diaSelecionado.equals("Todos") || (scheduleDays != null && scheduleDays.contains(diaSelecionado))) {
                                String goingTime = passengerUserModel.getCronograma() != null ? passengerUserModel.getCronograma().getGoingTime() : "Não informado";
                                String returnTime = passengerUserModel.getCronograma() != null ? passengerUserModel.getCronograma().getReturnTime() : "Não informado";

                                String passengerInfo = nome + "\nEndereço: " + endereco +
                                        "\nHorário de ida: " + goingTime +
                                        "\nHorário de volta: " + returnTime +
                                        "\nDias: " + (scheduleDays != null ? scheduleDays.toString() : "Não informado");

                                pass.add(passengerInfo);

                                Log.d("ShowPassenger", "Passageiro adicionado: " + passengerInfo);
                            }
                        }
                    } else {
                        Log.d("ShowPassenger", "Passageiro não encontrado na modelagem.");
                    }
                }

                ArrayAdapter<String> adaptador = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, pass);
                listaPassageiros.setAdapter(adaptador);

                Log.d("ShowPassenger", "Número de passageiros filtrados: " + pass.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Erro ao carregar dados: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
