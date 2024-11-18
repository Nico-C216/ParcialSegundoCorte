/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.modelo;

/**Clase con la informacion de las canciones
 *
 * @author Nicolas
 */
public class Cancion {

    private int id;
    private String nombre;
    private String artista;
    private String rutaArchivo;

    /**
     * Constructor
     * @param id
     * @param nombre
     * @param artista
     * @param rutaArchivo 
     */
    public Cancion(int id, String nombre, String artista, String rutaArchivo) {
        this.id = id;
        this.nombre = nombre;
        this.artista = artista;
        this.rutaArchivo = rutaArchivo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getArtista() {
        return artista;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }
}
