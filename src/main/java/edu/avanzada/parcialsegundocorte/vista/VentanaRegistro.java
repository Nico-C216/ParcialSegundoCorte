/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.vista;

import edu.avanzada.parcialsegundocorte.control.ControladorVentanas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana del ergistro de usuarios
 *
 * @author Nicolas
 */
public class VentanaRegistro extends JFrame {

    public JTextField txtUsuario;
    public JTextField txtContrasena;
    public JTextField txtSaldo;
    public JButton btnRegistrar;

    public VentanaRegistro() {
        // Configuración inicial de la ventana
        setTitle("Registro de Usuario");
        setSize(400, 300); // Ampliar tamaño horizontal
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2));

        // Componentes de la ventana
        JLabel lblUsuario = new JLabel("Usuario:");
        txtUsuario = new JTextField();

        JLabel lblContrasena = new JLabel("Contraseña (Puerto del servidor):");
        txtContrasena = new JTextField();

        JLabel lblSaldo = new JLabel("Saldo inicial:");
        txtSaldo = new JTextField();

        btnRegistrar = new JButton("Registrar");

        // Añadir componentes al layout
        add(lblUsuario);
        add(txtUsuario);
        add(lblContrasena);
        add(txtContrasena);
        add(lblSaldo);
        add(txtSaldo);
        add(btnRegistrar);
    }

    // Métodos para obtener los valores ingresados
    public String getUsuario() {
        return txtUsuario.getText();
    }

    public String getPuertoServidor() {
        return txtContrasena.getText();
    }

    public String getSaldo() {
        return txtSaldo.getText();
    }
}
