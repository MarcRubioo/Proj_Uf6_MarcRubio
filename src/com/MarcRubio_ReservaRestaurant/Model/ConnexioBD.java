package com.MarcRubio_ReservaRestaurant.Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnexioBD {
    public static Connection getCon() throws SQLException{
        Connection con = null;

        try{FileInputStream file = new FileInputStream("C:\\Users\\marcr\\OneDrive\\Escritorio\\1r DAM\\LEA\\UF1- Entorns de desenvolupament\\Proj_Uf6_MarcRubio\\src\\com\\MarcRubio_ReservaRestaurant\\Fitxers\\Proporties_BD.properties");
            Properties pro = new Properties();
            pro.load(file);

            String direccio = pro.getProperty("direccio");
            String usuari = pro.getProperty("usuari");
            String pass = pro.getProperty("pass");

            con = DriverManager.getConnection(direccio,usuari,pass);
        }
        catch(IOException e){
        System.out.println(e.getMessage());
        }

        return con;
    }

    public static void  ConnexioMYSQL() {

        try(Connection connection = ConnexioBD.getCon()) {

            System.out.println("Connexio a la base de dades Correcte!!!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
