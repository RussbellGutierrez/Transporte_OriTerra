package com.example.russbell.transporte_oriterra.Threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Class.Class_planilla;
import com.example.russbell.transporte_oriterra.Class.NClass_carga;
import com.example.russbell.transporte_oriterra.Interfaces.NotifyingThreadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Russbell on 14/11/2017.
 */

public class ThreadCarga extends AsyncTask<Boolean,String,Boolean> {

    private String nropla;
    private String fechanropla;
    private Context context;
    private Class_planilla planilla;
    private ProgressDialog pd;
    private JSONObject response;
    private NotifyingThreadListener listener;
    private static final String TAG="ThreadCarga";
    public static final ArrayList<NClass_carga> tmpCarga=new ArrayList<>();
    public static final ArrayList<Class_planilla> tmpPlanilla=new ArrayList<>();

    public ThreadCarga(JSONObject response, NotifyingThreadListener listener, Context context){
        this.response=response;
        this.listener=listener;
        this.context=context;
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.setMessage("Download carga...");
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Boolean... booleans) {
        boolean resultado;
        tmpCarga.clear();
        tmpPlanilla.clear();
        try{
            JSONArray jacarga=new JSONArray(response.getString("data"));

            for(int i=0;i<jacarga.length();i++){
                JSONObject jcar = jacarga.getJSONObject(i);
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
                tmpCarga.add(carga);
                nropla=jcar.getString("codprov")+"-"+jcar.getString("seriepla")+"-"+jcar.getString("tipopla")+"-"+jcar.getString("nropla");
                fechanropla=jcar.getString("fecha");
                planilla=new Class_planilla(nropla,fechanropla);
                //Log.wtf(TAG,"Iteracion "+i);
            }
            //===========PEQUEÑA FUNCION PARA AGREGAR PLANILLAS DIFERENTES=============
            if (!tmpPlanilla.contains(planilla))
            {
                tmpPlanilla.add(planilla);
            }
            //===========PEQUEÑA FUNCION PARA AGREGAR PLANILLAS DIFERENTES=============
            resultado=true;
        }catch (JSONException e) {
            e.printStackTrace();
            resultado=false;
        }
        return resultado;
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean){
            pd.dismiss();
            listener.AsyncCompleteListener(1);
        }else {
            pd.dismiss();
            Toast.makeText(context,"Error al guardar datos de carga",Toast.LENGTH_SHORT).show();
        }
    }
}
