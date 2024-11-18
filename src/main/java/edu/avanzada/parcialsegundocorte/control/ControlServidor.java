/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.modelo.Cancion;
import edu.avanzada.parcialsegundocorte.modelo.CancionDAO;
import edu.avanzada.parcialsegundocorte.modelo.Cliente;
import edu.avanzada.parcialsegundocorte.modelo.ClienteDAO;
import java.net.Socket;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**Clase especializada en el manejo del servidor con el usuario
 *
 * @author Nicolas
 */
public class ControlServidor {

    private CancionDAO cancionDAO;
    private ClienteDAO clienteDAO;

    /**
     * Constructor
     */
    public ControlServidor() {
        this.cancionDAO = new CancionDAO();
        this.clienteDAO = new ClienteDAO();
    }

    /**
     * Cargar canciones desde la base de datos en la tabla
     * @param tablaCanciones 
     */
    public void cargarCancionesEnTabla(JTable tablaCanciones) {
        List<Cancion> canciones = cancionDAO.obtenerCanciones();
        DefaultTableModel modeloTabla = new DefaultTableModel(
                new String[]{"Nombre", "Artista", "Ruta"}, 0
        );
        tablaCanciones.setModel(modeloTabla);

        for (Cancion cancion : canciones) {
            modeloTabla.addRow(new Object[]{
                cancion.getNombre(),
                cancion.getArtista(),
                cancion.getRutaArchivo()
            });
        }
    }

    /**
     * Validar credenciales del cliente
     * @param usuario
     * @param contrasena
     * @return 
     */
    public boolean verificarCredenciales(String usuario, String contrasena) {
        return clienteDAO.verificarCredenciales(usuario, contrasena);
    }

    /**
     * Verificar si el cliente tiene saldo suficiente para descargar una canción
     * @param usuario
     * @param precioCancion
     * @return 
     */
    public boolean validarSaldo(String usuario, double precioCancion) {
        double saldoActual = clienteDAO.obtenerSaldo(usuario);
        return saldoActual >= precioCancion;
    }

    /**
     * Procesar descarga de la canción
     * @param socketCliente
     * @param usuario
     * @param nombreCancion
     * @param precioCancion
     * @return 
     */
    public boolean procesarDescarga(Socket socketCliente, String usuario, String nombreCancion, double precioCancion) {
        if (!validarSaldo(usuario, precioCancion)) {
            System.out.println("Saldo insuficiente para descargar la canción.");
            return false;
        }

        // Descontar el saldo del cliente
        boolean saldoDescontado = clienteDAO.descontarSaldo(usuario, precioCancion);
        if (!saldoDescontado) {
            System.out.println("Error al descontar el saldo.");
            return false;
        }

        // Obtener la ruta del archivo desde la base de datos
        String rutaArchivo = cancionDAO.obtenerRutaPorNombre(nombreCancion);
        if (rutaArchivo == null) {
            System.out.println("No se encontró la ruta del archivo para la canción: " + nombreCancion);
            return false;
        }

        // Enviar el archivo al cliente
        ArchivosEnviados archivosEnviados = new ArchivosEnviados();
        archivosEnviados.enviarArchivo(socketCliente, rutaArchivo);

        System.out.println("La canción '" + nombreCancion + "' fue descargada exitosamente.");
        return true;
    }

    /**
     * Registra al usuario en la base de datos
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
     * @param usuario
     * @param nuevoSaldo
     * @return 
     */
    public boolean actualizarSaldoCliente(String usuario, double nuevoSaldo) {
        System.out.println("Actualizando saldo para el usuario: " + usuario + " con saldo: " + nuevoSaldo);
        return clienteDAO.actualizarSaldo(usuario, nuevoSaldo);
    }

}
