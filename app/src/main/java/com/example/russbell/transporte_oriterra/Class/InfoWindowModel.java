package com.example.russbell.transporte_oriterra.Class;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.Threads.ThreadDocumento;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Russbell on 4/10/2017.
 */

public class InfoWindowModel implements MapboxMap.InfoWindowAdapter {

    //=======variables para cliente==========
    private String cliente;
    private String direccion;
    private String negocio;
    private String ruta;
    private String estado;
    //=======variables para fletero==========
    private String fletero_nom;
    private String fletero_ruta;
    private String fletero_longitud;
    private String fletero_latitud;
    //=======variables==========
    private LayoutInflater inflater;
    //private int i=0;
    private Context context;
    private static final String TAG = "InfoWindowModel";

    //=========nuevo view para infowindow de cliente==========
    @BindView(R.id.cliente) LinearLayout ly_cliente;
    @BindView(R.id.txt_datocliente) TextView txt_datocliente;
    @BindView(R.id.txt_direccion) TextView txt_direccion;
    @BindView(R.id.txt_negocio) TextView txt_negocio;
    @BindView(R.id.txt_ruta) TextView txt_ruta;
    @BindView(R.id.txt_estado) TextView txt_estado;
    @BindView(R.id.txt_documentos) TextView txt_documentos;
    /*@BindView(R.id.fab_atendido) FloatingActionButton fab_atendido;
    @BindView(R.id.fab_rechazo) FloatingActionButton fab_rechazo;*/

    //=========nuevo view para infowindow de vendedor==========
    @BindView(R.id.fletero) LinearLayout ly_fletero;
    @BindView(R.id.txt_datofletero) TextView txt_datofletero;
    @BindView(R.id.txt_longitud) TextView txt_longitud;
    @BindView(R.id.txt_latitud) TextView txt_latitud;

    public InfoWindowModel(LayoutInflater inflater, Context context){
        this.inflater = inflater;
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        //Carga layout personalizado.
        //View v = inflater.inflate(R.layout.infowindow_customize, null);
        View v = inflater.inflate(R.layout.info_custom, null);
        ButterKnife.bind(this,v);
        Timber.tag(TAG).i("Info window is activated!!!!");
        obtainClient();
        //mSocket.connect();
        return v;
    }

    /*@Override
    public View getInfoContents(Marker m) {
        //Carga layout personalizado.
        //View v = inflater.inflate(R.layout.infowindow_customize, null);
        View v = inflater.inflate(R.layout.info_custom, null);
        ButterKnife.bind(this,v);
        Log.i(TAG,"Info window is activated!!!!");
        obtainClient();
        //mSocket.connect();
        return v;
    }*/

    private void obtainClient(){
        //int veces=0;
        String documentos= "";
        String planilla =Holder_data.codprov+"-"+Holder_data.seriepla+"-"+Holder_data.tipopla+"-"+Holder_data.nropla;
        String compare= String.valueOf(Holder_data.posicion.getLatitude()+","+Holder_data.posicion.getLongitude());
        if (Holder_data.titulo.equalsIgnoreCase("cliente")){
            ly_fletero.setVisibility(View.GONE);
            ly_cliente.setVisibility(View.VISIBLE);
            for (NClass_documento item: ThreadDocumento.tmpDocumento){
                String coord=item.getYCoord()+","+item.getXCoord();
                String plani=item.getCodprov()+"-"+item.getSeriepla()+"-"+item.getTipopla()+"-"+item.getNropla();
                if (coord.equals(compare) && plani.equalsIgnoreCase(planilla)){
                    cliente=item.getCliente()+"-"+item.getNomcli();
                    ruta=item.getRuta();
                    negocio=item.getNegocio();
                    direccion=item.getDircli();
                    switch (item.getEstado()) {
                        case "0":
                            estado="Pendiente";
                            break;
                        case "1":
                            estado="Atendido";
                            break;
                        case "2":
                            estado="Rechazado";
                            break;
                    }
                    documentos=getAllDocuments(compare,planilla);
                    Log.d(TAG,"Cliente codigo "+item.getCliente()+" con estado "+item.getEstado());
                    Holder_data.newDataCliente(item.getCliente(),item.getNomcli());
                    /*if(documentos.equals("")){
                        documentos=item.getDocumento();
                        //veces++;
                    }else if(!documentos.equals("")){
                        documentos+="\n"+item.getDocumento();
                    }*/
                    break;
                }
                //i++;
                //Log.d(TAG,coord);
            }
            txt_datocliente.setText(cliente);
            if(direccion.length()==25){
                txt_direccion.setText(direccion);
            }else{
                String first=direccion.substring(0,25);
                String second=direccion.substring(25);
                String directnew=first+"\n"+second;
                txt_direccion.setText(directnew);
            }
            //txt_direccion.setText(direccion);
            txt_negocio.setText(negocio);
            txt_ruta.setText(ruta);
            txt_estado.setText(estado);
            txt_documentos.setText(documentos);
        }else {
            ly_fletero.setVisibility(View.VISIBLE);
            ly_cliente.setVisibility(View.GONE);

            fletero_nom=Holder_data.titulo;
            fletero_longitud=String.valueOf(Holder_data.posicion.getLongitude());
            fletero_latitud=String.valueOf(Holder_data.posicion.getLatitude());

            txt_datofletero.setText(fletero_nom);
            txt_longitud.setText(fletero_longitud);
            txt_latitud.setText(fletero_latitud);
        }
    }

    private String getAllDocuments(String xandy,String planilla){
        String documentos="";
        for (NClass_documento item: ThreadDocumento.tmpDocumento){
            String coord=item.getYCoord()+","+item.getXCoord();
            String plani=item.getCodprov()+"-"+item.getSeriepla()+"-"+item.getTipopla()+"-"+item.getNropla();
            if (coord.equals(xandy) && plani.equalsIgnoreCase(planilla)){
                if(documentos.equals("")){
                    documentos=item.getDocumento();
                }else if(!documentos.equals("")){
                    documentos+="\n"+item.getDocumento();
                }
            }
        }
        return documentos;
    }
}
