/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;


import edu.avanzada.parcialsegundocorte.modelo.ConexionMSQ;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/**Clase especializada en subir la URL de las canciones a la base de datos
 *
 * @author Nicolas
 */
public class SubirCancion {

    private static final String DIRECTORIO_SERVIDOR = "C:\\Users\\Usuario\\OneDrive\\Documentos\\Nico\\Universidad\\Programacion Avanzada\\Canciones";

    /**
     * Metodo para subir la cancion
     */
    public static void subirCancion() {
        // Crear el JFileChooser para seleccionar archivos .mp3
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos MP3", "mp3");
        fileChooser.setFileFilter(filtro);

        int seleccion = fileChooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            String nombreArchivo = archivoSeleccionado.getName();
            String rutaDestino = DIRECTORIO_SERVIDOR + nombreArchivo;

            // Copiar el archivo al servidor
            try (InputStream entrada = new FileInputStream(archivoSeleccionado);
                 OutputStream salida = new FileOutputStream(rutaDestino)) {

                byte[] buffer = new byte[4096];
                int bytesLeidos;

                while ((bytesLeidos = entrada.read(buffer)) != -1) {
                    salida.write(buffer, 0, bytesLeidos);
                }

                System.out.println("Archivo copiado exitosamente al servidor.");

                // Registrar el archivo en la base de datos
                registrarCancionEnBD(nombreArchivo, "Artista Desconocido", rutaDestino);

            } catch (IOException ex) {
                System.out.println("Error al copiar el archivo: " + ex.getMessage());
            }
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }

    /**
     * Metodo para registrar una cancion a la base de datos
     * @param nombre
     * @param artista
     * @param rutaArchivo 
     */
    private static void registrarCancionEnBD(String nombre, String artista, String rutaArchivo) {
        String sql = "INSERT INTO canciones (nombre, artista, rutaArchivo) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionMSQ.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, artista);
            ps.setString(3, rutaArchivo);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Canción registrada exitosamente en la base de datos.");
            } else {
                System.out.println("No se pudo registrar la canción en la base de datos.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al registrar la canción: " + ex.getMessage());
        }
    }

    /**
     * Metodo para subir las caicones de manera separada al programa
     * @param args 
     */
    public static void main(String[] args) {
        subirCancion();
    }
}
