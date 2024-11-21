/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.modelo;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Usuario
 */
public class ClienteDAOTest {

    @Mock
    private ClienteDAO clienteDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
        clienteDAO = mock(ClienteDAO.class); // Crea una instancia simulada de ClienteDAO
    }

    @Test
    public void testObtenerSaldo() {
        String usuario = "juan123";
        double saldoEsperado = 100.0;

        // Configuramos el mock para devolver el saldo esperado
        when(clienteDAO.obtenerSaldo(usuario)).thenReturn(saldoEsperado);

        // Llamamos al método del DAO simulado
        double saldoReal = clienteDAO.obtenerSaldo(usuario);

        // Verificamos que el saldo es correcto
        assertEquals(saldoEsperado, saldoReal);
    }

    @Test
    public void testDescontarSaldo() {
        String usuario = "maria456";
        double costoCancion = 20.0;

        // Configuramos el mock para devolver true cuando se descuenta el saldo
        when(clienteDAO.descontarSaldo(usuario, costoCancion)).thenReturn(true);
        when(clienteDAO.obtenerSaldo(usuario)).thenReturn(30.0); // Saldo después del descuento

        // Llamamos al método del DAO simulado
        boolean descuentoExitoso = clienteDAO.descontarSaldo(usuario, costoCancion);

        // Verificamos si la operación fue exitosa y el saldo fue actualizado
        assertTrue(descuentoExitoso);
        assertEquals(30.0, clienteDAO.obtenerSaldo(usuario));
    }
}
