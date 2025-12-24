package org.example.gui;

import org.example.database.DbConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.Date; // SQL Tarih formatı için

public class RaporEkrani extends JFrame {

    private JTextField txtBaslangic, txtBitis;
    private JLabel lblSonuc;

    public RaporEkrani() {
        setTitle("Ciro Raporu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- Başlık ---
        JLabel lblBaslik = new JLabel("Tarih Aralıklı Ciro Hesapla");
        lblBaslik.setFont(new Font("Arial", Font.BOLD, 16));
        lblBaslik.setBounds(80, 20, 250, 30);
        add(lblBaslik);

        // --- Başlangıç Tarihi ---
        JLabel lblBas = new JLabel("Başlangıç (YYYY-AA-GG):");
        lblBas.setBounds(30, 70, 150, 25);
        add(lblBas);

        txtBaslangic = new JTextField("2020-01-01"); // Örnek tarih
        txtBaslangic.setBounds(190, 70, 120, 25);
        add(txtBaslangic);

        // --- Bitiş Tarihi ---
        JLabel lblBit = new JLabel("Bitiş (YYYY-AA-GG):");
        lblBit.setBounds(30, 110, 150, 25);
        add(lblBit);

        txtBitis = new JTextField("2030-12-31"); // Örnek tarih
        txtBitis.setBounds(190, 110, 120, 25);
        add(txtBitis);

        // --- Hesapla Butonu ---
        JButton btnHesapla = new JButton("HESAPLA");
        btnHesapla.setBounds(120, 160, 150, 40);
        btnHesapla.setBackground(new Color(0, 102, 204));
        btnHesapla.setForeground(Color.WHITE);
        add(btnHesapla);

        // --- Sonuç Etiketi ---
        lblSonuc = new JLabel("Toplam Ciro: 0.00 TL");
        lblSonuc.setFont(new Font("Arial", Font.BOLD, 18));
        lblSonuc.setForeground(new Color(34, 139, 34)); // Yeşil renk
        lblSonuc.setBounds(80, 220, 250, 30);
        add(lblSonuc);

        // --- Buton Olayı ---
        btnHesapla.addActionListener(e -> ciroHesapla());

        setVisible(true);
    }

    private void ciroHesapla() {
        try {
            Connection conn = DbConnection.getConnection();

            String sql = "{ ? = call ciro_hesapla(?, ?) }";
            CallableStatement cs = conn.prepareCall(sql);

            // DÜZELTME 1: Veritabanı ne gönderiyorsa (NUMERIC) onu beklediğimizi söylüyoruz.
            cs.registerOutParameter(1, Types.NUMERIC);

            cs.setDate(2, Date.valueOf(txtBaslangic.getText()));
            cs.setDate(3, Date.valueOf(txtBitis.getText()));

            cs.execute();

            // DÜZELTME 2: Veriyi en güvenli yol olan BigDecimal olarak alıyoruz.
            // (Direkt getDouble yapmak bazen sürücü hatasına yol açabiliyor)
            java.math.BigDecimal sonuc = cs.getBigDecimal(1);

            // Eğer hiç satış yoksa null gelebilir, o zaman 0.0 kabul edelim
            double ciro = (sonuc != null) ? sonuc.doubleValue() : 0.0;

            lblSonuc.setText("Toplam Ciro: " + ciro + " TL");

            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hata Detayı: " + ex.getMessage());
        }
    }
}