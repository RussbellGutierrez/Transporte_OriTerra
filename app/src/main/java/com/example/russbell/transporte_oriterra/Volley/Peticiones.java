package com.example.russbell.transporte_oriterra.Volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.russbell.transporte_oriterra.Activity.Activity_planilla;
import com.example.russbell.transporte_oriterra.Class.UrlConexion;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.NotifyingThreadListener;
import com.example.russbell.transporte_oriterra.SQL.SQL_helper;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;
import com.example.russbell.transporte_oriterra.Threads.ThreadCarga;
import com.example.russbell.transporte_oriterra.Threads.ThreadDetalle;
import com.example.russbell.transporte_oriterra.Threads.ThreadDocumento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Rusbell Gutierrez on 11/08/2017.
 */

public class Peticiones {
    private static String TAG="Peticiones";
    private UrlConexion multiurl;
    private SQL_sentences sql;
    private SQL_helper helper;
    private String basedatos;
    private Context context;
    private String url= "";
    private NotifyingThreadListener listener;

    public Peticiones(String basedatos, Context context,NotifyingThreadListener listener){
        this.basedatos=basedatos;
        this.context=context;
        this.listener=listener;
        multiurl=new UrlConexion();
        multiurl.setIP(basedatos);
        helper=new SQL_helper(context);
        sql=new SQL_sentences(context);
    }

    public Peticiones(String basedatos, Context context){
        this.basedatos=basedatos;
        this.context=context;
        multiurl=new UrlConexion();
        multiurl.setIP(basedatos);
        helper=new SQL_helper(context);
        sql=new SQL_sentences(context);
    }

    public Peticiones(Context context){
        this.context=context;
        multiurl=new UrlConexion();
        multiurl.setIP(Holder_data.basedatos);
        helper=new SQL_helper(context);
        sql=new SQL_sentences(context);
    }

    public Peticiones(){
        multiurl=new UrlConexion();
        multiurl.setIP(Holder_data.basedatos);
    }

    public void userLogin(HashMap<String,String> parametros,
                          final ProgressDialog pd){

        url=multiurl.urlMultiple(basedatos,"transporte");
        Log.w(TAG,url);
        pd.show();
        pd.setMessage("Conectando...");
        JsonObjectRequest datos= new JsonObjectRequest(Request.Method.POST, url, new JSONObject(parametros),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        pd.dismiss();
                        Log.i(TAG,response.toString());
                        try{
                            JSONObject jo=response.getJSONObject("usuario");
                            String cod = jo.getString("codigo");
                            String contra = jo.getString("codigo");
                            String nom = jo.getString("descrip");
                            String tipousua=jo.getString("tipousua");
                            String token=response.getString("token");

                            //Class_camion camion=new Class_camion(Integer.parseInt(cod),Integer.parseInt(contra),nom);

                            //revisar si se instancio el objeto cuando salte el errror java.lang.null.point
                            //sql.guardarCamion(camion);
                            /*if (contra.equals()){
                                pd.dismiss();
                                verificandoCarga(true,pd,codigo);
                                Log.i(TAG,"DATOS DE CAMION cod: "+cod+" pass: "+contra);
                            }else {
                                pd.dismiss();
                                Toast.makeText(context,"Verifique su contraseña",Toast.LENGTH_SHORT).show();
                            }*/

                            switch (tipousua) {
                                case "1":{
                                    Toast.makeText(context,"Administrador no puede usar la aplicación",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                case "2": {
                                    Toast.makeText(context,"Supervisor de ventas no puede usar la aplicación",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                case "3": {
                                    Toast.makeText(context,"Vendedor no puede usar la aplicación",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                case "4":{
                                    Toast.makeText(context,"Supervisor de transporte no puede usar la aplicación",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                case "5":{
                                    verificandoCarga(true,pd,cod);
                                    /*Intent intent = new Intent(context, Activity_planilla.class);
                                    context.startActivity(intent);
                                    ((Activity) context).finish();*/
                                    break;
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            pd.dismiss();
                            Toast.makeText(context,"Código no encontrado",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        pd.dismiss();
                        errorVolley(error);
                        Toast.makeText(context,"No existe en "+Holder_data.basedatos,Toast.LENGTH_SHORT).show();
                    }
                });
        DatosSingleton.getInstance(context).addtoRequestQueue(datos);
    }

    public void verificandoCarga(boolean conexion,ProgressDialog pd,String codigo){
        pd.show();
        pd.setMessage("Verificando carga.........");
        int i=sql.existeRegistro(codigo);
        if (conexion){
            if (i>0){
                pd.dismiss();
                Log.wtf(TAG,"Codigo: "+codigo);
                Intent intent = new Intent(context,Activity_planilla.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }else{
                pd.dismiss();
                obtainDataCarga(codigo);
                obtainDataDocumento(codigo);
                obtainDataDetalle(codigo);
            }
        }else {
            if (i>0){
                pd.dismiss();
                Log.wtf(TAG,"Codigo: "+codigo);
                Intent intent = new Intent(context, Activity_planilla.class);
                context.startActivity(intent);
                ((Activity)context).finish();
            }else{
                pd.dismiss();
                Log.wtf(TAG,"Codigo: "+codigo);
                Toast.makeText(context,"Sin datos almacenados",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void obtainDataCarga(String codigo){

        HashMap<String,String> parametros=new HashMap<String, String>();
        parametros.put("fletero", Holder_data.codcamion);

        url=multiurl.urlMultiple(basedatos,"carga");
        JsonObjectRequest productoData= new JsonObjectRequest(Request.Method.POST, url, new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,response.toString());
                try {
                    if (response.getString("data").equals("[]")){
                        Toast.makeText(context, "No tiene carga asignada", Toast.LENGTH_SHORT).show();
                    }else {
                        ThreadCarga thread=new ThreadCarga(response,listener,context);
                        thread.execute();
                        listener.ThreadCompleteListener("producto",response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error.networkResponse);
                Toast.makeText(context,"No se pudo obtener datos",Toast.LENGTH_SHORT).show();
            }
        });
        DatosSingleton.getInstance(context).addtoRequestQueue(productoData);
    }

    public void obtainDataDocumento(String codigo){
        HashMap<String,String> parametros=new HashMap<String, String>();
        parametros.put("fletero", Holder_data.codcamion);

        url=multiurl.urlMultiple(basedatos,"documento");
        JsonObjectRequest clienteData= new JsonObjectRequest(Request.Method.POST, url, new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,response.toString());
                try {
                    if (response.getString("data").equals("[]")){
                        Toast.makeText(context, "No tiene documentos para la carga", Toast.LENGTH_SHORT).show();
                    }else {
                        ThreadDocumento thread=new ThreadDocumento(response,listener,context);
                        thread.execute();
                        listener.ThreadCompleteListener("cliente",response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error.networkResponse);
                Toast.makeText(context,"No se pudo obtener datos",Toast.LENGTH_SHORT).show();
            }
        });
        DatosSingleton.getInstance(context).addtoRequestQueue(clienteData);
    }

    public void obtainDataDetalle(String codigo){
        HashMap<String,String> parametros=new HashMap<String, String>();
        parametros.put("fletero", Holder_data.codcamion);

        url=multiurl.urlMultiple(basedatos,"detalle");
        JsonObjectRequest detalleData= new JsonObjectRequest(Request.Method.POST, url, new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG,response.toString());
                try {
                    if (response.getString("data").equals("[]")){
                        Toast.makeText(context, "No existe detalle de la carga", Toast.LENGTH_SHORT).show();
                    }else {
                        ThreadDetalle thread=new ThreadDetalle(response,listener,context);
                        thread.execute();
                        listener.ThreadCompleteListener("detalle",response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error.networkResponse);
                Toast.makeText(context,"No se pudo obtener datos",Toast.LENGTH_SHORT).show();
            }
        });
        DatosSingleton.getInstance(context).addtoRequestQueue(detalleData);
    }

    public String connectionPath (Context context, String direccion) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(direccion);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void errorVolley(VolleyError error){
        if (error == null || error.networkResponse == null) {
            Log.d(TAG,"No error");
        }else{
            if (error.networkResponse.statusCode==500){
                Log.i(TAG,""+error.networkResponse.statusCode);
                Toast.makeText(context,"Error de conexión",Toast.LENGTH_SHORT).show();
            }else {
                Log.i(TAG,""+error.networkResponse.statusCode);
                //Toast.makeText(context,new String(error.networkResponse.data),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
