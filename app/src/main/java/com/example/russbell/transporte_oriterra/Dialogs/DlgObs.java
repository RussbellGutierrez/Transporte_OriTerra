package com.example.russbell.transporte_oriterra.Dialogs;

import android.app.Dialog;
//import android.app.DialogFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Class.UrlConexion;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.InterfaceMap;
import com.example.russbell.transporte_oriterra.Interfaces.MultipleEvents;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by Rusbell Gutierrez on 14/08/2017.
 */

public class DlgObs extends DialogFragment{

    private MultipleEvents listener;
    private InterfaceMap maplistener;
    private boolean estado=false;
    private SQL_sentences sql;
    private static final String TAG="Dlg_Obs";

    public DlgObs(){
    }

    public Socket mSocket;
    {
        UrlConexion url = new UrlConexion();
        url.setIP(Holder_data.basedatos);
        Log.i(TAG,url.setIP(Holder_data.basedatos));
        try{
            mSocket = IO.socket(url.urlSocket(Holder_data.basedatos));
            Log.w(TAG, url.urlSocket(Holder_data.basedatos));
        }catch(URISyntaxException e){}
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSocket.connect();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        listener=Holder_data.listener;
        maplistener=Holder_data.mappistener;
        sql=new SQL_sentences(getContext());
        return createRadioDialog();
    }

    public AlertDialog createRadioDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View v=inflater.inflate(R.layout.dlgobs,null);
        RadioGroup rg=(RadioGroup)v.findViewById(R.id.radio_group);

        builder.setView(v);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb1=(RadioButton)group.findViewById(R.id.rb_atendido);
                RadioButton rb2=(RadioButton)group.findViewById(R.id.rb_rechazado);
                boolean op1=rb1.isChecked();
                boolean op2=rb2.isChecked();
                if (op1){
                    estado=true;
                }else if(op2){
                    estado=false;
                }
            }
        });

        Button ok=(Button)v.findViewById(R.id.ok);
        Button cancel=(Button)v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                boolean connected=isNetworkConnected(getContext());

                if(estado){
                    //txt_estado.setText("Atendido");
                    sql.guardarEstado("Atendido",Holder_data.codigocliente,Holder_data.nropla);
                    if (Holder_data.refreshing==1){
                        listener.CustomEvents("Atendido");
                    }else {
                        maplistener.onMapChangeState("Atendido");
                    }
                    if(connected){
                        sendToSocket(1);
                    }else {
                        Toast.makeText(getContext(),"Guardado pero no enviado al servidor.\nRevise su conexión a internet",Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }else {
                    //txt_estado.setText("Pedido rechazado");
                    sql.guardarEstado("Rechazado",Holder_data.codigocliente,Holder_data.nropla);
                    if (Holder_data.refreshing==1){
                        listener.CustomEvents("Rechazado");
                    }else {
                        maplistener.onMapChangeState("Rechazado");
                    }
                    if(connected){
                        sendToSocket(2);
                    }else {
                        Toast.makeText(getContext(),"Guardado pero no enviado al servidor.\nRevise su conexión a internet",Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            }
        });
        return builder.create();
    }

    private void sendToSocket(int estado){
        Log.d(TAG,"Socket emit!!");
        JSONObject sObject=new JSONObject();
        try {
            sObject.put("codprov",Holder_data.codprov);
            sObject.put("tipopla",Holder_data.tipopla);
            sObject.put("seriepla",Holder_data.seriepla);
            sObject.put("nropla",Holder_data.nropla);
            sObject.put("cliente",Holder_data.codigocliente);
            sObject.put("resultado",estado);

            mSocket.emit("resultado", sObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isNetworkConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return !(info == null || !info.isConnected() || !info.isAvailable());
    }
}
