/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.modelo;

import java.sql.*;

/**
 *
 * @author Nicolas
 */
public class ClienteDAO {

    // Método para verificar el saldo de un cliente
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

    // Método para descontar saldo tras una compra
    public boolean descontarSaldo(String usuario, double monto) {
        String sql = "UPDATE clientes SET saldo = saldo - ? WHERE usuario = ?";

        try (Connection cn = ConexionMSQ.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setDouble(1, monto);
            ps.setString(2, usuario);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException ex) {
            System.out.println("Error al descontar saldo: " + ex.getMessage());
        }

        return false;
    }

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

    public boolean actualizarSaldo(String usuario, double nuevoSaldo) {
        String sql = "UPDATE clientes SET saldo = ? WHERE usuario = ?";

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
