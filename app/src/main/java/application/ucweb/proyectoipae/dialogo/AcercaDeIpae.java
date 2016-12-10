package application.ucweb.proyectoipae.dialogo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import application.ucweb.proyectoipae.R;

/**
 * Created by ucweb02 on 25/08/2016.
 */
public class AcercaDeIpae extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialogo_acerca_de_ipae, null);
        Button button = (Button) view.findViewById(R.id.btnCerrar);
        builder.setView(view);
        button.setBackground(getResources().getDrawable(R.drawable.btn_azul));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }
}
