/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo; // paquete (carpeta) donde esta esta clase: el modelo

public class Caballo { // molde de un caballo: guarda sus datos (no hace nada mas)
    private int id; // numero unico del caballo; lo asigna la base (autoincremental)
    private String nombre; // nombre del caballo
    private String raza; // raza del caballo
    private int edad; // edad en anios (numero entero)
    private String pelaje; // color del pelaje
    private String estado; // "Disponible" o "En descanso"

    public Caballo(String nombre, String raza, int edad, String pelaje, String estado) { // constructor: crea un caballo con estos datos (sin id; el id se pone despues)
        this.nombre = nombre; // this.nombre es el atributo; nombre es el parametro que llego
        this.raza = raza; // guarda la raza recibida
        this.edad = edad; // guarda la edad recibida
        this.pelaje = pelaje; // guarda el pelaje recibido
        this.estado = estado; // guarda el estado recibido
    } // fin del constructor

    public int getId() { return id; } // getter: devuelve el id (los atributos son private, se leen con getters)
    public void setId(int id) { this.id = id; } // setter: cambia el id (lo usan los DAO y los controladores)
    public String getNombre() { return nombre; } // getter: devuelve el nombre
    public void setNombre(String nombre) { this.nombre = nombre; } // setter: cambia el nombre
    public String getRaza() { return raza; } // getter: devuelve la raza
    public void setRaza(String raza) { this.raza = raza; } // setter: cambia la raza
    public int getEdad() { return edad; } // getter: devuelve la edad
    public void setEdad(int edad) { this.edad = edad; } // setter: cambia la edad
    public String getPelaje() { return pelaje; } // getter: devuelve el pelaje
    public void setPelaje(String pelaje) { this.pelaje = pelaje; } // setter: cambia el pelaje
    public String getEstado() { return estado; } // getter: devuelve el estado
    public void setEstado(String estado) { this.estado = estado; } // setter: cambia el estado

    @Override // indica que se sobrescribe un metodo que ya existe en Object (la clase padre de todas)
    public String toString() { return nombre; } // texto del caballo: el JComboBox lo usa para mostrar el nombre en el desplegable
} // fin de la clase