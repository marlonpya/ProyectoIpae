package application.ucweb.proyectoipae.aplicacion;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ucweb02 on 03/08/2016.
 */
public class Configuracion extends Application {

    private static final String TAG = "Configuracion";
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public static Configuracion mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        RealmConfiguration configuracionR = new RealmConfiguration
                .Builder(getApplicationContext())
                .name("proyectoipae.db")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                    }
                })
                .build();
        Realm.setDefaultConfiguration(configuracionR);
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Slimamif.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized Configuracion getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

}
