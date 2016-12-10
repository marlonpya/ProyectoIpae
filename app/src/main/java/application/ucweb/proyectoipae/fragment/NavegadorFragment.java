package application.ucweb.proyectoipae.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import application.ucweb.proyectoipae.R;
import application.ucweb.proyectoipae.adapter.NavegadorAdapter;
import application.ucweb.proyectoipae.aplicacion.BaseActivity;
import application.ucweb.proyectoipae.model.ItemNavegador;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavegadorFragment extends Fragment {
    @BindView(R.id.listaRecyclerView) RecyclerView listaRecyclerView;
    @BindView(R.id.ivIpae_panel) ImageView icono_ipae_panel;
    @BindDrawable(R.drawable.ic_navigation_menu_blue) Drawable drwIconoNavegador;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavegadorAdapter adapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;

    public NavegadorFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navegador, container, false);
        ButterKnife.bind(this,view);

        BaseActivity.usarGlide(getActivity(), R.drawable.icono_ipae_panel, icono_ipae_panel);
        adapter = new NavegadorAdapter(getItemsNavegador(), getActivity());
        listaRecyclerView.setAdapter(adapter);
        listaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listaRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), listaRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view,position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) { }

        }));

        return view;
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) { return true; }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child,recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(),e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child,rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
    }

    public void setDrawerListener(FragmentDrawerListener listener) { this.drawerListener = listener; }

    public List<ItemNavegador> getItemsNavegador() {
        List<ItemNavegador> lista = new ArrayList<>();
        lista.add(new ItemNavegador("Acerca de IPAE", R.drawable.icono_panel_acerca));
        lista.add(new ItemNavegador("Sugerencias", R.drawable.icono_panel_sugerencias));
        lista.add(new ItemNavegador("Cerrar", R.drawable.icono_panel_cerrar));
        return lista;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar){
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerToggle.setHomeAsUpIndicator(drwIconoNavegador);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

}
