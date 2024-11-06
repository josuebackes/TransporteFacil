package com.example.myapplication.Activity;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_embarque);

        RecyclerView recyclerView = findViewById(R.id.rv_embarque);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        embarqueAdapter = new EmbarqueAdapter(new ArrayList<>());
        recyclerView.setAdapter(embarqueAdapter);

        loadPassengersFromFirebase();
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
}
