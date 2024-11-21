/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.modelo;

import java.sql.*;

/**
 * Clase que maneja el DAO para el registro de los clientes en la base de datos
 *
 * @author Nicolas
 */
public class ClienteDAO {

    /**
     * Método para verificar el saldo de un cliente
     *
     * @param usuario
     * @return
     */
    public double obtenerSaldo(String usuario) {
        double saldo = 0.0;
        String sql = "SELECT saldo FROM clientes WHERE usuario = ?";

        try (Connection cn = ConexionMSQ.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    saldo = rs.getDouble("saldo");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener el saldo del cliente: " + ex.getMessage());
        }

        return saldo;
    }

    /**
     * Método para descontar saldo tras una compra
     * @param nombreUsuario
     * @param costoCancion
     * @return 
     */
    public boolean descontarSaldo(String nombreUsuario, double costoCancion) {
        String verificarSaldo = "SELECT saldo FROM usuarios WHERE nombre = ?";
        String actualizarSaldo = "UPDATE usuarios SET saldo = saldo - ? WHERE nombre = ?";

        try (Connection cn = ConexionMSQ.getConexion()) {
            // Verificar saldo
            try (PreparedStatement ps = cn.prepareStatement(verificarSaldo)) {
                ps.setString(1, nombreUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double saldoActual = rs.getDouble("saldo");
                        if (saldoActual >= costoCancion) {
                            // Actualizar saldo
                            try (PreparedStatement ps2 = cn.prepareStatement(actualizarSaldo)) {
                                ps2.setDouble(1, costoCancion);
                                ps2.setString(2, nombreUsuario);
                                ps2.executeUpdate();
                                return true; // Saldo descontado correctamente
                            }
                        } else {
                            System.out.println("No hay saldo suficiente.");
                            return false; // Saldo insuficiente
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al descontar saldo: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Metodo para verificar las credenciales
     *
     * @param usuario
     * @param contrasena
     * @return
     */
    public boolean verificarCredenciales(String usuario, String contrasena) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE usuario = ? AND contrasena = ?";
        try (Connection cn = ConexionMSQ.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error al verificar credenciales: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Metodo para actualizar el saldo del usuario
     *
     * @param usuario
     * @param nuevoSaldo
     * @return
     */
    public boolean actualizarSaldo(String usuario, double nuevoSaldo) {
        String sql = "UPDATE clientes SET saldo = saldo + ? WHERE usuario = ?";

        try (Connection cn = ConexionMSQ.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, usuario);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar saldo: " + ex.getMessage());
        }

        return false;
    }

    /**
     * Metodo para registrar al usuario
     *
     * @param cliente
     * @return
     */
    public boolean registrarUsuario(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, usuario, contrasena, saldo) VALUES (?, ?, ?, ?)";
        try (Connection cn = ConexionMSQ.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getUsuario());
            ps.setString(3, cliente.getContraseña());
            ps.setDouble(4, cliente.getSaldoPendiente());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException ex) {
            System.out.println("Error al registrar usuario: " + ex.getMessage());
        }
        return false;
    }

}
