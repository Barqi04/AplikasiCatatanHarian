/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.CatatanController;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Catatan;

import java.sql.SQLException; 
import java.text.SimpleDateFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;



/**
 *
 * @author USER
 */
public class CatatanHarianFrame extends javax.swing.JFrame {

    private DefaultTableModel model; 
    private CatatanController controller;

    /**
     * Creates new form CatatanHarianFrame
     */
    public CatatanHarianFrame() {
        initComponents();
        controller = new CatatanController();
        
        txtAreaPreview.setEditable(false);
        txtAreaPreview.setLineWrap(true);
        txtAreaPreview.setWrapStyleWord(true);

        // Set tanggal otomatis ke hari ini saat aplikasi dibuka
        dateChooserTanggal.setDate(new java.util.Date());

        model = new DefaultTableModel(
            new String[]{"ID", "Judul", "Isi Catatan", "Tanggal", "Status"}, 0
        );

        tableNotes.setModel(model);

        loadNotes();
    }
    
    private void loadNotes() {
        try {
            model.setRowCount(0); // bersihkan tabel

            List<Catatan> notes = controller.getAllNotes();

            for (Catatan note : notes) {
                model.addRow(new Object[]{
                    note.getId(),
                    note.getJudul(),
                    note.getIsi(),
                    note.getTanggal(),
                    note.getStatus()
                });
            }

        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }
    
    private void showError (String message){
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    
    private void addNote() {

        String judul = txtJudulCatatan.getText().trim();
        String isi = txtAreaIsiCatatan.getText().trim();

        if (judul.isEmpty() || isi.isEmpty() || dateChooserTanggal.getDate() == null) {
            showError("Semua data wajib diisi!");
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tanggal = sdf.format(dateChooserTanggal.getDate());

            String status = radioBelumSelesai.isSelected() ? "Belum Selesai" : "Selesai";

            controller.addNote(judul, isi, tanggal, status);
            loadNotes();
            clearFields();

            JOptionPane.showMessageDialog(this, "Catatan berhasil ditambahkan!");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    private void editNote() {

        int row = tableNotes.getSelectedRow();
        if (row == -1) {
            showError("Pilih catatan yang akan diedit!");
            return;
        }

        try {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());

            String judul = txtJudulCatatan.getText().trim();
            String isi = txtAreaIsiCatatan.getText().trim();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tanggal = sdf.format(dateChooserTanggal.getDate());

            String status = radioBelumSelesai.isSelected() ? "Belum Selesai" : "Selesai";

            controller.updateNote(id, judul, isi, tanggal, status);

            loadNotes();
            clearFields();

            JOptionPane.showMessageDialog(this, "Catatan berhasil diperbarui!");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    private void searchNote() {
        try {
            String keyword = txtSearch.getText().trim();

            model.setRowCount(0);

            for (Catatan note : controller.searchNotes(keyword)) {
                model.addRow(new Object[]{
                    note.getId(),
                    note.getJudul(),
                    note.getIsi(),
                    note.getTanggal(),
                    note.getStatus()
                });
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    private void deleteNote() {
    int row = tableNotes.getSelectedRow();
    if (row == -1) {
        showError("Pilih catatan yang akan dihapus!");
        return;
    }

    try {
        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        controller.deleteNote(id);

        loadNotes();
        clearFields();

        JOptionPane.showMessageDialog(this, "Catatan berhasil dihapus!");

    } catch (Exception e) {
        showError(e.getMessage());
    }
}
    
    private void populateFields() {
        int row = tableNotes.getSelectedRow();
        if (row == -1) return;

        txtJudulCatatan.setText(model.getValueAt(row, 1).toString());
        txtAreaIsiCatatan.setText(model.getValueAt(row, 2).toString());

        try {
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd")
                    .parse(model.getValueAt(row, 3).toString());
            dateChooserTanggal.setDate(date);
        } catch (Exception e) {}

        String status = model.getValueAt(row, 4).toString();
        if (status.equals("Belum Selesai"))
            radioBelumSelesai.setSelected(true);
        else
            radioSelesai.setSelected(true);
    }
    
    
    
    private void clearFields() {
        txtJudulCatatan.setText("");
        txtAreaIsiCatatan.setText("");
        dateChooserTanggal.setDate(null);
        radioBelumSelesai.setSelected(true);
        txtAreaPreview.setText("");
    }
    
    private void exportCSV() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Data Catatan");

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getAbsolutePath().endsWith(".csv"))
            file = new File(file.getAbsolutePath() + ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("ID,Judul,Isi,Tanggal,Status\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                writer.write(
                    model.getValueAt(i, 0) + "," +
                    model.getValueAt(i, 1) + "," +
                    model.getValueAt(i, 2) + "," +
                    model.getValueAt(i, 3) + "," +
                    model.getValueAt(i, 4) + "\n"
                );
            }

            JOptionPane.showMessageDialog(this, "Export berhasil!");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void importCSV() {

    showCSVGuide(); // Tampilkan panduan format CSV

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Apakah Anda yakin file CSV yang dipilih sudah sesuai dengan format?",
        "Konfirmasi Impor CSV",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Import Data Catatan");

    int result = chooser.showOpenDialog(this);
    if (result != JFileChooser.APPROVE_OPTION) return;

    File file = chooser.getSelectedFile();

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

        String header = reader.readLine(); // baca header

        if (!validateCSVHeader(header)) {
            JOptionPane.showMessageDialog(
                this,
                "Header CSV tidak valid!\nHeader wajib: ID,Judul,Isi,Tanggal,Status",
                "Kesalahan CSV",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String line;
        int rowCount = 0;
        int errorCount = 0;

        StringBuilder errorLog = new StringBuilder("Kesalahan pada baris:\n");

        while ((line = reader.readLine()) != null) {
            rowCount++;

            String[] parts = line.split(",");

            if (parts.length < 5) {
                errorCount++;
                errorLog.append("Baris ").append(rowCount + 1)
                        .append(": Jumlah kolom tidak sesuai.\n");
                continue;
            }

            String judul = parts[1].trim();
            String isi = parts[2].trim();
            String tanggal = parts[3].trim();
            String status = parts[4].trim();

            // Validasi wajib isi
            if (judul.isEmpty() || isi.isEmpty()) {
                errorCount++;
                errorLog.append("Baris ").append(rowCount + 1)
                        .append(": Judul atau Isi kosong.\n");
                continue;
            }

            // Validasi status
            if (!status.equalsIgnoreCase("Selesai") &&
                !status.equalsIgnoreCase("Belum Selesai")) {

                errorCount++;
                errorLog.append("Baris ").append(rowCount + 1)
                        .append(": Status tidak valid. Gunakan 'Selesai' atau 'Belum Selesai'.\n");
                continue;
            }

            try {
                controller.addNote(judul, isi, tanggal, status);
            } catch (SQLException ex) {
                errorCount++;
                errorLog.append("Baris ").append(rowCount + 1)
                        .append(": Gagal menyimpan ke database - ")
                        .append(ex.getMessage()).append("\n");
            }
        }

        loadNotes();

        if (errorCount > 0) {
            JOptionPane.showMessageDialog(
                this,
                errorLog.toString(),
                "Beberapa Data Tidak Masuk",
                JOptionPane.WARNING_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(this, "Semua data berhasil diimpor!");
        }

        } catch (Exception e) {
            showError("Gagal membaca file: " + e.getMessage());
        }
    }
    
    private void showCSVGuide() {
    String message =
        "Format CSV untuk impor catatan:\n" +
        "- Header wajib: ID,Judul,Isi,Tanggal,Status\n" +
        "- ID boleh kosong (diabaikan)\n" +
        "- Judul dan Isi wajib diisi\n" +
        "- Status hanya boleh: Selesai / Belum Selesai\n\n" +
        "Contoh:\n" +
        "1,Mengerjakan PBO,Hari ini saya belajar PBO,2025-11-16,Selesai\n" +
        "2,Olahraga,Pergi lari pagi,2025-11-15,Belum Selesai\n";

        JOptionPane.showMessageDialog(
            this,
            message,
            "Panduan CSV",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private boolean validateCSVHeader(String header) {
        return header != null &&
            header.trim().equalsIgnoreCase("ID,Judul,Isi,Tanggal,Status");
    }







    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblJudulCatatan = new javax.swing.JLabel();
        txtJudulCatatan = new javax.swing.JTextField();
        lblIsiCatatan = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaIsiCatatan = new javax.swing.JTextArea();
        lblTanggal = new javax.swing.JLabel();
        dateChooserTanggal = new com.toedter.calendar.JDateChooser();
        lblStatis = new javax.swing.JLabel();
        radioBelumSelesai = new javax.swing.JRadioButton();
        radioSelesai = new javax.swing.JRadioButton();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableNotes = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        btnExport = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();
        lblPreview = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAreaPreview = new javax.swing.JTextArea();
        lblJudulPreview = new javax.swing.JLabel();
        lblTanggalPreview = new javax.swing.JLabel();
        lblTampilJudul = new javax.swing.JLabel();
        lblTampilTanggal = new javax.swing.JLabel();
        lblTampilStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(16, 24, 60));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 35));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 50));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(245, 245, 245));
        jLabel1.setText("Aplikasi Catatan Harian");
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(16, 24, 60));

        lblJudulCatatan.setForeground(new java.awt.Color(245, 245, 245));
        lblJudulCatatan.setText("Judul Catatan");

        lblIsiCatatan.setForeground(new java.awt.Color(245, 245, 245));
        lblIsiCatatan.setText("Isi Catatan");

        txtAreaIsiCatatan.setColumns(20);
        txtAreaIsiCatatan.setRows(5);
        jScrollPane1.setViewportView(txtAreaIsiCatatan);

        lblTanggal.setForeground(new java.awt.Color(245, 245, 245));
        lblTanggal.setText("Tanggal");

        lblStatis.setForeground(new java.awt.Color(245, 245, 245));
        lblStatis.setText("Status");

        buttonGroup1.add(radioBelumSelesai);
        radioBelumSelesai.setForeground(new java.awt.Color(245, 245, 245));
        radioBelumSelesai.setText("Belum Selesai");

        buttonGroup1.add(radioSelesai);
        radioSelesai.setForeground(new java.awt.Color(245, 245, 245));
        radioSelesai.setText("Selesai");

        btnTambah.setBackground(new java.awt.Color(24, 158, 101));
        btnTambah.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(245, 245, 245));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(241, 196, 15));
        btnEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(0, 0, 0));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(230, 68, 72));
        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(245, 245, 245));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        tableNotes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableNotes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableNotesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableNotes);

        txtSearch.setText("Search...");
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchFocusGained(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        btnExport.setBackground(new java.awt.Color(88, 155, 250));
        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExport.setForeground(new java.awt.Color(245, 245, 245));
        btnExport.setText("Export");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        btnImport.setBackground(new java.awt.Color(88, 155, 250));
        btnImport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnImport.setForeground(new java.awt.Color(245, 245, 245));
        btnImport.setText("Import");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        lblPreview.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblPreview.setForeground(new java.awt.Color(245, 245, 245));
        lblPreview.setText("PREVIEW");

        txtAreaPreview.setColumns(20);
        txtAreaPreview.setRows(5);
        jScrollPane4.setViewportView(txtAreaPreview);

        lblJudulPreview.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblJudulPreview.setForeground(new java.awt.Color(245, 245, 245));
        lblJudulPreview.setText("Judul :");

        lblTanggalPreview.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTanggalPreview.setForeground(new java.awt.Color(245, 245, 245));
        lblTanggalPreview.setText("Tanggal :");

        lblTampilJudul.setForeground(new java.awt.Color(245, 245, 245));

        lblTampilTanggal.setForeground(new java.awt.Color(245, 245, 245));

        lblTampilStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTampilStatus.setForeground(new java.awt.Color(245, 245, 245));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTambah))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnImport)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnExport))
                            .addComponent(txtSearch))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblJudulCatatan)
                                .addComponent(lblIsiCatatan)
                                .addComponent(lblTanggal)
                                .addComponent(lblStatis))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(radioBelumSelesai)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(radioSelesai)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(txtJudulCatatan)
                                .addComponent(dateChooserTanggal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPreview)
                        .addGap(185, 185, 185))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTampilStatus)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(lblJudulPreview)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblTampilJudul)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTanggalPreview)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblTampilTanggal))
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblJudulCatatan)
                                    .addComponent(txtJudulCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPreview)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblJudulPreview)
                                    .addComponent(lblTanggalPreview)
                                    .addComponent(lblTampilJudul)
                                    .addComponent(lblTampilTanggal))))
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIsiCatatan)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTanggal)
                            .addComponent(dateChooserTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatis)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(radioBelumSelesai)
                                .addComponent(radioSelesai)))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTambah)
                            .addComponent(btnEdit)
                            .addComponent(btnHapus))
                        .addGap(18, 18, 18)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExport)
                    .addComponent(btnImport)
                    .addComponent(lblTampilStatus))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        addNote();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        editNote();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        deleteNote();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        searchNote();
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        if (txtSearch.getText().equals("Search...")) {
            txtSearch.setText("");
            txtSearch.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtSearchFocusGained

    private void tableNotesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableNotesMouseClicked
        populateFields();
        
        int row = tableNotes.getSelectedRow();
        if (row != -1) {
            String judul = model.getValueAt(row, 1).toString();
            String isi = model.getValueAt(row, 2).toString();
            String tanggal = model.getValueAt(row, 3).toString();
            String status = model.getValueAt(row, 4).toString();


            // Set ke preview
            lblTampilJudul.setText(judul);
            lblTampilTanggal.setText(tanggal);
            lblTampilStatus.setText(status);
            txtAreaPreview.setText(isi);
        }
    }//GEN-LAST:event_tableNotesMouseClicked

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        exportCSV();
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        importCSV();
    }//GEN-LAST:event_btnImportActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CatatanHarianFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CatatanHarianFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CatatanHarianFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CatatanHarianFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CatatanHarianFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnTambah;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser dateChooserTanggal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblIsiCatatan;
    private javax.swing.JLabel lblJudulCatatan;
    private javax.swing.JLabel lblJudulPreview;
    private javax.swing.JLabel lblPreview;
    private javax.swing.JLabel lblStatis;
    private javax.swing.JLabel lblTampilJudul;
    private javax.swing.JLabel lblTampilStatus;
    private javax.swing.JLabel lblTampilTanggal;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JLabel lblTanggalPreview;
    private javax.swing.JRadioButton radioBelumSelesai;
    private javax.swing.JRadioButton radioSelesai;
    private javax.swing.JTable tableNotes;
    private javax.swing.JTextArea txtAreaIsiCatatan;
    private javax.swing.JTextArea txtAreaPreview;
    private javax.swing.JTextField txtJudulCatatan;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
