/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.control;

import edu.avanzada.parcialsegundocorte.modelo.ClienteDAO;

/**Clase especializada en manejar el saldo del cliente
 *
 * @author Nicolas
 */
public class ClienteService {

    private final ClienteDAO clienteDAO;

    /**
     * Constructor
     * @param clienteDAO 
     */
    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    /**
     * Metodo para autenticar al cliente
     * @param usuario
     * @param contrasena
     * @return 
     */
    public boolean autenticarCliente(String usuario, String contrasena) {
        return clienteDAO.verificarCredenciales(usuario, contrasena);
    }

    /**
     * Metodo para obtener el saldo del cliente
     * @param usuario
     * @return 
     */
    public double obtenerSaldo(String usuario) {
        return clienteDAO.obtenerSaldo(usuario);
    }

    /**
     * Metodo para descontar el saldo en la descarga
     * @param usuario
     * @param monto
     * @return 
     */
    public boolean descontarSaldo(String usuario, double monto) {
        double saldoActual = clienteDAO.obtenerSaldo(usuario);
        if (saldoActual >= monto) {
            return clienteDAO.actualizarSaldo(usuario, saldoActual - monto);
        }
        return false;
    }
}
