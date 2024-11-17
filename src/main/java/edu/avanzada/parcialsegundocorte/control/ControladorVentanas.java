/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.vista.Reproductor;
import edu.avanzada.parcialsegundocorte.vista.VentanaCanciones;
import edu.avanzada.parcialsegundocorte.vista.VentanaRegistro;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Usuario
 */
public class ControladorVentanas {

    private VentanaRegistro ventanaRegistro;
    private VentanaCanciones ventanaCanciones;
    private Reproductor reproductor;

    public ControladorVentanas() {
        // Inicializar ventanas y pasar este controlador
        ventanaRegistro = new VentanaRegistro();
        ventanaCanciones = new VentanaCanciones();
        reproductor = new Reproductor();

        // Configurar eventos de VentanaCanciones
        ventanaCanciones.jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ventanaCanciones.jTable1.getSelectedRow();
                if (selectedRow != -1) {
                    String nombreCancion = (String) ventanaCanciones.jTable1.getValueAt(selectedRow, 0);
                    mostrarReproductor(nombreCancion);
                } else {
                    JOptionPane.showMessageDialog(ventanaCanciones, "Por favor, selecciona una canción.");
                }
            }
        });

        ventanaCanciones.jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarVentanaRegistro();
            }
        });

        // Configurar eventos de Reproductor
        reproductor.btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverALaLista();
            }
        });

        // Mostrar la ventana inicial
        ventanaRegistro.setVisible(true);
        ventanaRegistro.btnRegistrar.addActionListener(e -> {
            String usuario = ventanaRegistro.getUsuario();
            String puertoServidor = ventanaRegistro.getPuertoServidor();

            // Validar y registrar al usuario
            registrarUsuario(usuario, puertoServidor);
        });
    }

    void mostrarVentanaRegistro() {
        ventanaRegistro.setVisible(true);
        ventanaCanciones.setVisible(false);
        reproductor.setVisible(false);
    }

    void mostrarVentanaCanciones() {
        ventanaRegistro.setVisible(false);
        ventanaCanciones.setVisible(true);
        reproductor.setVisible(false);
    }

    void mostrarReproductor(String nombreCancion) {
        ventanaRegistro.setVisible(false);
        ventanaCanciones.setVisible(false);
        reproductor.setVisible(true);
        reproductor.nombre_can.setText(nombreCancion); // Mostrar el nombre de la canción
    }

    // Método para registrar usuario
    public void registrarUsuario(String usuario, String puertoServidor) {
        System.out.println("Usuario registrado: " + usuario + ", Puerto: " + puertoServidor);

        // Aquí validarías usuario/puerto con el servidor en el futuro
        if (!usuario.isEmpty() && !puertoServidor.isEmpty()) {
            mostrarVentanaCanciones();
        } else {
            JOptionPane.showMessageDialog(ventanaRegistro, "Usuario o puerto no pueden estar vacíos.");
        }
    }

    private void volverALaLista() {
        reproductor.setVisible(false);
        ventanaCanciones.setVisible(true);
    }
    
    void manejarIngresoSaldo(ActionListener actionListener) {
        ventanaCanciones.btnSaldo.addActionListener(actionListener);
    }

    public VentanaRegistro getVentanaRegistro() {
        return ventanaRegistro;
    }

    public VentanaCanciones getVentanaCanciones() {
        return ventanaCanciones;
    }

    public Reproductor getReproductor() {
        return reproductor;
    }
}
