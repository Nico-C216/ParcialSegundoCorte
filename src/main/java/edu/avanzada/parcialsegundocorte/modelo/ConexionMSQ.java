/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase especializada en la conexion de la base de datos con el servidor
 *
 * @author Nicolas
 */
public class ConexionMSQ {

    private static Connection cn = null;

    // Datos de conexión 
    private static final String URLBD = "jdbc:mariadb://localhost:3306/parcial2";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    /**
     * Método para obtener la conexión
     *
     * @return
     */
    public static Connection getConexion() {
        try {
            if (cn == null || cn.isClosed()) { // Verifica si la conexión está cerrada
                cn = DriverManager.getConnection(URLBD, USUARIO, CONTRASENA);
                System.out.println("Conexión exitosa a la base de datos.");
            }
        } catch (SQLException ex) {
            System.out.println("Error en la conexión: " + ex.getMessage());
            ex.printStackTrace();
        }
        return cn;
    }

    /**
     * Método para cerrar la conexión
     */
    public static void desconectar() {
        if (cn != null) {
            try {
                cn.close();
                cn = null;
                System.out.println("Conexión cerrada.");
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }
}
