package application.ucweb.proyectoipae;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.aplicacion.Configuracion;
import application.ucweb.proyectoipae.model.Acelera;
import application.ucweb.proyectoipae.model.PreguntaAcelera;
import application.ucweb.proyectoipae.model.Test;
import application.ucweb.proyectoipae.realm.AceleraRealmAdapter;
import application.ucweb.proyectoipae.realm.MetodoServices;
import application.ucweb.proyectoipae.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoipae.util.Constantes;
import application.ucweb.proyectoipae.util.Preferencia;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class AceleraActivity extends BaseActivity {
    private static final String TAG = AceleraActivity.class.getSimpleName();
    @BindView(R.id.toolbar_principal) Toolbar toolbar;
    @BindView(R.id.listaRecyclerAcelera) RealmRecyclerView listaRecyclerAcelera;
    @BindView(R.id.iv_tarjeta_innova_acelera) ImageView cabecera_acelera;
    @BindView(R.id.ivToolbar_Icono) ImageView icono_toolbar;
    @BindView(R.id.layout_a_acelera) LinearLayout layout;
    private AceleraRealmAdapter realmAdapter;
    private RealmResults<Acelera> listaR;
    private Realm realm;
    private ProgressDialog pDialog;
    private boolean preguntas_por_enviar_a = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hacerTransicion();
        }
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("ipae");

        setContentView(R.layout.activity_acelera);
        setToolbarSon(toolbar, this, icono_toolbar);
        iniciarLayout();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Generando test..");
        pDialog.setCancelable(false);

        //VARIABLE QUE DEFINE SI ES QUE NO HA ENVIADO AUN TODAS LAS PREGUNTAS
        preguntas_por_enviar_a = getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE).getBoolean(Constantes.KEY_ESTADO_DIAGNOSTICO_ACELERA, false);
        if (preguntas_por_enviar_a) { mostrarMensajePreguntasNoEnviadas(this, 1, this); }
        Log.d(TAG, "diagnostico_activado_" + String.valueOf(preguntas_por_enviar_a));

        if (!Test.getTest(1).isEstado_token()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = sdf.format(new Date());
            int id_categoria = Test.getTest(1).getId_categoria();
            String token = Test.getTest(1).getToken_test();
            int idPeriodo = realm.where(Acelera.class).max(Acelera.A_ID_PERIODO).intValue();
            generarToken(fecha, id_categoria, token, idPeriodo);
            Log.d(TAG, "generarToken/fecha_"+fecha);
            Log.d(TAG, "generarToken/idCategoria_"+String.valueOf(id_categoria));
            Log.d(TAG, "generarToken/token_"+token);
            Log.d(TAG, "generarToken/idPeriodo"+ String.valueOf(idPeriodo));
            cargarPreguntasAcelera();
        }
    }

    private void cargarPreguntasAcelera() {
        if (ConexionBroadcastReceiver.isConnected()) {
            showDialog(pDialog);
            JsonObjectRequest request = new JsonObjectRequest(
                    Constantes.URL_GET_PREGUNTAS,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                MetodoServices.eliminarPreguntasAcelera();
                                Realm realm = Realm.getDefaultInstance();
                                JSONObject data = response.getJSONObject("data");
                                JSONArray array = data.getJSONArray("dataSubcategorias");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    int tipo_categoria = object1.getInt("idCategoria");
                                    if (tipo_categoria == 1) {
                                        realm.beginTransaction();
                                        Acelera acelera = realm.createObject(Acelera.class);
                                        acelera.setId_ace((long) object1.getLong("idSubCategoria"));
                                        acelera.setIcono_ace(object1.getString("imagen_subc"));
                                        acelera.setCategoria_ace(object1.getString("nombre_subc"));
                                        acelera.setEstado_ace(false);
                                        acelera.setEstado_orden_ace(false);
                                        acelera.setId_periodo_ace(object1.getInt("idPeriodo"));
                                        realm.copyToRealm(acelera);
                                        realm.commitTransaction();
                                        realm.close();
                                        Log.d(TAG, acelera.toString());
                                    }
                                }

                                Acelera acelera_ = realm.where(Acelera.class).findFirst();
                                realm.beginTransaction();
                                Acelera acelera1 = new Acelera();
                                acelera1.setId_ace(acelera_.getId_ace());
                                acelera1.setIcono_ace(acelera_.getIcono_ace());
                                acelera1.setCategoria_ace(acelera_.getCategoria_ace());
                                acelera1.setEstado_ace(true);
                                acelera1.setEstado_orden_ace(true);
                                acelera1.setId_periodo_ace(acelera_.getId_periodo_ace());
                                realm.copyToRealmOrUpdate(acelera1);
                                realm.commitTransaction();
                                realm.close();

                                Realm realm2 = Realm.getDefaultInstance();
                                JSONArray array2 = data.getJSONArray("dataPreguntas");
                                for (int i = 0; i < array2.length(); i++) {
                                    JSONObject object2 = array2.getJSONObject(i);
                                    int id_pregunta = object2.getInt("idPregunta");
                                    String pregunta = object2.getString("pregunta");
                                    String pregt_pa = object2.getString("rp4");
                                    boolean pregt_pa2 = (pregt_pa == null || pregt_pa.isEmpty() || pregt_pa.equals("null"));
                                    long id_json = Long.parseLong(object2.getString("idSubCategoria"));
                                    Acelera acelera = realm2.where(Acelera.class).equalTo(Acelera.A_ID, id_json).findFirst();
                                    if (acelera != null) {
                                        realm2.beginTransaction();
                                        PreguntaAcelera pa = realm2.createObject(PreguntaAcelera.class);
                                        pa.setId_pregunta_ace(PreguntaAcelera.getUltimoId());
                                        pa.setId_pregunta_servidor_ace(id_pregunta);
                                        pa.setNum_pregunta_ace((int) acelera.getId_ace());
                                        pa.setPregunta_ace(pregunta);
                                        pa.setValor_respuesta(pregt_pa2);
                                        pa.setValor_ace("");
                                        pa.setRespondido_ace(false);
                                        pa.setId_boton_ace(-1);
                                        acelera.getPreguntas().add(pa);
                                        realm2.copyToRealm(pa);
                                        realm2.commitTransaction();
                                        realm2.close();
                                        Log.d(TAG, pa.toString());
                                    }
                                }
                                Preferencia.iniciarPreferenciaAcelera(-1, -1, 1, AceleraActivity.this);
                                startActivity(new Intent(AceleraActivity.this, AceleraActivity.class));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hidepDialog(pDialog);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) { VolleyLog.d(TAG, error.toString()); }
                            hidepDialog(pDialog);
                            if (ConexionBroadcastReceiver.isConnected()) { cargarPreguntasAcelera(); }
                        }
                    }
            );
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { showSnack(layout); }
    }

    private void generarToken(final String fecha, final int id_categoria, final String token, final int idPeriodo) {
        if (ConexionBroadcastReceiver.isConnected()) {
            showDialog(pDialog);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Constantes.URL_INSERT_GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hidepDialog(pDialog);
                            try {
                                JSONObject object = new JSONObject(response);
                                int id_test = object.getInt("id");
                                //TODO: CUANDO RECIBO EL ID DEL TEST, ACTIVO UN REGISTRO(TEST) REALM PARA USARLO TEST1
                                Test.activarTests(1, id_test);
                                Log.d(TAG, object.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidepDialog(pDialog);
                            if (error != null) {
                                VolleyLog.d(error.toString());
                            }
                            if (ConexionBroadcastReceiver.isConnected()){ generarToken(fecha, id_categoria, token, idPeriodo); }
                            else if (!ConexionBroadcastReceiver.isConnected()){
                                Toast.makeText(getApplicationContext(), "Verifique su conexión a internet.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AceleraActivity.this,PrincipalActivity.class));
                                finish();
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("fecha", fecha);
                    params.put("idCategoria", String.valueOf(id_categoria));
                    params.put("token", token);
                    params.put("from", "android");
                    params.put("idPeriodo", String.valueOf(idPeriodo));
                    Log.d(TAG, params.toString());
                    return params;
                }
            };
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { showSnack(layout); }
    }

    private void hacerTransicion() {
        Slide slide1 = new Slide();
        slide1.setDuration(1000);
        getWindow().setEnterTransition(slide1);

        Fade fade2 = new Fade();
        fade2.setDuration(1000);
        getWindow().setReturnTransition(fade2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciarLayout();
    }

    private void iniciarLayout() {
        realm = Realm.getDefaultInstance();
        listaR = realm.where(Acelera.class).findAll();
        realmAdapter = new AceleraRealmAdapter(this, listaR, true, true);
        listaRecyclerAcelera.setAdapter(realmAdapter);
        realmAdapter.notifyDataSetChanged();
        usarGlide(this, R.drawable.tarjeta_acelera_lista, cabecera_acelera);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

}
