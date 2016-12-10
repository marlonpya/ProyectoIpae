package application.ucweb.proyectoipae;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.aplicacion.Configuracion;
import application.ucweb.proyectoipae.model.Area;
import application.ucweb.proyectoipae.model.PreguntaAcelera;
import application.ucweb.proyectoipae.model.PreguntaInnova;
import application.ucweb.proyectoipae.model.Test;
import application.ucweb.proyectoipae.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoipae.util.Constantes;
import application.ucweb.proyectoipae.util.Preferencia;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class DiagnosticoActivity extends BaseActivity {
    @BindView(R.id.layout_a_diagnostico) RelativeLayout layout;
    @BindView(R.id.toolbar_principal) Toolbar toolbar;
    @BindView(R.id.ivToolbar_Icono) ImageView icono_toolbar;
    @BindView(R.id.iv_fondo_ad) ImageView fondo;
    @BindView(R.id.radioTerminos) RadioButton radioTerminos;
    @BindView(R.id.tvNombres) EditText tvNombres;
    @BindView(R.id.tvCorreo_diagnostico) EditText tvCorreo_diagnostico;
    @BindView(R.id.tvTelefono_contacto) EditText tvTelefono_contacto;
    @BindView(R.id.tvCargo_empresa) EditText tvCargo_empresa;
    @BindView(R.id.tvRazon_social) EditText tvRazon_social;
    @BindView(R.id.tvArea) TextView tvArea;
    @BindString(R.string.texto_area) String textoArea;
    @BindString(R.string.mensaje_validar_terminos) String validarTerminos;
    @BindString(R.string.mensaje_validar_combo) String validarCombos;
    public static final String TAG = DiagnosticoActivity.class.getSimpleName();
    private String[] arrayOpcionFactura;
    private String textoCombo;
    private RealmResults<Area> listaArea;
    private String[] arrayAreas;
    private int extraOpcionArea; //<-- RECIBE EL INDICADOR PARA ENVIAR PREGUNTAS(EXTRA ACELERA 1 - INNOVA 2)
    private ProgressDialog pDialog;
    private int id_area = 0;
    private int id_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostico);
        iniciarLayout();

        extraOpcionArea = getIntent().getIntExtra(Constantes.KEY_MANDAR_ID_AREA, -1);

        // extraOpcionArea -> EXTRA ACELERA 1 - INNOVA 2
        Log.d(TAG, "extraOpcionArea_"+String.valueOf(extraOpcionArea));

        Realm realm = Realm.getDefaultInstance();
        listaArea = realm.where(Area.class).equalTo(Area.A_CAT, extraOpcionArea).findAll();

        arrayAreas = new String[listaArea.size()];
        for (int i = 0; i < listaArea.size(); i++) {
            arrayAreas[i] = listaArea.get(i).getDescripcion_area();
        }
        Log.d(TAG, listaArea.toString());
        Log.d(TAG, Arrays.toString(arrayAreas));
        id_test = Test.getTest(extraOpcionArea).getId_test_servidor();
        Log.d(TAG, "id_test"+String.valueOf(id_test));
    }

    /**
     * RECOGE LOS DATOS DEL FORMULARIO DIAGNOSTICO, LOS DATOS INGRESADOS POR EL USUARIO
     * SI DEVUELVE UN STATUS TRUE PROSIGUE AL METODO enviarServicioTodasRespuestas(extra);
     * @param extra ID DE LA VARIABLE extraOpcionArea
     * @param idTest ID DEL TEST PROPORCIONADO POR EL SERVIDOR
     * @param txtNombres
     * @param txtCorreo
     * @param txtCargo
     * @param txtTelefono
     * @param txtRazonSocial
     * @param cbAreas AREAS DEL COMBO MOSTRADO EL DIÁLOGO
     * @param respuesta RESPUESTA DEL DIÁLOGO (SI O NO)
     */
    private void enviarServicioFormulario(final int extra,
                                          final int idTest,
                                          final String txtNombres,
                                          final String txtCorreo,
                                          final String txtCargo,
                                          final String txtTelefono,
                                          final String txtRazonSocial,
                                          final int cbAreas,
                                          final String respuesta) {
        if (ConexionBroadcastReceiver.isConnected()) {
        //showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.URL_GUARDAR_FORMULARIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject object = new JSONObject(response);
                            boolean status = object.getBoolean("status");
                            if (status) {
                                String message = object.getString("message");
                                eliminarHistorial(extra);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getBaseContext(), PrincipalActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                                finish();
                            } else { showDialog(pDialog); }
                            Log.d(TAG, object.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG,e.getMessage());
                        }
                        hidepDialog(pDialog);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog(pDialog);
                        if (error!=null) { VolleyLog.d(error.toString()); }
                        new AlertDialog.Builder(DiagnosticoActivity.this)
                                .setTitle("Error de Conexión")
                                .setMessage("Hubo un error y no se pudo procesar la solicitud.Por favor, inténtalo de nuevo.")
                                .setPositiveButton("OK", null)
                                .create().show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idTest", String.valueOf(idTest));
                params.put("txtNombres", txtNombres);
                params.put("txtCorreo", txtCorreo);
                params.put("txtCargo", txtCargo);
                params.put("txtTelefono", txtTelefono);
                params.put("txtRazonSocial", txtRazonSocial);
                params.put("cbAreas", String.valueOf(cbAreas));
                params.put("pregunta", respuesta);
                Log.d(TAG, params.toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { showSnack(layout); }
    }

    /**
     * DESACTIVA LA PREFERENCIA QUE HACE QUE AL ENTRAR A LA LISTA ENTRE A DIAGNÓSTICO
     * DESACTIVA EL TEST GENERADO PARA SER REUTILIZADO
     * @param id_area RECIBE EL ID DE AREA (ACELERA O INNOVA)
     */
    private void eliminarHistorial(int id_area) {
        switch (id_area) {
            case 1: Preferencia.desactivarDiagnosticoAcelera(this);
                    //Preferencia.activarFin(this, 1, true);
                    Test.desactivarTest(1);
                    //Preferencia.controlPreguntasFin(this, 1, true);
                    break;
            case 2: Preferencia.desactivarDiagnosticoInnova(this);
                    //Preferencia.activarFin(this, 2, true);
                    Test.desactivarTest(2);
                    //Preferencia.controlPreguntasFin(this, 2, true);
                    break;
        }
    }

    /**
     * GENERA UN DIALOGO CON UNA PREGUNTA DE SI O NO, LUEGO PROCESO EL METODO enviarServicioFormulario(Params...);
     */
    @OnClick(R.id.btnEnviarDiagnostico)
    public void enviarDiagnostico() {
        if (validarEnviarDiagnostico()) {
            arrayOpcionFactura = new String[]{"Si", "No"};
            new AlertDialog.Builder(DiagnosticoActivity.this)
                    .setCancelable(true)
                    .setTitle("¿ Su empresa factura entre 10 Millones a más anuales ?")
                    .setSingleChoiceItems(arrayOpcionFactura, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String respuesta = arrayOpcionFactura[which];
                            dialog.dismiss();
                            try {
                                enviarServicioTodasRespuestas(respuesta);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /*enviarServicioFormulario(
                                    extraOpcionArea,
                                    Test.getTest(extraOpcionArea).getId_test_servidor(),
                                    tvNombres.getText().toString().trim(),
                                    tvCorreo_diagnostico.getText().toString().trim(),
                                    tvCargo_empresa.getText().toString().trim(),
                                    tvTelefono_contacto.getText().toString().trim(),
                                    tvRazon_social.getText().toString().trim(),
                                    listaArea.get(id_area).getId_area_servidor(),
                                    respuesta
                            );*/
                        }
                    })
                    .create()
                    .show();
        }
            /*try {
                enviarServicioTodasRespuestas(extraOpcionArea);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
    }

    /**
     * METODO PARA VALIDAR LA CONEXION A INTERNET; CAMPOS VACIOS Y ACEPTAR, TERMINOS Y CONDICIONES.
     * @return RETORNA TRUE, SI PASA LAS VALIDACIONES.
     */
    private boolean validarEnviarDiagnostico() {
        boolean valor = false;
        textoCombo = tvArea.getText().toString();
        if (!ConexionBroadcastReceiver.isConnected()) {
            showSnack(layout);
        } else if(tvCargo_empresa.getText().toString().isEmpty() ||
                tvCorreo_diagnostico.getText().toString().isEmpty() ||
                tvNombres.getText().toString().isEmpty() ||
                tvRazon_social.getText().toString().isEmpty() ||
                tvCorreo_diagnostico.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
        } else if (textoCombo.equals(textoArea)) {
            Toast.makeText(getApplicationContext(), validarCombos, Toast.LENGTH_SHORT).show();
        } else if (!radioTerminos.isChecked()) {
            Toast.makeText(getApplicationContext(), validarTerminos, Toast.LENGTH_SHORT).show();
        } else {
            valor = true;
        }
        return valor;
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        usarGlide(this, R.drawable.fondo_principal_final, fondo);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Enviando..");
    }

    /**
     * SELECCIONA UNA AREA DE LA BASE DE DATOS REALM, IMPORTANTE: SETEA EL id_area
     * PARA ENCONTRAR EL ID DE LA AREA DE LA LISTA listaArea
     */
    @OnClick(R.id.cboArea)
    public void mostrarAreas(){
        new AlertDialog.Builder(DiagnosticoActivity.this)
                .setTitle("Seleccione una área:")
                .setSingleChoiceItems(arrayAreas, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvArea.setText(arrayAreas[which]);
                        id_area = which;
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    /**
     * CREA UN MAP QUE RECOGE LA TODAS LAS PREGUNTAS PARA SER ENVIADO
     * @param id DEL LAS PREGUNTAS
     * @return TODAS LAS PREGUNTAS
     * @throws JSONException
     */
    private Map<String, String> crearMapTodasRespuestas(int id) throws JSONException {
        Realm realm = Realm.getDefaultInstance();
        final String dataRespuestas = "dataRespuestas";
        final String rp = "rp";
        final String idPregunta = "idPregunta";
        final String subCategoria = "subCategoria";
        final String idTest = "idTest";
        Map<String, String> params = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        if (id == 1) {
            RealmResults<PreguntaAcelera> preguntas = realm.where(PreguntaAcelera.class).findAll();
            for (int i = 0; i < preguntas.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(rp, preguntas.get(i).getValor_ace());
                jsonObject.put(idPregunta, String.valueOf(preguntas.get(i).getId_pregunta_servidor_ace()));
                jsonObject.put(subCategoria, String.valueOf(preguntas.get(i).getNum_pregunta_ace()));
                jsonObject.put(idTest, String.valueOf(id_test));
                jsonArray.put(jsonObject);
            }
                params.put(dataRespuestas, jsonArray.toString());
            Log.d(TAG, "size/a_" + String.valueOf(preguntas.size()));
            Log.d(TAG, "params_" + params.toString());
        } else if (id == 2) {
            RealmResults<PreguntaInnova> preguntas = realm.where(PreguntaInnova.class).findAll();
            for (int i = 0; i < preguntas.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(rp, preguntas.get(i).getValor_ace());
                jsonObject.put(idPregunta, String.valueOf(preguntas.get(i).getId_pregunta_servidor_inn()));
                jsonObject.put(subCategoria, String.valueOf(preguntas.get(i).getNum_pregunta_inn()));
                jsonObject.put(idTest, String.valueOf(id_test));
                jsonArray.put(jsonObject);
            }
            params.put(dataRespuestas, jsonArray.toString());
            Log.d(TAG, "size/i_" + String.valueOf(preguntas.size()));
            Log.d(TAG, "params_" + params.toString());
        }
        realm.close();
        return params;
    }

    /**
     * ENVIA TODAS LAS PREGUNTAS DE REALM
     * PARA SABER QUE PREGUNTAS TOMAR (ACELERA O INNOVA)
     * @param respuesta RESPUESTA SI O NO
     * @throws JSONException
     */
    private void enviarServicioTodasRespuestas(final String respuesta) throws JSONException {
        if (ConexionBroadcastReceiver.isConnected()) {
            showDialog(pDialog);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Constantes.URL_ENVIAR_TEST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.getBoolean("status")) {
                                    enviarServicioFormulario(
                                            extraOpcionArea,
                                            id_test,
                                            tvNombres.getText().toString().trim(),
                                            tvCorreo_diagnostico.getText().toString().trim(),
                                            tvCargo_empresa.getText().toString().trim(),
                                            tvTelefono_contacto.getText().toString().trim(),
                                            tvRazon_social.getText().toString().trim(),
                                            listaArea.get(id_area).getId_area_servidor(),
                                            respuesta
                                    );
                                } else{ hidepDialog(pDialog); }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "json_"+response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidepDialog(pDialog);
                            if (error != null) { VolleyLog.d(error.toString()); }
                            new AlertDialog.Builder(DiagnosticoActivity.this)
                                    .setTitle("Error de Conexión")
                                    .setMessage("Hubo un error y no se pudo procesar la solicitud.Por favor, inténtelo de nuevo.")
                                    .setPositiveButton("OK", null)
                                    .create().show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    try {
                        params = crearMapTodasRespuestas(extraOpcionArea);
                        Log.d(TAG, "DATA-ENVIADA_" + params.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return params;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { showSnack(layout); }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), PrincipalActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){ onBackPressed(); }
        return true;
    }
}
