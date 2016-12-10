package application.ucweb.proyectoipae.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 22/08/2016.
 */
public class Area extends RealmObject {
    public static final String TAG = Area.class.getSimpleName();

    public static final String A_ID     = "id_area";
    public static final String A_ID_SERVIDOR= "id_area_servidor";
    public static final String A_DESC   = "descripcion_area";
    public static final String A_CAT    = "categoria_area";

    @PrimaryKey
    private long id_area;
    private int id_area_servidor;
    private String descripcion_area;
    private int categoria_area;

    public static int getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Area.class).max(A_ID);
        return number == null ? 0 : number.intValue() + 1;
    }

    public static void reiniciarAreas() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(Area.class);
        realm.commitTransaction();
        realm.close();
        Log.d(TAG,"reiniciarAreas");
    }

    public static int getSize() {
        return getLastId() == 0 ? 0 : getLastId() - 1;
    }

    public long getId_area() {
        return id_area;
    }

    public int getId_area_servidor() {
        return id_area_servidor;
    }

    public void setId_area_servidor(int id_area_servidor) {
        this.id_area_servidor = id_area_servidor;
    }

    public void setId_area(long id_area) {
        this.id_area = id_area;
    }

    public String getDescripcion_area() {
        return descripcion_area;
    }

    public void setDescripcion_area(String descripcion_area) {
        this.descripcion_area = descripcion_area;
    }

    public int getCategoria_area() {
        return categoria_area;
    }

    public void setCategoria_area(int categoria_area) {
        this.categoria_area = categoria_area;
    }
}
