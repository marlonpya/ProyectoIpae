package application.ucweb.proyectoipae.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 24/08/2016.
 */
public class Categoria extends RealmObject {
    public static final String TAG = "Puntaje";

    public static final String C_ID = "id_categoria";
    public static final String C_NOMBRE = "nombre_categoria";
    public static final String C_P1 = "puntaje1";
    public static final String C_P2 = "puntaje2";
    public static final String C_P3 = "puntaje3";
    public static final String C_P4 = "puntaje4";

    @PrimaryKey
    private long id_categoria;
    private String nombre_categoria;

    public long getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(long id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

}
