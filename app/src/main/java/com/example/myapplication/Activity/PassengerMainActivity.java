package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Model.CronogramaModel;
import com.example.myapplication.Model.PassengerUserModel;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PassengerMainActivity extends AppCompatActivity {

    private CheckBox checkboxMonday, checkboxTuesday, checkboxWednesday, checkboxThursday, checkboxFriday;
    private TimePicker timePickerGoing, timePickerReturn;
    private Button btnSaveSchedule, btnCancelarShedule;
    private TextView selecionaDias, ida, volta;

    private FirebaseAuth mAuth;
    private DatabaseReference passengerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            finish();
            return;
        } else {
            passengerRef = FirebaseDatabase.getInstance().getReference("usuarios").child(mAuth.getCurrentUser().getUid());
        }

        selecionaDias = findViewById(R.id.txt_selecionar_dias);
        ida = findViewById(R.id.ida);
        volta = findViewById(R.id.volta);

        checkboxMonday = findViewById(R.id.checkbox_monday);
        checkboxTuesday = findViewById(R.id.checkbox_tuesday);
        checkboxWednesday = findViewById(R.id.checkbox_wednesday);
        checkboxThursday = findViewById(R.id.checkbox_thursday);
        checkboxFriday = findViewById(R.id.checkbox_friday);

        timePickerGoing = findViewById(R.id.time_picker_going);
        timePickerReturn = findViewById(R.id.time_picker_return);

        btnSaveSchedule = findViewById(R.id.btn_save_schedule);
        btnCancelarShedule = findViewById(R.id.btn_cancelar_schedule);

        btnSaveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassengerSchedule();
            }
        });

        btnCancelarShedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideScheduleElements();
            }
        });

        // Inicialmente, ocultar os elementos
        hideScheduleElements();
    }

    private void hideScheduleElements() {
        checkboxMonday.setVisibility(View.GONE);
        checkboxTuesday.setVisibility(View.GONE);
        checkboxWednesday.setVisibility(View.GONE);
        checkboxThursday.setVisibility(View.GONE);
        checkboxFriday.setVisibility(View.GONE);
        timePickerGoing.setVisibility(View.GONE);
        timePickerReturn.setVisibility(View.GONE);
        btnSaveSchedule.setVisibility(View.GONE);
        selecionaDias.setVisibility(View.GONE);
        ida.setVisibility(View.GONE);
        volta.setVisibility(View.GONE);
        btnCancelarShedule.setVisibility(View.GONE);
    }

    private void showScheduleElements() {
        checkboxMonday.setVisibility(View.VISIBLE);
        checkboxTuesday.setVisibility(View.VISIBLE);
        checkboxWednesday.setVisibility(View.VISIBLE);
        checkboxThursday.setVisibility(View.VISIBLE);
        checkboxFriday.setVisibility(View.VISIBLE);
        timePickerGoing.setVisibility(View.VISIBLE);
        timePickerReturn.setVisibility(View.VISIBLE);
        btnSaveSchedule.setVisibility(View.VISIBLE);
        selecionaDias.setVisibility(View.VISIBLE);
        ida.setVisibility(View.VISIBLE);
        volta.setVisibility(View.VISIBLE);
        btnCancelarShedule.setVisibility(View.VISIBLE);
    }

    public void savePassengerSchedule() {
        List<String> selectedDays = new ArrayList<>();
        if (checkboxMonday.isChecked()) selectedDays.add("segunda");
        if (checkboxTuesday.isChecked()) selectedDays.add("terça");
        if (checkboxWednesday.isChecked()) selectedDays.add("quarta");
        if (checkboxThursday.isChecked()) selectedDays.add("quinta");
        if (checkboxFriday.isChecked()) selectedDays.add("sexta");

        int hourGoing = timePickerGoing.getHour();
        int minuteGoing = timePickerGoing.getMinute();
        String goingTime = String.format(Locale.getDefault(), "%02d:%02d", hourGoing, minuteGoing);

        int hourReturn = timePickerReturn.getHour();
        int minuteReturn = timePickerReturn.getMinute();
        String returnTime = String.format(Locale.getDefault(), "%02d:%02d", hourReturn, minuteReturn);

        if (!selectedDays.isEmpty()) {
            CronogramaModel cronograma = new CronogramaModel(goingTime, returnTime, selectedDays);
            PassengerUserModel passengerUser = new PassengerUserModel();

            passengerUser.setId(mAuth.getCurrentUser().getUid());
            passengerUser.setCronograma(cronograma);

            passengerUser.SalvarCronograma();

            Toast.makeText(this, "Cronograma salvo com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Selecione pelo menos um dia", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.passenger_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.passenger_action_diary) {
            Toast.makeText(this, "Definir dias e horários", Toast.LENGTH_SHORT).show();
            showScheduleElements();
            return true;

        } else if (id == R.id.passenger_action_show_logout) {
            Intent intent = new Intent(PassengerMainActivity.this, Login.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
