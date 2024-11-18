/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.modelo.CancionDAO;
import edu.avanzada.parcialsegundocorte.modelo.ClienteDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Clase especializada en el manejo de de los hilos de cada cliete
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

            System.out.println("Cliente conectado desde: " + socketCliente.getInetAddress().getHostAddress());

            if (!autenticarCliente(entrada, salida)) {
                System.out.println("Cliente rechazado: autenticación fallida.");
                return;
            }

            System.out.println("Usuario autenticado: " + usuario);
            enviarListaCanciones(salida);
            procesarSolicitudes(entrada, salida);

        } catch (IOException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
        } finally {
            System.out.println("Cliente desconectado: " + usuario);
        }
    }

    /**
     * Metodo para autenticar al cliente
     * @param entrada
     * @param salida
     * @return
     * @throws IOException 
     */
    private boolean autenticarCliente(BufferedReader entrada, PrintWriter salida) throws IOException {
        salida.println("Ingrese su usuario:");
        String usuario = entrada.readLine();
        salida.println("Ingrese su contraseña:");
        String contrasena = entrada.readLine();

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
     * @param salida 
     */
    private void enviarListaCanciones(PrintWriter salida) {
        salida.println("Lista de canciones disponibles:");
        cancionDAO.obtenerCanciones().forEach(c
                -> salida.println(c.getNombre() + "|" + c.getArtista() + "|" + c.getRutaArchivo()));
    }

    /**
     * Metodo para procesar la solicitud del usuario
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
     * Metodo para procesar la descarga
     * @param nombreCancion
     * @param salida 
     */
    private void procesarDescarga(String nombreCancion, PrintWriter salida) {
        String rutaCancion = cancionDAO.obtenerRutaPorNombre(nombreCancion);
        if (rutaCancion == null) {
            salida.println("Canción no encontrada.");
            return;
        }

        double costo = 1500.0;
        if (clienteService.descontarSaldo(usuario, costo)) {
            archivosEnviados.enviarArchivo(socketCliente, rutaCancion);
            salida.println("Canción descargada exitosamente.");
        } else {
            salida.println("Saldo insuficiente.");
        }
    }
}
