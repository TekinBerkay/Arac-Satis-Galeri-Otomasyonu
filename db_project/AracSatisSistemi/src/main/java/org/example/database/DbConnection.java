package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    // Veritabanı adı pgAdmin'de oluşturduğumuz ile aynı olmalı: AracSatisDb
    private static final String URL = "jdbc:postgresql://localhost:5432/AracSatisDb";
    private static final String USER = "postgres";   // Burayı kontrol et
    private static final String PASSWORD = "sifre";    // Buraya kendi şifreni yaz!

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("BAŞARILI: Veritabanına bağlandı!");
        } catch (SQLException e) {
            System.out.println("HATA: Bağlantı başarısız!");
            e.printStackTrace();
        }
        return conn;
    }

    // Test etmek için main metodu (Daha sonra sileceğiz)
    public static void main(String[] args) {
        getConnection();
    }
}