/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import java.io.*;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Nicolas
 */
public class ArchivosEnviados {

    private static final int TAMANO_BUFFER = 4096;

    // Método para enviar el archivo
    public void enviarArchivo(Socket socketCliente, String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isEmpty()) {
            System.out.println("Error: No hay una ruta valida del archivo.");
            return;
        }

        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Error: no existe el archivo: " + rutaArchivo);
            return;
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archivo));
             OutputStream os = socketCliente.getOutputStream()) {

            byte[] buffer = new byte[TAMANO_BUFFER];
            int bytesLeidos;

            while ((bytesLeidos = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesLeidos);
                os.flush();
            }
            System.out.println("Archivo enviado exitosamente! " + archivo.getName());

        } catch (IOException e) {
            System.out.println("Error al enviar el archivo: " + e.getMessage());
        }
    }
}
