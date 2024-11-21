/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.modelo;

import java.util.ArrayList;
import java.util.Arrays;
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
public class CancionDAOTest {

    @Mock
    private CancionDAO cancionDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
        cancionDAO = mock(CancionDAO.class); // Crea una instancia simulada de CancionDAO
    }

    @Test
    public void testObtenerCanciones() {
        List<Cancion> cancionesEsperadas = Arrays.asList(
                new Cancion(1, "Cancion1", "Artista1", "ruta1"),
                new Cancion(2, "Cancion2", "Artista2", "ruta2")
        );

        // Configuramos el mock para devolver la lista simulada
        when(cancionDAO.obtenerCanciones()).thenReturn(cancionesEsperadas);

        // Llamamos al método del DAO simulado
        List<Cancion> cancionesObtenidas = cancionDAO.obtenerCanciones();

        // Verificamos que la lista obtenida no está vacía y coincide
        assertNotNull(cancionesObtenidas);
        assertEquals(2, cancionesObtenidas.size());
    }

    @Test
    public void testObtenerRutaPorNombre() {
        String nombreCancion = "Cancion1";
        String rutaEsperada = "ruta1";

        // Configuramos el mock para devolver la ruta esperada
        when(cancionDAO.obtenerRutaPorNombre(nombreCancion)).thenReturn(rutaEsperada);

        // Llamamos al método del DAO simulado
        String rutaReal = cancionDAO.obtenerRutaPorNombre(nombreCancion);

        assertEquals(rutaEsperada, rutaReal);
    }

    @Test
    public void testObtenerCancionPorId() {
        String idCancion = "1";
        Cancion cancionEsperada = new Cancion(1, "Cancion1", "Artista1", "ruta1");

        // Configuramos el mock para devolver la canción esperada
        when(cancionDAO.obtenerCancionPorId(idCancion)).thenReturn(cancionEsperada);

        // Llamamos al método del DAO simulado
        Cancion cancionReal = cancionDAO.obtenerCancionPorId(idCancion);

        assertNotNull(cancionReal);
        assertEquals(cancionEsperada.getId(), cancionReal.getId());
    }
}
