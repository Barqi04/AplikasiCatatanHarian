/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.CatatanController;
import java.awt.Color;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Catatan;

import java.sql.SQLException; 
import java.text.SimpleDateFormat;
import java.util.logging.Level; 
import java.util.logging.Logger; 
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

        model = new DefaultTableModel(
            new String[]{"ID", "Judul", "Isi", "Tanggal", "Status"}, 0
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 32));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 35));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Aplikasi Catatan Harian");
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        lblJudulCatatan.setText("Judul Catatan");

        lblIsiCatatan.setText("Isi Catatan");

        txtAreaIsiCatatan.setColumns(20);
        txtAreaIsiCatatan.setRows(5);
        jScrollPane1.setViewportView(txtAreaIsiCatatan);

        lblTanggal.setText("Tanggal");

        lblStatis.setText("Status");

        buttonGroup1.add(radioBelumSelesai);
        radioBelumSelesai.setText("Belum Selesai");

        buttonGroup1.add(radioSelesai);
        radioSelesai.setText("Selesai");

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

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

        btnExport.setText("Export");

        btnImport.setText("Import");

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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblJudulCatatan)
                            .addComponent(lblIsiCatatan)
                            .addComponent(lblTanggal)
                            .addComponent(lblStatis))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dateChooserTanggal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(radioBelumSelesai)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(radioSelesai))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                            .addComponent(txtJudulCatatan))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnImport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExport)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJudulCatatan)
                    .addComponent(txtJudulCatatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport)
                    .addComponent(btnImport))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
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
                            .addComponent(btnHapus)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
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
    }//GEN-LAST:event_tableNotesMouseClicked

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
    private javax.swing.JLabel lblIsiCatatan;
    private javax.swing.JLabel lblJudulCatatan;
    private javax.swing.JLabel lblStatis;
    private javax.swing.JLabel lblTanggal;
    private javax.swing.JRadioButton radioBelumSelesai;
    private javax.swing.JRadioButton radioSelesai;
    private javax.swing.JTable tableNotes;
    private javax.swing.JTextArea txtAreaIsiCatatan;
    private javax.swing.JTextField txtJudulCatatan;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
