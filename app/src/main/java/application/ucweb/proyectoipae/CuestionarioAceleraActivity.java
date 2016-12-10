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
import application.ucweb.proyectoipae.model.Acelera;
import application.ucweb.proyectoipae.model.PreguntaAcelera;
import application.ucweb.proyectoipae.util.Constantes;
import application.ucweb.proyectoipae.util.Preferencia;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class CuestionarioAceleraActivity extends BaseActivity {
    @BindView(R.id.btnPanel_no_acelera) LinearLayout btnNO;
    @BindView(R.id.btnPanel_si_acelera) LinearLayout btnSI;
    @BindView(R.id.btnPanel_proceso_acelera) LinearLayout btnPROCESO;
    @BindView(R.id.btnAvanzarAcelera) ImageView btnAvanzar;
    @BindView(R.id.btnRetrocederAcelera) ImageView btnRetroceder;
    @BindView(R.id.iv_fondo_aca) ImageView fondo;
    @BindView(R.id.toolbar_principal) Toolbar toolbar;
    @BindView(R.id.ivCuestionario_acelera_imagen) ImageView aceleraImagen;
    @BindView(R.id.iv_panel_circular_acelera) ImageView panel_circular;
    @BindView(R.id.iv_tarjeta_acelera_vacio)ImageView tarjeta_vacia;
    @BindView(R.id.ivToolbar_Icono) ImageView icono_toolbar;
    @BindView(R.id.aceleraCategoria) TextView aceleraCategoria;
    @BindView(R.id.aceleraPregunta) TextView aceleraPregunta;
    @BindView(R.id.tv_cantidad_preguntas_acelera) TextView numero_pregunta_total;
    @BindView(R.id.tv_cantidad_actual_acelera) TextView numero_pregunta_actual;
    @BindView(R.id.tvDe_acelera) TextView tvDe_acelera;
    @BindDrawable(R.drawable.boton_derecha_acelera) Drawable drwDerecha;
    @BindDrawable(R.drawable.boton_izquierda_acelera) Drawable drwIzquierda;
    @BindDrawable(R.drawable.boton_derecha_opaco) Drawable drwOpacoDerecha;
    @BindDrawable(R.drawable.boton_izquierda_opaco) Drawable drwOpacoIzquierda;
    private static final String TAG = CuestionarioAceleraActivity.class.getSimpleName();
    private Realm realm;
    private int cantidadLista = 0;
    private Acelera acelera = null;
    private PreguntaAcelera preguntaAcelera = null;
    private long id_acelera;
    private long id_pregunta_acelera = 0;
    private int id_num_pregunta_acelera = 0;
    private int texto_posicion = 0;
    private int cantidad_maxima = 0;
    private int id_periodo = 0;
    private String ruta_imagen = "";
    private String pregunta_acelera = "";
    private int[] arrayInt ;
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

        //ID DE ENTIDAD ACELERA
        id_acelera = getIntent().getLongExtra(Constantes.KEY_ID_ACELERA, -1);
        Log.d(TAG, "id_acelera " + String.valueOf(id_acelera));

        //ID DE ENTIDAD PREGUNTA_ACELERA
        id_num_pregunta_acelera = getIntent().getIntExtra(Constantes.KEY_NUM_ORDEN_ACELERA, 0);
        Log.d(TAG, "id_num_pregunta_acelera " + String.valueOf(id_num_pregunta_acelera));

        //GUARDA EL NÚMERO DE RESPUESTA EN DONDE SE ENCUENTRA EL USUARIO
        texto_posicion = getIntent().getIntExtra(Constantes.KEY_POSICION_PREGUNTA_ACELERA, 1);
        Log.d(TAG, "texto_posicion" + String.valueOf(texto_posicion));

        //SI ES RECIBIDA EL EXTRA ACTIVAR_PREFERENCIA SE ACTIVA LA PREFERENCIA PARA SER USADA
        activar_preferencia = getIntent().getBooleanExtra(Constantes.KEY_ACTIVAR_PREFERENCIA_ACELERA, false);
        Log.d(TAG,"activar_preferencia_" + String.valueOf(activar_preferencia));

        //SI EXISTE ACTUALIZACIÓN DE UNA PREGUNTA
        excepcion_pregunta_actualizada = getIntent().getBooleanExtra(Constantes.KEY_UNICA_PREGUNTA_ACELERA, false);
        Log.d(TAG, "excepcion_pregunta_actualizada_a" + String.valueOf(excepcion_pregunta_actualizada));

        //VARIABLE INT_ARRAY PARA GUARDAR COMO PREFERENCIA, DONDE SE ENCUENTRA EL USUARIO;
        // SI ES QUE SUS DATOS SON VÁLIDOS COMO MUESTRA EL IF, SERÁN CAMBIADOS HASTA DONDE MANDE LA PREFERENCIA
        // REQUERIMIENTO EXTRA: LA PREFERENCIA ES CAPTURADA DESDE LA ACTIVIDAD ACELERA_ACTIVITY
        if (activar_preferencia) {
            arrayInt = Preferencia.traerIdPreferenciaAcelera(this);
            Log.d(TAG, "arrayLocal_" + Arrays.toString(arrayInt));
            if (arrayInt[0] != -1 && arrayInt[1] != -1 && arrayInt[2] > -1) {
                id_acelera = (int) arrayInt[0];
                id_num_pregunta_acelera = arrayInt[1];
                texto_posicion = arrayInt[2];
            }
        }
        //SI EXISTE UNA ACTUALIZACIÓN DE UNA SOLA PREGUNTA
        if (excepcion_pregunta_actualizada) {
            id_acelera = getIntent().getLongExtra(Constantes.KEY_UNICA_ID_FORANEA_ACELERA, -1);
            id_num_pregunta_acelera = getIntent().getIntExtra(Constantes.KEY_UNICA_ID_ACELERA, -1);
            texto_posicion = -1;
            Log.d(TAG, "idtraigo*_" + String.valueOf(id_acelera));
            Log.d(TAG, "foraneatraido*_" + String.valueOf(id_num_pregunta_acelera));
            Log.d(TAG, "posiciontraido*_" + String.valueOf(texto_posicion));
        }
        //GUARDAMOS EL TAMAÑO DE TODOS LOS TÓPICOS DE ACELERA (ORGANIZACIÓN, COMERCIAL Y MARKETING, OPTIMIZACIÓN, ETC..),
        // EN ESTE CASO 3; Y CADA ENTIDAD ACELERA TIENE SUS PROPIAS PREGUNTAS
        cantidadLista = Acelera.getSize();

        //INICIAMOS LA ENTIDAD ACELERA
        realm = Realm.getDefaultInstance();
        acelera = realm.where(Acelera.class).equalTo(Acelera.A_ID, id_acelera).findFirst();
        ruta_imagen = acelera.getIcono_ace();
        pregunta_acelera = acelera.getCategoria_ace();
        id_periodo = acelera.getId_periodo_ace();

        //INICIAMOS LA ENTIDAD PREGUNTA_ACELERA, CON TODAS LAS PREGUNTAS DE ACELERA
        RealmResults<PreguntaAcelera> lista = realm.where(PreguntaAcelera.class)
                .equalTo(PreguntaAcelera.PA_NUM_PREGUNTA, (int)id_acelera)
                .findAll();

        //RECOGEMOS LA CANTIDAD DE LAS PREGUNTAS PARA MOSTRAR (Ejm: 1 DE 10)
        if (!excepcion_pregunta_actualizada) {
            cantidad_maxima = lista.size();
            //EN ESTA PARTE MOSTRAMOS LA PREGUNTA_ACELERA
            preguntaAcelera = lista.get(id_num_pregunta_acelera); //RECIBE EL PRIMER PUNTERO
        } else {
            preguntaAcelera = realm.where(PreguntaAcelera.class).equalTo(PreguntaAcelera.PA_ID_SERVIDOR, id_num_pregunta_acelera).findFirst();
        }

        //OBTENEMOS EL ÚLTIMO ID GUARDADO, DESDE EL ADAPTADOR
        if (getIntent().hasExtra(Constantes.KEY_POSICION_ACELERA)) {
            int id_acelera = getIntent().getIntExtra(Constantes.KEY_POSICION_ACELERA, -1);
            Log.d(TAG,"id_acelera" + String.valueOf(id_acelera));
            preguntaAcelera = realm.where(PreguntaAcelera.class).equalTo(PreguntaAcelera.PA_ID, id_acelera).findFirst();
        }

        id_pregunta_acelera = preguntaAcelera.getId_pregunta_ace();
        Log.d(TAG, "id_pregunta_acelera "+ String.valueOf(id_pregunta_acelera));
        valor_respuesta = preguntaAcelera.isValor_respuesta();


        //TODO: ACELERA settear setContentView(R.layout.layout); 1 o 2, continuar después para tener el control del layout o views
        excepcionBotton(valor_respuesta); //<--
        iniciarLayout();

        usarGlide(this, acelera.getIcono_ace(), aceleraImagen);
        aceleraImagen.setColorFilter(Color.WHITE);
        aceleraCategoria.setText(acelera.getCategoria_ace());
        numero_pregunta_total.setText(String.valueOf(cantidad_maxima));
        numero_pregunta_actual.setText(String.valueOf(texto_posicion));
        aceleraPregunta.setText(preguntaAcelera.getPregunta_ace());

        pintarBotonPresionado(preguntaAcelera.getId_boton_ace());
        Log.d(TAG, "acelera_"+acelera.toString());
        Log.d(TAG, "preguntaAcelera"+preguntaAcelera.toString());
        Log.d(TAG, "listaAcelera"+lista.toString());
        btnAvanzar.setBackground(drwDerecha);
        btnRetroceder.setBackground(drwIzquierda);
        validarControl(texto_posicion, 1, cantidad_maxima, btnRetroceder, btnAvanzar);

        if (excepcion_pregunta_actualizada) {
            btnAvanzar.setBackground(drwOpacoDerecha);
            btnRetroceder.setBackground(drwOpacoIzquierda);
            btnAvanzar.setClickable(false);
            btnRetroceder.setClickable(false);

            numero_pregunta_total.setTextColor(Color.TRANSPARENT);
            tvDe_acelera.setTextColor(Color.TRANSPARENT);
            numero_pregunta_actual.setTextColor(Color.TRANSPARENT);
        }
    }

    private void pintarBotonPresionado(int respuesta) {
        if (respuesta != -1) {
            LinearLayout miLinear = (LinearLayout) findViewById(respuesta);
            LinearLayout btnNOAPLICA = (LinearLayout)findViewById(R.id.btnPanel_noaplica_acelera);
            if (respuesta == R.id.btnPanel_noaplica_acelera) {
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
        if (preguntaAcelera.getId_pregunta_ace() == PreguntaAcelera.getUltimoIdPorPreguntaRespondida()) {
            btnAvanzar.setClickable(false);
            btnAvanzar.setBackground(drwOpacoDerecha);
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

    private void excepcionBotton(boolean numero_respuestas) {
        if (numero_respuestas) {
            setContentView(R.layout.activity_cuestionario_acelera);
            usarGlide(this, R.drawable.panel_circular_acelera, panel_circular);
        } else {
            setContentView(R.layout.activity_cuestionario_acelera2);
            usarGlide(this, R.drawable.panel_circular_acelera2, panel_circular);
            final LinearLayout button = (LinearLayout) findViewById(R.id.btnPanel_noaplica_acelera);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "button_/texto_posicion" + String.valueOf(texto_posicion));
                    responder(button.getId());
                }
            });
        }
    }

    @OnClick({R.id.btnPanel_no_acelera, R.id.btnPanel_proceso_acelera, R.id.btnPanel_si_acelera})
    public void presionarOpcionPanelAcelera(LinearLayout layout) {
        switch (layout.getId()) {
            case R.id.btnPanel_no_acelera :         responder(layout.getId()); break;
            case R.id.btnPanel_proceso_acelera :    responder(layout.getId()); break;
            case R.id.btnPanel_si_acelera :         responder(layout.getId()); break;
            default: break;
        }
    }

    private void respoderExcepcionPreguntaActualizada(int id_button) {
        Realm realm1 = Realm.getDefaultInstance();
        realm1.beginTransaction();
        PreguntaAcelera pa = new PreguntaAcelera();
        pa.setId_pregunta_ace(preguntaAcelera.getId_pregunta_ace());
        pa.setId_pregunta_servidor_ace(preguntaAcelera.getId_pregunta_servidor_ace());
        pa.setNum_pregunta_ace(preguntaAcelera.getNum_pregunta_ace());
        pa.setPregunta_ace(preguntaAcelera.getPregunta_ace());
        pa.setValor_respuesta(valor_respuesta);
        pa.setValor_ace(devolverResultado(id_button));
        if (preguntaAcelera.getId_pregunta_ace() < (long) PreguntaAcelera.getUltimoIdPorPreguntaRespondida() - 1){ pa.setRespondido_ace(true); } else { pa.setRespondido_ace(false); }
        pa.setId_boton_ace(id_button);
        realm1.copyToRealmOrUpdate(pa);
        realm1.commitTransaction();
        realm1.close();

        Log.d(TAG, pa.toString());
        Log.d(TAG, "hecho");

        startActivity(new Intent(getBaseContext(), PrincipalActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

    private void responder( int id_button) {
        if (excepcion_pregunta_actualizada) {
            respoderExcepcionPreguntaActualizada( id_button);
        } else {
            Intent intent = new Intent(this, CuestionarioAceleraActivity.class);
            int siguiente_pregunta = id_num_pregunta_acelera + 1;
            int siguiente_posicion = texto_posicion + 1;
            intent.putExtra(Constantes.KEY_NUM_ORDEN_ACELERA, siguiente_pregunta);
            intent.putExtra(Constantes.KEY_ID_ACELERA, id_acelera);
            intent.putExtra(Constantes.KEY_POSICION_PREGUNTA_ACELERA, siguiente_posicion);
            if (cantidad_maxima < siguiente_posicion) {
                intent.putExtra(Constantes.KEY_ACTIVAR_PREFERENCIA_ACELERA, true);
            }
            Preferencia.iniciarPreferenciaAcelera((int) id_acelera, siguiente_pregunta, siguiente_posicion, this);
            Log.d(TAG, "extraLong " + String.valueOf(id_acelera));
            Log.d(TAG, "idPreguntaAcelera " + String.valueOf(id_num_pregunta_acelera));

            Realm realm1 = Realm.getDefaultInstance();
            realm1.beginTransaction();
            PreguntaAcelera pa = new PreguntaAcelera();
            pa.setId_pregunta_ace(preguntaAcelera.getId_pregunta_ace());
            pa.setId_pregunta_servidor_ace(preguntaAcelera.getId_pregunta_servidor_ace());
            pa.setNum_pregunta_ace(preguntaAcelera.getNum_pregunta_ace());
            pa.setPregunta_ace(preguntaAcelera.getPregunta_ace());
            pa.setValor_respuesta(valor_respuesta);
            pa.setValor_ace(devolverResultado(id_button));
            pa.setRespondido_ace(true);
            pa.setId_boton_ace(id_button);
            realm1.copyToRealmOrUpdate(pa);
            realm1.commitTransaction();
            realm1.close();

            if (cantidad_maxima < siguiente_posicion) {
                Realm realm2 = Realm.getDefaultInstance();
                realm2.beginTransaction();
                Acelera acelera = new Acelera();
                acelera.setId_ace(id_acelera);
                acelera.setIcono_ace(ruta_imagen);
                acelera.setCategoria_ace(pregunta_acelera);
                acelera.setEstado_ace(false);
                acelera.setEstado_orden_ace(true);
                acelera.setId_periodo_ace(id_periodo);
                realm2.copyToRealmOrUpdate(acelera);
                realm2.commitTransaction();
                realm2.close();
                Log.d(TAG, "acelera" + acelera.toString());
                startMyIntent(this, intent, this);
                Acelera.activarSiguentePregunta(id_acelera);
                Preferencia.iniciarPreferenciaAcelera((int)id_acelera + 1, 0 ,1, this);
                if (cantidadLista == (int) id_acelera) {
                    Preferencia.iniciarPreferenciaAcelera(-1, -1 ,1, this);
                    Preferencia.activarDiagnosticoAcelera(this);
                    Log.d(TAG,"CANTIDAD-LISTA_IGUAL_A_ID-ACELERA_"+String.valueOf(cantidadLista+"_"+id_acelera));
                    Intent intentFinal = new Intent(this, DiagnosticoActivity.class);
                    intentFinal.putExtra(Constantes.KEY_MANDAR_ID_AREA, 1);
                    startActivity(intentFinal);
                    finish();
                }

            } else {
                startMyIntent(this, intent, this);
            }
        }
    }

    private String devolverResultado(int id_boton) {
        String resultado = "empty";
        if (id_boton == R.id.btnPanel_noaplica_acelera) {
            resultado = "NO APLICA";
        } else if (id_boton == R.id.btnPanel_no_acelera) {
            resultado = "NO";
        } else if (id_boton == R.id.btnPanel_proceso_acelera) {
            resultado = "EN PROCESO";
        } else if (id_boton == R.id.btnPanel_si_acelera) {
            resultado = "SI";
        }
        Log.d(TAG, "devolverResultado/respuesta_" + String.valueOf(id_boton));
        Log.d(TAG, "devolverResultado/resultado_" + resultado);
        return resultado;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){ onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        usarGlide(this, R.drawable.fondo_principal_final, fondo);
        usarGlide(this, R.drawable.tarjeta_estandar_vacio, tarjeta_vacia);
    }

    @OnClick({R.id.btnRetrocederAcelera, R.id.btnAvanzarAcelera})
    public void capturarRespuesta(ImageView view) {
        int posicion = 0;
        switch (view.getId()) {
            case R.id.btnRetrocederAcelera: posicion = -1; break;
            case R.id.btnAvanzarAcelera:    posicion = +1; break;
            default: posicion = 0; break;
        }
        Log.d(TAG, "posicion_"+String.valueOf(posicion));
        Intent intent = new Intent(CuestionarioAceleraActivity.this, CuestionarioAceleraActivity.class);
        int siguiente_pregunta = id_num_pregunta_acelera + posicion;
        int siguiente_posicion = texto_posicion + posicion;
        intent.putExtra(Constantes.KEY_NUM_ORDEN_ACELERA, siguiente_pregunta);
        intent.putExtra(Constantes.KEY_ID_ACELERA, id_acelera);
        intent.putExtra(Constantes.KEY_POSICION_PREGUNTA_ACELERA, siguiente_posicion);
        startMyIntent(this, intent, this);
    }

}
