package application.ucweb.proyectoipae.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collections;
import java.util.List;

import application.ucweb.proyectoipae.R;
import application.ucweb.proyectoipae.model.ItemNavegador;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 02/08/2016.
 */
public class NavegadorAdapter extends RecyclerView.Adapter<NavegadorAdapter.MyViewHolder> {
    List<ItemNavegador> lista = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavegadorAdapter(List<ItemNavegador> lista, Context context) {
        this.lista = lista;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_navegador,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemNavegador item = lista.get(position);
        holder.itemNombre.setText(item.getTitulo());
        Glide.with(this.context)
                .load(item.getIcono())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .into(holder.itemImagen);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_nav) TextView itemNombre;
        @BindView(R.id.iv_item_nav) ImageView itemImagen;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
