package application.ucweb.proyectoipae.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoipae.R;
import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.aplicacion.Configuracion;
import application.ucweb.proyectoipae.model.PreguntaAcelera;
import application.ucweb.proyectoipae.model.Test;
import application.ucweb.proyectoipae.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoipae.util.Constantes;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class SugerenciasFragment extends Fragment {
    public static final String TAG = SugerenciasFragment.class.getSimpleName();
    @BindView(R.id.layout_f_sugerencias) LinearLayout layout;
    @BindView(R.id.tvNombre_completo) EditText tvNombre_completo;
    @BindView(R.id.tvCorreo) EditText tvCorreo;
    @BindView(R.id.tvConsulta) EditText tvConsulta;
    @BindString(R.string.mensaje_llenar_campos) String mensaje;
    private ProgressDialog pDialog;

    private Unbinder unbinder;

    public SugerenciasFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sugerencias, container, false);
        unbinder = ButterKnife.bind(this,view);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Enviando..");
        return view;
    }

    @OnClick(R.id.btnEnviar)
    public void enviarServicioSugerencia() {
        String nombre = tvNombre_completo.getText().toString().trim();
        String correo = tvCorreo.getText().toString().trim();
        String consulta = tvConsulta.getText().toString().trim();
        if (ConexionBroadcastReceiver.isConnected()) {
            if (!nombre.isEmpty() || !correo.isEmpty() || !consulta.isEmpty()) {
                enviarServicioSugerencia(nombre, correo, consulta);
            } else { Toast.makeText(getActivity(), "Se requiere de todos los campos", Toast.LENGTH_SHORT).show(); }
        } else { BaseActivity.showSnack(layout); }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void enviarServicioSugerencia(final String nombre_completo, final String correo, final String sugerencia) {
        if (ConexionBroadcastReceiver.isConnected()){
            BaseActivity.showDialog(pDialog);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Constantes.URL_ENVIAR_SUGERENCIA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            BaseActivity.hidepDialog(pDialog);
                            try {
                                JSONObject object = new JSONObject(response);
                                Log.d(TAG, object.toString());
                                BaseActivity.mostrarMensaje(getActivity(),object.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            tvNombre_completo.setText("");
                            tvConsulta.setText("");
                            tvCorreo.setText("");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            BaseActivity.hidepDialog(pDialog);
                            if (error != null) { VolleyLog.d(error.toString()); }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nombrecompleto", nombre_completo);
                    params.put("correo", correo);
                    params.put("consulta", sugerencia);
                    Log.d(TAG, params.toString());
                    return params;
                }
            };
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        } else { BaseActivity.showSnack(layout); }
    }

}
