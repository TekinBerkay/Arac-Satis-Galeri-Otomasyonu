package org.example.gui;

import org.example.database.DbConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class MusteriEkrani extends JFrame {

    private JTextField txtAdSoyad, txtTelefon, txtTc, txtAdres;
    private JTable table;
    private DefaultTableModel model;

    // Seçili satırın ID'si
    private int secilenMusteriID = -1;

    public MusteriEkrani() {
        setTitle("Müşteri Yönetimi");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- Etiketler ve Kutular ---

        JLabel lblAd = new JLabel("Ad Soyad:");
        lblAd.setBounds(20, 20, 80, 25);
        add(lblAd);

        txtAdSoyad = new JTextField();
        txtAdSoyad.setBounds(100, 20, 150, 25);
        add(txtAdSoyad);

        JLabel lblTel = new JLabel("Telefon:");
        lblTel.setBounds(20, 60, 80, 25);
        add(lblTel);

        txtTelefon = new JTextField();
        txtTelefon.setBounds(100, 60, 150, 25);
        add(txtTelefon);

        JLabel lblTc = new JLabel("TC No:");
        lblTc.setBounds(300, 20, 80, 25);
        add(lblTc);

        txtTc = new JTextField();
        txtTc.setBounds(360, 20, 150, 25);
        add(txtTc);

        JLabel lblAdres = new JLabel("Adres:");
        lblAdres.setBounds(300, 60, 80, 25);
        add(lblAdres);

        txtAdres = new JTextField();
        txtAdres.setBounds(360, 60, 250, 25);
        add(txtAdres);

        // --- Butonlar ---

        JButton btnListele = new JButton("LİSTELE");
        btnListele.setBounds(50, 110, 100, 30);
        add(btnListele);

        JButton btnEkle = new JButton("EKLE");
        btnEkle.setBounds(160, 110, 100, 30);
        add(btnEkle);

        JButton btnGuncelle = new JButton("GÜNCELLE");
        btnGuncelle.setBounds(270, 110, 100, 30);
        add(btnGuncelle);

        JButton btnSil = new JButton("SİL");
        btnSil.setBounds(380, 110, 100, 30);
        add(btnSil);

        // --- Tablo ---

        String[] kolonlar = {"ID", "Ad Soyad", "Telefon", "TC No", "Adres"};
        model = new DefaultTableModel(kolonlar, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 160, 740, 280);
        add(scrollPane);

        // --- Olaylar ---

        btnListele.addActionListener(e -> musterileriGetir());
        btnEkle.addActionListener(e -> musteriEkle());
        btnGuncelle.addActionListener(e -> musteriGuncelle());
        btnSil.addActionListener(e -> musteriSil());

        // Tabloya tıklayınca verileri kutulara doldur
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int satir = table.getSelectedRow();
                secilenMusteriID = Integer.parseInt(model.getValueAt(satir, 0).toString());

                txtAdSoyad.setText(model.getValueAt(satir, 1).toString());
                txtTelefon.setText(model.getValueAt(satir, 2).toString());
                txtTc.setText(model.getValueAt(satir, 3).toString());
                txtAdres.setText(model.getValueAt(satir, 4).toString());
            }
        });

        // Açılışta listele
        musterileriGetir();

        setVisible(true);
    }

    // --- METODLAR ---

    private void musterileriGetir() {
        model.setRowCount(0);
        try {
            Connection conn = DbConnection.getConnection();
            String sql = "SELECT * FROM Tbl_Musteriler ORDER BY MusteriID ASC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] satir = {
                        rs.getInt("MusteriID"),
                        rs.getString("AdSoyad"),
                        rs.getString("Telefon"),
                        rs.getString("TCNo"),
                        rs.getString("Adres")
                };
                model.addRow(satir);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void musteriEkle() {
        try {
            Connection conn = DbConnection.getConnection();
            String sql = "INSERT INTO Tbl_Musteriler (AdSoyad, Telefon, TCNo, Adres) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtAdSoyad.getText());
            ps.setString(2, txtTelefon.getText());
            ps.setString(3, txtTc.getText());
            ps.setString(4, txtAdres.getText());

            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Müşteri Eklendi!");
            musterileriGetir();
            txtAdSoyad.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
        }
    }

    private void musteriGuncelle() {
        if (secilenMusteriID == -1) return;
        try {
            Connection conn = DbConnection.getConnection();
            String sql = "UPDATE Tbl_Musteriler SET AdSoyad=?, Telefon=?, TCNo=?, Adres=? WHERE MusteriID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtAdSoyad.getText());
            ps.setString(2, txtTelefon.getText());
            ps.setString(3, txtTc.getText());
            ps.setString(4, txtAdres.getText());
            ps.setInt(5, secilenMusteriID);

            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Müşteri Güncellendi!");
            musterileriGetir();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
        }
    }

    private void musteriSil() {
        if (secilenMusteriID == -1) {
            JOptionPane.showMessageDialog(null, "Seçim yapmadınız!");
            return;
        }
        try {
            Connection conn = DbConnection.getConnection();
            String sql = "DELETE FROM Tbl_Musteriler WHERE MusteriID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, secilenMusteriID);
            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Müşteri Silindi!");
            musterileriGetir();
            secilenMusteriID = -1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
        }
    }
}