package application.ucweb.proyectoipae;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;

import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.realm.MetodoServices;
import application.ucweb.proyectoipae.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;

public class InicioActivity extends BaseActivity {
    @BindView(R.id.iv_inicio_fondo_ipae) ImageView fondo_ipae;
    @BindView(R.id.pico_01) ImageView img1;
    @BindView(R.id.pico_02) ImageView img2;
    @BindView(R.id.pico_03) ImageView img3;
    @BindView(R.id.pico_04) ImageView img4;
    @BindView(R.id.pico_05) ImageView img5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        usarGlide(this, R.drawable.fondo_principal_final, fondo_ipae);
        boolean cerrar_aplicacion = getIntent().getBooleanExtra(Constantes.KEY_MATAR_APLICACION, false);
        if (cerrar_aplicacion) {
            onBackPressed();
        }
    }

    @OnClick(R.id.btnEmpezar)
    public void empezar() {
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

    @OnClick(R.id.picos)
    public void girarPicos() {
        ImageView[] listaImageView = {img1, img2, img3, img4, img5};
        for (ImageView unidadImageView : listaImageView) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(unidadImageView, "rotation", 0f, 1080f);
            animator.setDuration(3000);
            animator.setInterpolator(new FastOutLinearInInterpolator());
            animator.start();
        }
    }

}
