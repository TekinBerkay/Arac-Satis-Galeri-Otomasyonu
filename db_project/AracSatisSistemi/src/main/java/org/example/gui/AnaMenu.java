package org.example.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnaMenu extends JFrame {

    public AnaMenu() {
        // --- Pencere Ayarları ---
        setTitle("Oto Galeri Otomasyonu - Ana Menü");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10)); // 2x2 ızgara düzeni
        setLocationRelativeTo(null); // Ortala

        // --- Butonlar ---

        // 1. Araç İşlemleri Butonu
        JButton btnArac = new JButton("ARAÇ YÖNETİMİ");
        btnArac.setFont(new Font("Arial", Font.BOLD, 14));
        btnArac.setBackground(new Color(70, 130, 180)); // Mavi renk
        btnArac.setForeground(Color.WHITE);
        add(btnArac);

        // 2. Müşteri İşlemleri Butonu
        JButton btnMusteri = new JButton("MÜŞTERİ YÖNETİMİ");
        btnMusteri.setFont(new Font("Arial", Font.BOLD, 14));
        btnMusteri.setBackground(new Color(60, 179, 113)); // Yeşil renk
        btnMusteri.setForeground(Color.WHITE);
        add(btnMusteri);

        // 3. Satış Ekranı Butonu
        JButton btnSatis = new JButton("SATIŞ YAP");
        btnSatis.setFont(new Font("Arial", Font.BOLD, 14));
        btnSatis.setBackground(new Color(255, 165, 0)); // Turuncu renk
        btnSatis.setForeground(Color.WHITE);
        add(btnSatis);

        // 4. Çıkış Butonu
        JButton btnCikis = new JButton("ÇIKIŞ");
        btnCikis.setFont(new Font("Arial", Font.BOLD, 14));
        btnCikis.setBackground(new Color(220, 20, 60)); // Kırmızı renk
        btnCikis.setForeground(Color.WHITE);
        add(btnCikis);

        // --- Buton Olayları (Şimdilik Boş, Sonra Dolduracağız) ---

        btnArac.addActionListener(e -> {
            // Eski mesajı sil, yerine bunu yaz:
            new AracEkrani().setVisible(true);
        });

        btnMusteri.addActionListener(e -> {
            new MusteriEkrani().setVisible(true);
        });

        btnSatis.addActionListener(e -> {
            new SatisEkrani().setVisible(true);
        });

        btnCikis.addActionListener(e -> {
            System.exit(0);
        });

        // AnaMenu constructor'ının içine ekle:
        JButton btnRapor = new JButton("CİRO RAPORU");
        btnRapor.setFont(new Font("Arial", Font.BOLD, 14));
        btnRapor.setBackground(new Color(128, 0, 128)); // Mor renk
        btnRapor.setForeground(Color.WHITE);
        add(btnRapor);

        // Tıklama olayı:
        btnRapor.addActionListener(e -> {
            new RaporEkrani();
        });

        setVisible(true);
    }

    // Test için
    public static void main(String[] args) {
        new AnaMenu();
    }
}