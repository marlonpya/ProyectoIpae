package application.ucweb.proyectoipae.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 09/08/2016.
 */
public class PreguntaAcelera extends RealmObject {
    public static final String TAG = PreguntaAcelera.class.getSimpleName();
    public static final String PA_ID            = "id_pregunta_ace";
    public static final String PA_ID_SERVIDOR   = "id_pregunta_servidor_ace";
    //LLAVE FORÁNEA
    public static final String PA_NUM_PREGUNTA  = "num_pregunta_ace";
    public static final String PA_PREGUNTA      = "pregunta_ace";
    public static final String PA_RESPUESTA     = "respuesta_ace";
    public static final String PA_VALOR_RESPUESTA= "valor_respuesta";
    public static final String PA_VALOR_TEXTO   = "valor_ace";
    public static final String PA_ID_BOTON      = "id_boton_ace";
    public static final String PA_RESPONDIDO    = "respondido_ace";

    @PrimaryKey
    private long id_pregunta_ace;
    private int id_pregunta_servidor_ace;
    private int num_pregunta_ace;
    private String pregunta_ace;
    private boolean valor_respuesta;
    private String valor_ace;
    private boolean respondido_ace;
    private int id_boton_ace;

    public static int getUltimoId(){
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(PreguntaAcelera.class).max(PA_ID);
        return number == null ? 0 : number.intValue() + 1;
    }

    public static int getUltimoIdPorPreguntaRespondida() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(PreguntaAcelera.class).equalTo(PA_RESPONDIDO, true).max(PA_ID);
        int valor = number == null ? 1 : number.intValue() + 1;
        Log.d(TAG,"getUltimoIdPorPreguntaRespondida/valor_"+String.valueOf(valor));
        return valor;
    }

    public long getId_pregunta_ace() {
        return id_pregunta_ace;
    }

    public void setId_pregunta_ace(long id_pregunta_ace) {
        this.id_pregunta_ace = id_pregunta_ace;
    }

    public int getId_pregunta_servidor_ace() {
        return id_pregunta_servidor_ace;
    }

    public void setId_pregunta_servidor_ace(int id_pregunta_servidor_ace) {
        this.id_pregunta_servidor_ace = id_pregunta_servidor_ace;
    }

    public int getNum_pregunta_ace() {
        return num_pregunta_ace;
    }

    public void setNum_pregunta_ace(int num_pregunta_ace) {
        this.num_pregunta_ace = num_pregunta_ace;
    }

    public String getPregunta_ace() {
        return pregunta_ace;
    }

    public void setPregunta_ace(String pregunta_ace) {
        this.pregunta_ace = pregunta_ace;
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

    public boolean isRespondido_ace() {
        return respondido_ace;
    }

    public void setRespondido_ace(boolean respondido_ace) {
        this.respondido_ace = respondido_ace;
    }

    public int getId_boton_ace() {
        return id_boton_ace;
    }

    public void setId_boton_ace(int id_boton_ace) {
        this.id_boton_ace = id_boton_ace;
    }

}
