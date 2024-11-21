/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.modelo.Cancion;
import edu.avanzada.parcialsegundocorte.modelo.CancionDAO;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;

/**
 * Clase especializada en el control de toda la aplicacion
 *
 * @author Nicolas
 */
public class ControladorAplicacion {

    private ControladorVentanas controladorVentanas;
    private ControlServidor controlServidor;
    private ClienteService clienteService;

    private String usuarioActual;

    /**
     * Constructor
     */
    public ControladorAplicacion() {
        controladorVentanas = new ControladorVentanas();
        controlServidor = new ControlServidor();

        configurarEventos();
    }

    /**
     * Configuracion de eventos entre la vista, servidor, base de datos y el
     * usuario
     */
    private void configurarEventos() {
        // Evento para registrar usuario en VentanaRegistro
        controladorVentanas.getVentanaRegistro().btnRegistrar.addActionListener(e -> {
            String usuario = controladorVentanas.getVentanaRegistro().getUsuario();
            String contrasena = controladorVentanas.getVentanaRegistro().getPuertoServidor();
            String saldoTexto = controladorVentanas.getVentanaRegistro().getSaldo();

            if (usuario.isEmpty() || contrasena.isEmpty() || saldoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(controladorVentanas.getVentanaRegistro(),
                        "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double saldoInicial = Double.parseDouble(saldoTexto);

                if (saldoInicial < 0) {
                    JOptionPane.showMessageDialog(controladorVentanas.getVentanaRegistro(),
                            "El saldo inicial no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean registroExitoso = controlServidor.registrarUsuario(usuario, usuario, contrasena, saldoInicial);
                if (registroExitoso) {
                    usuarioActual = usuario; // Guardar el usuario registrado
                    JOptionPane.showMessageDialog(controladorVentanas.getVentanaRegistro(), "Registro exitoso.");
                    controladorVentanas.mostrarVentanaCanciones();
                    cargarCancionesEnLista();
                } else {
                    JOptionPane.showMessageDialog(controladorVentanas.getVentanaRegistro(),
                            "Error al registrar usuario. Intenta nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(controladorVentanas.getVentanaRegistro(),
                        "El saldo inicial debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Evento para manejar el ingreso de saldo
        controladorVentanas.getVentanaCanciones().btnSaldo.addActionListener(e -> {
            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(controladorVentanas.getVentanaCanciones(),
                        "Debe estar autenticado para agregar saldo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String saldoIngresado = controladorVentanas.getVentanaCanciones().txtSaldo.getText();
            try {
                double nuevoSaldo = Double.parseDouble(saldoIngresado);

                if (nuevoSaldo < 0) {
                    JOptionPane.showMessageDialog(controladorVentanas.getVentanaCanciones(),
                            "El saldo no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean actualizado = controlServidor.actualizarSaldoCliente(usuarioActual, nuevoSaldo);
                if (actualizado) {
                    JOptionPane.showMessageDialog(controladorVentanas.getVentanaCanciones(),
                            "Saldo actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(controladorVentanas.getVentanaCanciones(),
                            "Error al actualizar el saldo. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(controladorVentanas.getVentanaCanciones(),
                        "Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Evento para seleccionar una canción y escucharla en el Reproductor
        controladorVentanas.getVentanaCanciones().jButton1.addActionListener(e -> {
            String cancionSeleccionada = controladorVentanas.getVentanaCanciones().jListCanciones.getSelectedValue();
            if (cancionSeleccionada != null) {
                controladorVentanas.mostrarReproductor(cancionSeleccionada);
            } else {
                JOptionPane.showMessageDialog(
                        controladorVentanas.getVentanaCanciones(),
                        "Por favor, selecciona una canción."
                );
            }
        });

        // Evento para descargar una canción
        controladorVentanas.getVentanaCanciones().jButton3.addActionListener(e -> {
            // Verificar que haya un usuario autenticado
            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(
                        controladorVentanas.getVentanaCanciones(),
                        "Debes estar autenticado para descargar canciones.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Obtener la canción seleccionada
            String cancionSeleccionada = controladorVentanas.getVentanaCanciones().jListCanciones.getSelectedValue();

            if (cancionSeleccionada != null) {
                // Intentar descargar la canción con el usuario actual
                boolean exito = controlServidor.descargarCancion(usuarioActual, null, cancionSeleccionada); // No pasamos contraseña, si no es necesaria

                if (exito) {
                    JOptionPane.showMessageDialog(
                            controladorVentanas.getVentanaCanciones(),
                            "La canción '" + cancionSeleccionada + "' se descargó correctamente."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            controladorVentanas.getVentanaCanciones(),
                            "Error al descargar la canción. Verifica tu saldo o intenta nuevamente.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                        controladorVentanas.getVentanaCanciones(),
                        "Por favor, selecciona una canción para descargar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        controladorVentanas.getVentanaCanciones().addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarCancionesEnLista(); // Cargar las canciones cuando se abre la ventana
            }
        });
    }

    /**
     * Metodo para cargar canciones en la tabla
     */
    public void cargarCancionesEnLista() {
        // Crear una instancia del DAO para obtener las canciones
        CancionDAO cancionDAO = new CancionDAO();
        List<Cancion> canciones = cancionDAO.obtenerCanciones();

        // Crear un modelo para JList
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Cancion cancion : canciones) {
            modelo.addElement(cancion.getNombre() + " - " + cancion.getArtista()); // Formato: Nombre - Artista
        }

        // Asignar el modelo al JList
        controladorVentanas.getVentanaCanciones().jListCanciones.setModel(modelo);
    }

    /**
     * Metodo para iniciar la aplicacion
     */
    public void iniciar() {
        controladorVentanas.mostrarVentanaRegistro();

        controladorVentanas.mostrarVentanaCanciones();
        cargarCancionesEnLista();
    }
}
