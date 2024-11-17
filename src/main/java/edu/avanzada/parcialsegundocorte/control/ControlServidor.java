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

/**
 *
 * @author Nicolas
 */
public class ControlServidor {

    private CancionDAO cancionDAO;
    private ClienteDAO clienteDAO;

    public ControlServidor() {
        this.cancionDAO = new CancionDAO();
        this.clienteDAO = new ClienteDAO();
    }

    // Cargar canciones desde la base de datos en la tabla
    public void cargarCancionesEnTabla(JTable tablaCanciones) {
        List<Cancion> canciones = cancionDAO.obtenerCanciones();
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaCanciones.getModel();
        modeloTabla.setRowCount(0); // Limpiar la tabla

        for (Cancion cancion : canciones) {
            modeloTabla.addRow(new Object[]{cancion.getNombre(), cancion.getArtista(), cancion.getRutaArchivo()});
        }
    }

    // Validar credenciales del cliente
    public boolean verificarCredenciales(String usuario, String contrasena) {
        return clienteDAO.verificarCredenciales(usuario, contrasena);
    }

    // Verificar si el cliente tiene saldo suficiente para descargar una canción
    public boolean validarSaldo(String usuario, double precioCancion) {
        double saldoActual = clienteDAO.obtenerSaldo(usuario);
        return saldoActual >= precioCancion;
    }

    // Procesar descarga de la canción
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

    public boolean registrarUsuario(String nombre, String usuario, String contrasena) {
        Cliente nuevoCliente = new Cliente(nombre, usuario, contrasena, 0.0); // Saldo inicial 0.0
        boolean registroExitoso = clienteDAO.registrarUsuario(nuevoCliente);

        if (registroExitoso) {
            System.out.println("Usuario registrado exitosamente: " + usuario);
        } else {
            System.out.println("Error al registrar el usuario: " + usuario);
        }

        return registroExitoso;
    }

    public boolean actualizarSaldoCliente(String usuario, double nuevoSaldo) {
        return clienteDAO.actualizarSaldo(usuario, nuevoSaldo);
    }

}