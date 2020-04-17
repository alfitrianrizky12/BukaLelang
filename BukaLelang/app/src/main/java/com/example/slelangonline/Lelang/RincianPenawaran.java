package com.example.slelangonline.Lelang;

public class RincianPenawaran {
    String IdRincianPenawaran,Penawar,Harga,NoTelp,CreateAt;

    public RincianPenawaran(String idRincianPenawaran, String penawar, String harga,String noTelp, String createAt) {
        IdRincianPenawaran = idRincianPenawaran;
        Penawar = penawar;
        Harga = harga;
        NoTelp= noTelp;
        CreateAt = createAt;
    }

    public String getIdRincianPenawaran() {
        return IdRincianPenawaran;
    }

    public void setIdRincianPenawaran(String idRincianPenawaran) {
        IdRincianPenawaran = idRincianPenawaran;
    }

    public String getPenawar() {
        return Penawar;
    }

    public void setPenawar(String penawar) {
        Penawar = penawar;
    }

    public String getNoTelp() {
        return NoTelp;
    }

    public void setNoTelp(String noTelp) {
        this.NoTelp = NoTelp;
    }

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String harga) {
        Harga = harga;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }
}
