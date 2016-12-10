package application.ucweb.proyectoipae.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import application.ucweb.proyectoipae.PrincipalActivity;
import application.ucweb.proyectoipae.R;
import application.ucweb.proyectoipae.model.Test;
import application.ucweb.proyectoipae.realm.MetodoServices;
import application.ucweb.proyectoipae.util.Constantes;
import application.ucweb.proyectoipae.util.Preferencia;

/**
 * Created by ucweb02 on 24/08/2016.
 */
public class MyFcmListenerService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String TAG = MyFcmListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "token01_"+Test.getTest(1).getToken_test());
        Log.d(TAG, "token02_"+Test.getTest(2).getToken_test());

        String message = remoteMessage.getData().get("message");
        int tipo = Integer.parseInt(remoteMessage.getData().get("tipo"));
        int id = Integer.parseInt(remoteMessage.getData().get("id"));
        if (tipo != 4) {
            Preferencia.controlPreferencia(tipo, this, true);
        }

        if (tipo == 1) {
            mostrarMensaje(message);
        } else if (Test.getTest(1).isEstado_token() || Test.getTest(2).isEstado_token() && tipo == 4) {
            Preferencia.guardarIdPregunta(this, id);
            // acelera o innova
            int id_pregunta = MetodoServices.saberPreguntaAceleraOInnova(id);
            if (id_pregunta == 1 && Test.getTest(1).isEstado_token()) {
                mostrarMensaje(message);
                Preferencia.controlPreferencia(tipo, this, true);
            } else if (id_pregunta == 2 && Test.getTest(2).isEstado_token()) {
                mostrarMensaje(message);
                Preferencia.controlPreferencia(tipo, this, true);
            }
        }

        Log.d(TAG, "mostrarMensaje/tipo_" + String.valueOf(tipo));
        Log.d(TAG, "mostrarMensaje/mensaje_" + message);
        Log.d(TAG, "mostrarMensaje/id_" + id);
    }

    private void mostrarMensaje(String mensaje) {
            SharedPreferences preferences = getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
            int identificador = preferences.getInt(Constantes.KEY_IDENTIFICADOR, 0);

            Intent i = new Intent(this, PrincipalActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("IPAE")
                    .setContentText(mensaje)
                    .setSmallIcon(R.drawable.icono_ipae)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{2000, 2000, 2000, 2000, 2000});

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            manager.notify(identificador, builder.build());

            SharedPreferences.Editor editor = preferences.edit();
            identificador++;
            editor.putInt(Constantes.KEY_IDENTIFICADOR, identificador);
            editor.commit();
    }

}
