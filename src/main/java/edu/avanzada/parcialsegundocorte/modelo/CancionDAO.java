/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**Clase con el parton DAO para ingresarlas a la base de datos
 *
 * @author Nicolas
 */
public class CancionDAO {

    /**
     * Método para obtener todas las canciones disponibles
     * @return 
     */
    public List<Cancion> obtenerCanciones() {
        List<Cancion> canciones = new ArrayList<>();
        String sql = "SELECT * FROM canciones";

        try (Connection cn = ConexionMSQ.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cancion cancion = new Cancion(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("artista"),
                        rs.getString("rutaArchivo")
                );
                canciones.add(cancion);
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener canciones: " + ex.getMessage());
        }

        return canciones;
    }

    /**
     * Método para obtener la ruta de una canción por su nombre
     * @param nombreCancion
     * @return 
     */
    public String obtenerRutaPorNombre(String nombreCancion) {
        String ruta = null;
        String sql = "SELECT rutaArchivo FROM canciones WHERE nombre = ?";

        try (Connection cn = ConexionMSQ.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombreCancion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ruta = rs.getString("rutaArchivo");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener la ruta de la canción: " + ex.getMessage());
        }

        return ruta;
    }
}
