/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import javax.swing.JOptionPane;

/**
 *
 * @author Nicolas
 */
public class ControladorAplicacion {

    private ControladorVentanas controladorVentanas;
    private ControlServidor controlServidor;

    private String usuarioActual;

    public ControladorAplicacion() {
        controladorVentanas = new ControladorVentanas();
        controlServidor = new ControlServidor();

        configurarEventos();
    }

    private void configurarEventos() {
        // Evento para registrar usuario en VentanaRegistro
        controladorVentanas.getVentanaRegistro().btnRegistrar.addActionListener(e -> {
            String usuario = controladorVentanas.getVentanaRegistro().getUsuario();
            String contrasena = controladorVentanas.getVentanaRegistro().getPuertoServidor();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(controladorVentanas.getVentanaRegistro(), "Usuario o contraseña no pueden estar vacíos.");
                return;
            }

            boolean registroExitoso = controlServidor.registrarUsuario(usuario, usuario, contrasena);
            if (registroExitoso) {
                usuarioActual = usuario; // Guardar el usuario registrado
                JOptionPane.showMessageDialog(controladorVentanas.getVentanaRegistro(), "Registro exitoso.");
                controladorVentanas.mostrarVentanaCanciones();
            } else {
                JOptionPane.showMessageDialog(controladorVentanas.getVentanaRegistro(), "Error al registrar usuario. Intenta nuevamente.");
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
            int selectedRow = controladorVentanas.getVentanaCanciones().jTable1.getSelectedRow();
            if (selectedRow != -1) {
                String nombreCancion = (String) controladorVentanas.getVentanaCanciones().jTable1.getValueAt(selectedRow, 0);
                controladorVentanas.mostrarReproductor(nombreCancion);
            } else {
                JOptionPane.showMessageDialog(
                        controladorVentanas.getVentanaCanciones(),
                        "Por favor, selecciona una canción."
                );
            }
        });

        // Evento para descargar una canción
        controladorVentanas.getVentanaCanciones().jButton3.addActionListener(e -> {
            int selectedRow = controladorVentanas.getVentanaCanciones().jTable1.getSelectedRow();
            if (selectedRow != -1) {
                String nombreCancion = (String) controladorVentanas.getVentanaCanciones().jTable1.getValueAt(selectedRow, 0);
                double precioCancion = 1500.0; // Precio fijo, ajustable según la lógica

                boolean exito = controlServidor.procesarDescarga(
                        null, usuarioActual, nombreCancion, precioCancion
                );
                if (exito) {
                    JOptionPane.showMessageDialog(
                            controladorVentanas.getVentanaCanciones(),
                            "La canción se descargó exitosamente."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            controladorVentanas.getVentanaCanciones(),
                            "Error al descargar la canción. Verifica tu saldo."
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                        controladorVentanas.getVentanaCanciones(),
                        "Por favor, selecciona una canción para descargar."
                );
            }
        });
    }

    private void cargarCancionesEnTabla() {
        controlServidor.cargarCancionesEnTabla(controladorVentanas.getVentanaCanciones().jTable1);
    }

    public void iniciar() {
        controladorVentanas.mostrarVentanaRegistro();
    }
}
