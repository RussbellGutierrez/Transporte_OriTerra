package com.example.russbell.transporte_oriterra.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Activity.Activity_acceso;
import com.example.russbell.transporte_oriterra.Class.InfoWindowModel;
import com.example.russbell.transporte_oriterra.Class.NClass_documento;
import com.example.russbell.transporte_oriterra.Dialogs.DlgObs;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.ComunicatorListener;
import com.example.russbell.transporte_oriterra.Interfaces.InterfaceMap;
import com.example.russbell.transporte_oriterra.Interfaces.MapListener;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.Threads.ThreadDocumento;
import com.example.russbell.transporte_oriterra.Threads.ThreadGetRoute;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class Fragment_ruta extends Fragment
        implements OnMapReadyCallback
        ,GoogleApiClient.ConnectionCallbacks
        ,GoogleApiClient.OnConnectionFailedListener
        ,LocationListener, View.OnClickListener
        ,GoogleMap.OnMapClickListener
        , GoogleMap.OnMarkerClickListener
        ,GoogleMap.OnInfoWindowLongClickListener
        ,InterfaceMap {

    private GoogleApiClient mGoogleApiClient;
    private ComunicatorListener listener;
    private LatLng coordenadas,coord_inicio;
    private int coord_touch=1;
    private GoogleMap googleMap;
    private Marker markerInit,markerPosition;
    private ArrayList<LatLng> points;
    private Polyline line,pathLine;
    private PolylineOptions polyOptions;
    //=======BOLEANOS PARA DETECTAR BOTONES PRESIONADOS=========
    private boolean active_location=true;
    private boolean active_track =true;
    private boolean active_path=true;
    //=======BOLEANOS PARA DETECTAR BOTONES PRESIONADOS=========
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static String TAG="Frag_ruta";
    private ArrayList<Marker> markerArray=new ArrayList<>();

    //=======================VIEWS=======================
    @BindView(R.id.fbtn_location) FloatingActionButton fbtn_location;
    @BindView(R.id.fbtn_road) FloatingActionButton fbtn_road;
    @BindView(R.id.fbtn_track) FloatingActionButton fbtn_track;
    @BindView(R.id.fbtn_path) FloatingActionButton fbtn_path;

    //=======================VIEWS=======================

    public Fragment_ruta(){
    }

    //============METODO PARA CONTRUIR EL API CLIENT===================
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    //============METODO PARA CONSTRUIR EL API CLIENT===================

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ComunicatorListener){
            listener=(ComunicatorListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    +" debe implementar ComunicatorListener");
        }
        Timber.tag(TAG).i("Attach on map");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mGoogleApiClient!=null){
            mGoogleApiClient.disconnect();
        }
        listener = null;
        Timber.tag(TAG).i("Detach on map");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        Timber.tag(TAG).i("Pause on map");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient!=null){
            mGoogleApiClient.reconnect();
        }
        Timber.tag(TAG).i("Resume on map");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()){
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
            mGoogleApiClient.disconnect();
        }
        Timber.tag(TAG).i("Stop on map");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coordenadas=new LatLng(Holder_data.latitud,Holder_data.longitud);
        Timber.tag(TAG).i("Create on map");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ruta,container, false);
        points=new ArrayList<LatLng>();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mp_limanorte);
        mapFragment.getMapAsync(this);
        ButterKnife.bind(this,view);
        setHasOptionsMenu(true);
        fbtn_location.setOnClickListener(this);
        fbtn_road.setOnClickListener(this);
        fbtn_track.setOnClickListener(this);
        fbtn_path.setOnClickListener(this);

        Timber.tag(TAG).i("Createview on map");
        return view;
    }

    @Override
    public void onClick(View v) {
        LocationManager locationManager=(LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps= false;
        boolean network=false;
    //===============VERIFICAMOS SI ESTA HABILITADO ALGUNO=================
        try{
            gps=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ignored){}
        try{
            network=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ignored){}
    //===============VERIFICAMOS SI ESTA HABILITADO ALGUNO=================
        if (!gps && !network){
            //===========GENERAMOS UN DIALOG DE ALERTA==================
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("GPS no activado, porfavor habilitelo");
            dialog.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getContext().startActivity(myIntent);
                    //get gps
                    //TODO: Buscar el modo de refrescar el fragment despues de activar el gps
                }
            });
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
            //===========GENERAMOS UN DIALOG DE ALERTA==================
        }else {
            //==============LOGICA DEL BOTON LOCATION==================
            if (v == fbtn_location) {
                fbtn_road.setSelected(false);
                String brown = "#A52A2A";
                active_location = true;
                fbtn_road.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(brown)));
                fbtn_road.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.truck));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 18));//16

            }//==============LOGICA DEL BOTON ROAD==================
            else if (v == fbtn_road) {
                if (active_location) {
                    v.setSelected(true);
                    fbtn_road.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    fbtn_road.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.truck_road));
                    active_location = false;
                } else {
                    v.setSelected(false);
                    String brown = "#A52A2A";
                    fbtn_road.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(brown)));
                    fbtn_road.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.truck));
                    active_location = true;
                }
            }//==============LOGICA DEL BOTON TRACK==================
            else if (v == fbtn_track){
                if (active_track){
                    active_track =false;
                    v.setSelected(true);
                    fbtn_track.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    //=========DIBUJAMOS EL MARCADOR DE PARTIDA EN EL MAPA==========
                    MarkerOptions mOptions = new MarkerOptions();
                    mOptions.position(coordenadas);
                    mOptions.title("Inicio");
                    mOptions.snippet("Latitud: "+coordenadas.latitude+" Longitud: "+coordenadas.longitude);
                    mOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.markstart));
                    markerInit=googleMap.addMarker(mOptions);

                    points.add(coordenadas);
                    //luego invocamos al metodo creado para dibujar
                    redrawLine(points);
                }else {
                    active_track =true;
                    v.setSelected(false);
                    String brown = "#A52A2A";
                    fbtn_track.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(brown)));
                    points.clear();
                    if (markerInit!=null){
                        markerInit.remove();
                    }
                    if (line!=null){
                        line.remove();
                    }
                }
            }//==============LOGICA DEL BOTON PATH==================
            else if (v==fbtn_path){
                if (Activity_acceso.isNetworkConnected(getContext())){
                    if (active_path){
                        active_path =false;
                        v.setSelected(true);
                        polyOptions=new PolylineOptions();
                        fbtn_path.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                        Toast.makeText(getContext(),"Toque el destino, máximo 8 destinos",Toast.LENGTH_SHORT).show();
                        Timber.tag(TAG).wtf("press me");
                    }else {
                        if(pathLine!=null){
                            active_path =true;
                            v.setSelected(false);
                            String brown = "#A52A2A";
                            fbtn_path.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(brown)));
                            pathLine.remove();
                            coord_touch=1;
                            coord_inicio=coordenadas;
                            Timber.tag(TAG).wtf("touch me");

                        }else {
                            active_path =true;
                            v.setSelected(false);
                            String brown = "#A52A2A";
                            fbtn_path.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(brown)));
                            coord_touch=1;
                            coord_inicio=coordenadas;
                            Timber.tag(TAG).wtf("touch me");
                        }
                    }
                }else {
                    Toast.makeText(getContext(),"Habilite una conexión a internet",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void redrawLine(ArrayList<LatLng> puntos){
        if (line!=null){
            line.remove();
        }
        PolylineOptions options=new PolylineOptions().width(5)
                .color(Color.RED).geodesic(true);
        for (int i=0;i<puntos.size();i++){
            LatLng point=puntos.get(i);
            options.add(point);
            Timber.tag(TAG).wtf("Coordenada N°" + i + " es: " + point.toString());
        }
        line=googleMap.addPolyline(options);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (fbtn_path.isSelected()){
            if(coord_touch>8){
                Toast.makeText(getContext(),"El límite son 8 coordenadas",Toast.LENGTH_SHORT).show();
            }else {
                Timber.tag(TAG).wtf(latLng.latitude + " " + latLng.longitude);
                tracePath(latLng);
                coord_touch++;
            }
        }
    }

    private void tracePath(LatLng destino){
        if (destino.latitude==0.0 && destino.longitude==0.0){
            Timber.tag(TAG).wtf("Coordenada invalida");
        }else {

            String urlcompleta;
            String urlinicio;
            String urlfin;
            if (coord_inicio==coordenadas){

                urlinicio ="origin="+coord_inicio.latitude+","+coord_inicio.longitude;
                urlfin ="destination="+destino.latitude+","+destino.longitude;
                String params = urlinicio +"&"+ urlfin;//+"&"+ruta;
                String output = "json";
                urlcompleta = "https://maps.googleapis.com/maps/api/directions/"
                        + output + "?" + params;

                Timber.tag(TAG).wtf("coordenada inicial: " + coord_inicio);
                Timber.tag(TAG).wtf("coordenada destino: " + destino);

                /*ThreadGetRoute drawPath=new ThreadGetRoute(destino,getContext(), urlcompleta,googleMap,polyOptions,pathLine,this);
                drawPath.execute();*/

            }else {

                urlinicio ="origin="+coord_inicio.latitude+","+coord_inicio.longitude;
                urlfin ="destination="+destino.latitude+","+destino.longitude;
                String params = urlinicio +"&"+ urlfin;//+"&"+ruta;
                String output = "json";
                urlcompleta = "https://maps.googleapis.com/maps/api/directions/"
                        + output + "?" + params;

                Timber.tag(TAG).wtf("coordenada inicial: " + coord_inicio);
                Timber.tag(TAG).wtf("coordenada destino: " + destino);
                /*ThreadGetRoute drawPath=new ThreadGetRoute(destino,getContext(), urlcompleta,googleMap,polyOptions,pathLine,this);
                drawPath.execute();*/
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (fbtn_path.isSelected()){
            if(coord_touch>8){
                Toast.makeText(getContext(),"El límite son 8 coordenadas",Toast.LENGTH_SHORT).show();
            }else {
                LatLng coord_mark = marker.getPosition();
                Timber.tag(TAG).wtf(coord_mark.latitude + " " + coord_mark.longitude);
                tracePath(coord_mark);
                coord_touch++;
                coord_mark =new LatLng(0.0,0.0);
            }
        }else {
            LatLng position=marker.getPosition();
            //Holder_data.newMarkClient(position,marker.getTitle());
            marker.showInfoWindow();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
            Timber.tag(TAG).i("Posición " + position);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Timber.tag(TAG).wtf("MapReady with coord X:" + Holder_data.longitud + " Y:" + Holder_data.latitud);
        coord_inicio=coordenadas;
        googleMap = map;
        //========PRUEBAS DE TILE OVERLAY==========
        //googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        //googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(new CustomMapTileProvider(getResources().getAssets())));
        //========PRUEBAS DE TILE OVERLAY==========
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowLongClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 16));

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            //googleMap.setInfoWindowAdapter(new InfoWindowModel(LayoutInflater.from(getActivity()),getContext()));
        }
        //Holder_data.newMap(googleMap);
        drawClientes(googleMap);
    }

    public void drawClientes(GoogleMap map){
        Timber.tag(TAG).i("drawClientes execute");
        String codcli="";
        for (NClass_documento item: ThreadDocumento.tmpDocumento){
            if (!item.getCliente().equals(codcli)){
                codcli=item.getCliente();
                String estado=item.getEstado();
                switch (estado){
                    case "1":
                        Marker greenmarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.pin_llego)));
                        markerArray.add(greenmarker);
                        break;
                    case "2":
                        Marker redmarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.pin_rechazo)));
                        markerArray.add(redmarker);
                        break;
                    case "Atendido":
                        Marker offlinegreenmarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.pin_llego)));
                        markerArray.add(offlinegreenmarker);
                        break;
                    case "Rechazado":
                        Marker offlineredmarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.pin_rechazo)));
                        markerArray.add(offlineredmarker);
                        break;
                    default:
                        Marker graymarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.pin_falta)));
                        markerArray.add(graymarker);
                        break;
                }
            }
        }
        Timber.tag(TAG).i("save markers on holder_data");
        //Holder_data.newMarkerArray(markerArray);
    }

    /*@Override
    public void onSetPolyline(Polyline polyline) {
        if (pathLine!=null){
            pathLine.remove();
        }
        pathLine=polyline;
    }

    @Override
    public void onTrace(LatLng destino){
        coord_inicio=destino;
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.tag(TAG).wtf("onConnected");
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        //revisar la prioridad, si consume demasiada bateria, CAMBIAR
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //comprueba si existe marcador de la localización
        //para eliminarlo y poner uno nuevo
        //dando la sensación de seguimiento
        if (markerPosition != null) {
            markerPosition.remove();
        }
        coordenadas = new LatLng(location.getLatitude(), location.getLongitude());
        //coord_inicio=latLng;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coordenadas);
        markerOptions.title(Holder_data.codcamion);
        markerOptions.snippet("Coordenada: "+coordenadas.toString());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marktruckmini));
        markerPosition = googleMap.addMarker(markerOptions);

        if (fbtn_track.isSelected()){
            points.add(coordenadas);
            //luego invocamos al metodo creado para dibujar
            redrawLine(points);
        }else {
            if (markerInit!=null){
                markerInit.remove();
            }
            Timber.tag(TAG).wtf("No polyline");
        }

        if (fbtn_road.isSelected()){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas,16));
        }else {
            Timber.tag(TAG).wtf("No active_track seguimiento");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permisos otorgados
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){

                        if (mGoogleApiClient==null){
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                }else {
                    Toast.makeText(getContext(),"Permisos denegados",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Timber.tag(TAG).w("Press infowindow!!!!");
        Holder_data.newRefreshAdapter(2);
        Holder_data.newInerfaceMap(this);
        new DlgObs().show(getFragmentManager(),"Dlg_obs");
    }

    @Override
    public void onMapChangeState(String event) {
        Timber.tag(TAG).i("MapChangeState events!!!");
        switch (event){
            case "Atendido":
                changeStateClient("1");
                /*Holder_data.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Marker marker: Holder_data.markerArray){
                            String codcliente=marker.getSnippet();
                            Timber.tag(TAG).i("Compare snippet " + codcliente + " with code " + Holder_data.codigocliente);
                            if (codcliente.equals(Holder_data.codigocliente)){
                                marker.setIcon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.pin_llego));
                                Timber.tag(TAG).i("The marker from " + codcliente + " is reachable!!!");
                                break;
                            }else{
                                Timber.tag(TAG).i("No marker exist :c");
                            }
                        }
                    }
                });*/
                break;
            case "Rechazado":
                changeStateClient("2");
                /*Holder_data.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Marker marker: Holder_data.markerArray){
                            String codcliente=marker.getSnippet();
                            Timber.tag(TAG).i("Compare snippet " + codcliente + " with code " + Holder_data.codigocliente);
                            if (codcliente.equals(Holder_data.codigocliente)){
                                marker.setIcon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.pin_rechazo));
                                Timber.tag(TAG).i("The marker from " + codcliente + " is reachable!!!");
                                break;
                            }else{
                                Timber.tag(TAG).i("No marker exist :c");
                            }
                        }
                    }
                });*/
                break;
        }
    }

    private void changeStateClient(String estado){
        int i=0;
        for (NClass_documento item: ThreadDocumento.tmpDocumento){
            if (item.getCliente().equals(Holder_data.codigocliente)){
                NClass_documento documento=ThreadDocumento.tmpDocumento.get(i);
                Timber.tag(TAG).d("Antes el cliente " + documento.getCliente() + " tiene estado " + documento.getEstado());
                documento.setEstado(estado);
                ThreadDocumento.tmpDocumento.set(i,documento);
                NClass_documento documento2=ThreadDocumento.tmpDocumento.get(i);
                Timber.tag(TAG).d("Ahora el cliente " + documento2.getCliente() + " tiene estado " + documento2.getEstado());
                break;
            }
            i++;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_cliente,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setInputType(InputType.TYPE_CLASS_PHONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*for(Marker marker: Holder_data.markerArray){
                    String codcliente=marker.getSnippet();
                    if (codcliente.equals(query)){
                        Holder_data.newMarkClient(marker.getPosition(),marker.getTitle());
                        marker.showInfoWindow();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
                        break;
                    }
                }*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
