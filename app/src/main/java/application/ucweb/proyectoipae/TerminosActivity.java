package application.ucweb.proyectoipae;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.model.Termino;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

public class TerminosActivity extends BaseActivity {
    @BindView(R.id.iv_fondo_at) ImageView fondo;
    @BindView(R.id.toolbar_principal) Toolbar toobar;
    @BindView(R.id.ivToolbar_Icono) ImageView icono_toolbar;
    @BindView(R.id.tvTerminos) TextView tvTerminos;
    @BindDrawable(R.drawable.ic_navigation_menu_blue) Drawable drwIconoIzquierdo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos);
        setToolbarSon(toobar, this, icono_toolbar);
        usarGlide(this, R.drawable.fondo_principal_final, fondo);
        tvTerminos.setText(Html.fromHtml(Termino.traerUltimoTexto()));
    }

    @OnClick(R.id.btnAceptar)
    public void aceptarTerminos(){
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){ onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }
}
