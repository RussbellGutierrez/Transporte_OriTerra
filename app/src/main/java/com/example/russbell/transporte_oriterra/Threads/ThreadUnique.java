package com.example.russbell.transporte_oriterra.Threads;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Class.NClass_carga;
import com.example.russbell.transporte_oriterra.Class.NClass_detalle;
import com.example.russbell.transporte_oriterra.Class.NClass_documento;
import com.example.russbell.transporte_oriterra.Interfaces.NotifyingThreadListener;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Russbell on 18/08/2017.
 */

public class ThreadUnique extends AsyncTask<Boolean,String,Boolean> {

    private JSONObject respProd;
    private JSONObject respCli;
    private JSONObject respDet;
    private Context context;
    private int mayor=0;
    private SQL_sentences sql;
    private boolean resultado=true;
    private NotifyingThreadListener listener;

    private static final String TAG="Thread Unique";
    public static final ArrayList<NClass_documento> tmpDocumento=new ArrayList<>();
    public static final ArrayList<NClass_detalle> tmpDetalle=new ArrayList<>();
    public static final ArrayList<NClass_carga> tmpCarga=new ArrayList<>();

    public ThreadUnique(JSONObject respProd, JSONObject respCli, JSONObject respDet, Context context,
                        NotifyingThreadListener listener){

        this.respCli=respCli;
        this.respDet=respDet;
        this.respProd=respProd;
        this.context=context;
        this.listener=listener;
        sql=new SQL_sentences(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context,"Almacenando datos...",Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("LogNotTimber")
    @Override
    protected Boolean doInBackground(Boolean... params) {
        try {
            //TODO: Revisar las conversiones de los datos JSONObject a JSONArray
            JSONArray jadocumentos=new JSONArray(respCli.getString("data"));
            JSONArray jacarga=new JSONArray(respProd.getString("data"));
            JSONArray jadetalle=new JSONArray(respDet.getString("data"));
            if (jadocumentos.length()>mayor){
                mayor=jadocumentos.length();
            }
            if (jacarga.length()>mayor){
                mayor=jacarga.length();
            }
            if (jadetalle.length()>mayor){
                mayor=jadetalle.length();
            }
            Log.i(TAG,"tamaño de mayor "+mayor);
            for (int i=0;i<mayor;i++){
                JSONObject jcar = new JSONObject();
                JSONObject jdoc = new JSONObject();
                JSONObject jdet = new JSONObject();
                if (i<jacarga.length()){
                    jcar=jacarga.getJSONObject(i);
                }
                if (i<jadocumentos.length()){
                    jdoc=jadocumentos.getJSONObject(i);
                }
                if (i<jadetalle.length()){
                    jdet=jadetalle.getJSONObject(i);
                }
                if (jdoc.length()!=0){
                    NClass_documento documento=new NClass_documento(jdoc.getString("sucursal"),jdoc.getString("esquema"),
                            jdoc.getString("codprov"),jdoc.getString("tipopla"),jdoc.getString("seriepla"),jdoc.getString("nropla"),
                            jdoc.getString("fletero"),jdoc.getString("cliente"),jdoc.getString("nomcli"),jdoc.getString("domicli"),
                            jdoc.getString("telefono"),jdoc.getString("negocio"),jdoc.getString("XCoord"),jdoc.getString("YCoord"),
                            jdoc.getString("documento"),jdoc.getString("nrodoc"),jdoc.getString("total"),jdoc.getString("estado"),jdoc.getString("fecha"),jdoc.getString("ruta"),jdoc.getString("tipopago"));
                    //Log.wtf(TAG,"Tipopago es "+jdoc.getString("tipopago"));
                    resultado=sql.saveDocumento(documento);
                    //Log.wtf(TAG,"documento iteracion "+i);
                }

                if (jdet.length()!=0){
                    NClass_detalle detalle=new NClass_detalle(jdet.getString("codprov"),jdet.getString("tipopla"),jdet.getString("seriepla"),
                            jdet.getString("nropla"),jdet.getString("fletero"),jdet.getString("idcliente"),jdet.getString("iddocumento"),
                            jdet.getString("serie"),jdet.getString("nrodoc"),jdet.getString("idlinea"),jdet.getString("codart"),
                            jdet.getString("cant"),jdet.getString("resto"),jdet.getString("unidades"),jdet.getString("precio"),
                            jdet.getString("impuesto"),jdet.getString("bruto"),jdet.getString("descuento"),jdet.getString("linea"),jdet.getString("fecha"));
                    resultado=sql.saveDetalle(detalle);
                    //Log.wtf(TAG,"detalle iteracion "+i);
                }

                if (jcar.length()!=0){

                    String articulo=jcar.getString("articulo");
                    JSONObject jart = new JSONObject(articulo);
                    NClass_carga carga=new NClass_carga(jcar.getString("codprov"),jcar.getString("seriepla"),jcar.getString("tipopla"),
                            jcar.getString("nropla"),jcar.getString("fletero"),
                            //========ARTICULO========
                            jart.getString("codart"),
                            jart.getString("descrip"),
                            jart.getString("resto"),
                            jart.getString("almacen"),
                            jart.getString("codbarra"),
                            jart.getString("codbarrauni"),
                            jart.getString("proveedor"),
                            jart.getString("linea"),
                            jart.getString("generico")
                            //========ARTICULO========
                            ,jcar.getString("cant"),jcar.getString("resto"),jcar.getString("total"),"Falta",jcar.getString("fecha"));
                    resultado=sql.saveCarga(carga);
                    //Log.wtf(TAG,"producto iteracion "+i);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean){
            Toast.makeText(context,"Datos almacenados correctamente",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"Error al guardar datos",Toast.LENGTH_SHORT).show();
        }
    }
}
