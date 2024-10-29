package com.example.myapplication.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.Model.PassengerUserModel;

import java.util.List;

public class PassengerAdapter extends ArrayAdapter<PassengerUserModel> {
    public PassengerAdapter(Context context, List<PassengerUserModel> passageiros) {
        super(context, 0, passageiros);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PassengerUserModel passageiro = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(passageiro.getNome());

        return convertView;
    }
}
