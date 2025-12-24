package org.example.gui;

import org.example.database.DbConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AracEkrani extends JFrame {

    // KUTULAR
    private JTextField txtMarka;
    private JTextField txtModel;

    private JTextField txtYil, txtFiyat, txtKm, txtPlaka, txtRenk; // KM eklendi

    private JTable table;
    private DefaultTableModel model;

    private int secilenAracID = -1;

    public AracEkrani() {
        setTitle("Araç Yönetim Paneli");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- SOL TARAFTAKİ KUTULAR ---

        JLabel lblMarka = new JLabel("Marka:");
        lblMarka.setBounds(20, 20, 80, 25);
        add(lblMarka);

        txtMarka = new JTextField();
        txtMarka.setBounds(100, 20, 150, 25);
        add(txtMarka);

        JLabel lblModel = new JLabel("Model:");
        lblModel.setBounds(20, 60, 80, 25);
        add(lblModel);

        txtModel = new JTextField();
        txtModel.setBounds(100, 60, 150, 25);
        add(txtModel);

        // --- SAĞ TARAFTAKİ KUTULAR ---

        JLabel lblYil = new JLabel("Yıl:");
        lblYil.setBounds(300, 20, 80, 25);
        add(lblYil);
        txtYil = new JTextField();
        txtYil.setBounds(360, 20, 150, 25);
        add(txtYil);

        // --- YENİ EKLENEN KM ALANI ---
        JLabel lblKm = new JLabel("KM:");
        lblKm.setBounds(300, 60, 80, 25);
        add(lblKm);
        txtKm = new JTextField();
        txtKm.setBounds(360, 60, 150, 25);
        add(txtKm);
        // -----------------------------

        JLabel lblFiyat = new JLabel("Fiyat:");
        lblFiyat.setBounds(300, 100, 80, 25);
        add(lblFiyat);
        txtFiyat = new JTextField();
        txtFiyat.setBounds(360, 100, 150, 25);
        add(txtFiyat);

        JLabel lblPlaka = new JLabel("Plaka:");
        lblPlaka.setBounds(550, 20, 80, 25);
        add(lblPlaka);
        txtPlaka = new JTextField();
        txtPlaka.setBounds(600, 20, 150, 25);
        add(txtPlaka);

        JLabel lblRenk = new JLabel("Renk:");
        lblRenk.setBounds(550, 60, 80, 25);
        add(lblRenk);
        txtRenk = new JTextField();
        txtRenk.setBounds(600, 60, 150, 25);
        add(txtRenk);

        // --- BUTONLAR ---
        JButton btnEkle = new JButton("EKLE");
        btnEkle.setBounds(100, 160, 100, 30);
        add(btnEkle);

        JButton btnGuncelle = new JButton("GÜNCELLE");
        btnGuncelle.setBounds(210, 160, 100, 30);
        add(btnGuncelle);

        JButton btnSil = new JButton("SİL");
        btnSil.setBounds(320, 160, 100, 30);
        add(btnSil);

        JButton btnListele = new JButton("LİSTELE");
        btnListele.setBounds(430, 160, 100, 30);
        add(btnListele);

        // --- TABLO ---
        // Tablo başlıklarına KM'yi ekledim
        String[] kolonlar = {"Araç No", "Marka", "Model", "Yıl", "KM", "Fiyat", "Plaka", "Renk", "Durum"};
        model = new DefaultTableModel(kolonlar, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 210, 840, 330);
        add(scrollPane);

        // --- Olaylar ---
        araclariGetir();

        btnEkle.addActionListener(e -> aracEkle());
        btnGuncelle.addActionListener(e -> aracGuncelle());
        btnSil.addActionListener(e -> aracSil());
        btnListele.addActionListener(e -> araclariGetir());

        // Tabloya tıklayınca kutuları doldur
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int satir = table.getSelectedRow();

                secilenAracID = Integer.parseInt(model.getValueAt(satir, 0).toString());

                txtMarka.setText(model.getValueAt(satir, 1).toString());
                txtModel.setText(model.getValueAt(satir, 2).toString());

                txtYil.setText(model.getValueAt(satir, 3).toString());
                txtKm.setText(model.getValueAt(satir, 4).toString());     // KM verisini çek
                txtFiyat.setText(model.getValueAt(satir, 5).toString());
                txtPlaka.setText(model.getValueAt(satir, 6).toString());
                txtRenk.setText(model.getValueAt(satir, 7).toString());
            }
        });

        setVisible(true);
    }

    // --- METODLAR ---

    private void araclariGetir() {
        model.setRowCount(0);
        try {
            Connection conn = DbConnection.getConnection();
            // Sorguya KM eklendi
            String sql = "SELECT A.AracID, MK.MarkaAd, MD.ModelAd, A.Yil, A.Kilometre, A.Fiyat, A.Plaka, A.Renk, A.Durum " +
                    "FROM Tbl_Araclar A " +
                    "JOIN Tbl_Modeller MD ON A.ModelID = MD.ModelID " +
                    "JOIN Tbl_Markalar MK ON MD.MarkaID = MK.MarkaID " +
                    "ORDER BY A.AracID ASC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] satir = {
                        rs.getInt("AracID"),
                        rs.getString("MarkaAd"),
                        rs.getString("ModelAd"),
                        rs.getInt("Yil"),
                        rs.getInt("Kilometre"), // KM verisi
                        rs.getDouble("Fiyat"),
                        rs.getString("Plaka"),
                        rs.getString("Renk"),
                        rs.getString("Durum")
                };
                model.addRow(satir);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void aracEkle() {
        try {
            Connection conn = DbConnection.getConnection();

            String girilenMarka = txtMarka.getText().trim();
            String girilenModel = txtModel.getText().trim();

            // --- ADIM 1: MARKA KONTROLÜ VE EKLEME ---
            int markaID = -1;
            String markaBulSQL = "SELECT MarkaID FROM Tbl_Markalar WHERE MarkaAd = ?";
            PreparedStatement psMarkaBul = conn.prepareStatement(markaBulSQL);
            psMarkaBul.setString(1, girilenMarka);
            ResultSet rsMarka = psMarkaBul.executeQuery();

            if (rsMarka.next()) {
                markaID = rsMarka.getInt("MarkaID"); // Marka zaten varmış
            } else {
                // Marka yok, hemen ekleyelim
                String markaEkleSQL = "INSERT INTO Tbl_Markalar (MarkaAd) VALUES (?) RETURNING MarkaID";
                PreparedStatement psMarkaEkle = conn.prepareStatement(markaEkleSQL);
                psMarkaEkle.setString(1, girilenMarka);
                ResultSet rsYeniMarka = psMarkaEkle.executeQuery();
                if (rsYeniMarka.next()) {
                    markaID = rsYeniMarka.getInt("MarkaID"); // Yeni ID'yi aldık
                }
            }

            // --- ADIM 2: MODEL KONTROLÜ VE EKLEME ---
            int modelID = -1;
            String modelBulSQL = "SELECT ModelID FROM Tbl_Modeller WHERE ModelAd = ? AND MarkaID = ?";
            PreparedStatement psModelBul = conn.prepareStatement(modelBulSQL);
            psModelBul.setString(1, girilenModel);
            psModelBul.setInt(2, markaID);
            ResultSet rsModel = psModelBul.executeQuery();

            if (rsModel.next()) {
                modelID = rsModel.getInt("ModelID"); // Model zaten varmış
            } else {
                // Model yok, hemen ekleyelim
                String modelEkleSQL = "INSERT INTO Tbl_Modeller (MarkaID, ModelAd) VALUES (?, ?) RETURNING ModelID";
                PreparedStatement psModelEkle = conn.prepareStatement(modelEkleSQL);
                psModelEkle.setInt(1, markaID);
                psModelEkle.setString(2, girilenModel);
                ResultSet rsYeniModel = psModelEkle.executeQuery();
                if (rsYeniModel.next()) {
                    modelID = rsYeniModel.getInt("ModelID"); // Yeni ID'yi aldık
                }
            }

            // --- ADIM 3: ARACI KAYDETME ---
            String sql = "INSERT INTO Tbl_Araclar (ModelID, Yil, Kilometre, Fiyat, Plaka, Renk, Durum) VALUES (?, ?, ?, ?, ?, ?, 'Satılık')";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, modelID); // Artık elimizde kesinlikle bir ID var
            ps.setInt(2, Integer.parseInt(txtYil.getText()));
            ps.setInt(3, Integer.parseInt(txtKm.getText()));
            ps.setDouble(4, Double.parseDouble(txtFiyat.getText()));
            ps.setString(5, txtPlaka.getText());
            ps.setString(6, txtRenk.getText());

            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(null, "Araç ve (gerekirse) Marka/Model Başarıyla Eklendi!");
            araclariGetir();

            txtPlaka.setText(""); // Temizlik

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Lütfen Yıl, KM ve Fiyat alanlarına SAYI giriniz!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
        }
    }

    private void aracGuncelle() {
        if (secilenAracID == -1) {
            JOptionPane.showMessageDialog(null, "Lütfen tablodan bir araç seçin!");
            return;
        }
        try {
            Connection conn = DbConnection.getConnection();
            // UPDATE Sorgusuna KM eklendi
            String sql = "UPDATE Tbl_Araclar SET Yil=?, Kilometre=?, Fiyat=?, Plaka=?, Renk=? WHERE AracID=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(txtYil.getText()));
            ps.setInt(2, Integer.parseInt(txtKm.getText())); // KM
            ps.setDouble(3, Double.parseDouble(txtFiyat.getText()));
            ps.setString(4, txtPlaka.getText());
            ps.setString(5, txtRenk.getText());
            ps.setInt(6, secilenAracID);

            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(null, "Güncellendi!");
            araclariGetir();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
        }
    }

    private void aracSil() {
        if (secilenAracID == -1) return;
        try {
            Connection conn = DbConnection.getConnection();
            String sql = "DELETE FROM Tbl_Araclar WHERE AracID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, secilenAracID);
            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(null, "Silindi!");
            araclariGetir();
            secilenAracID = -1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
        }
    }
}