package org.example.gui;

import org.example.database.DbConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginEkrani extends JFrame {

    // Ekran Elemanları
    JTextField txtKullaniciAdi;
    JPasswordField txtSifre;
    JButton btnGiris;

    public LoginEkrani() {
        // --- 1. Pencere Ayarları ---
        setTitle("Araç Satış Sistemi - Giriş");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Elemanları koordinatla yerleştirmek için
        setLocationRelativeTo(null); // Ekranın ortasında açılsın

        // --- 2. Elemanları Ekleme ---

        // Kullanıcı Adı Etiketi ve Kutusu
        JLabel lblUser = new JLabel("Kullanıcı Adı:");
        lblUser.setBounds(50, 50, 100, 30);
        add(lblUser);

        txtKullaniciAdi = new JTextField();
        txtKullaniciAdi.setBounds(150, 50, 150, 30);
        add(txtKullaniciAdi);

        // Şifre Etiketi ve Kutusu
        JLabel lblPass = new JLabel("Şifre:");
        lblPass.setBounds(50, 100, 100, 30);
        add(lblPass);

        txtSifre = new JPasswordField();
        txtSifre.setBounds(150, 100, 150, 30);
        add(txtSifre);

        // Giriş Butonu
        btnGiris = new JButton("Giriş Yap");
        btnGiris.setBounds(150, 160, 150, 40);
        add(btnGiris);

        // --- 3. Butona Tıklama Olayı (Veritabanı Kontrolü) ---
        btnGiris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                girisKontrol();
            }
        });

        setVisible(true);
    }

    private void girisKontrol() {
        String kadi = txtKullaniciAdi.getText();
        String sifre = new String(txtSifre.getPassword());

        // Veritabanı Bağlantısı ve Sorgu
        try {
            Connection conn = DbConnection.getConnection();

            // SQL Injection olmaması için '?' kullanıyoruz
            String sql = "SELECT * FROM Tbl_Personel WHERE KullaniciAdi=? AND Sifre=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kadi);
            ps.setString(2, sifre);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Kayıt bulundu!
                String yetki = rs.getString("Yetki");
                JOptionPane.showMessageDialog(null, "Giriş Başarılı! Hoşgeldin: " + kadi + "\nYetki: " + yetki);

                // Giriş başarılıysa Ana Menüyü aç
                new AnaMenu().setVisible(true);
                this.dispose(); // Login ekranını kapat

            } else {
                JOptionPane.showMessageDialog(null, "Hatalı Kullanıcı Adı veya Şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }

            // Bağlantıyı kapat
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage());
        }
    }

    // Test için main metodu
    public static void main(String[] args) {
        new LoginEkrani();
    }
}