/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.modelo.Cancion;
import edu.avanzada.parcialsegundocorte.modelo.CancionDAO;
import edu.avanzada.parcialsegundocorte.modelo.Cliente;
import edu.avanzada.parcialsegundocorte.modelo.ClienteDAO;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase especializada en el manejo del servidor con el usuario
 *
 * @author Nicolas
 */
public class ControlServidor {

    private CancionDAO cancionDAO;
    private ClienteDAO clienteDAO;
    private ClienteService clienteService;
    private String direccionServidor;
    private int puertoServidor;

    /**
     * Constructor
     */
    public ControlServidor() {
        this.cancionDAO = new CancionDAO();
        this.clienteDAO = new ClienteDAO();
        this.clienteService = new ClienteService(clienteDAO);
        this.direccionServidor = "127.0.0.1";
        this.puertoServidor = ConfigProperties.getPuerto();

    }

    /**
     * Cargar canciones desde la base de datos en la tabla
     *
     * @param tablaCanciones
     */
    public void cargarCancionesEnTabla(JTable tablaCanciones) {
        List<Cancion> canciones = cancionDAO.obtenerCanciones();
        DefaultTableModel modeloTabla = new DefaultTableModel(
                new String[]{"Nombre", "Artista", "Ruta"}, 0
        );
        tablaCanciones.setModel(modeloTabla);

        if (canciones.isEmpty()) {
            System.out.println("No se encontraron canciones para mostrar.");
        }

        for (Cancion cancion : canciones) {
            modeloTabla.addRow(new Object[]{
                cancion.getNombre(),
                cancion.getArtista(),
                cancion.getRutaArchivo()
            });
        }

        System.out.println("Total de canciones cargadas en la tabla: " + canciones.size());
    }

    /**
     * Validar credenciales del cliente
     *
     * @param usuario
     * @param contrasena
     * @return
     */
    public boolean verificarCredenciales(String usuario, String contrasena) {
        return clienteDAO.verificarCredenciales(usuario, contrasena);
    }

    /**
     * Verificar si el cliente tiene saldo suficiente para descargar una canción
     *
     * @param usuario
     * @param precioCancion
     * @return
     */
    public boolean validarSaldo(String usuario, double precioCancion) {
        double saldoActual = clienteService.obtenerSaldo(usuario);
        System.out.println("Saldo actual del usuario: " + saldoActual);
        return saldoActual >= precioCancion;
    }

    /**
     * Procesar descarga de la canción
     *
     * @param socketCliente
     * @param usuario
     * @param nombreCancion
     * @param precioCancion
     * @return
     */
    public boolean procesarDescarga(Socket socketCliente, String usuario, String nombreCancion, double precioCancion) {
        if (!clienteService.descontarSaldo(usuario, precioCancion)) {
            System.out.println("Saldo insuficiente o error al actualizar saldo.");
            return false;
        }

        String rutaArchivo = cancionDAO.obtenerRutaPorNombre(nombreCancion);
        if (rutaArchivo == null) {
            System.out.println("No se encontró la ruta del archivo para la canción: " + nombreCancion);
            return false;
        }

        ArchivosEnviados archivosEnviados = new ArchivosEnviados();
        archivosEnviados.enviarArchivo(socketCliente, rutaArchivo);

        System.out.println("La canción '" + nombreCancion + "' fue descargada exitosamente.");
        return true;
    }

    /**
     * Registra al usuario en la base de datos
     *
     * @param nombre
     * @param usuario
     * @param contrasena
     * @return
     */
    public boolean registrarUsuario(String nombre, String usuario, String contrasena, double saldoInicial) {
        Cliente nuevoCliente = new Cliente(nombre, usuario, contrasena, saldoInicial);
        boolean registroExitoso = clienteDAO.registrarUsuario(nuevoCliente);

        if (registroExitoso) {
            System.out.println("Usuario registrado exitosamente: " + usuario);
        } else {
            System.out.println("Error al registrar el usuario: " + usuario);
        }

        return registroExitoso;
    }

    /**
     * Actualiza el saldo del cliente
     *
     * @param usuario
     * @param nuevoSaldo
     * @return
     */
    public boolean actualizarSaldoCliente(String usuario, double nuevoSaldo) {
        System.out.println("Actualizando saldo para el usuario: " + usuario + " con saldo: " + nuevoSaldo);
        return clienteDAO.actualizarSaldo(usuario, nuevoSaldo);
    }

    /**
     * Metodo para poder descargar la cancion
     * @param usuario
     * @param contrasena
     * @param nombreCancion
     * @return 
     */
    public boolean descargarCancion(String usuario, String contrasena, String nombreCancion) {
        try (Socket socketCliente = new Socket(direccionServidor, puertoServidor); BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream())); PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true)) {

            // Autenticar al usuario
            if (!autenticarUsuario(entrada, salida, usuario, contrasena)) {
                System.out.println("Error: Autenticación fallida.");
                return false;
            }

            // Enviar solicitud de descarga
            salida.println("DESCARGAR " + nombreCancion);

            // Leer respuesta del servidor
            String respuesta = entrada.readLine();
            if ("Canción descargada exitosamente.".equals(respuesta)) {
                // Usar ArchivosRecibidos para guardar la canción
                ArchivosRecibidos archivosRecibidos = new ArchivosRecibidos();
                archivosRecibidos.recibirArchivo(socketCliente, nombreCancion);
                System.out.println("Descarga completada: " + nombreCancion);
                return true;
            } else {
                System.out.println("Error del servidor: " + respuesta);
                return false;
            }

        } catch (IOException e) {
            System.out.println("Error durante la conexión al servidor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para autenticar al usuario en el servidor.
     *
     * @param entrada Flujo de entrada del servidor.
     * @param salida Flujo de salida hacia el servidor.
     * @param usuario Usuario del cliente.
     * @param contrasena Contraseña del cliente.
     * @return true si la autenticación fue exitosa, false de lo contrario.
     */
    private boolean autenticarUsuario(BufferedReader entrada, PrintWriter salida, String usuario, String contrasena) throws IOException {
        // Leer mensajes del servidor
        System.out.println(entrada.readLine()); // "Ingrese su usuario:"
        salida.println(usuario);

        System.out.println(entrada.readLine()); // "Ingrese su contraseña:"
        salida.println(contrasena);

        String respuesta = entrada.readLine();
        System.out.println(respuesta); // Mensaje del servidor sobre el resultado

        return "Autenticación exitosa.".equals(respuesta);
    }

}
