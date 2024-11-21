/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.modelo.Cancion;
import edu.avanzada.parcialsegundocorte.modelo.CancionDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;
import static org.mariadb.jdbc.client.socket.impl.UnixDomainSocket.socket;

/**
 * Clase especializada en el manejo de de los hilos de cada cliete
 *
 * @author Nicolas
 */
public class ClienteHandler implements Runnable {

    private Socket socketCliente;
    private ClienteService clienteService;
    private CancionDAO cancionDAO;
    private ArchivosEnviados archivosEnviados;

    private String usuario;

    /**
     * Constructor
     *
     * @param socketCliente
     * @param clienteService
     * @param cancionDAO
     */
    public ClienteHandler(Socket socketCliente, ClienteService clienteService, CancionDAO cancionDAO) {
        this.socketCliente = socketCliente;
        this.clienteService = clienteService;
        this.cancionDAO = cancionDAO;
        this.archivosEnviados = new ArchivosEnviados();
    }

    /**
     * Metodo para conectar varios usuarios al servidor
     */
    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream())); PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true)) {

            String linea;
            while ((linea = entrada.readLine()) != null) {
                System.out.println("Solicitud recibida: " + linea);

                String[] comando = linea.split(" ");
                if (comando[0].equalsIgnoreCase("DESCARGAR") && comando.length == 2) {
                    String idCancion = comando[1];
                    procesarDescarga(idCancion, salida);
                } else {
                    salida.println("Comando no reconocido: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al comunicarse con el cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socketCliente.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }

    /**
     * Metodo para autenticar al cliente
     *
     * @param entrada
     * @param salida
     * @return
     * @throws IOException
     */
    private boolean autenticarCliente(String usuario, String contrasena, PrintWriter salida) {
        if (clienteService.autenticarCliente(usuario, contrasena)) {
            this.usuario = usuario;
            salida.println("Autenticación exitosa.");
            return true;
        } else {
            salida.println("Credenciales incorrectas.");
            return false;
        }
    }

    /**
     * Metodo para enviarle unalista de todas las canciones al usuario
     *
     * @param salida
     */
    private void enviarListaCanciones(PrintWriter salida) {
        salida.println("Lista de canciones disponibles:");
        cancionDAO.obtenerCanciones().forEach(c
                -> salida.println(c.getNombre() + "|" + c.getArtista() + "|" + c.getRutaArchivo()));
    }

    /**
     * Metodo para procesar la solicitud del usuario
     *
     * @param entrada
     * @param salida
     * @throws IOException
     */
    private void procesarSolicitudes(BufferedReader entrada, PrintWriter salida) throws IOException {
        String solicitud;
        while ((solicitud = entrada.readLine()) != null) {
            if (solicitud.startsWith("DESCARGAR ")) {
                String nombreCancion = solicitud.substring(10).trim();
                procesarDescarga(nombreCancion, salida);
            } else if (solicitud.equals("SALIR")) {
                salida.println("Desconectando...");
                break;
            } else {
                salida.println("Comando no reconocido. Intenta: DESCARGAR <nombre_cancion> o SALIR");
            }
        }
    }

    /**
     * Procesa la solicitud de descarga de una canción.
     *
     * @param idCancion El ID de la canción solicitada.
     * @param salida El flujo de salida al cliente.
     */
    private void procesarDescarga(String idCancion, PrintWriter salida) {
        
        try {
            System.out.println("Recibiendo solicitud de descarga para la canción con ID: " + idCancion);
            Cancion cancion = cancionDAO.obtenerCancionPorId(idCancion);

            if (cancion == null) {
                salida.println("ERROR: Canción no encontrada.");
                return;
            }

            File archivoCancion = new File(cancion.getRutaArchivo());

            if (!archivoCancion.exists()) {
                salida.println("ERROR: Archivo de la canción no disponible en el servidor.");
                return;
            }

            // Confirmación de inicio de la descarga
            salida.println("OK: Iniciando descarga de la canción...");

            // Enviar el archivo al cliente
            try (BufferedOutputStream salidaArchivo = new BufferedOutputStream(socketCliente.getOutputStream()); FileInputStream entradaArchivo = new FileInputStream(archivoCancion)) {

                byte[] buffer = new byte[4096];
                int bytesLeidos;

                while ((bytesLeidos = entradaArchivo.read(buffer)) != -1) {
                    salidaArchivo.write(buffer, 0, bytesLeidos);
                }

                salidaArchivo.flush();  // Asegurarse de que todo se haya enviado
                System.out.println("Archivo enviado correctamente: " + cancion.getNombre());
            }

        } catch (IOException e) {
            salida.println("ERROR: Ocurrió un problema al enviar el archivo.");
            System.out.println("Error al procesar la descarga: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
