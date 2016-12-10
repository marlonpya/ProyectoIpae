package application.ucweb.proyectoipae.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import application.ucweb.proyectoipae.model.Test;
import application.ucweb.proyectoipae.util.Constantes;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ucweb02 on 24/08/2016.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {
    public static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    private String token = "empty";

    @Override
    public void onTokenRefresh() {

        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "token_" + token);

        if (Test.getLastId() == 0) { Test.crearTests(); }
        Test.actualizarTokens(token);

        if (Test.getTest(1).isEstado_token()) {
            int id_test = Test.getTest(1).getId_test_servidor();
            registrarToken(token, id_test);
        }
        if (Test.getTest(2).isEstado_token()) {
            int id_test = Test.getTest(2).getId_test_servidor();
            registrarToken(token, id_test);
        }
    }

    private void registrarToken(String token, int id) {
        Log.d(TAG, "registrarToken/token_" + token);
        Log.d(TAG, "registrarToken/id_" + String.valueOf(id));
        Log.d(TAG, "TOKEN_REGISTRADO_INICIADO______________________");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("idTest", String.valueOf(id))
                .add("token", token)
                .build();

        Log.d(TAG, "registrarToken/idTest_" + String.valueOf(id));
        Log.d(TAG, "registrarToken/token_" + token);

        Request request = new Request.Builder()
                .url(Constantes.URL_UPDATE_ID)
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());

        }
    }

}
