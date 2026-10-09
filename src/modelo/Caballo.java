/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Caballo {
    private int id;
    private String nombre;
    private String raza;
    private int edad;
    private String pelaje;
    private String estado;

    public Caballo(String nombre, String raza, int edad, String pelaje, String estado) {
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.pelaje = pelaje;
        this.estado = estado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getPelaje() { return pelaje; }
    public void setPelaje(String pelaje) { this.pelaje = pelaje; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() { return nombre; }
}

