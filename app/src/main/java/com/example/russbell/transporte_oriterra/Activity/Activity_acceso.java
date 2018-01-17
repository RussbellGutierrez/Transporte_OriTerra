package com.example.russbell.transporte_oriterra.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Class.UrlConexion;
import com.example.russbell.transporte_oriterra.Dialogs.DlgBD;
import com.example.russbell.transporte_oriterra.Dialogs.DlgCheckbd;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.NotifyingThreadListener;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_helper;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;
import com.example.russbell.transporte_oriterra.Threads.ThreadUnique;
import com.example.russbell.transporte_oriterra.Volley.Peticiones;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Activity_acceso extends AppCompatActivity
        implements View.OnClickListener,
        TextView.OnEditorActionListener,NotifyingThreadListener{

    private SQL_helper helper;
    private int threadcontador=0;
    private int contador=0;
    private JSONObject responseProd;
    private JSONObject responseCli;
    private JSONObject responseDet;
    private String codigo="";
    private String contra="";
    private String basedatos="";
    private ProgressDialog pd;
    private static String TAG="Debug_ActAcceso";
    private SharedPreferences.Editor loginPrefsEditor;

    @BindView(R.id.fab_cloud) FloatingActionButton fab_cloud;
    @BindView(R.id.img_setting) ImageView img_setting;
    @BindView(R.id.logo_bd) ImageView logo_bd;
    @BindView(R.id.txt_basedatos) TextView txt_basedatos;
    @BindView(R.id.btn_verificar) Button btn_verificar;
    @BindView(R.id.edit_codigo) EditText edit_codigo;
    @BindView(R.id.edit_contrasena) EditText edit_contrasena;
    @BindView(R.id.check_recordar) CheckBox check_recordar;

    public Activity_acceso() {
    }

    public Socket oSocket;
    //Socket terranorte
    {
        UrlConexion url = new UrlConexion();
        try{
            oSocket = IO.socket(url.urlSocket("oriunda"));
            Log.w(TAG, url.urlSocket("oriunda"));
        }catch(URISyntaxException e){}
    }
    //Socket oriunda
    public Socket tSocket;
    {
        UrlConexion url = new UrlConexion();
        try{
            tSocket = IO.socket(url.urlSocket("terranorte"));
            Log.w(TAG, url.urlSocket("terranorte"));
        }catch(URISyntaxException e){}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        helper=new SQL_helper(this);
        SQL_sentences sql = new SQL_sentences(this);

        if (sql.checkBD()){
            new DlgCheckbd().show(getFragmentManager(),"Dialog_Truncate");
        }

        //this.deleteDatabase("BDTransportista");
        permisosMarshmallow(this);
        ButterKnife.bind(this);

        oSocket.connect();
        tSocket.connect();

        if (!basedatos.equals("")){
            txt_basedatos.setText(basedatos);
        }

        //TODO: ProgressDialog programatically
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        changeLogo();
        edit_contrasena.setOnEditorActionListener(this);
        btn_verificar.setOnClickListener(this);
        img_setting.setOnClickListener(this);
        fab_cloud.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean connected=isNetworkConnected(this);
        if (v==btn_verificar){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_codigo.getWindowToken(), 0);

            codigo = edit_codigo.getText().toString();
            contra=edit_contrasena.getText().toString();
            basedatos=txt_basedatos.getText().toString();

            Holder_data.newDataCamion(basedatos,codigo,contra);

            //cuando esta checked, guarda los datos en loginPrefsEditor
            if (check_recordar.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("codigo", codigo);
                loginPrefsEditor.apply();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
            iniciarLogin();
        }else if (v==img_setting){
            new DlgBD().show(getFragmentManager(),"Dialog_BD");
        }else if (v==fab_cloud){
            if(connected){
                switch (basedatos){
                    case "terranorte":
                        uploadData(tSocket);
                        break;
                    case "oriunda":
                        uploadData(oSocket);
                        break;
                }
            }else {
                Toast.makeText(this,"No se comunico con el servidor.\nRevise su conexión a internet",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId==EditorInfo.IME_ACTION_DONE){

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_codigo.getWindowToken(), 0);

            codigo = edit_codigo.getText().toString();
            contra=edit_contrasena.getText().toString();
            basedatos=txt_basedatos.getText().toString();

            Holder_data.newDataCamion(basedatos,codigo,contra);

            //cuando esta checked, guarda los datos en loginPrefsEditor
            if (check_recordar.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("codigo", codigo);
                loginPrefsEditor.apply();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
            iniciarLogin();
        }
        return false;
    }

    private void changeLogo(){
        SharedPreferences loginPreferences;
        boolean saveLogin;

        if(txt_basedatos.getText().toString().equals("terranorte")){
            logo_bd.setImageResource(R.drawable.terra_logo);
        }else if (txt_basedatos.getText().toString().equals("oriunda")){
            logo_bd.setImageResource(R.drawable.ori_logo);
        }
        //TODO: Recordar datos con el checkbox
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.apply();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            edit_codigo.setText(loginPreferences.getString("codigo", ""));
            check_recordar.setChecked(true);
        }

    }

    private void iniciarLogin(){
        boolean connected=isNetworkConnected(this);
        Peticiones peti = new Peticiones(basedatos, this,this);
        if (connected){

            HashMap<String,String> parametros=new HashMap<String, String>();
            parametros.put("usuario", Holder_data.codcamion);
            parametros.put("clave",Holder_data.contrasena);
            peti.userLogin(parametros,pd);
        }else {
            peti.verificandoCarga(false,pd,codigo);
        }
    }

    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return !(info == null || !info.isConnected() || !info.isAvailable());
    }

    private void permisosMarshmallow(final Activity activity){
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    public void ThreadCompleteListener(String volleyName, JSONObject response) {

        switch (volleyName){
            case "producto":
                responseProd=response;
                contador+=1;
                break;
            case "cliente":
                responseCli=response;
                contador+=1;
                break;
            case "detalle":
                responseDet=response;
                contador+=1;
                break;
        }
        if (contador==3){
            ThreadUnique unique=new ThreadUnique(responseProd,responseCli,responseDet,this,this);
            unique.execute();
        }
    }

    @Override
    public void AsyncCompleteListener(int count) {
        threadcontador+=count;
        if (threadcontador==3){
            Log.d(TAG,"Reach activity!!!");
            Intent i=new Intent(this,Activity_planilla.class);
            startActivity(i);
            finish();
        }
    }

    private void uploadData(Socket socket){
        SQLiteDatabase db=helper.getReadableDatabase();
        if (db!=null){
            Cursor cursor=db.rawQuery("select codprov, tipopla,seriepla,nropla,cliente,estado " +
                    "from documentos where estado <> '0'",null);
            if (cursor.moveToFirst()){
                do{
                    JSONObject sObject=new JSONObject();
                    try {
                        int estado=0;
                        switch (cursor.getString(5)){
                            case "Atendido":
                                estado=1;
                                break;
                            case "Rechazado":
                                estado=2;
                                break;
                        }

                        sObject.put("codprov",cursor.getString(0));
                        sObject.put("tipopla",cursor.getString(1));
                        sObject.put("seriepla",cursor.getString(2));
                        sObject.put("nropla",cursor.getString(3));
                        sObject.put("cliente",cursor.getString(4));
                        sObject.put("resultado",estado);

                        socket.emit("resultado", sObject);
                        Log.d(TAG,"Socket to upload emit!!");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }while (cursor.moveToNext());
            }
            Toast.makeText(this,"Datos subidos al servidor",Toast.LENGTH_SHORT).show();
        }else {
            Log.i(TAG,"Database is null :c");
        }
    }
}
