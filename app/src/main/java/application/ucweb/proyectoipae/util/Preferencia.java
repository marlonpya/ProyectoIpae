package application.ucweb.proyectoipae.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Arrays;

import application.ucweb.proyectoipae.aplicacion.BaseActivity;

/**
 * Created by ucweb02 on 15/08/2016.
 */
public class Preferencia extends BaseActivity {
    public static final String TAG = Preferencia.class.getSimpleName();

    public static int[] traerIdPreferenciaAcelera(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        int num_acelera_pregunta        = preferencia.getInt(Constantes.KEY_NUM_ID_PREFERENCIA_A, -1);
        int orden_acelera_pregunta      = preferencia.getInt(Constantes.KEY_ORDEN_ID_PREFERENCIA_A, -1);
        int posicion_pregunta_acelera   = preferencia.getInt(Constantes.KEY_POSICION_PREGUNTA_ACELERA, 1);
        int[] array = {num_acelera_pregunta, orden_acelera_pregunta, posicion_pregunta_acelera};
        Log.d(TAG, Arrays.toString(array));
        return array;
    }

    public static int[] traerIdPreferenciaInnova(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        int num_acelera_pregunta        = preferencia.getInt(Constantes.KEY_NUM_ID_PREFERENCIA_I, -1);
        int orden_acelera_pregunta      = preferencia.getInt(Constantes.KEY_ORDEN_ID_PREFERENCIA_I, -1);
        int posicion_pregunta_innova    = preferencia.getInt(Constantes.KEY_POSICION_PREGUNTA_INNOVA, 1);
        int[] array = {num_acelera_pregunta, orden_acelera_pregunta, posicion_pregunta_innova};
        Log.d(TAG, Arrays.toString(array));
        return array;
    }

    public static void iniciarPreferenciaAcelera(int id1, int id2, int id3, Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putInt(Constantes.KEY_NUM_ID_PREFERENCIA_A, id1);
        editor.putInt(Constantes.KEY_ORDEN_ID_PREFERENCIA_A, id2);
        editor.putInt(Constantes.KEY_POSICION_PREGUNTA_ACELERA, id3);
        Log.d(TAG,"iniciarPreferenciaAcelera/id1_"+ String.valueOf(id1));
        Log.d(TAG,"iniciarPreferenciaAcelera/id2_"+ String.valueOf(id2));
        Log.d(TAG,"iniciarPreferenciaAcelera/id3_"+ String.valueOf(id3));
        editor.commit();
    }

    public static void iniciarPreferenciaInnova(int id1, int id2, int id3, Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putInt(Constantes.KEY_NUM_ID_PREFERENCIA_I, id1);
        editor.putInt(Constantes.KEY_ORDEN_ID_PREFERENCIA_I, id2);
        editor.putInt(Constantes.KEY_POSICION_PREGUNTA_INNOVA, id3);
        Log.d(TAG,"iniciarPreferenciaInnova/id1_"+ String.valueOf(id1));
        Log.d(TAG,"iniciarPreferenciaInnova/id2_"+ String.valueOf(id2));
        Log.d(TAG,"iniciarPreferenciaInnova/id3_"+ String.valueOf(id3));
        editor.commit();
    }

    public static void activarDiagnosticoAcelera(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean(Constantes.KEY_ESTADO_DIAGNOSTICO_ACELERA, true);
        editor.commit();
    }

    public static void activarDiagnosticoInnova(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean(Constantes.KEY_ESTADO_DIAGNOSTICO_INNOVA, true);
        editor.commit();
    }

    public static void desactivarDiagnosticoAcelera(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean(Constantes.KEY_ESTADO_DIAGNOSTICO_ACELERA, false);
        editor.commit();
    }

    public static void desactivarDiagnosticoInnova(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean(Constantes.KEY_ESTADO_DIAGNOSTICO_INNOVA, false);
        editor.commit();
    }

    /*public static void activarFin(Context context, int unidad, boolean valor) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        switch (unidad){
            case 1:
                editor.putBoolean(Constantes.KEY_FIN_ACELERA, valor);
                editor.commit();
                Log.d(TAG,"activarFin/A");
                break;
            case 2:
                editor.putBoolean(Constantes.KEY_FIN_INNOVA, valor);
                editor.commit();
                Log.d(TAG,"activarFin/I");
                break;
        }
    }*/

    /*public static void controlPreguntasFin(Context context, int numero, boolean valor) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_ACTUALIZACIONES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        switch (numero){
            case 1:
                editor.putBoolean(Constantes.KEY_FIN_PREGUNTAS_ACELERA, valor);
                editor.commit();
                Log.d(TAG,"controlPreguntasFin/A");
                break;
            case 2:
                editor.putBoolean(Constantes.KEY_FIN_PREGUNTAS_INNOVA, valor);
                editor.commit();
                Log.d(TAG,"controlPreguntasFin/I");
                break;
        }
    }*/

    /*public static void desactivarFin(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean(Constantes.KEY_FIN_ACELERA, false);
        editor.putBoolean(Constantes.KEY_FIN_INNOVA, false);
        editor.commit();
        Log.d(TAG, "desactivarFin");
    }

    public static void activarPrefActualizarTodo(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean(Constantes.KEY_ACTUALIZAR_TODO, true);
        editor.commit();
    }

    public static void desactivarPrefActualizarTodo(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putBoolean(Constantes.KEY_ACTUALIZAR_TODO, false);
        editor.commit();
    }*/

    public static void limpiarPreferenciasPreguntas(Context context) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_PREGUNTAS, Context.MODE_PRIVATE);
        preferencia.edit().clear().commit();
        Log.d(TAG, "limpiarPreferencias");
    }

    public static void controlPreferencia(int tipo, Context context, boolean valor) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_ACTUALIZACIONES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        switch (tipo) {
            case 1: editor.putBoolean(Constantes.KEY_ACTUALIZAR_PREGUNTAS, valor);  break;
            case 2: editor.putBoolean(Constantes.KEY_ACTUALIZAR_AREAS, valor);      break;
            case 3: editor.putBoolean(Constantes.KEY_ACTUALIZAR_TERMINOS, valor);   break;
            case 4: editor.putBoolean(Constantes.KEY_ACTUALIZAR_UNA_PREGUNTA, valor); break;
        }
        editor.commit();
        Log.d(TAG, "controlPreferencia/tipo_"+String.valueOf(tipo));
        Log.d(TAG, "controlPreferencia/valor"+String.valueOf(valor));
    }

    public static void guardarIdPregunta(Context context, int numero) {
        SharedPreferences preferencia = context.getSharedPreferences(Constantes.PREFERENCIA_PARA_ACTUALIZACIONES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putInt(Constantes.KEY_PREGUNTA_ACTUALIZADA, numero);
        editor.commit();
        Log.d(TAG, "guardarIdPregunta/numero_" + String.valueOf(numero));
    }

}
