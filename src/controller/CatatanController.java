/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.sql.SQLException;
import java.util.List;
import model.Catatan;
import model.CatatanDAO;
/**
 *
 * @author USER
 */
public class CatatanController {
    private CatatanDAO noteDAO;

    public CatatanController() {
        noteDAO = new CatatanDAO();
    }

    // Method mengambil semua catatan
    public List<Catatan> getAllNotes() throws SQLException {
        return noteDAO.getAllNotes();
    }

    // Method menambah catatan
    public void addNote(String judul, String isi, String tanggal, String status) throws SQLException {
        Catatan note = new Catatan(0, judul, isi, tanggal, status);
        noteDAO.addNote(note);
    }

    // Method mengupdate catatan
    public void updateNote(int id, String judul, String isi, String tanggal, String status) throws SQLException {
        Catatan note = new Catatan(id, judul, isi, tanggal, status);
        noteDAO.updateNote(note);
    }

    // Method menghapus catatan
    public void deleteNote(int id) throws SQLException {
        noteDAO.deleteNote(id);
    }

    // Method pencarian catatan
    public List<Catatan> searchNotes(String keyword) throws SQLException {
        return noteDAO.searchNotes(keyword);
    }
}
