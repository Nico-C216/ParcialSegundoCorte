/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.modelo;

/**
 *
 * @author Nicolas
 */
public class Cliente {
    private String nombre;
    private String usuario;
    private String contraseña;
    private double saldoPendiente;

    public Cliente(String nombre, String usuario, String contraseña, double saldoPendiente) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.saldoPendiente = saldoPendiente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }
    
    
    
}
