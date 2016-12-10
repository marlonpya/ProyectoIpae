package application.ucweb.proyectoipae.model;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 02/08/2016.
 */
public class Acelera extends RealmObject {

    public static final String A_ID             = "id_ace";
    public static final String A_ICONO          = "icono_ace";
    public static final String A_CATEGORIA      = "categoria_ace";
    public static final String A_ID_CATEGORIA   = "id_categoria_ace";
    public static final String A_ESTADO         = "estado_ace";
    public static final String A_ORDEN_ESTADO   = "estado_orden_ace";
    public static final String A_ID_PERIODO     = "id_periodo_ace";

    @PrimaryKey
    private long id_ace;
    private String icono_ace;
    private String categoria_ace;
    private boolean estado_ace;
    private boolean estado_orden_ace;
    private int id_periodo_ace;
    private RealmList<PreguntaAcelera> preguntas;

    public static void activarSiguentePregunta(long id_traido) {
        long id = id_traido + 1;
        if (getUltimoId() > id) {
            Realm realm = Realm.getDefaultInstance();
            Acelera aceleraEstablecio = realm.where(Acelera.class).equalTo(A_ID, id).findFirst();
            realm.beginTransaction();
            Acelera acelera = new Acelera();
            acelera.setId_ace(aceleraEstablecio.getId_ace());
            acelera.setIcono_ace(aceleraEstablecio.getIcono_ace());
            acelera.setCategoria_ace(aceleraEstablecio.getCategoria_ace());
            acelera.setEstado_ace(true);
            acelera.setEstado_orden_ace(true);
            acelera.setId_periodo_ace(aceleraEstablecio.getId_periodo_ace());
            realm.copyToRealmOrUpdate(acelera);
            realm.commitTransaction();
            realm.close();
        }
    }

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number numero = realm.where(Acelera.class).max(A_ID);
        return numero == null ? 0 : numero.intValue() + 1;
    }

    public static int getSize() {
        return getUltimoId() == 0 ? 0 : getUltimoId() - 1;
    }

    public RealmList<PreguntaAcelera> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(RealmList<PreguntaAcelera> preguntas) {
        this.preguntas = preguntas;
    }

    public long getId_ace() {
        return id_ace;
    }

    public void setId_ace(long id_ace) {
        this.id_ace = id_ace;
    }

    public String getIcono_ace() {
        return icono_ace;
    }

    public void setIcono_ace(String icono_ace) {
        this.icono_ace = icono_ace;
    }

    public String getCategoria_ace() {
        return categoria_ace;
    }

    public void setCategoria_ace(String categoria_ace) {
        this.categoria_ace = categoria_ace;
    }

    public boolean isEstado_ace() {
        return estado_ace;
    }

    public void setEstado_ace(boolean estado_ace) {
        this.estado_ace = estado_ace;
    }

    public boolean isEstado_orden_ace() {
        return estado_orden_ace;
    }

    public void setEstado_orden_ace(boolean estado_orden_ace) {
        this.estado_orden_ace = estado_orden_ace;
    }

    public int getId_periodo_ace() {
        return id_periodo_ace;
    }

    public void setId_periodo_ace(int id_periodo_ace) {
        this.id_periodo_ace = id_periodo_ace;
    }
}
