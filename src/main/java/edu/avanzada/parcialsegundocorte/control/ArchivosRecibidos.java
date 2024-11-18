/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import java.io.*;
import java.net.Socket;
import javax.swing.JFileChooser;

/**Clase especializada en que el usuario reciba y pueda descargar el archivo 
 * enviado por el servidor
 *
 * @author Nicolas
 */
public class ArchivosRecibidos {

    public static final int TAMANO_BUFFER = 4096;
    private String rutaCarpetaDescarga;

    /**
     * Constructor que abre un FileChooser para escocger la ruta de descarga
     */
    public ArchivosRecibidos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona la carpeta de destino para las canciones");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int seleccion = fileChooser.showSaveDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            rutaCarpetaDescarga = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("Carpeta de destino seleccionada: " + rutaCarpetaDescarga);
        } else {
            System.out.println("No se seleccionó ninguna carpeta.");
            rutaCarpetaDescarga = null;
        }
    }

    /**
     * Metodo para recibir el archivo
     * @param socket
     * @param nombreArchivo 
     */
    public void recibirArchivo(Socket socket, String nombreArchivo) {
        if (rutaCarpetaDescarga == null) {
            System.out.println("Error: No se ha seleccionado la carpeta de descarga.");
            return;
        }

        File archivoDestino = new File(rutaCarpetaDescarga + File.separator + nombreArchivo); //Ruta donde llegan las caniones

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(archivoDestino)); InputStream is = socket.getInputStream()) {

            byte[] buffer = new byte[TAMANO_BUFFER];
            int bytesLeidos;

            while ((bytesLeidos = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesLeidos);
            }
            System.out.println("Archivo " + nombreArchivo + " descargado exitosamente.");

        } catch (IOException e) {
            System.out.println("Error al recibir el archivo: " + e.getMessage());
        }
    }

}
