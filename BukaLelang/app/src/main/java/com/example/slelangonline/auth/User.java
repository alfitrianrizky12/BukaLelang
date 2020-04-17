package com.example.slelangonline.auth;

public class User {
    String uid,nama, email , password, photoprofile,alamat,noTelp, level;

    public String getNama() {
        return nama;
    }
    public User(){}
    public User(String uid, String nama, String email, String password, String photoprofile, String alamat, String noTelp, String level) {
        this.uid = uid;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.photoprofile = photoprofile;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.level = level;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoprofile() {
        return photoprofile;
    }

    public void setPhotoprofile(String photoprofile) {
        this.photoprofile = photoprofile;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }
}
