package application.ucweb.proyectoipae.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 15/08/2016.
 */
public class PreguntaInnova extends RealmObject{
    public static final String TAG = PreguntaInnova.class.getSimpleName();
    public static final String IA_ID            = "id_pregunta_inn";
    public static final String IA_ID_SERVIDOR   = "id_pregunta_servidor_inn";
    //LLAVE FORÁNEA
    public static final String IA_NUM_PREGUNTA  = "num_pregunta_inn";
    //NUMERO ORDEN SEGÚN FORANEA 1,2,3..

    public static final String IA_PREGUNTA      = "pregunta_inn";
    public static final String IA_RESPUESTA     = "respuesta_inn";
    public static final String IA_ID_BOTON      = "id_boton_inn";
    public static final String IA_RESPONDIDO    = "respondido_inn";

    @PrimaryKey
    private long id_pregunta_inn;
    private int id_pregunta_servidor_inn;
    private int num_pregunta_inn;
    private String pregunta_inn;
    private boolean valor_respuesta;
    private String valor_ace;
    private boolean respondido_inn;
    private int id_boton_inn;

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(PreguntaInnova.class).max(IA_ID);
        return number == null ? 0 : number.intValue() + 1;
    }

    public static int getUltimoIdPorPreguntaRespondida() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(PreguntaInnova.class).equalTo(IA_RESPONDIDO, true).max(IA_ID);
        int valor = number == null ? 1 : number.intValue() + 1;
        Log.d(TAG,"getUltimoIdPorPreguntaRespondida/valor_"+ String.valueOf(valor));
        return valor;
    }

    public long getId_pregunta_inn() {
        return id_pregunta_inn;
    }

    public void setId_pregunta_inn(long id_pregunta_inn) {
        this.id_pregunta_inn = id_pregunta_inn;
    }

    public int getId_pregunta_servidor_inn() {
        return id_pregunta_servidor_inn;
    }

    public void setId_pregunta_servidor_inn(int id_pregunta_servidor_inn) {
        this.id_pregunta_servidor_inn = id_pregunta_servidor_inn;
    }

    public int getNum_pregunta_inn() {
        return num_pregunta_inn;
    }

    public void setNum_pregunta_inn(int num_pregunta_inn) {
        this.num_pregunta_inn = num_pregunta_inn;
    }

    public String getPregunta_inn() {
        return pregunta_inn;
    }

    public void setPregunta_inn(String pregunta_inn) {
        this.pregunta_inn = pregunta_inn;
    }

    public boolean isValor_respuesta() {
        return valor_respuesta;
    }

    public void setValor_respuesta(boolean valor_respuesta) {
        this.valor_respuesta = valor_respuesta;
    }

    public String getValor_ace() {
        return valor_ace;
    }

    public void setValor_ace(String valor_ace) {
        this.valor_ace = valor_ace;
    }

    public boolean isRespondido_inn() {
        return respondido_inn;
    }

    public void setRespondido_inn(boolean respondido_inn) {
        this.respondido_inn = respondido_inn;
    }

    public int getId_boton_inn() {
        return id_boton_inn;
    }

    public void setId_boton_inn(int id_boton_inn) {
        this.id_boton_inn = id_boton_inn;
    }
}
