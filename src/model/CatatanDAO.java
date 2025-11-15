/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author USER
 */
public class CatatanDAO {
    public List<Catatan> getAllNotes() throws SQLException {
        List<Catatan> notes = new ArrayList<>();

        String sql = "SELECT * FROM notes ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Catatan note = new Catatan(
                    rs.getInt("id"),
                    rs.getString("judul"),
                    rs.getString("isi"),
                    rs.getString("tanggal"),
                    rs.getString("status")
                );
                notes.add(note);
            }
        }
        return notes;
    }

    // ================================
    // Tambah catatan
    // ================================
    public void addNote(Catatan note) throws SQLException {

        String sql = "INSERT INTO notes (judul, isi, tanggal, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, note.getJudul());
            ps.setString(2, note.getIsi());
            ps.setString(3, note.getTanggal());
            ps.setString(4, note.getStatus());

            ps.executeUpdate();
        }
    }

    // ================================
    // Update catatan
    // ================================
    public void updateNote(Catatan note) throws SQLException {

        String sql = "UPDATE notes SET judul=?, isi=?, tanggal=?, status=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, note.getJudul());
            ps.setString(2, note.getIsi());
            ps.setString(3, note.getTanggal());
            ps.setString(4, note.getStatus());
            ps.setInt(5, note.getId());

            ps.executeUpdate();
        }
    }

    // ================================
    // Hapus catatan
    // ================================
    public void deleteNote(int id) throws SQLException {

        String sql = "DELETE FROM notes WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ================================
    // Pencarian catatan
    // ================================
    public List<Catatan> searchNotes(String keyword) throws SQLException {
        List<Catatan> notes = new ArrayList<>();

        String sql = "SELECT * FROM notes WHERE judul LIKE ? OR isi LIKE ? ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Catatan note = new Catatan(
                    rs.getInt("id"),
                    rs.getString("judul"),
                    rs.getString("isi"),
                    rs.getString("tanggal"),
                    rs.getString("status")
                );
                notes.add(note);
            }
        }
        return notes;
    }
}
