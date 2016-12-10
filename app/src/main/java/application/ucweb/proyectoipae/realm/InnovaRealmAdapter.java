package application.ucweb.proyectoipae.realm;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import application.ucweb.proyectoipae.CuestionarioInnovaActivity;
import application.ucweb.proyectoipae.R;
import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.model.Innova;
import application.ucweb.proyectoipae.model.PreguntaInnova;
import application.ucweb.proyectoipae.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 08/08/2016.
 */
public class InnovaRealmAdapter extends RealmBasedRecyclerViewAdapter<Innova, InnovaRealmAdapter.ViewHolder> {

    public InnovaRealmAdapter(
            Context context,
            RealmResults<Innova> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public InnovaRealmAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_acelera_cardview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(InnovaRealmAdapter.ViewHolder viewHolder, int i) {
        final Innova item = realmResults.get(i);
        BaseActivity.usarGlide(getContext(), item.getIcono_inn(), viewHolder.imagen);
        //viewHolder.imagen.setImageUrl(item.getIcono_inn(), imageLoader);
        viewHolder.imagen.setColorFilter(Color.parseColor("#f6aa2e"));
        viewHolder.texto.setText(item.getCategoria_inn());
        viewHolder.texto.setTextColor(Color.parseColor("#f6aa2e"));
        viewHolder.boton.setClickable(true);
        viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CuestionarioInnovaActivity.class);
                intent.putExtra(Constantes.KEY_ID_INNOVA, item.getId_inn());
                intent.putExtra(Constantes.KEY_ACTIVAR_PREFERENCIA_INNOVA, true);
                intent.putExtra(Constantes.KEY_POSICION_INNOVA, PreguntaInnova.getUltimoIdPorPreguntaRespondida());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext()).toBundle());
                } else {
                    getContext().startActivity(intent);
                }
            }
        });
        //Inhabilitamos los botones
        if (!item.isEstado_inn()) {
            viewHolder.boton.setClickable(false);
            viewHolder.boton.setOnClickListener(null);
        }
        //Cambiamos de color
        if (!item.isEstado_orden_inn()) {
            viewHolder.imagen.setColorFilter(Color.parseColor("#b5b1b0"));
            viewHolder.texto.setTextColor(Color.parseColor("#b5b1b0"));
        }
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.iv_item_acelera) ImageView imagen;
        @BindView(R.id.tv_item_acelera) TextView texto;
        @BindView(R.id.btnRecycler) LinearLayout boton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
