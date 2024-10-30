package com.example.myapplication.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.PassengerUserModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.PassengerViewHolder> {

    private final Context context;
    private final ArrayList<PassengerUserModel> passengers;

    public PassengerAdapter(Context context, ArrayList<PassengerUserModel> passengers) {
        this.context = context;
        this.passengers = passengers;
    }

    @NonNull
    @Override
    public PassengerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_passenger, parent, false);
        return new PassengerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerViewHolder holder, int position) {
        PassengerUserModel passenger = passengers.get(position);
        holder.nameTextView.setText("Nome: " + passenger.getNome());
        holder.addressTextView.setText("Endereço: " + (passenger.getEndereco() != null ? passenger.getEndereco() : ""));
        holder.goingTimeTextView.setText("Horário de ida: " + (passenger.getCronograma() != null ? passenger.getCronograma().getGoingTime() : ""));
        holder.returnTimeTextView.setText("Horário de volta: " + (passenger.getCronograma() != null ? passenger.getCronograma().getReturnTime() : ""));
        holder.scheduleDaysTextView.setText("Dias: " + (passenger.getCronograma() != null ? passenger.getCronograma().getScheduleDays().toString() : ""));
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }

    public static class PassengerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView, goingTimeTextView, returnTimeTextView, scheduleDaysTextView;

        public PassengerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_nome);
            addressTextView = itemView.findViewById(R.id.tv_endereco);
            goingTimeTextView = itemView.findViewById(R.id.tv_horario_ida);
            returnTimeTextView = itemView.findViewById(R.id.tv_horario_volta);
            scheduleDaysTextView = itemView.findViewById(R.id.tv_dias);
        }
    }
}
