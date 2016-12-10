package application.ucweb.proyectoipae.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 24/08/2016.
 */
public class Test extends RealmObject {
    public static final String T_ID = "id_test";
    public static final String T_ID_SERVIDOR = "id_test_servidor";
    public static final String T_CAT = "id_categoria";
    public static final String T_ESTADO = "estado_token";
    public static final String T_TERMINADO  = "";

    //2016-08-18 17:03:55
    public static final String TAG = Test.class.getSimpleName();

    @PrimaryKey
    private long id_test;
    private String fecha_test;
    private int id_categoria;
    private String token_test;
    private boolean estado_token;
    private int id_test_servidor;

    public static int getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Test.class).max(T_ID);
        return number == null ? 0 : number.intValue() + 1;
    }

    public static Test getTest(int sub_categoria) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Test.class).equalTo(T_ID, sub_categoria).findFirst();
    }

    public static void crearTests() {
        Log.d(TAG,"crearTests();");
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Test test1 = realm.createObject(Test.class);
        test1.setId_test(getLastId());
        test1.setFecha_test("");
        test1.setId_categoria(1);
        test1.setToken_test("");
        test1.setEstado_token(false);
        test1.setId_test_servidor(0);
        realm.copyToRealmOrUpdate(test1);
        realm.commitTransaction();

        realm.beginTransaction();
        Test test2 = realm.createObject(Test.class);
        test2.setId_test(getLastId());
        test2.setFecha_test("");
        test2.setId_categoria(2);
        test2.setToken_test("");
        test2.setEstado_token(false);
        test2.setId_test_servidor(0);
        realm.copyToRealmOrUpdate(test2);
        realm.commitTransaction();

        realm.close();

        Log.d(TAG, "crearTest/test1_"+test1.toString());
        Log.d(TAG, "crearTest/test2_"+test2.toString());
    }

    public static void actualizarTokens(String token) {
        Log.d(TAG,"actualizarTokens();");
        Realm realm = Realm.getDefaultInstance();

        Test test01 = getTest(1);
        Test test02 = getTest(2);

        realm.beginTransaction();
        Test test1 = new Test();
        test1.setId_test(test01.getId_test());
        test1.setFecha_test(test01.getFecha_test());
        test1.setId_categoria(test01.getId_categoria());
        test1.setEstado_token(test01.isEstado_token());
        test1.setToken_test(token);
        test1.setId_test_servidor(test01.getId_test_servidor());
        realm.copyToRealmOrUpdate(test1);
        realm.commitTransaction();

        realm.beginTransaction();
        Test test2 = new Test();
        test2.setId_test(test02.getId_test());
        test2.setFecha_test(test02.getFecha_test());
        test2.setId_categoria(test02.getId_categoria());
        test2.setEstado_token(test02.isEstado_token());
        test2.setToken_test(token);
        test2.setId_test_servidor(test02.getId_test_servidor());
        realm.copyToRealmOrUpdate(test2);
        realm.commitTransaction();

        realm.close();
        Log.d(TAG, "actualizarTokens/test1_"+test1.toString());
        Log.d(TAG, "actualizarTokens/test2_"+test2.toString());
    }

    public static void activarTests(int id_test_int, int id_servidor) {
        Log.d(TAG,"activarTests/id_test_int_"+String.valueOf(id_test_int));
        Log.d(TAG,"activarTests/id_servidor_"+String.valueOf(id_servidor));
        Realm realm = Realm.getDefaultInstance();
        Test test01 = getTest(1);
        Test test02 = getTest(2);
        switch (id_test_int) {
            case 1 :
                realm.beginTransaction();
                Test test1 = new Test();
                test1.setId_test(test01.getId_test());
                test1.setEstado_token(true);
                test1.setId_test_servidor(id_servidor);
                test1.setId_categoria(test01.getId_categoria());
                test1.setFecha_test(test01.getFecha_test());
                test1.setToken_test(test01.getToken_test());
                realm.copyToRealmOrUpdate(test1);
                realm.commitTransaction();
                realm.close();
                Log.d(TAG, "activarTests/test1"+test1.toString());
                break;

            case 2:
                realm.beginTransaction();
                Test test2 = new Test();
                test2.setId_test(test02.getId_test());
                test2.setEstado_token(true);
                test2.setId_test_servidor(id_servidor);
                test2.setId_categoria(test02.getId_categoria());
                test2.setFecha_test(test02.getFecha_test());
                test2.setToken_test(test02.getToken_test());
                realm.copyToRealmOrUpdate(test2);
                realm.commitTransaction();
                realm.close();
                Log.d(TAG, "activarTests/test2"+test2.toString());
                break;
        }
    }

    public static void desactivarTest(int id) {
        Log.d(TAG, "desactivarTest/_"+String.valueOf(id));
        Test test01 = getTest(1);
        Test test02 = getTest(2);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        switch (id) {
            case 1:
                Test test = new Test();
                test.setId_test(test01.getId_test());
                test.setFecha_test("");
                test.setId_categoria(1);
                test.setToken_test(test01.getToken_test());
                test.setEstado_token(false);
                test.setId_test_servidor(0);
                realm.copyToRealmOrUpdate(test);
                realm.commitTransaction();
                realm.close();
                Log.d(TAG,"desactivarTest/test1"+test.toString());
                break;
            case 2:
                Test test2 = new Test();
                test2.setId_test(test02.getId_test());
                test2.setFecha_test("");
                test2.setId_categoria(2);
                test2.setToken_test(test02.getToken_test());
                test2.setEstado_token(false);
                test2.setId_test_servidor(0);
                realm.copyToRealmOrUpdate(test2);
                realm.commitTransaction();
                realm.close();
                Log.d(TAG,"desactivarTest/test2"+test2.toString());
                break;
        }
    }

    public static void reiniciarToken(int id_test) {
        Realm realm = Realm.getDefaultInstance();
        Test test1 = getTest(1);
        Test test2 = getTest(2);
        if (id_test == 1) {
            realm.beginTransaction();
            Test test = new Test();
            test.setId_test(test1.getId_test());
            test.setFecha_test("");
            test.setId_categoria(0);
            test.setToken_test("");
            test.setEstado_token(true);
            test.setId_test_servidor(0);
            realm.copyToRealmOrUpdate(test1);
            realm.commitTransaction();
            Log.d(TAG, test.toString());
        } else if (id_test == 2) {
            realm.beginTransaction();
            Test test = new Test();
            test.setId_test(test2.getId_test());
            test.setFecha_test("");
            test.setId_categoria(1);
            test.setToken_test("");
            test.setEstado_token(true);
            test.setId_test_servidor(0);
            realm.copyToRealmOrUpdate(test2);
            realm.commitTransaction();
        }
    }

    public long getId_test() {
        return id_test;
    }

    public void setId_test(long id_test) {
        this.id_test = id_test;
    }

    public String getFecha_test() {
        return fecha_test;
    }

    public void setFecha_test(String fecha_test) {
        this.fecha_test = fecha_test;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getToken_test() {
        return token_test;
    }

    public void setToken_test(String token_test) {
        this.token_test = token_test;
    }

    public boolean isEstado_token() {
        return estado_token;
    }

    public void setEstado_token(boolean estado_token) {
        this.estado_token = estado_token;
    }

    public int getId_test_servidor() {
        return id_test_servidor;
    }

    public void setId_test_servidor(int id_test_servidor) {
        this.id_test_servidor = id_test_servidor;
    }
}
