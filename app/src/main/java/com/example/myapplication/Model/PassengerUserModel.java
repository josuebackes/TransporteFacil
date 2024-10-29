package com.example.myapplication.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class PassengerUserModel {
    private String id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String telefone;
    private String dataNascimento;
    private String email;
    private String rua;
    private String numeroCasa;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String userType;

    // Novos campos para o cronograma
    private List<String> scheduleDays;
    private String goingTime;
    private String returnTime;
    private CronogramaModel cronograma;

    public PassengerUserModel() {}

    // Adicione um construtor com o campo cronograma
    public PassengerUserModel(String id, String nome, String sobrenome, String cpf, String telefone, String dataNascimento, String email, String rua, String numeroCasa, String cidade, String bairro, String estado, String cep, String userType, CronogramaModel cronograma) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.rua = rua;
        this.numeroCasa = numeroCasa;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.userType = userType;
        this.cronograma = cronograma;
    }

    public void setCronograma(CronogramaModel cronograma) {
        this.cronograma = cronograma;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<String> getScheduleDays() {
        return scheduleDays;
    }

    public void setScheduleDays(List<String> scheduleDays) {
        this.scheduleDays = scheduleDays;
    }

    public String getGoingTime() {
        return goingTime;
    }

    public void setGoingTime(String goingTime) {
        this.goingTime = goingTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public void Salvar() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("usuarios").child(getId()).setValue(this);
    }

    public void SalvarCronograma() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = reference.child("usuarios").child(getId()).child("cronograma");

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("scheduleDays", cronograma.getScheduleDays());
        updates.put("goingTime", cronograma.getGoingTime());
        updates.put("returnTime", cronograma.getReturnTime());

        userRef.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Cronograma atualizado com sucesso!");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Erro ao atualizar o cronograma: " + e.getMessage());
                });
    }

    public boolean isScheduledForToday() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String today = getDayString(dayOfWeek);

        return cronograma != null && cronograma.containsDay(today); // use o método containsDay
    }

    public CronogramaModel getCronograma() {
        return cronograma;
    }

    private String getDayString(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY: return "segunda";
            case Calendar.TUESDAY: return "terça";
            case Calendar.WEDNESDAY: return "quarta";
            case Calendar.THURSDAY: return "quinta";
            case Calendar.FRIDAY: return "sexta";
            case Calendar.SATURDAY: return "sábado";
            case Calendar.SUNDAY: return "domingo";
            default: return "";
        }
    }

    public String getEndereco() {
        StringBuilder endereco = new StringBuilder();

        if (rua != null && !rua.isEmpty()) {
            endereco.append(rua);
        }
        if (numeroCasa != null && !numeroCasa.isEmpty()) {
            endereco.append(", ").append(numeroCasa);
        }
        if (bairro != null && !bairro.isEmpty()) {
            endereco.append(", ").append(bairro);
        }
        if (cidade != null && !cidade.isEmpty()) {
            endereco.append(", ").append(cidade);
        }
        if (estado != null && !estado.isEmpty()) {
            endereco.append(", ").append(estado);
        }

        return endereco.toString();
    }

}
