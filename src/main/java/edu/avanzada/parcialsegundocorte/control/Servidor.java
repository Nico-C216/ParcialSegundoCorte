/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.modelo.CancionDAO;
import edu.avanzada.parcialsegundocorte.modelo.ClienteDAO;
import edu.avanzada.parcialsegundocorte.modelo.ConexionMSQ;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Nicolas
 */
public class Servidor {

    private ServerSocket sckEntradaServidor = null;
    private int puerto;
    private int contador;
    private ClienteService clienteService;
    private CancionDAO cancionDAO;

    public Servidor() {
        this.puerto = ConfigProperties.getPuerto();

        if (this.puerto == -1) {
            System.out.println("Error crítico: No se pudo obtener un puerto válido desde 'Servidor.properties'.");
            System.exit(1);
        }

        if (ConexionMSQ.getConexion() == null) {
            System.out.println("Error crítico: No se pudo establecer conexión con la base de datos.");
            System.exit(1);
        }

        this.clienteService = new ClienteService(new ClienteDAO());
        this.cancionDAO = new CancionDAO();
    }

    public void iniciarServidor() {
        if (!escucharPuerto()) {
            System.out.println("Error: no se pudo abrir el puerto " + puerto);
            return;
        }
        System.out.println("Servidor activo y escuchando en el puerto: " + puerto);

        procesarSolicitudes();

        detenerServidor();

        // Cerrar la conexión a la base de datos al detener el servidor
        ConexionMSQ.desconectar();
    }

    private boolean escucharPuerto() {
        try {
            sckEntradaServidor = new ServerSocket(puerto);
            return true;
        } catch (IOException exc) {
            System.out.println("No fue posible abrir el puerto: " + puerto);
            return false;
        }
    }

    private void procesarSolicitudes() {
        System.out.println("Esperando conexión de clientes...");

        try {
            while (true) {
                Socket sckSalidaServidor = sckEntradaServidor.accept();
                System.out.println("Cliente conectado desde: " + sckSalidaServidor.getInetAddress().getHostAddress());

                // Crear un nuevo hilo para procesar al cliente
                ClienteHandler clienteHandler = new ClienteHandler(sckSalidaServidor, clienteService, cancionDAO);
                new Thread(clienteHandler).start();

                contador++;
                System.out.println("Hilo de procesamiento para el cliente #" + contador + " ha sido iniciado");
            }
        } catch (IOException exc) {
            System.out.println("Error al aceptar la conexión del cliente.");
        }
    }

    private void detenerServidor() {
        try {
            if (sckEntradaServidor != null && !sckEntradaServidor.isClosed()) {
                sckEntradaServidor.close();
                System.out.println("Servidor detenido.");
            }
        } catch (IOException exc) {
            System.out.println("Error al cerrar el servidor: " + exc.getMessage());
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciarServidor();
    }
}
