/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author USER
 */
public class Catatan {
    private int id;
    private String judul;
    private String isi;
    private String tanggal;
    private String status;
    
    public Catatan(int id, String judul, String isi, String tanggal, String status) {
        this.id = id;
        this.judul = judul;
        this.isi = isi;
        this.tanggal = tanggal;
        this.status = status;
    }

    // GETTER & SETTER
    public int getId() { return id; }
        public void setId(int id) { this.id = id;
    }

    public String getJudul() { return judul; }
        public void setJudul(String judul) { this.judul = judul; 
    }

    public String getIsi() { return isi; }
        public void setIsi(String isi) { this.isi = isi; 
    }

    public String getTanggal() { return tanggal; }
        public void setTanggal(String tanggal) { this.tanggal = tanggal; 
    }
    
    public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; 
    }
}
