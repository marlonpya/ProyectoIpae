package application.ucweb.proyectoipae.model;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 04/08/2016.
 */
public class Innova extends RealmObject {

    public static final String I_ID = "id_inn";
    public static final String I_ID_PERIODO = "id_periodo_inn";

    @PrimaryKey
    private long id_inn;
    private String icono_inn;
    private String categoria_inn;
    private boolean estado_inn;
    private boolean estado_orden_inn;
    private int id_periodo_inn;
    private RealmList<PreguntaInnova> preguntas;

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number numero = realm.where(Innova.class).max(I_ID);
        return numero == null ? 0 : numero.intValue() + 1;
    }

    public static int getSize() {
        return getUltimoId() == 0 ? 0 : getUltimoId() - 1;
    }

    public static void activarSiguentePregunta(long id_traido) {
        long id = id_traido + 1;
        if (getUltimoId() > id) {
            Realm realm = Realm.getDefaultInstance();
            Innova innovaEstablecio = realm.where(Innova.class).equalTo(I_ID, id).findFirst();
            realm.beginTransaction();
            Innova innova = new Innova();
            innova.setId_inn(innovaEstablecio.getId_inn());
            innova.setIcono_inn(innovaEstablecio.getIcono_inn());
            innova.setCategoria_inn(innovaEstablecio.getCategoria_inn());
            innova.setEstado_inn(true);
            innova.setEstado_orden_inn(true);
            innova.setId_periodo_inn(innovaEstablecio.getId_periodo_inn());
            realm.copyToRealmOrUpdate(innova);
            realm.commitTransaction();
            realm.close();
        }
    }

    public long getId_inn() {
        return id_inn;
    }

    public void setId_inn(long id_inn) {
        this.id_inn = id_inn;
    }

    public String getIcono_inn() {
        return icono_inn;
    }

    public void setIcono_inn(String icono_inn) {
        this.icono_inn = icono_inn;
    }

    public String getCategoria_inn() {
        return categoria_inn;
    }

    public void setCategoria_inn(String categoria_inn) {
        this.categoria_inn = categoria_inn;
    }

    public boolean isEstado_inn() {
        return estado_inn;
    }

    public void setEstado_inn(boolean estado_inn) {
        this.estado_inn = estado_inn;
    }

    public boolean isEstado_orden_inn() {
        return estado_orden_inn;
    }

    public void setEstado_orden_inn(boolean estado_orden_inn) {
        this.estado_orden_inn = estado_orden_inn;
    }

    public RealmList<PreguntaInnova> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(RealmList<PreguntaInnova> preguntas) {
        this.preguntas = preguntas;
    }

    public int getId_periodo_inn() {
        return id_periodo_inn;
    }

    public void setId_periodo_inn(int id_periodo_inn) {
        this.id_periodo_inn = id_periodo_inn;
    }
}
