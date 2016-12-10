package application.ucweb.proyectoipae.model;

/**
 * Created by ucweb02 on 02/08/2016.
 */
public class ItemNavegador {
    private String titulo;
    private Integer icono;

    public ItemNavegador(String titulo) {
        this.titulo = titulo;
    }

    public ItemNavegador(String titulo, Integer icono) {
        this.titulo = titulo;
        this.icono = icono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getIcono() {
        return icono;
    }

    public void setIcono(Integer icono) {
        this.icono = icono;
    }
}
