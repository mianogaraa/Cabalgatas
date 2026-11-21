package controlador; // paquete de los controladores

import java.awt.event.KeyAdapter; // escucha el teclado
import java.awt.event.KeyEvent; // evento de tecla
import java.sql.SQLException; // error de la base
import java.util.List; // lista de objetos
import javax.swing.JOptionPane; // cuadros de mensaje
import javax.swing.table.DefaultTableModel; // datos de una tabla
import modelo.Caballo; // el caballo
import modelo.CaballoDAO; // SQL de caballos
import modelo.Fechas; // traductor de fechas
import modelo.Movimiento; // la cabalgata
import modelo.MovimientoDAO; // SQL de cabalgatas
import vista.CaballoDialog; // formulario de caballo
import vista.GrupoDialog; // formulario de grupo
import vista.MovimientoDialog; // formulario de cabalgata
import vista.VentanaPrincipal; // la ventana principal

public class ControladorPrincipal { // controlador de la ventana principal

    private VentanaPrincipal vista; // la ventana
    private CaballoDAO caballoDAO = new CaballoDAO(); // DAO de caballos
    private MovimientoDAO movimientoDAO = new MovimientoDAO(); // DAO de cabalgatas
    private List<Caballo> todosLosCaballos; // todos, sin filtrar
    private List<Caballo> caballos; // los que se ven en la tabla
    private List<Movimiento> movimientos; // cabalgatas del caballo elegido

    public ControladorPrincipal(VentanaPrincipal vista) { // constructor: recibe la ventana
        this.vista = vista; // guarda la ventana
        vista.getTablaCaballos().getSelectionModel().addListSelectionListener(e -> { // al elegir un caballo
            if (!e.getValueIsAdjusting()) { // cuando termina de cambiar la seleccion
                cargarMovimientos(); // muestra sus cabalgatas (maestro-detalle)
            }
        });
        vista.getTxtBusqueda().addKeyListener(new KeyAdapter() { // escucha el buscador
            @Override // sobrescribe el metodo de KeyAdapter
            public void keyReleased(KeyEvent e) { // al soltar una tecla
                cargarCaballos(); // filtra mientras escribe
            }
        });
        vista.getBtnAgregarCaballo().addActionListener(e -> agregarCaballo()); // boton Agregar caballo
        vista.getBtnModificarCaballo().addActionListener(e -> modificarCaballo()); // boton Modificar caballo
        vista.getBtnEliminarCaballo().addActionListener(e -> eliminarCaballo()); // boton Eliminar caballo
        vista.getBtnAgregarMovimiento().addActionListener(e -> agregarMovimiento()); // boton Agregar cabalgata
        vista.getBtnGrupoMovimiento().addActionListener(e -> agregarGrupo()); // boton Grupo
        vista.getBtnModificarMovimiento().addActionListener(e -> modificarMovimiento()); // boton Modificar cabalgata
        vista.getBtnEliminarMovimiento().addActionListener(e -> eliminarMovimiento()); // boton Eliminar cabalgata
        cargarCaballos(); // carga la tabla al arrancar
    }

    private void cargarCaballos() { // llena la tabla de caballos
        String texto = vista.getTextoBusqueda(); // texto del buscador
        try { // la base puede fallar
            todosLosCaballos = caballoDAO.listarTodos(); // pide todos
            caballos = texto.isEmpty() ? todosLosCaballos : caballoDAO.buscar(texto); // sin texto: todos; con texto: busca
        } catch (SQLException ex) { // si falla la base
            mostrarError(ex); // muestra el error
            return; // sale
        }
        DefaultTableModel modelo = vista.getModeloCaballos(); // datos de la tabla
        modelo.setRowCount(0); // borra las filas
        for (Caballo c : caballos) { // recorre los caballos
            modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getRaza(), c.getEdad(), c.getPelaje(), c.getEstado()}); // agrega una fila
        }
        cargarMovimientos(); // actualiza la tabla de abajo
    }

    private void cargarMovimientos() { // llena la tabla de cabalgatas
        DefaultTableModel modelo = vista.getModeloMovimientos(); // datos de la tabla
        modelo.setRowCount(0); // borra las filas
        Caballo seleccionado = getCaballoSeleccionado(); // caballo elegido o null
        if (seleccionado == null) { // si no hay ninguno
            return; // tabla vacia
        }
        try { // la base puede fallar
            movimientos = movimientoDAO.listarPorCaballo(seleccionado.getId()); // cabalgatas de ese caballo
        } catch (SQLException ex) { // si falla la base
            mostrarError(ex); // muestra el error
            return; // sale
        }
        for (Movimiento m : movimientos) { // recorre las cabalgatas
            modelo.addRow(new Object[]{m.getId(), m.getTipoRecorrido(), Fechas.aTexto(m.getFecha()), m.getHoraInicio(), m.getCliente(), // fila: id, tipo, fecha, hora, cliente
                m.getDuracion(), m.getPrecio(), m.calcularPrecioFinal()}); // duracion, precio y precio final (polimorfismo)
        }
    }

    private void agregarCaballo() { // boton Agregar caballo
        abrirFormularioCaballo("Nuevo caballo", null); // formulario vacio
    }

    private void modificarCaballo() { // boton Modificar caballo
        Caballo seleccionado = getCaballoSeleccionado(); // caballo elegido
        if (seleccionado == null) { // si no eligio ninguno
            avisar("Elegi un caballo de la tabla"); // avisa
            return; // sale
        }
        abrirFormularioCaballo("Modificar caballo", seleccionado); // formulario con sus datos
    }

    private void abrirFormularioCaballo(String titulo, Caballo caballo) { // sirve para alta y modificacion
        CaballoDialog dialogo = new CaballoDialog(vista, titulo); // crea la vista
        ControladorCaballo controlador = new ControladorCaballo(dialogo, caballo); // crea su controlador
        dialogo.setVisible(true); // lo muestra y espera (modal)
        if (controlador.isGuardado()) { // si se guardo
            cargarCaballos(); // recarga la tabla
        }
    }

    private void eliminarCaballo() { // boton Eliminar caballo
        Caballo seleccionado = getCaballoSeleccionado(); // caballo elegido
        if (seleccionado == null) { // si no eligio ninguno
            avisar("Elegi un caballo de la tabla"); // avisa
            return; // sale
        }
        try { // la base puede fallar
            if (!movimientoDAO.listarPorCaballo(seleccionado.getId()).isEmpty()) { // si tiene cabalgatas
                avisar("No se puede eliminar: el caballo tiene cabalgatas registradas. Eliminalas primero."); // regla de negocio
                return; // no borra
            }
            if (!confirmar("Eliminar a " + seleccionado.getNombre() + "?")) { // pide confirmacion
                return; // si dice que no, sale
            }
            caballoDAO.eliminar(seleccionado.getId()); // DELETE
        } catch (SQLException ex) { // si falla la base
            mostrarError(ex); // muestra el error
            return; // sale
        }
        cargarCaballos(); // recarga la tabla
    }

    private void agregarMovimiento() { // boton Agregar cabalgata
        if (hayCaballos()) { // solo si hay caballos
            abrirFormularioMovimiento("Nueva cabalgata", null); // formulario vacio
        }
    }

    private void modificarMovimiento() { // boton Modificar cabalgata
        Movimiento seleccionado = getMovimientoSeleccionado(); // cabalgata elegida
        if (seleccionado == null) { // si no eligio ninguna
            avisar("Elegi una cabalgata de la tabla de abajo"); // avisa
            return; // sale
        }
        abrirFormularioMovimiento("Modificar cabalgata", seleccionado); // formulario con sus datos
    }

    private void abrirFormularioMovimiento(String titulo, Movimiento existente) { // sirve para alta y modificacion
        MovimientoDialog dialogo = new MovimientoDialog(vista, titulo, todosLosCaballos); // crea la vista
        ControladorMovimiento controlador = new ControladorMovimiento(dialogo, existente); // crea su controlador
        dialogo.setVisible(true); // lo muestra y espera (modal)
        mostrarResultado(controlador.getGuardado()); // selecciona el caballo si se guardo
    }

    private void agregarGrupo() { // boton Grupo
        if (!hayCaballos()) { // si no hay caballos
            return; // sale
        }
        GrupoDialog dialogo = new GrupoDialog(vista, todosLosCaballos); // crea la vista
        ControladorGrupo controlador = new ControladorGrupo(dialogo); // crea su controlador
        dialogo.setVisible(true); // lo muestra y espera (modal)
        List<Movimiento> guardados = controlador.getGuardados(); // cabalgatas guardadas
        if (!guardados.isEmpty()) { // si se guardo el grupo
            avisar("Se agendaron " + guardados.size() + " cabalgatas para el grupo " + guardados.get(0).getCliente()); // avisa cuantas
            mostrarResultado(guardados.get(0)); // selecciona el primer caballo
        }
    }

    private boolean hayCaballos() { // true si hay al menos un caballo
        if (todosLosCaballos.isEmpty()) { // si no hay ninguno
            avisar("Primero carga un caballo"); // avisa
            return false; // no hay
        }
        return true; // hay
    }

    private void eliminarMovimiento() { // boton Eliminar cabalgata
        Movimiento seleccionado = getMovimientoSeleccionado(); // cabalgata elegida
        if (seleccionado == null) { // si no eligio ninguna
            avisar("Elegi una cabalgata de la tabla de abajo"); // avisa
            return; // sale
        }
        if (!confirmar("Eliminar la cabalgata de " + seleccionado.getCliente() + "?")) { // pide confirmacion
            return; // si dice que no, sale
        }
        try { // la base puede fallar
            movimientoDAO.eliminar(seleccionado.getId()); // DELETE
        } catch (SQLException ex) { // si falla la base
            mostrarError(ex); // muestra el error
            return; // sale
        }
        cargarMovimientos(); // recarga la tabla
    }

    private void mostrarResultado(Movimiento guardado) { // selecciona el caballo de la cabalgata guardada
        if (guardado == null) { // si no se guardo nada
            return; // no hace nada
        }
        int id = guardado.getCaballo().getId(); // id del caballo
        int fila = buscarFila(id); // fila donde esta
        if (fila < 0) { // si el buscador lo esconde
            vista.setTextoBusqueda(""); // borra el buscador
            cargarCaballos(); // recarga todos
            fila = buscarFila(id); // busca otra vez
        }
        if (fila >= 0) { // si lo encontro
            vista.getTablaCaballos().setRowSelectionInterval(fila, fila); // selecciona esa fila
        }
        cargarMovimientos(); // muestra sus cabalgatas
    }

    private int buscarFila(int caballoId) { // fila del caballo, o -1
        for (int i = 0; i < caballos.size(); i++) { // recorre con indice i
            if (caballos.get(i).getId() == caballoId) { // si el id coincide
                return i; // devuelve la fila
            }
        }
        return -1; // no esta
    }

    private Caballo getCaballoSeleccionado() { // caballo elegido o null
        int fila = vista.getTablaCaballos().getSelectedRow(); // fila elegida (-1 si ninguna)
        if (fila < 0) { // si no hay
            return null; // devuelve null
        }
        return caballos.get(fila); // el caballo de esa fila
    }

    private Movimiento getMovimientoSeleccionado() { // cabalgata elegida o null
        int fila = vista.getTablaMovimientos().getSelectedRow(); // fila elegida (-1 si ninguna)
        if (fila < 0) { // si no hay
            return null; // devuelve null
        }
        return movimientos.get(fila); // la cabalgata de esa fila
    }

    private void avisar(String mensaje) { // cuadro de mensaje
        JOptionPane.showMessageDialog(vista, mensaje); // lo muestra
    }

    private boolean confirmar(String mensaje) { // pregunta Si/No
        int respuesta = JOptionPane.showConfirmDialog(vista, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION); // muestra el cuadro
        return respuesta == JOptionPane.YES_OPTION; // true si dijo Si
    }

    private void mostrarError(SQLException ex) { // error de la base
        JOptionPane.showMessageDialog(vista, "Error en la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // cuadro de error
    }
}