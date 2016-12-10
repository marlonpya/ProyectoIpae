package application.ucweb.proyectoipae.realm;

import android.util.Log;

import application.ucweb.proyectoipae.model.Acelera;
import application.ucweb.proyectoipae.model.Area;
import application.ucweb.proyectoipae.model.Categoria;
import application.ucweb.proyectoipae.model.Innova;
import application.ucweb.proyectoipae.model.PreguntaAcelera;
import application.ucweb.proyectoipae.model.PreguntaInnova;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ucweb02 on 31/08/2016.
 */

/**
 * ESTA CLASE RECIBE TODOS MENSAJES DEL SERVIDOR PARA MODIFICAR O AGREGAR A LA BASE DE DATOS REALM
 */
public class MetodoServices {
    public static final String TAG = MetodoServices.class.getSimpleName();

    public static void eliminarTodaslasPreguntas() {
        eliminarPreguntasAcelera();
        eliminarPreguntasInnova();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Categoria>  c = realm.where(Categoria.class).findAll();
        c.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "eliminarTodaslasPreguntas()");
    }

    public static void eliminarPreguntasAcelera() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<PreguntaAcelera> pa = realm.where(PreguntaAcelera.class).findAll();
        RealmResults<Acelera> a = realm.where(Acelera.class).findAll();
        pa.deleteAllFromRealm();
        a.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "eliminarPreguntasAcelera()");
    }

    public static void eliminarPreguntasInnova() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<PreguntaInnova> pi = realm.where(PreguntaInnova.class).findAll();
        RealmResults<Innova> i = realm.where(Innova.class).findAll();
        pi.deleteAllFromRealm();
        i.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "eliminarPreguntasInnova()");
    }

    public static void eliminarAreas() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Area> a = realm.where(Area.class).findAll();
        a.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        Log.d(TAG, "eliminarAreas()");
    }

    public static void limpiarBDRealm() {
        eliminarTodaslasPreguntas();
        eliminarAreas();
        Log.d(TAG,"ELIMINADOS");
    }

    /**
     *
     * @param id_topico (UNO - 1) PARA ACELERA - (DOS - 2) PARA INNOVA
     * @param id_pregunta EL ID DE BASE DE DATOS, NO REALM
     */
    public static void agregarPregunta(int id_topico, int id_pregunta) {
        Realm realm = Realm.getDefaultInstance();
        switch (id_topico) {
            case 1:
                PreguntaAcelera aceleraServer = realm.where(PreguntaAcelera.class).equalTo(PreguntaAcelera.PA_NUM_PREGUNTA, id_pregunta).findFirst();
                PreguntaAcelera pa = new PreguntaAcelera();
                pa.setId_pregunta_ace(aceleraServer.getId_pregunta_ace());
        }
    }

    public static void eliminarPreguntas() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<PreguntaAcelera> preguntaAceleras = realm.where(PreguntaAcelera.class).findAll();
        final RealmResults<PreguntaInnova> preguntaInnovas = realm.where(PreguntaInnova.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                preguntaAceleras.deleteAllFromRealm();
                preguntaInnovas.deleteAllFromRealm();
            }
        });
        Log.d(TAG, "eliminarPreguntas/preguntaAceleras_" + preguntaAceleras.toString());
        Log.d(TAG, "eliminarPreguntas/preguntaInnovas_" + preguntaInnovas.toString());
    }

    public static void modificarUnaPregunta(int id_servidor, String texto) {
        Realm realm = Realm.getDefaultInstance();
        PreguntaAcelera pAcelera = realm.where(PreguntaAcelera.class).equalTo(PreguntaAcelera.PA_ID_SERVIDOR, id_servidor).findFirst();
        PreguntaInnova pInnova = realm.where(PreguntaInnova.class).equalTo(PreguntaInnova.IA_ID_SERVIDOR, id_servidor).findFirst();
        if (pAcelera != null) {
            realm.beginTransaction();
            PreguntaAcelera acelera = new PreguntaAcelera();
            acelera.setId_pregunta_ace(pAcelera.getId_pregunta_ace());
            acelera.setId_pregunta_servidor_ace(pAcelera.getId_pregunta_servidor_ace());
            acelera.setNum_pregunta_ace(pAcelera.getNum_pregunta_ace());
            acelera.setPregunta_ace(pAcelera.getPregunta_ace());
            acelera.setValor_respuesta(pAcelera.isValor_respuesta());
            acelera.setValor_ace(pAcelera.getValor_ace());
            acelera.setRespondido_ace(pAcelera.isRespondido_ace());
            acelera.setId_boton_ace(-1);
            realm.copyToRealmOrUpdate(acelera);
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, "acelera_actualizado");
        } else {
            realm.beginTransaction();
            PreguntaInnova innova = new PreguntaInnova();
            innova.setId_pregunta_inn(pInnova.getId_pregunta_inn());
            innova.setId_pregunta_servidor_inn(pInnova.getId_pregunta_servidor_inn());
            innova.setNum_pregunta_inn(pInnova.getNum_pregunta_inn());
            innova.setPregunta_inn(texto);
            innova.setValor_respuesta(pInnova.isValor_respuesta());
            innova.setValor_ace(pInnova.getValor_ace());
            innova.setRespondido_inn(pInnova.isRespondido_inn());
            innova.setId_boton_inn(-1);
            realm.copyToRealmOrUpdate(innova);
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, "innova_actualizado");
        }
    }

    public static int saberPreguntaAceleraOInnova(int id_servidor) {
        int resultado = 0;
        Realm realm = Realm.getDefaultInstance();
        PreguntaAcelera acelera = realm.where(PreguntaAcelera.class).equalTo(PreguntaAcelera.PA_ID_SERVIDOR, id_servidor).findFirst();
        PreguntaInnova innova = realm.where(PreguntaInnova.class).equalTo(PreguntaInnova.IA_ID_SERVIDOR, id_servidor).findFirst();
        if (acelera != null) {
            resultado = 1;
            Log.d(TAG, "saberPreguntaAceleraOInnova/resultado1_"+String.valueOf(resultado));
        } else if (innova != null) {
            resultado = 2;
            Log.d(TAG, "saberPreguntaAceleraOInnova/resultado2_"+String.valueOf(resultado));
        }
        return resultado;
    }

}
