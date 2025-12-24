package org.example.gui;

import org.example.database.DbConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class SatisEkrani extends JFrame {

    // Tablolar ve Modeller
    private JTable tblAraclar, tblMusteriler;
    private DefaultTableModel mdlArac, mdlMusteri;

    // Seçilen ID'leri tutmak için
    private int secilenAracID = -1;
    private int secilenMusteriID = -1;
    private double secilenAracFiyati = 0.0;

    public SatisEkrani() {
        setTitle("Satış İşlemleri");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1, 2)); // Ekranı ikiye böl (Sol: Araçlar, Sağ: Müşteriler)
        setLocationRelativeTo(null);

        // --- SOL PANEL (ARAÇLAR) ---
        JPanel pnlSol = new JPanel();
        pnlSol.setLayout(null);
        pnlSol.setBorder(BorderFactory.createTitledBorder("Adım 1: Satılacak Aracı Seç"));

        // Araç Tablosu
        String[] colArac = {"ID", "Marka", "Model", "Fiyat", "Plaka"};
        mdlArac = new DefaultTableModel(colArac, 0);
        tblAraclar = new JTable(mdlArac);
        JScrollPane scArac = new JScrollPane(tblAraclar);
        scArac.setBounds(10, 30, 460, 450);
        pnlSol.add(scArac);

        add(pnlSol);

        // --- SAĞ PANEL (MÜŞTERİLER + BUTON) ---
        JPanel pnlSag = new JPanel();
        pnlSag.setLayout(null);
        pnlSag.setBorder(BorderFactory.createTitledBorder("Adım 2: Müşteriyi Seç ve Sat"));

        // Müşteri Tablosu
        String[] colMusteri = {"ID", "Ad Soyad", "TC No"};
        mdlMusteri = new DefaultTableModel(colMusteri, 0);
        tblMusteriler = new JTable(mdlMusteri);
        JScrollPane scMusteri = new JScrollPane(tblMusteriler);
        scMusteri.setBounds(10, 30, 460, 350);
        pnlSag.add(scMusteri);

        // Satış Butonu
        JButton btnSatisYap = new JButton("SATIŞI TAMAMLA");
        btnSatisYap.setBounds(10, 400, 460, 50);
        btnSatisYap.setBackground(new Color(0, 128, 0));
        btnSatisYap.setForeground(Color.WHITE);
        btnSatisYap.setFont(new Font("Arial", Font.BOLD, 18));
        pnlSag.add(btnSatisYap);

        add(pnlSag);

        // --- OLAYLAR ---

        // Araç Seçimi
        tblAraclar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblAraclar.getSelectedRow();
                secilenAracID = Integer.parseInt(mdlArac.getValueAt(row, 0).toString());
                secilenAracFiyati = Double.parseDouble(mdlArac.getValueAt(row, 3).toString());
            }
        });

        // Müşteri Seçimi
        tblMusteriler.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblMusteriler.getSelectedRow();
                secilenMusteriID = Integer.parseInt(mdlMusteri.getValueAt(row, 0).toString());
            }
        });

        // Satış Butonu Tıklama
        btnSatisYap.addActionListener(e -> satisiGerceklestir());

        // Verileri Yükle
        musaitAraclariGetir();
        musterileriGetir();

        setVisible(true);
    }

    // --- METODLAR ---

    private void musaitAraclariGetir() {
        mdlArac.setRowCount(0);
        try {
            Connection conn = DbConnection.getConnection();
            // SADECE 'Satılık' OLANLARI GETİRİYORUZ (Önemli!)
            String sql = "SELECT A.AracID, MK.MarkaAd, MD.ModelAd, A.Fiyat, A.Plaka " +
                    "FROM Tbl_Araclar A " +
                    "JOIN Tbl_Modeller MD ON A.ModelID = MD.ModelID " +
                    "JOIN Tbl_Markalar MK ON MD.MarkaID = MK.MarkaID " +
                    "WHERE A.Durum = 'Satılık' " +  // Filtre burası
                    "ORDER BY A.AracID ASC";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("AracID"),
                        rs.getString("MarkaAd"),
                        rs.getString("ModelAd"),
                        rs.getDouble("Fiyat"),
                        rs.getString("Plaka")
                };
                mdlArac.addRow(row);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void musterileriGetir() {
        mdlMusteri.setRowCount(0);
        try {
            Connection conn = DbConnection.getConnection();
            String sql = "SELECT MusteriID, AdSoyad, TCNo FROM Tbl_Musteriler";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {rs.getInt("MusteriID"), rs.getString("AdSoyad"), rs.getString("TCNo")};
                mdlMusteri.addRow(row);
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void satisiGerceklestir() {
        if (secilenAracID == -1 || secilenMusteriID == -1) {
            JOptionPane.showMessageDialog(null, "Lütfen bir ARAÇ ve bir MÜŞTERİ seçiniz!");
            return;
        }

        try {
            Connection conn = DbConnection.getConnection();

            // Satış Tablosuna Ekleme
            // Not: PersonelID şimdilik 1 (Admin) olarak sabitliyoruz.
            String sql = "INSERT INTO Tbl_Satislar (AracID, MusteriID, PersonelID, SatisFiyati) VALUES (?, ?, 1, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, secilenAracID);
            ps.setInt(2, secilenMusteriID);
            ps.setDouble(3, secilenAracFiyati);

            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(null, "Satış Başarıyla Yapıldı!\n(Trigger sayesinde araç stoktan düştü)");

            // Listeyi yenile ki satılan araç listeden kaybolsun
            musaitAraclariGetir();
            secilenAracID = -1; // Seçimi sıfırla

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
        }
    }
}