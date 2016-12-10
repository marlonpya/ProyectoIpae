package application.ucweb.proyectoipae;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.model.Innova;
import application.ucweb.proyectoipae.model.PreguntaInnova;
import application.ucweb.proyectoipae.util.Constantes;
import application.ucweb.proyectoipae.util.Preferencia;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class CuestionarioInnovaActivity extends BaseActivity {
    @BindView(R.id.btnAvanzarInnova) ImageView btnAvanzar;
    @BindView(R.id.btnRetrocederInnova) ImageView btnRetroceder;
    @BindView(R.id.btnPanel_no_innova) LinearLayout btnNO;
    @BindView(R.id.btnPanel_proceso_innova) LinearLayout btnPROCESO;
    @BindView(R.id.btnPanel_si_innova) LinearLayout btnSI;
    @BindView(R.id.iv_fondo_aci) ImageView fondo;
    @BindView(R.id.toolbar_principal) Toolbar toolbar;
    @BindView(R.id.ivCuestionario_innova_imagen) ImageView innovaImagen;
    @BindView(R.id.iv_panel_circular_innova) ImageView panel_circular;
    @BindView(R.id.iv_tarjeta_innova_vacio) ImageView tarjeta_vacia;
    @BindView(R.id.ivToolbar_Icono) ImageView icono_toolbar;
    @BindView(R.id.tvCuestionario_innova_categoria) TextView innovaCategoria;
    @BindView(R.id.tvCuestionario_innova_pregunta) TextView innovaPregunta;
    @BindView(R.id.tv_cantidad_preguntas_innova) TextView numero_pregunta_total;
    @BindView(R.id.tv_cantidad_actual_innova) TextView numero_pregunta_actual;
    @BindView(R.id.tvDe_innova) TextView tvDe_innova;
    @BindDrawable(R.drawable.boton_derecha_innova) Drawable drwDerecha;
    @BindDrawable(R.drawable.boton_izquierda_innova) Drawable drwIzquierda;
    @BindDrawable(R.drawable.boton_derecha_opaco) Drawable drwOpacoDerecha;
    @BindDrawable(R.drawable.boton_izquierda_opaco) Drawable drwOpacoIzquierda;
    public static final String TAG = CuestionarioInnovaActivity.class.getSimpleName();
    private Realm realm;
    private int cantidadLista;
    private Innova innova = null;
    private PreguntaInnova preguntaInnova = null;
    private long id_innova;
    private long id_pregunta_innova = 0;
    private int id_num_pregunta_innova = 0;
    private int texto_posicion = 0;
    private int cantidad_maxima = 0;
    private int id_periodo = 0;
    private String ruta_imagen = "";
    private String pregunta_innova;
    private int[] arrayInt;
    private boolean valor_respuesta = false;
    private boolean activar_preferencia = false;
    private boolean excepcion_pregunta_actualizada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            hacerTransicion();
        }
        super.onCreate(savedInstanceState);

        //ID DE ENTIDAD INNOVA
        id_innova = getIntent().getLongExtra(Constantes.KEY_ID_INNOVA, -1);
        Log.d(TAG,"id_innova_"+String.valueOf(id_innova));

        //ID DE ENTIDAD PREGUNTA_INNOVA
        id_num_pregunta_innova = getIntent().getIntExtra(Constantes.KEY_NUM_ORDEN_INNOVA, 0);
        Log.d(TAG,"id_num_pregunta_innova_"+String.valueOf(id_num_pregunta_innova));

        //GUARDA EL NÚMERO DE RESPUESTA EN DONDE SE ENCUENTRA EL USUARIO
        texto_posicion = getIntent().getIntExtra(Constantes.KEY_POSICION_PREGUNTA_INNOVA, 1);
        Log.d(TAG, "texto_posicion_"+String.valueOf(texto_posicion));

        //SI ES RECIBIDA EL EXTRA ACTIVAR PREFERENCIA SE ACTIVA LA PREFERENCIA PARA SER USADA
        activar_preferencia = getIntent().getBooleanExtra(Constantes.KEY_ACTIVAR_PREFERENCIA_INNOVA, false);
        Log.d(TAG, "activar_preferencia_"+String.valueOf(activar_preferencia));

        //SI EXISTE ACTUALIZACIÓN DE UNA PREGUNTA
        excepcion_pregunta_actualizada = getIntent().getBooleanExtra(Constantes.KEY_UNICA_PREGUNTA_INNOVA, false);
        Log.d(TAG, "excepcion_pregunta_actualizada_i" + String.valueOf(excepcion_pregunta_actualizada));

        //VARIABLE INT_ARRAY PARA GUARDAR COMO PREFERENCIA, DÓNDE SE ENCUENTRA EL USUARIO;
        // SI ES QUE SUS DATOS SON VÁLIDOS COMO MUESTRA EL IF, SERÁN CAMBIADOS HASTA DONDE MANDE LA PREFERENCIA
        // REQUERIMIENTO EXTRA: LA PREFERENCIA ES CAPTURADA DESDE LA ACTIVIDAD INNOVA_ACTIVITY
        if (activar_preferencia) {
            arrayInt = Preferencia.traerIdPreferenciaInnova(this);
            Log.d(TAG, "arrayLocal_" + Arrays.toString(arrayInt));
            if (arrayInt[0] != -1 && arrayInt[1] != -1 && arrayInt[2] > -1) {
                id_innova = (int) arrayInt[0];
                id_num_pregunta_innova = arrayInt[1];
                texto_posicion = arrayInt[2];
            }
        }

        if (excepcion_pregunta_actualizada) {
            id_innova = getIntent().getLongExtra(Constantes.KEY_UNICA_ID_FORANEA_INNOVA, -1);
            id_num_pregunta_innova = getIntent().getIntExtra(Constantes.KEY_UNICA_ID_INNOVA, -1);
            texto_posicion = - 1;
            Log.d(TAG, "idtraido_i" + String.valueOf(id_innova));
            Log.d(TAG, "foranea_1" + String.valueOf(id_num_pregunta_innova));
        }
        //GUARDAMOS EL TAMAÑO DE TODOS LOS TÓPICOS DE INNOVA (ORGANIZACIÓN, COMERCIAL Y MARKETING, OPTIMIZACIÓN, ETC..),
        // EN ESTE CASO 3; Y CADA ENTIDAD INNOVA TIENE SUS PROPIAS PREGUNTAS
        cantidadLista = Innova.getSize();

        //INICIAMOS LA ENTIDAD INNOVA
        realm = Realm.getDefaultInstance();
        innova = realm.where(Innova.class).equalTo(Innova.I_ID, id_innova).findFirst();
        ruta_imagen = innova.getIcono_inn();
        pregunta_innova = innova.getCategoria_inn();
        id_periodo = innova.getId_periodo_inn();

        //INICIAMOS LA ENTIDAD PREGUNTA_INNOVA, CON TODAS LAS PREGUNTAS DE INNOVA
        RealmResults<PreguntaInnova> lista = realm.where(PreguntaInnova.class)
                .equalTo(PreguntaInnova.IA_NUM_PREGUNTA, (int) id_innova)
                .findAll();

        //RECOGEMOS LA CANTIDAD DE LAS PREGUNTAS PARA MOSTRAR (Ejm: 1 DE 10)
        if (!excepcion_pregunta_actualizada) {
            cantidad_maxima = lista.size();
            //EN ESTA PARTE MOSTRAMOS LA PREGUNTA_INNOVA
            preguntaInnova = lista.get(id_num_pregunta_innova);//RECIBE EL PRIMER PUNTERO, NO ID
        } else {
            preguntaInnova = realm.where(PreguntaInnova.class).equalTo(PreguntaInnova.IA_ID_SERVIDOR, id_num_pregunta_innova).findFirst();
        }

        //OBTENEMOS EL ÚLTIMO ID GUARDADO, DESDE EL ADAPTADOR
        if (getIntent().hasExtra(Constantes.KEY_POSICION_INNOVA)) {
            int id_innova = getIntent().getIntExtra(Constantes.KEY_POSICION_INNOVA, -1);
            Log.d(TAG, "id_innova_" + String.valueOf(id_innova));
            preguntaInnova = realm.where(PreguntaInnova.class).equalTo(PreguntaInnova.IA_ID, id_innova).findFirst();

        }
        id_pregunta_innova = preguntaInnova.getId_pregunta_inn();
        Log.d(TAG,"id_pregunta_innova_"+String.valueOf(id_pregunta_innova));
        valor_respuesta = preguntaInnova.isValor_respuesta();

        //TODO: INNOVA settear setContentView(R.layout.layout); 1 o 2, continuar después para tener el control del layout o views
        excepcionBotton(valor_respuesta); //<--
        iniciarLayout();

        usarGlide(this, innova.getIcono_inn(), innovaImagen);
        innovaImagen.setColorFilter(Color.WHITE);
        innovaCategoria.setText(innova.getCategoria_inn());
        numero_pregunta_total.setText(String.valueOf(cantidad_maxima));
        numero_pregunta_actual.setText(String.valueOf(texto_posicion));
        innovaPregunta.setText(preguntaInnova.getPregunta_inn());

        pintarBotonPresionado(preguntaInnova.getId_boton_inn());
        Log.d(TAG,"innova_"+innova.toString());
        Log.d(TAG, "preguntaInnova_"+preguntaInnova.toString());
        Log.d(TAG,"listaInnova"+lista.toString());
        btnAvanzar.setBackground(drwDerecha);
        btnRetroceder.setBackground(drwIzquierda);
        validarControl(texto_posicion, 1, cantidad_maxima, btnRetroceder, btnAvanzar);

        if (excepcion_pregunta_actualizada) {
            btnAvanzar.setBackground(drwOpacoDerecha);
            btnRetroceder.setBackground(drwOpacoIzquierda);
            btnAvanzar.setClickable(false);
            btnRetroceder.setClickable(false);

            numero_pregunta_actual.setTextColor(Color.TRANSPARENT);
            tvDe_innova.setTextColor(Color.TRANSPARENT);
            numero_pregunta_total.setTextColor(Color.TRANSPARENT);
        }
    }

    private void pintarBotonPresionado(int respuesta){
        if (respuesta != -1) {
            LinearLayout miLinear = (LinearLayout) findViewById(respuesta);
            LinearLayout btnNOAPLICA = (LinearLayout)findViewById(R.id.btnPanel_noaplica_innova);
            if (respuesta == R.id.btnPanel_noaplica_innova) {
                btnNOAPLICA.setPressed(true);
            } else {
                miLinear.setPressed(true);
            }
            Log.d(TAG,"respuesta_" + String.valueOf(respuesta));
        }
    }

    private void validarControl(int posicion, int inicio, int fin, ImageView izquierda, ImageView derecha) {
        if (posicion == inicio) {
            izquierda.setClickable(false);
            izquierda.setBackground(drwOpacoIzquierda);
        } else if (posicion == fin) {
            derecha.setClickable(false);
            derecha.setBackground(drwOpacoDerecha);
        }
        if (preguntaInnova.getId_pregunta_inn() == PreguntaInnova.getUltimoIdPorPreguntaRespondida()) {
            btnAvanzar.setClickable(false);
            btnAvanzar.setBackground(drwOpacoDerecha);
        }
    }

    private void excepcionBotton(boolean variable_4_botones) {
        if (variable_4_botones) {
            setContentView(R.layout.activity_cuestionario_innova);
            usarGlide(this, R.drawable.panel_circular_innova, panel_circular);
        } else {
            setContentView(R.layout.activity_cuestionario_innova2);
            usarGlide(this, R.drawable.panel_circular_innova2, panel_circular);
            final LinearLayout button = (LinearLayout)findViewById(R.id.btnPanel_noaplica_innova);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "button_/texto_posicion" + String.valueOf(texto_posicion));
                    responder(button.getId());
                }
            });
        }
    }

    private void hacerTransicion() {
        Slide slide1 = new Slide();
        slide1.setDuration(1000);
        getWindow().setEnterTransition(slide1);

        Slide slide2 = new Slide();
        slide2.setDuration(1000);
        getWindow().setReturnTransition(slide2);
    }

    @OnClick({R.id.btnPanel_no_innova, R.id.btnPanel_proceso_innova, R.id.btnPanel_si_innova})
    public void presionarOpcionPanelInnova(LinearLayout linearLayout){
        switch (linearLayout.getId()){
            case R.id.btnPanel_no_innova : responder(linearLayout.getId()); break;
            case R.id.btnPanel_proceso_innova : responder(linearLayout.getId()); break;
            case R.id.btnPanel_si_innova : responder(linearLayout.getId()); break;
            default: break;
        }
    }

    private void responderExcepcionPreguntaActualizada(int id_button) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PreguntaInnova pi = new PreguntaInnova();
        pi.setId_pregunta_inn(preguntaInnova.getId_pregunta_inn());
        pi.setId_pregunta_servidor_inn(preguntaInnova.getId_pregunta_servidor_inn());
        pi.setNum_pregunta_inn(preguntaInnova.getNum_pregunta_inn());
        pi.setPregunta_inn(preguntaInnova.getPregunta_inn());
        pi.setValor_respuesta(valor_respuesta);
        pi.setValor_ace(devolverResultado(id_button));
        if (preguntaInnova.getId_pregunta_inn() < (long)PreguntaInnova.getUltimoIdPorPreguntaRespondida() - 1) { pi.setRespondido_inn(true); } else { pi.setRespondido_inn(false); }
        pi.setId_boton_inn(id_button);
        realm.copyToRealmOrUpdate(pi);
        realm.commitTransaction();
        realm.close();

        Log.d(TAG, pi.toString());
        Log.d(TAG, "hecho_i");

        startActivity(new Intent(getBaseContext(), PrincipalActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

    private void responder(int id_button) {
        if (excepcion_pregunta_actualizada) {
            responderExcepcionPreguntaActualizada(id_button);
        } else {
            Intent intent = new Intent(this, CuestionarioInnovaActivity.class);
            int siguiente_pregunta = id_num_pregunta_innova + 1;
            int siguiente_posicion = texto_posicion + 1;
            intent.putExtra(Constantes.KEY_NUM_ORDEN_INNOVA, siguiente_pregunta);
            intent.putExtra(Constantes.KEY_ID_INNOVA, id_innova);
            intent.putExtra(Constantes.KEY_POSICION_PREGUNTA_INNOVA, siguiente_posicion);
            if (cantidad_maxima < siguiente_posicion) {
                intent.putExtra(Constantes.KEY_ACTIVAR_PREFERENCIA_INNOVA,true);
            }
            Preferencia.iniciarPreferenciaInnova((int) id_innova, siguiente_pregunta, siguiente_posicion, this);
            Log.d(TAG, "responder/id_innova_" + String.valueOf(id_innova));
            Log.d(TAG, "responder/id_num_pregunta_innova_" + String.valueOf(id_num_pregunta_innova));
            Realm realm1 = Realm.getDefaultInstance();
            realm1.beginTransaction();
            PreguntaInnova pi = new PreguntaInnova();
            pi.setId_pregunta_inn(preguntaInnova.getId_pregunta_inn());
            pi.setId_pregunta_servidor_inn(preguntaInnova.getId_pregunta_servidor_inn());
            pi.setNum_pregunta_inn(preguntaInnova.getNum_pregunta_inn());
            pi.setPregunta_inn(preguntaInnova.getPregunta_inn());
            pi.setValor_respuesta(valor_respuesta);
            pi.setValor_ace(devolverResultado(id_button));
            pi.setRespondido_inn(true);
            pi.setId_boton_inn(id_button);
            realm1.copyToRealmOrUpdate(pi);
            realm1.commitTransaction();
            realm1.close();
            //cantidad_maxima = la cantidad de preguntas que tiene un tópico (ejm 8 preguntas)
            if (cantidad_maxima < siguiente_posicion) {
                Log.d(TAG,"CANTIDAD-MAXIMA_MENOR_A_SIGUIENTE-POSICION_"+String.valueOf(cantidad_maxima+"_"+siguiente_posicion));
                Realm realm2 = Realm.getDefaultInstance();
                realm2.beginTransaction();
                Innova innova = new Innova();
                innova.setId_inn(id_innova);
                innova.setIcono_inn(ruta_imagen);
                innova.setCategoria_inn(pregunta_innova);
                innova.setEstado_inn(false);
                innova.setEstado_orden_inn(true);
                innova.setId_periodo_inn(id_periodo);
                realm2.copyToRealmOrUpdate(innova);
                realm2.commitTransaction();
                realm2.close();
                Log.d(TAG,"innova_" + innova.toString());
                startMyIntent(this, intent, this);
                Innova.activarSiguentePregunta(id_innova);
                Preferencia.iniciarPreferenciaInnova((int)id_innova + 1, 0, 1, this);
                if (cantidadLista == (int) id_innova) {
                    Preferencia.iniciarPreferenciaInnova(-1, -1, 1, this);
                    Preferencia.activarDiagnosticoInnova(this);
                    Log.d(TAG,"CANTIDAD-LISTA_IGUAL_A_ID-INNOVA_"+String.valueOf(cantidadLista+"_"+id_innova));
                    Intent intentFinal = new Intent(this, DiagnosticoActivity.class);
                    intentFinal.putExtra(Constantes.KEY_MANDAR_ID_AREA, 2);
                    startActivity(intentFinal);
                    finish();
                }
            } else {
                startMyIntent(this, intent, this);
            }
        }
    }

    private String devolverResultado(int id_boton) {
        String resultado = "Empty";
        if (id_boton == R.id.btnPanel_noaplica_innova) {
            resultado = "NO APLICA";
        } else if(id_boton == R.id.btnPanel_no_innova) {
            resultado = "NO";
        } else if (id_boton == R.id.btnPanel_proceso_innova) {
            resultado = "EN PROCESO";
        } else if (id_boton == R.id.btnPanel_si_innova) {
            resultado = "SI";
        }
        Log.d(TAG, "devolverResultado/resultado_"+resultado);
        Log.d(TAG, "devolverResultado/respuesta" + String.valueOf(id_boton));
        return resultado;
    }

    private void iniciarLayout(){
        setToolbarSon(toolbar, this, icono_toolbar);
        usarGlide(this, R.drawable.fondo_principal_final, fondo);
        usarGlide(this, R.drawable.tarjeta_estandar_vacio, tarjeta_vacia);
    }

    @OnClick({R.id.btnRetrocederInnova, R.id.btnAvanzarInnova})
    public void capturarRespuesta(ImageView view) {
        int posicion = 0;
        switch (view.getId()) {
            case R.id.btnRetrocederInnova: posicion = -1; break;
            case R.id.btnAvanzarInnova:    posicion = +1; break;
            default: posicion = 0; break;
        }
        Log.d(TAG, "posicion_"+String.valueOf(posicion));
        Intent intent = new Intent(this, CuestionarioInnovaActivity.class);
        int siguiente_pregunta = id_num_pregunta_innova + posicion;
        int siguiente_posicion = texto_posicion + posicion;
        intent.putExtra(Constantes.KEY_NUM_ORDEN_INNOVA, siguiente_pregunta);
        intent.putExtra(Constantes.KEY_ID_INNOVA, id_innova);
        intent.putExtra(Constantes.KEY_POSICION_PREGUNTA_INNOVA, siguiente_posicion);
        startMyIntent(this, intent, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){ onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }
}
