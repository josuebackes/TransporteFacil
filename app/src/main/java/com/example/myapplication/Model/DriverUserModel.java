package com.example.myapplication.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverUserModel {
    private String id;
    private String nome;
    private String sobrenome;
    private String cpf;
    private String cnh;
    private String phone;
    private String birth;
    private String email;
    private String userType;

    public DriverUserModel(){
    }

    public DriverUserModel(String id, String nome, String sobrenome, String cnh, String cpf, String phone, String birth, String email, String userType) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cnh = cnh;
        this.cpf = cpf;
        this.phone = phone;
        this.birth = birth;
        this.email = email;
        this.userType = userType;
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

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void Salvar(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("usuarios").child(getId()).setValue(this);
    }
}
