package application.ucweb.proyectoipae.fragment;


import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import application.ucweb.proyectoipae.AceleraActivity;
import application.ucweb.proyectoipae.InnovaActivity;
import application.ucweb.proyectoipae.PrincipalActivity;
import application.ucweb.proyectoipae.R;
import application.ucweb.proyectoipae.TerminosActivity;
import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.model.Acelera;
import application.ucweb.proyectoipae.model.Innova;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    @BindView(R.id.layout_f_menu) LinearLayout layout;
    @BindView(R.id.btnAcelera) ImageView imagenAcelera;
    @BindView(R.id.btnInnova) ImageView imagenInnova;
    private ProgressDialog pDialog;
    private Unbinder unbinder;
    public static final String TAG = MenuFragment.class.getSimpleName();

    public MenuFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        iniciarLayout();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Sincronizando..");
        pDialog.setCancelable(false);
        return view;
    }

    @OnClick(R.id.btnLeerTerminos)
    public void leerTerminos() {
        startActivity(new Intent(getActivity(), TerminosActivity.class));
    }

    @OnClick(R.id.btnAcelera)
    public void irAcelera() {
            Intent intent = new Intent(getActivity(), AceleraActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            } else {
                startActivity(intent);
            }

    }

    @OnClick(R.id.btnInnova)
    public void irInnova() {
            Intent intent = new Intent(getActivity(), InnovaActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            } else {
                startActivity(intent);
            }

    }

    /*public static void mostrarMensajeSincronizar(final ProgressDialog pDialog, final Context context, final View layout) {
        new AlertDialog.Builder(context)
                .setTitle("IPAE")
                .setMessage("Acepta sincronizar la aplicación IPAE ?")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrincipalActivity.consumirTodo(pDialog, layout, context);
                    }
                })
                .setNegativeButton("NO", null)
                .create()
                .show();
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void iniciarLayout() {
        BaseActivity.usarGlide(getActivity(), R.drawable.tarjeta_acelera, imagenAcelera);
        BaseActivity.usarGlide(getActivity(), R.drawable.tarjeta_innova, imagenInnova);
    }

}
