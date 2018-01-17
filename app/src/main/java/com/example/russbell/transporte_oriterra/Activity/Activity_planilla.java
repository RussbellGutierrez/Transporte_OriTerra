package com.example.russbell.transporte_oriterra.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Adapter.Adapter_planilla;
import com.example.russbell.transporte_oriterra.Class.Class_planilla;
import com.example.russbell.transporte_oriterra.Class.NClass_documento;
import com.example.russbell.transporte_oriterra.Dialogs.DlgGoogle;
import com.example.russbell.transporte_oriterra.Dialogs.DlgSes;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;
import com.example.russbell.transporte_oriterra.Threads.ThreadDocumento;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rusbell Gutierrez on 14/08/2017.
 */

public class Activity_planilla extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static String TAG="Debug_Actplan";
    private static final int PERMISSION_ACCESS_COARSE_LOCATION=1;

    private int intentos=1;
    //private Bundle bundle;
    private String basedatos;
    private String codcamion;
    private GoogleApiClient googleApiClient;
    private ArrayList<Class_planilla> planillafeed;

    @BindView(R.id.recyclerViewRep) RecyclerView recyclerView;
    @BindView(R.id.txt_documentos) TextView txt_documentos;
    @BindView(R.id.txt_clientes) TextView txt_clientes;
    @BindView(R.id.txt_contado) TextView txt_contado;
    @BindView(R.id.txt_corriente) TextView txt_corriente;
    @BindView(R.id.view_line) View view_line;

    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_planilla);

        ButterKnife.bind(this);
        googleApiClient=new GoogleApiClient.Builder(this,this,this)
                .addApi(LocationServices.API).build();
        Holder_data.newGoogleApi(googleApiClient);
        mostrarPlanilla();
        detalleDia();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient!=null){
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        new DlgSes().show(getFragmentManager(),"Dialog_Sesion");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permisos de localización concedidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Localización requerida para el funcionamiento", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        double longitud;
        double latitud;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

            Location lastLocation = LocationServices.
                    FusedLocationApi.getLastLocation(googleApiClient);

            if (lastLocation != null) {
                longitud = lastLocation.getLongitude();
                latitud = lastLocation.getLatitude();
                Holder_data.newDataCoord(longitud,latitud);
            } else {
                longitud = -77.070990;
                latitud = -11.936365;
                Holder_data.newDataCoord(longitud,latitud);
            }
        }else {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                        }
                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(getApplicationContext(),"No permitió GPS satelital", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        }
                    }).check();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Conexión suspendida", Toast.LENGTH_SHORT).show();
        new DlgGoogle().show(getFragmentManager(),"Dialog_google");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Conexión fallida, reconectando...", Toast.LENGTH_SHORT).show();
        googleApiClient.connect();
        intentos++;
        if (intentos==3){
            intentos=1;
            Toast.makeText(this, "Conexión fallida con servicios de Google", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("basedatos",basedatos);
        outState.putString("codcamion",codcamion);
        //outState.putParcelableArrayList("planillafeed",planillafeed);
        Holder_data.newGoogleApi(googleApiClient);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        basedatos=savedInstanceState.getString("basedatos");
        codcamion=savedInstanceState.getString("codcamion");
        //planillafeed=savedInstanceState.getParcelableArrayList("planillafeed");
    }

    public void mostrarPlanilla(){
        LinearLayoutManager linear=new LinearLayoutManager(this);
        Adapter_planilla adapter=new Adapter_planilla(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(adapter);
    }

    private void detalleDia(){
        SQL_sentences sql=new SQL_sentences(this);
        ArrayList<String> onlyclientes=new ArrayList<>();
        int totalclientes=0;
        int totaldocumentos=0;
        int totalcon=0;
        int totalcte=0;
        String cliente="";
        if (!ThreadDocumento.tmpDocumento.isEmpty()){
            for(NClass_documento documento:ThreadDocumento.tmpDocumento){
                /*Log.i(TAG,"Valor de cliente es "+cliente);
                if (!cliente.equals(documento.getCliente())){
                    Log.i(TAG,"Cliente encontrado es "+documento.getCliente());
                    cliente=documento.getCliente();
                    Log.i(TAG,"Cliente ahora es "+cliente);
                    totalclientes++;
                }*/
                onlyclientes.add(documento.getCliente());
                if (documento.getTipopago().equals("CONTADO")){
                    totalcon++;
                }else {
                    totalcte++;
                }
                totaldocumentos++;
            }
            Set<String> clientes=new HashSet<>(onlyclientes);
            txt_documentos.setText(String.valueOf(totaldocumentos));
            txt_clientes.setText(String.valueOf(clientes.size()));
            txt_contado.setText(String.valueOf(totalcon));
            txt_corriente.setText(String.valueOf(totalcte));
        }else {
            String[] result=sql.detalleDia();
            txt_documentos.setText(result[0]);
            txt_clientes.setText(result[1]);
            txt_contado.setText(result[2]);
            txt_corriente.setText(result[3]);
        }
    }
}
