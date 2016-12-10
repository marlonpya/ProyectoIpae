package application.ucweb.proyectoipae.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 14/09/2016.
 */
public class Termino extends RealmObject {
    public static final String TAG = Termino.class.getSimpleName();
    public static final String T_ID = "id_termino";

    @PrimaryKey
    private long id_termino;
    private int id_servidor;
    private String descripcion_termino;

    public long getId_termino() {
        return id_termino;
    }

    public void setId_termino(long id_termino) {
        this.id_termino = id_termino;
    }

    public int getId_servidor() {
        return id_servidor;
    }

    public void setId_servidor(int id_servidor) {
        this.id_servidor = id_servidor;
    }

    public String getDescripcion_termino() {
        return descripcion_termino;
    }

    public void setDescripcion_termino(String descripcion_termino) {
        this.descripcion_termino = descripcion_termino;
    }

    public static String traerUltimoTexto() {
        Realm realm = Realm.getDefaultInstance();
        String termino_ultimo = realm.where(Termino.class).equalTo(T_ID, traerUltimoID() - 1).findFirst().getDescripcion_termino();
        Log.d(TAG, "traerUltimoTexto/termino_ultimo" + termino_ultimo);
        return termino_ultimo;
    }

    public static int traerUltimoID() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Termino.class).max(T_ID);
        return number == null ? 0 : number.intValue() + 1;
    }
}
