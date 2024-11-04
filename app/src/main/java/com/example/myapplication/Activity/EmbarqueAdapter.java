package com.example.myapplication.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.PassengerUserModel;
import com.example.myapplication.R;

import java.util.List;

public class EmbarqueAdapter extends RecyclerView.Adapter {

    List<PassengerUserModel> passageiros;

    public EmbarqueAdapter(List<PassengerUserModel> passageiros) {
        this.passageiros = passageiros;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passageiros_embarque, parent, false);
        ViewHolderClass vhClass= new ViewHolderClass(view);
        return vhClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        PassengerUserModel passageiro = passageiros.get(position);

        viewHolderClass.tv_nome_passageiro_embarque.setText("Nome: " + passageiro.getNome() + " " + passageiro.getSobrenome());
        viewHolderClass.tv_endereco_passageiro_embarque.setText("Endereço: " + passageiro.getEndereco());

        // Verificação para evitar NullPointerException
        if (passageiro.getCronograma() != null) {
            if (passageiro.getCronograma().getGoingTime() != null) {
                viewHolderClass.tv_horario_ida_passageiro_embarque.setText("Horário de ida: " + passageiro.getCronograma().getGoingTime());
            } else {
                viewHolderClass.tv_horario_ida_passageiro_embarque.setText(""); // Ou outro valor padrão
            }

            if (passageiro.getCronograma().getReturnTime() != null) {
                viewHolderClass.tv_horario_volta_passageiro_embarque.setText("Horário de volta: " + passageiro.getCronograma().getReturnTime());
            } else {
                viewHolderClass.tv_horario_volta_passageiro_embarque.setText(""); // Ou outro valor padrão
            }
        } else {
            viewHolderClass.tv_horario_ida_passageiro_embarque.setText(""); // Ou outro valor padrão
            viewHolderClass.tv_horario_volta_passageiro_embarque.setText(""); // Ou outro valor padrão
        }
    }


    @Override
    public int getItemCount() {
        if (passageiros != null) {
            return passageiros.size();
        }
        return 0;
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView tv_nome_passageiro_embarque, tv_sobrenome_passageiro_embarque,  tv_endereco_passageiro_embarque, tv_horario_ida_passageiro_embarque, tv_horario_volta_passageiro_embarque;
        CheckBox cb_embarque, cb_entregue;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            tv_nome_passageiro_embarque = itemView.findViewById(R.id.tv_nome_passageiro_embarque);
            tv_endereco_passageiro_embarque = itemView.findViewById(R.id.tv_endereco_passageiro_embarque);
            tv_horario_ida_passageiro_embarque = itemView.findViewById(R.id.tv_horario_ida_passageiro_embarque);
            tv_horario_volta_passageiro_embarque = itemView.findViewById(R.id.tv_horario_volta_passageiro_embarque);
            cb_embarque = itemView.findViewById(R.id.cb_embarque);
            cb_entregue = itemView.findViewById(R.id.cb_entregue);
        }
    }
}
