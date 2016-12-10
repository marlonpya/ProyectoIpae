package application.ucweb.proyectoipae;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.aplicacion.Configuracion;
import application.ucweb.proyectoipae.dialogo.AcercaDeIpae;
import application.ucweb.proyectoipae.fragment.MenuFragment;
import application.ucweb.proyectoipae.fragment.NavegadorFragment;
import application.ucweb.proyectoipae.fragment.SugerenciasFragment;
import application.ucweb.proyectoipae.model.Acelera;
import application.ucweb.proyectoipae.model.Area;
import application.ucweb.proyectoipae.model.Categoria;
import application.ucweb.proyectoipae.model.Innova;
import application.ucweb.proyectoipae.model.PreguntaAcelera;
import application.ucweb.proyectoipae.model.PreguntaInnova;
import application.ucweb.proyectoipae.model.Termino;
import application.ucweb.proyectoipae.model.Test;
import application.ucweb.proyectoipae.realm.MetodoServices;
import application.ucweb.proyectoipae.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoipae.util.Constantes;
import application.ucweb.proyectoipae.util.Preferencia;
import butterknife.BindDrawable;
import butterknife.BindView;
import io.realm.Realm;

public class PrincipalActivity extends BaseActivity implements NavegadorFragment.FragmentDrawerListener {
    @BindView(R.id.iv_fondo_principal) ImageView fondo;
    @BindView(R.id.myDrawerLayout) DrawerLayout myDrawerLayout;
    @BindView(R.id.toolbar_principal) Toolbar toolbar;
    @BindView(R.id.ivToolbar_Icono) ImageView icono_toolbar;
    @BindDrawable(R.drawable.ic_navigation_more_vert) Drawable drwIconoDerecho;
    @BindDrawable(R.drawable.ic_navigation_menu_blue) Drawable drwIconoNavegador;
    public static final String TAG = PrincipalActivity.class.getSimpleName();
    private NavegadorFragment navegadorFragment;
    private int posicion_fragment = 0;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        iniciarLayout();
        configuracionNavegador();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sincronizando..");
        pDialog.setCancelable(false);

        if (Area.getSize() == 0) { consumirTodo(pDialog, myDrawerLayout, this); }
    }

    private void consumirTerminos(final ProgressDialog pDialog) {
        if (ConexionBroadcastReceiver.isConnected()) {
            showDialog(pDialog);
            JsonObjectRequest request = new JsonObjectRequest(
                    Constantes.URL_GET_TERMINOS,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                JSONObject object = response.getJSONObject("data");
                                JSONArray dataTerminos = object.getJSONArray("dataTerminos");
                                JSONObject object1 = dataTerminos.getJSONObject(dataTerminos.length() - 1);
                                Termino termino = realm.createObject(Termino.class);
                                termino.setId_termino(Termino.traerUltimoID());
                                termino.setId_servidor(object1.getInt("idTermino"));
                                termino.setDescripcion_termino(object1.getString("texto"));
                                realm.copyToRealm(termino);
                                realm.commitTransaction();
                                realm.close();
                                Log.d(TAG, termino.toString());
                                Preferencia.controlPreferencia(3, PrincipalActivity.this, false);
                                hidepDialog(pDialog);
                                mostrarMensaje(PrincipalActivity.this, "Se actualizaron los términos correctamente");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                hidepDialog(pDialog);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidepDialog(pDialog);
                            consumirTerminos(pDialog);
                            if (error != null) { VolleyLog.d(error.toString()); }
                        }
                    }
            );
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { showSnack(myDrawerLayout); }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciarPosicion();
        SharedPreferences preferencia = getSharedPreferences(Constantes.PREFERENCIA_PARA_ACTUALIZACIONES, Context.MODE_PRIVATE);
        if (preferencia.getBoolean(Constantes.KEY_ACTUALIZAR_PREGUNTAS, false)) {
            mostrarDialogoActualizarPreguntas();
        }
        if (preferencia.getBoolean(Constantes.KEY_ACTUALIZAR_UNA_PREGUNTA, false)) {
            if (ConexionBroadcastReceiver.isConnected()) {
                mostrarDialogoActualizarUnaPregunta();
            }
        }
        if (preferencia.getBoolean(Constantes.KEY_ACTUALIZAR_AREAS, false)) {
            consumirAreas(pDialog);
        }
        if (preferencia.getBoolean(Constantes.KEY_ACTUALIZAR_TERMINOS, false)) {
            consumirTerminos(pDialog);
        }
    }

    private void iniciarLayout(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setOverflowIcon(drwIconoDerecho);
        toolbar.setNavigationIcon(drwIconoNavegador);
        usarGlide(this, R.drawable.fondo_principal_final, fondo);
        usarGlide(this, R.drawable.icono_toolbar_ipae, icono_toolbar);
        iniciarPosicion();
    }

    private void configuracionNavegador(){
        navegadorFragment = (NavegadorFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navegadorFragment.setUp(R.id.fragment_navigation_drawer, myDrawerLayout, toolbar);
        navegadorFragment.setDrawerListener(this);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        display(position);
    }

    private void display(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0: FragmentManager manager = getSupportFragmentManager();
                    AcercaDeIpae dialogo = new AcercaDeIpae();
                    dialogo.show(manager, "TAGDIALOGO");
                    break;
            case 1: fragment = new SugerenciasFragment();
                    posicion_fragment = 1;
                    break;
            case 2: Intent intent = new Intent(this, InicioActivity.class);
                    intent.putExtra(Constantes.KEY_MATAR_APLICACION, true);
                    startActivity(intent);
                    finish();
                    break;
            default: break;
        }
        if (fragment != null){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.contenedor,fragment);
            transaction.commit();
        }
    }

    private void consumirUnaPregunta(final ProgressDialog pDialog, final View layout, final int id_guardado) {
        if (ConexionBroadcastReceiver.isConnected()) {
            showDialog(pDialog);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Constantes.URL_GET_UNA_PREGUNTA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hidepDialog(pDialog);
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray jsonArray = object.getJSONArray("data");
                                int id_pregunta = jsonArray.getJSONObject(0).getInt("idPregunta");
                                int id_foranea = jsonArray.getJSONObject(0).getInt("idSubCategoria");
                                String pregunta = jsonArray.getJSONObject(0).getString("pregunta");
                                Log.d(TAG, "id_pregunta*_"+String.valueOf(id_pregunta));
                                Log.d(TAG, "id_foranea*_" + String.valueOf(id_foranea));
                                MetodoServices.modificarUnaPregunta(id_pregunta, pregunta);
                                if (MetodoServices.saberPreguntaAceleraOInnova(id_pregunta) == 1) {
                                    startActivity(new Intent(PrincipalActivity.this, CuestionarioAceleraActivity.class)
                                    .putExtra(Constantes.KEY_UNICA_PREGUNTA_ACELERA, true)
                                    .putExtra(Constantes.KEY_UNICA_ID_ACELERA,  id_pregunta)
                                    .putExtra(Constantes.KEY_UNICA_ID_FORANEA_ACELERA, (long) id_foranea));
                                } else if(MetodoServices.saberPreguntaAceleraOInnova(id_pregunta) == 2){
                                    startActivity(new Intent(PrincipalActivity.this, CuestionarioInnovaActivity.class)
                                    .putExtra(Constantes.KEY_UNICA_PREGUNTA_INNOVA, true)
                                    .putExtra(Constantes.KEY_UNICA_ID_INNOVA, id_pregunta)
                                    .putExtra(Constantes.KEY_UNICA_ID_FORANEA_INNOVA, (long) id_foranea));
                                }
                                Log.d(TAG, object.toString());
                                Preferencia.controlPreferencia(4, PrincipalActivity.this, false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidepDialog(pDialog);
                            consumirUnaPregunta(pDialog, layout, id_guardado);
                            if (error != null) { VolleyLog.d(error.toString()); }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("idPregunta", String.valueOf(id_guardado));
                    Log.d(TAG, String.valueOf(id_guardado));
                    return params;
                }
            };
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { showSnack(layout); }
    }

    private void consumirPreguntas(final ProgressDialog pDialog, final View layout, final Context context) {
        if (ConexionBroadcastReceiver.isConnected()) {
            showDialog(pDialog);
            JsonObjectRequest request = new JsonObjectRequest(
                    Constantes.URL_GET_PREGUNTAS,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                MetodoServices.eliminarTodaslasPreguntas();
                                Realm realm = Realm.getDefaultInstance();
                                JSONObject data = response.getJSONObject("data");
                                JSONArray arrayCategoria = data.getJSONArray("dataCategorias");
                                for (int i = 0; i < arrayCategoria.length(); i++) {
                                    JSONObject jsonCategoria = arrayCategoria.getJSONObject(i);
                                    realm.beginTransaction();
                                    Categoria categoria = realm.createObject(Categoria.class);
                                    categoria.setId_categoria(jsonCategoria.getLong("idCategoria"));
                                    categoria.setNombre_categoria(jsonCategoria.getString("nombre_cat"));
                                    realm.copyToRealm(categoria);
                                    realm.commitTransaction();
                                    realm.close();
                                    Log.d(TAG, categoria.toString());
                                }

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

                                    } else if (tipo_categoria == 2) {
                                        realm.beginTransaction();
                                        Innova innova = realm.createObject(Innova.class);
                                        innova.setId_inn((long) object1.getLong("idSubCategoria"));
                                        innova.setIcono_inn(object1.getString("imagen_subc"));
                                        innova.setCategoria_inn(object1.getString("nombre_subc"));
                                        innova.setEstado_inn(false);
                                        innova.setEstado_orden_inn(false);
                                        innova.setId_periodo_inn(object1.getInt("idPeriodo"));
                                        realm.copyToRealm(innova);
                                        realm.commitTransaction();
                                        realm.close();
                                        Log.d(TAG, innova.toString());
                                    }

                                }
                                Realm realm1 = Realm.getDefaultInstance();

                                Innova innova_ = realm1.where(Innova.class).findFirst();
                                realm1.beginTransaction();
                                Innova innova1 = new Innova();
                                innova1.setId_inn(innova_.getId_inn());
                                innova1.setIcono_inn(innova_.getIcono_inn());
                                innova1.setCategoria_inn(innova_.getCategoria_inn());
                                innova1.setEstado_inn(true);
                                innova1.setEstado_orden_inn(true);
                                innova1.setId_periodo_inn(innova_.getId_periodo_inn());
                                realm1.copyToRealmOrUpdate(innova1);
                                realm1.commitTransaction();
                                realm.close();

                                Acelera acelera_ = realm1.where(Acelera.class).findFirst();
                                realm1.beginTransaction();
                                Acelera acelera1 = new Acelera();
                                acelera1.setId_ace(acelera_.getId_ace());
                                acelera1.setIcono_ace(acelera_.getIcono_ace());
                                acelera1.setCategoria_ace(acelera_.getCategoria_ace());
                                acelera1.setEstado_ace(true);
                                acelera1.setEstado_orden_ace(true);
                                acelera1.setId_periodo_ace(acelera_.getId_periodo_ace());
                                realm1.copyToRealmOrUpdate(acelera1);
                                realm1.commitTransaction();
                                realm.close();
                                //
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
                                        realm.close();

                                        Log.d(TAG, pa.toString());
                                    }
                                    Innova innova = realm2.where(Innova.class).equalTo(Innova.I_ID, id_json).findFirst();
                                    if (innova != null) {
                                        realm2.beginTransaction();
                                        PreguntaInnova pi = realm2.createObject(PreguntaInnova.class);
                                        pi.setId_pregunta_inn(PreguntaInnova.getUltimoId());
                                        pi.setId_pregunta_servidor_inn(id_pregunta);
                                        pi.setNum_pregunta_inn((int) innova.getId_inn());
                                        pi.setPregunta_inn(pregunta);
                                        pi.setValor_respuesta(pregt_pa2);
                                        pi.setValor_ace("");
                                        pi.setRespondido_inn(false);
                                        pi.setId_boton_inn(-1);
                                        innova.getPreguntas().add(pi);

                                        realm2.copyToRealm(pi);
                                        realm2.commitTransaction();
                                        realm.close();

                                        Log.d(TAG, pi.toString());
                                    }
                                }
                                Test.desactivarTest(1);
                                Test.desactivarTest(2);
                                Preferencia.controlPreferencia(1, PrincipalActivity.this, false);
                                Preferencia.limpiarPreferenciasPreguntas(PrincipalActivity.this);
                                mostrarMensaje(context, "Sincronización terminada");

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, e.toString());
                            }
                            hidepDialog(pDialog);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidepDialog(pDialog);
                            consumirPreguntas(pDialog, layout, PrincipalActivity.this);
                            if (error != null) { VolleyLog.d(error.toString()); }
                        }
                    }
            );
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { showSnack(layout); }
    }

    public static void consumirTodo(final ProgressDialog pDialog, final View layout, final Context context) {
        if (ConexionBroadcastReceiver.isConnected()) {
            showDialog(pDialog);
            JsonObjectRequest request = new JsonObjectRequest(
                    Constantes.URL_DATA,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Realm realm = Realm.getDefaultInstance();
                                JSONObject object = response.getJSONObject("data");
                                JSONArray dataArea = object.getJSONArray("dataArea");
                                for (int i = 0; i < dataArea.length(); i++) {
                                    JSONObject area = dataArea.getJSONObject(i);
                                    realm = Realm.getDefaultInstance();
                                    if (area.getInt("estado") == 1) {
                                        realm.beginTransaction();
                                        Area objArea = realm.createObject(Area.class);
                                        objArea.setId_area(Area.getLastId());
                                        objArea.setId_area_servidor(area.getInt("idArea"));
                                        objArea.setDescripcion_area(area.getString("area"));
                                        objArea.setCategoria_area(area.getInt("idCategoria"));
                                        realm.copyToRealm(objArea);
                                        realm.commitTransaction();
                                        realm.close();

                                        Log.d(TAG, objArea.toString());
                                    }
                                }

                                JSONArray arrayCategoria = object.getJSONArray("dataCategorias");
                                for (int i = 0; i < arrayCategoria.length(); i++) {
                                    JSONObject jsonCategoria = arrayCategoria.getJSONObject(i);
                                    realm.beginTransaction();
                                    Categoria categoria = realm.createObject(Categoria.class);
                                    categoria.setId_categoria(jsonCategoria.getLong("idCategoria"));
                                    categoria.setNombre_categoria(jsonCategoria.getString("nombre_cat"));
                                    realm.copyToRealm(categoria);
                                    realm.commitTransaction();
                                    realm.close();
                                    Log.d(TAG, categoria.toString());
                                }

                                JSONArray array = object.getJSONArray("dataSubcategorias");
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

                                    } else if (tipo_categoria == 2) {
                                        realm.beginTransaction();
                                        Innova innova = realm.createObject(Innova.class);
                                        innova.setId_inn((long) object1.getLong("idSubCategoria"));
                                        innova.setIcono_inn(object1.getString("imagen_subc"));
                                        innova.setCategoria_inn(object1.getString("nombre_subc"));
                                        innova.setEstado_inn(false);
                                        innova.setEstado_orden_inn(false);
                                        innova.setId_periodo_inn(object1.getInt("idPeriodo"));
                                        realm.copyToRealm(innova);
                                        realm.commitTransaction();
                                        Log.d(TAG, innova.toString());
                                    }

                                }
                                Realm realm1 = Realm.getDefaultInstance();
                                Innova innova_ = realm1.where(Innova.class).findFirst();

                                realm1.beginTransaction();

                                Innova innova1 = new Innova();
                                innova1.setId_inn(innova_.getId_inn());
                                innova1.setIcono_inn(innova_.getIcono_inn());
                                innova1.setCategoria_inn(innova_.getCategoria_inn());
                                innova1.setEstado_inn(true);
                                innova1.setEstado_orden_inn(true);
                                innova1.setId_periodo_inn(innova_.getId_periodo_inn());

                                realm1.copyToRealmOrUpdate(innova1);
                                realm1.commitTransaction();
                                realm.close();

                                Acelera acelera_ = realm1.where(Acelera.class).findFirst();

                                realm1.beginTransaction();

                                Acelera acelera1 = new Acelera();
                                acelera1.setId_ace(acelera_.getId_ace());
                                acelera1.setIcono_ace(acelera_.getIcono_ace());
                                acelera1.setCategoria_ace(acelera_.getCategoria_ace());
                                acelera1.setEstado_ace(true);
                                acelera1.setEstado_orden_ace(true);
                                acelera1.setId_periodo_ace(acelera_.getId_periodo_ace());

                                realm1.copyToRealmOrUpdate(acelera1);
                                realm1.commitTransaction();
                                realm.close();
                                //
                                Realm realm2 = Realm.getDefaultInstance();
                                JSONArray array2 = object.getJSONArray("dataPreguntas");
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
                                        realm.close();

                                        Log.d(TAG, pa.toString());
                                    }
                                    Innova innova = realm2.where(Innova.class).equalTo(Innova.I_ID, id_json).findFirst();
                                    if (innova != null) {
                                        realm2.beginTransaction();
                                        PreguntaInnova pi = realm2.createObject(PreguntaInnova.class);
                                        pi.setId_pregunta_inn(PreguntaInnova.getUltimoId());
                                        pi.setId_pregunta_servidor_inn(id_pregunta);
                                        pi.setNum_pregunta_inn((int) innova.getId_inn());
                                        pi.setPregunta_inn(pregunta);
                                        pi.setValor_respuesta(pregt_pa2);
                                        pi.setValor_ace("");
                                        pi.setRespondido_inn(false);
                                        pi.setId_boton_inn(-1);
                                        innova.getPreguntas().add(pi);

                                        realm2.copyToRealm(pi);
                                        realm2.commitTransaction();
                                        realm.close();

                                        Log.d(TAG, pi.toString());
                                    }

                                }
                                JSONArray jsonATerminos = object.getJSONArray("dataTerminos");
                                for (int i = 0; i < jsonATerminos.length(); i++) {
                                    JSONObject jsonOTermino = jsonATerminos.getJSONObject(i);
                                    realm.beginTransaction();
                                    Termino termino = realm.createObject(Termino.class);
                                    termino.setId_termino(Termino.traerUltimoID());
                                    termino.setId_servidor(jsonOTermino.getInt("idTermino"));
                                    termino.setDescripcion_termino(jsonOTermino.getString("texto"));
                                    realm.copyToRealm(termino);
                                    realm.commitTransaction();
                                    realm.close();

                                    Log.d(TAG, termino.toString());
                                }
                                hidepDialog(pDialog);
                                mostrarMensaje(context, "Sincronización terminada");
                            } catch (JSONException e) {
                                hidepDialog(pDialog);
                                e.printStackTrace();
                                if (e != null) {
                                    Log.d(TAG, e.toString());
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidepDialog(pDialog);
                            if (ConexionBroadcastReceiver.isConnected() && Area.getSize() == 0){ consumirTodo(pDialog, layout, context); }
                            if (error != null) { VolleyLog.d(error.toString()); }
                        }
                    });
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { showSnack(layout); }
    }

    public void consumirAreas(final ProgressDialog pDialog) {
        if (ConexionBroadcastReceiver.isConnected()) {
            showDialog(pDialog);
            JsonObjectRequest request = new JsonObjectRequest(
                    Constantes.URL_GET_AREAS,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hidepDialog(pDialog);
                            try {
                                MetodoServices.eliminarAreas();
                                Realm realm = Realm.getDefaultInstance();
                                JSONObject object = response.getJSONObject("data");
                                JSONArray dataArea = object.getJSONArray("dataArea");
                                for (int i = 0; i < dataArea.length(); i++) {
                                    JSONObject area = dataArea.getJSONObject(i);
                                    if (area.getInt("estado") == 1) {
                                        realm.beginTransaction();
                                        Area objArea = realm.createObject(Area.class);
                                        objArea.setId_area(Area.getLastId());
                                        objArea.setId_area_servidor(area.getInt("idArea"));
                                        objArea.setDescripcion_area(area.getString("area"));
                                        objArea.setCategoria_area(area.getInt("idCategoria"));
                                        realm.copyToRealm(objArea);
                                        realm.commitTransaction();
                                        realm.close();

                                        Log.d(TAG, objArea.toString());
                                    }
                                }
                                Preferencia.controlPreferencia(2, PrincipalActivity.this, false);
                                mostrarMensaje(PrincipalActivity.this, "Se actualizaron las áreas correctamente");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) { VolleyLog.d(error.toString()); }
                            hidepDialog(pDialog);
                             consumirAreas(pDialog);
                        }
                    });
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        }
    }

    private void iniciarPosicion() {
        Fragment fragment = new MenuFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.contenedor,fragment);
        transaction.commit();
        posicion_fragment = 0;
    }

    @Override
    public void onBackPressed() {
        if (posicion_fragment == 1) {
            iniciarPosicion();
        } else {
            startActivity(new Intent(getBaseContext(), InicioActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
    }

    private void mostrarDialogodeActualizacion() {
        new AlertDialog.Builder(this)
                .setTitle("IPAE")
                .setMessage("Usted tiene una actualización pendiente, si está de acuerdo se reiniciarán todas las preguntas ?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ConexionBroadcastReceiver.isConnected()) {
                            //Preferencia.limpiarPreferencias(PrincipalActivity.this);
                            MetodoServices.limpiarBDRealm();
                            Test.desactivarTest(1);
                            Test.desactivarTest(2);
                            consumirTodo(pDialog, myDrawerLayout, PrincipalActivity.this);
                        } else { showSnack( myDrawerLayout); }
                    }
                })
                .setNegativeButton("NO", null)
                .setCancelable(false)
                .create()
                .show();
    }

    private void mostrarDialogoActualizarPreguntas() {
        new AlertDialog.Builder(this)
                .setTitle("IPAE")
                .setMessage("IPAE tiene una actualización de preguntas, si acepta se reiniciarán todas las preguntas, está de acuerdo ?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        consumirPreguntas(pDialog, myDrawerLayout, PrincipalActivity.this);
                    }
                })
                .setNegativeButton("NO", null)
                .setCancelable(false)
                .create()
                .show();
    }

    private void mostrarDialogoActualizarUnaPregunta() {
        new AlertDialog.Builder(this)
                .setTitle("IPAE")
                .setMessage("IPAE tiene una actualización de pregunta, ir a la pregunta actualizada?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id_guardado = getSharedPreferences(Constantes.PREFERENCIA_PARA_ACTUALIZACIONES, Context.MODE_PRIVATE).getInt(Constantes.KEY_PREGUNTA_ACTUALIZADA, -1);
                        Log.d(TAG, String.valueOf(id_guardado));
                        consumirUnaPregunta(pDialog, myDrawerLayout, id_guardado);
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

}
