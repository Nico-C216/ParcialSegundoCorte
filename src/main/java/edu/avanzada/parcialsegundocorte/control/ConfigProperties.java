/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Nicolas
 */
public class ConfigProperties {

    private static Properties propiedades;

    static {
        propiedades = new Properties();
        try {
            // Cambia "ruta/completa/del/proyecto/Servidor.properties" por la ruta absoluta en tu sistema
            FileInputStream entrada = new FileInputStream("C:\\Users\\Usuario\\OneDrive\\Documentos\\Nico\\Universidad\\Programacion Avanzada\\Paricl-Segundo-Corte-PA\\ParcialSegundoCorte\\src\\main\\java\\edu\\avanzada\\parcialsegundocorte\\data\\Servidor.properties"); 
            propiedades.load(entrada);
            System.out.println("Archivo de configuración cargado exitosamente.");
        } catch (IOException e) {
            System.out.println("Error: No se encontró el archivo de configuración en la ruta especificada.");
        }
    }

    public static int getPuerto() {
        String puertoStr = propiedades.getProperty("puerto");

        try {
            if (puertoStr != null) {
                return Integer.parseInt(puertoStr);
            } else {
                System.out.println("Error: Clave 'puerto' no encontrada en el archivo de configuración.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Puerto inválido en el archivo de configuración.");
        }

        return -1; // Indica que no se pudo obtener un puerto válido
    }

}
