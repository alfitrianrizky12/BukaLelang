package com.example.slelangonline.Lelang;

public class Lelang {
    private String idLelang;
    private String picTips;
    private String titleTips;
    private String namePemenang;
    private int hargaAwal;
    private int hargaAkhir;
    private String content;
    private String createAt;
    private String closeAt;
    private String pemilik;
    private String status;
    public Lelang(String idLelang, String picTips, String titleTips, String content, String pemilik, String createAt, String closeAt,int hargaAwal, int hargaAkhir, String namePemenang, String status) {
        this.idLelang = idLelang;
        this.picTips = picTips;
        this.titleTips = titleTips;
        this.content = content;
        this.pemilik= pemilik;
        this.createAt = createAt;
        this.closeAt = closeAt;
        this.hargaAwal= hargaAwal;
        this.hargaAkhir=hargaAkhir;
        this.namePemenang = namePemenang;
        this.status=status;
    }

    public Lelang() {}

    public String getIdLelang() {
        return idLelang;
    }

    public void setIdLelang(String idLelang) {
        this.idLelang = idLelang;
    }

    public String getPicTips() {
        return picTips;
    }

    public void setPicTips(String picTips) {
        this.picTips = picTips;
    }

    public String getTitleTips() {
        return titleTips;
    }

    public void setTitleTips(String titleTips) {
        this.titleTips = titleTips;
    }

    public String getNamePemenang() {
        return namePemenang;
    }

    public void setNamePemenang(String namePemenang) {
        this.namePemenang = namePemenang;
    }

    public int getHargaAwal() {
        return hargaAwal;
    }

    public void setHargaAwal(int hargaAwal) {
        this.hargaAwal = hargaAwal;
    }

    public int getHargaAkhir() {
        return hargaAkhir;
    }

    public void setHargaAkhir(int hargaAkhir) {
        this.hargaAkhir = hargaAkhir;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(String closeAt) {
        this.closeAt = closeAt;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
