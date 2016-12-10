package application.ucweb.proyectoipae.aplicacion;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import application.ucweb.proyectoipae.DiagnosticoActivity;
import application.ucweb.proyectoipae.R;
import application.ucweb.proyectoipae.util.Constantes;
import application.ucweb.proyectoipae.util.Preferencia;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by ucweb02 on 02/08/2016.
 */
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    Realm realm;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    /*@Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        }
        super.onCreate(savedInstanceState, persistentState);
    }

    public static void setToolbarSon(Toolbar toolbar, AppCompatActivity activity, ImageView imageView){
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_blue);
        toolbar.setOverflowIcon(activity.getResources().getDrawable(R.drawable.ic_navigation_more_vert));
        usarGlide(activity.getApplicationContext(), R.drawable.icono_toolbar_ipae, imageView);
    }

    public static void usarGlide(Context context, int rutaIcono, ImageView imageView){
        Glide.with(context)
                .load(rutaIcono)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void usarGlide(Context context, String rutaIcono, ImageView imageView){
        Glide.with(context)
                .load(rutaIcono)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    public static void showDialog(ProgressDialog dialog) {
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    public static void hidepDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static void startMyIntent(Context context, Intent intent, BaseActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            activity.finish();
        } else {
            context.startActivity(intent);
            activity.finish();
        }
    }

    public static void mostrarMensaje(Context context, String mensaje) {
        new AlertDialog.Builder(context)
                .setTitle("IPAE")
                .setMessage(mensaje)
                .setCancelable(true)
                .create()
                .show();
    }

    public static void showSnack(View view) {
        String message = "Lo sentimos, usted no tiene conexión a internet.";
        int color = Color.WHITE;
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    public static void mostrarMensajePreguntasNoEnviadas(final Context context, final int id, final BaseActivity activity) {
        new AlertDialog.Builder(context)
                .setTitle("IPAE")
                .setMessage("¿Usted tiene un envío de respuestas pendiente, desea enviar respuestas?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (id == 1){
                            context.startActivity(new Intent(context,DiagnosticoActivity.class).putExtra(Constantes.KEY_MANDAR_ID_AREA, 1));
                        }else {
                            context.startActivity(new Intent(context,DiagnosticoActivity.class).putExtra(Constantes.KEY_MANDAR_ID_AREA, 2));
                        }
                        activity.finish();
                    }
                })
                .setNegativeButton("NO", null)
                .create()
                .show();
    }

}
