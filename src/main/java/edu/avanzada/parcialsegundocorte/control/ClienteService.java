/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.modelo.ClienteDAO;

/**
 *
 * @author Nicolas
 */
public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public boolean autenticarCliente(String usuario, String contrasena) {
        return clienteDAO.verificarCredenciales(usuario, contrasena);
    }

    public double obtenerSaldo(String usuario) {
        return clienteDAO.obtenerSaldo(usuario);
    }

    public boolean descontarSaldo(String usuario, double monto) {
        double saldoActual = clienteDAO.obtenerSaldo(usuario);
        if (saldoActual >= monto) {
            return clienteDAO.actualizarSaldo(usuario, saldoActual - monto);
        }
        return false;
    }
}
