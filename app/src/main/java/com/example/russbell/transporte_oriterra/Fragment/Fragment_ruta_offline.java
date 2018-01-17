package com.example.russbell.transporte_oriterra.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.Toast;

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
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.api.optimizedtrips.v1.MapboxOptimizedTrips;
import com.mapbox.services.api.optimizedtrips.v1.models.OptimizedTripsResponse;
import com.mapbox.services.commons.models.Position;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.services.Constants.PRECISION_6;

public class Fragment_ruta_offline extends Fragment
        implements View.OnClickListener, OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks, MapboxMap.OnInfoWindowLongClickListener
        ,GoogleApiClient.OnConnectionFailedListener, LocationListener
        ,MapboxMap.OnMapClickListener,MapListener, MapboxMap.OnMarkerClickListener,InterfaceMap{

    private LatLng coordenadas,coord_inicio;
    private int coord_touch=1;
    private Polyline line,pathLine;
    private PolylineOptions polyOptions;
    private Marker markerInit,markerPosition;
    private ArrayList<LatLng> points;
    private GoogleApiClient mGoogleApiClient;
    private boolean isEndNotified;
    private int regionSelected;
    private MenuItem menu;

    //Routes
    private DirectionsRoute optimizedRoute;
    private NavigationMapRoute navigationMapRoute;
    private List<Position> paradas;
    private Position origin;
    private MapboxOptimizedTrips optimizedClients;
    //Config OptimizedTrips
    private static final String FIRST = "first";
    private static final String ANY = "any";
    private static final String ROUTE_COLOR = "#4169E1";
    private static final int POLYLINE_WIDTH = 3;
    // Offline objects
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;
    //=======================ACTIVES=========================
    private boolean active_location=true;
    private boolean active_track=true;
    private boolean active_path=true;
    //=======================ACTIVES=========================
    private ComunicatorListener listener;
    private static final String ACCESS_TOKEN="pk.eyJ1IjoicnVzc2JlbGwiLCJhIjoiY2pic2hxNzVyMHM2czJxcjBjZGYzZ25zMSJ9.dQplOv_Eiu6eU-60_r0KlQ";
    private static final String TAG="Fragment_rutasoff";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String JSON_CHARSET = "UTF-8";
    private static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";
    //=======================VIEWS=======================
    private MapboxMap mapBox;
    private ArrayList<Marker> markerArray=new ArrayList<>();
    @BindView(R.id.fbtn_location) FloatingActionButton fbtn_location;
    @BindView(R.id.fbtn_road) FloatingActionButton fbtn_road;
    @BindView(R.id.fbtn_track) FloatingActionButton fbtn_track;
    @BindView(R.id.fbtn_path) FloatingActionButton fbtn_path;
    @BindView(R.id.progress_circle) CircleProgress progressCircle;
    @BindView(R.id.mapView) MapView mapView;
    @BindView(R.id.rotateloading) RotateLoading progressRotate;
    //=======================VIEWS=======================

    public Fragment_ruta_offline(){
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paradas = new ArrayList<>();
        coordenadas=new LatLng(Holder_data.latitud,Holder_data.longitud);
        Timber.tag(TAG).i("Create on map");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity().getApplicationContext(),ACCESS_TOKEN);
        points=new ArrayList<LatLng>();
        View view=inflater.inflate(R.layout.fragment_ruta_offline,container, false);

        //SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment);

        offlineManager = OfflineManager.getInstance(getActivity());

        ButterKnife.bind(this,view);

        setHasOptionsMenu(true);
        mapView.getMapAsync(this);
        fbtn_location.setOnClickListener(this);
        fbtn_road.setOnClickListener(this);
        fbtn_track.setOnClickListener(this);
        fbtn_path.setOnClickListener(this);

        Timber.tag(TAG).i("Createview on map");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_mapa,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setInputType(InputType.TYPE_CLASS_PHONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(Marker marker: Holder_data.markerArray){
                    String codcliente=marker.getSnippet();
                    if (codcliente.equals(query)){
                        Holder_data.newMarkClient(marker.getPosition(),marker.getTitle());
                        mapBox.selectMarker(marker);
                        //marker.showInfoWindow(mapBox,mapView);
                        mapBox.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
                        break;
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menu=item;
        switch (item.getItemId()){
            case R.id.m_download:
                downloadRegionDialog();
                return true;
            case R.id.m_earth:
                downloadedRegionList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//==========================================METODOS PARA DESCARGAR MAPAS OFFLINE=====================================================
    private void downloadRegionDialog() {
        // Set up download interaction. Display a dialog
        // when the user clicks download button and require
        // a user-provided region name
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText regionNameEdit = new EditText(getActivity());
        regionNameEdit.setHint("Nombre del mapa");

        // Build the dialog box
        builder.setTitle("Nombre de la región...")
                .setView(regionNameEdit)
                .setMessage("Descargar la región del mapa que está visualizando")
                .setPositiveButton("Descargar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String regionName = regionNameEdit.getText().toString();
                        // Require a region name to begin the download.
                        // If the user-provided string is empty, display
                        // a toast message and do not begin download.
                        if (regionName.length() == 0) {
                            Toast.makeText(getActivity(), "Debe colocar un nombre a la región", Toast.LENGTH_SHORT).show();
                        } else {
                            // Begin download process
                            downloadRegion(regionName);
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Display the dialog
        builder.show();
    }

    private void downloadRegion(final String regionName) {
        // Define offline region parameters, including bounds,
        // min/max zoom, and metadata

        // Start the progressCircle
        startProgress();

        // Create offline definition using the current
        // style and boundaries of visible map area
        String styleUrl = mapBox.getStyleUrl();
        LatLngBounds bounds = mapBox.getProjection().getVisibleRegion().latLngBounds;
        double minZoom = mapBox.getCameraPosition().zoom;
        double maxZoom = mapBox.getMaxZoomLevel();
        float pixelRatio = this.getResources().getDisplayMetrics().density;
        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                styleUrl, bounds, minZoom, maxZoom, pixelRatio);

        // Build a JSONObject using the user-defined offline region title,
        // convert it into string, and use it to create a metadata variable.
        // The metadata variable will later be passed to createOfflineRegion()
        byte[] metadata;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_FIELD_REGION_NAME, regionName);
            String json = jsonObject.toString();
            metadata = json.getBytes(JSON_CHARSET);
        } catch (Exception exception) {
            Timber.tag(TAG).e("Failed to encode metadata: " + exception.getMessage());
            metadata = null;
        }

        // Create the offline region and launch the download
        offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
            @Override
            public void onCreate(OfflineRegion offlineRegion) {
                Timber.tag(TAG).d("Offline region created: " + regionName);
                Fragment_ruta_offline.this.offlineRegion = offlineRegion;
                launchDownload();
            }

            @Override
            public void onError(String error) {
                Timber.tag(TAG).e("Error: " + error);
            }
        });
    }

    private void launchDownload() {
        // Set up an observer to handle download progress and
        // notify the user when the region is finished downloading
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
            @Override
            public void onStatusChanged(OfflineRegionStatus status) {
                // Compute a percentage
                double percentage = status.getRequiredResourceCount() >= 0
                        ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                        0.0;

                if (status.isComplete()) {
                    // Download complete
                    endProgress("Región descargada!!!");
                    return;
                } else if (status.isRequiredResourceCountPrecise()) {
                    // Switch to determinate state
                    setPercentage((int) Math.round(percentage));
                }

                // Log what is being currently downloaded
                Timber.tag(TAG).d(String.format("%s/%s resources; %s bytes downloaded.",
                        String.valueOf(status.getCompletedResourceCount()),
                        String.valueOf(status.getRequiredResourceCount()),
                        String.valueOf(status.getCompletedResourceSize())));
            }

            @Override
            public void onError(OfflineRegionError error) {
                Timber.tag(TAG).e("onError reason: " + error.getReason());
                Timber.tag(TAG).e("onError message: " + error.getMessage());
            }

            @Override
            public void mapboxTileCountLimitExceeded(long limit) {
                Timber.tag(TAG).e("Mapbox tile count limit exceeded: " + limit);
            }
        });

        // Change the region state
        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
    }

    private void downloadedRegionList() {
        // Build a region list when the user clicks the list button

        // Reset the region selected int to 0
        regionSelected = 0;

        // Query the DB asynchronously
        offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
            @Override
            public void onList(final OfflineRegion[] offlineRegions) {
                // Check result. If no regions have been
                // downloaded yet, notify user and return
                if (offlineRegions == null || offlineRegions.length == 0) {
                    Toast.makeText(getActivity(), "Sin regiones aún", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add all of the region names to a list
                ArrayList<String> offlineRegionsNames = new ArrayList<>();
                for (OfflineRegion offlineRegion : offlineRegions) {
                    offlineRegionsNames.add(getRegionName(offlineRegion));
                }
                final CharSequence[] items = offlineRegionsNames.toArray(new CharSequence[offlineRegionsNames.size()]);

                // Build a dialog containing the list of regions
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Lista de regiones")
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Track which region the user selects
                                regionSelected = which;
                            }
                        })
                        .setPositiveButton("Ir a la región", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                Toast.makeText(getActivity(), items[regionSelected], Toast.LENGTH_LONG).show();

                                // Get the region bounds and zoom
                                LatLngBounds bounds = ((OfflineTilePyramidRegionDefinition)
                                        offlineRegions[regionSelected].getDefinition()).getBounds();
                                double regionZoom = ((OfflineTilePyramidRegionDefinition)
                                        offlineRegions[regionSelected].getDefinition()).getMinZoom();

                                // Create new camera position
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(bounds.getCenter())
                                        .zoom(regionZoom)
                                        .build();

                                // Move camera to new position
                                mapBox.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }
                        })
                        .setNeutralButton("Borrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Make progressCircle indeterminate and
                                // set it to visible to signal that
                                // the deletion process has begun
                                progressRotate.start();
                                //progressCircle.setVisibility(View.VISIBLE);

                                // Begin the deletion process
                                offlineRegions[regionSelected].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
                                    @Override
                                    public void onDelete() {
                                        // Once the region is deleted, remove the
                                        // progressCircle and display a toast
                                        //progressCircle.setVisibility(View.INVISIBLE);
                                        progressRotate.stop();
                                        //progressCircle.setIndeterminate(false);
                                        Toast.makeText(getActivity(), "Región borrada",
                                                Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        //progressCircle.setVisibility(View.INVISIBLE);
                                        //progressCircle.setIndeterminate(false);
                                        progressRotate.stop();
                                        Timber.tag(TAG).e("Error: " + error);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // When the user cancels, don't do anything.
                                // The dialog will automatically close
                            }
                        }).create();
                dialog.show();

            }

            @Override
            public void onError(String error) {
                Timber.tag(TAG).e("Error: " + error);
            }
        });
    }

    private String getRegionName(OfflineRegion offlineRegion) {
        // Get the region name from the offline region metadata
        String regionName;
        String FORMAT="Region %1$d";

        try {
            byte[] metadata = offlineRegion.getMetadata();
            String json = new String(metadata, JSON_CHARSET);
            JSONObject jsonObject = new JSONObject(json);
            regionName = jsonObject.getString(JSON_FIELD_REGION_NAME);
        } catch (Exception exception) {
            Timber.tag(TAG).e("Failed to decode metadata: %s", exception.getMessage());
            regionName = String.format(FORMAT,offlineRegion.getID());
        }
        return regionName;
    }

    // Progress bar methods
    private void startProgress() {
        // Disable buttons
        menu.setEnabled(false);

        // Start and show the progress bar
        isEndNotified = false;
        //progressCircle.setIndeterminate(true);
        progressRotate.stop();
        progressCircle.setVisibility(View.VISIBLE);
    }

    private void setPercentage(final int percentage) {
        //progressCircle.setIndeterminate(false);
        progressRotate.stop();
        progressCircle.setProgress(percentage);
    }

    private void endProgress(final String message) {
        // Don't notify more than once
        if (isEndNotified) {
            return;
        }

        // Enable buttons
        menu.setEnabled(true);

        // Stop and hide the progress bar
        isEndNotified = true;
        //progressCircle.setIndeterminate(false);
        progressRotate.stop();
        progressCircle.setVisibility(View.GONE);

        // Show a toast
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
//==========================================METODOS PARA DESCARGAR MAPAS OFFLINE=====================================================

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
                mapBox.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 18));//16

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
                    //============DIBUJAMOS EL MARCADOR DE PARTIDA EN EL MAPA=============
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(coordenadas);
                    markerOptions.title("inicio");
                    markerOptions.snippet("Latitud: "+coordenadas.getLatitude()+" Longitud: "+coordenadas.getLongitude());
                    markerOptions.icon(changeMarker("meta"));
                    markerInit=mapBox.addMarker(markerOptions);

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
                /*if (Activity_acceso.isNetworkConnected(getContext())){
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
                }*/
            }
        }
    }

    @Override
    public void onSetPolyline(Polyline polyline) {
        if (pathLine!=null){
            pathLine.remove();
        }
        pathLine=polyline;
    }

    @Override
    public void onTrace(LatLng destino){
        coord_inicio=destino;
    }

    private Icon changeMarker(String marcador){
        IconFactory iconFactory = IconFactory.getInstance(getActivity().getApplicationContext());
        Icon icon = null;
        switch(marcador){
            case "camionero":
                icon= iconFactory.fromResource(R.drawable.marktruckmini);
                break;
            case "meta":
                icon= iconFactory.fromResource(R.drawable.markstart);
                break;
            case "verde":
                icon= iconFactory.fromResource(R.drawable.pin_llego);
                break;
            case "rojo":
                icon= iconFactory.fromResource(R.drawable.pin_rechazo);
                break;
            case "gris":
                icon= iconFactory.fromResource(R.drawable.pin_nuevo_falta);
                break;
        }
        return icon;
    }

    private void redrawLine(ArrayList<LatLng> puntos){
        if (line!=null){
            line.remove();
        }
        PolylineOptions options=new PolylineOptions().width(5)
                .color(Color.RED);
        for (int i=0;i<puntos.size();i++){
            LatLng point=puntos.get(i);
            options.add(point);
            Timber.tag(TAG).wtf("Coordenada N°" + i + " es: " + point.toString());
        }
        line=mapBox.addPolyline(options);

    }

    @Override
    public void onMapClick(@NonNull  LatLng latLng) {
        if (fbtn_path.isSelected()){
            if(coord_touch>8){
                Toast.makeText(getContext(),"El límite son 8 coordenadas",Toast.LENGTH_SHORT).show();
            }else {
                Timber.tag(TAG).wtf(latLng.getLatitude() + " " + latLng.getLongitude());
                //getRoute(latLng);
                //tracePath(latLng);
                coord_touch++;
            }
            coord_inicio=latLng;
        }
    }

    /*private void getRoute(LatLng destino) {

        Position origin = Position.fromLngLat(coord_inicio.getLongitude(),coord_inicio.getLatitude());
        Position destination = Position.fromLngLat(destino.getLongitude(),destino.getLatitude());
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().getRoutes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().getRoutes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(mapView,mapBox);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }*/

    private void tracePath(LatLng destino){
        if (destino.getLatitude()==0.0 && destino.getLongitude()==0.0){
            Timber.tag(TAG).wtf("Coordenada invalida");
        }else {

            String urlcompleta;
            String urlinicio;
            String urlfin;
            if (coord_inicio==coordenadas){

                urlinicio ="origin="+coord_inicio.getLatitude()+","+coord_inicio.getLongitude();
                urlfin ="destination="+destino.getLatitude()+","+destino.getLongitude();
                String params = urlinicio +"&"+ urlfin;//+"&"+ruta;
                String output = "json";
                urlcompleta = "https://maps.googleapis.com/maps/api/directions/"
                        + output + "?" + params;

                Timber.tag(TAG).wtf("coordenada inicial: " + coord_inicio);
                Timber.tag(TAG).wtf("coordenada destino: " + destino);

                ThreadGetRoute drawPath=new ThreadGetRoute(destino,getContext(), urlcompleta,mapBox,polyOptions,pathLine,this);
                drawPath.execute();

            }else {

                urlinicio ="origin="+coord_inicio.getLatitude()+","+coord_inicio.getLongitude();
                urlfin ="destination="+destino.getLatitude()+","+destino.getLongitude();
                String params = urlinicio +"&"+ urlfin;//+"&"+ruta;
                String output = "json";
                urlcompleta = "https://maps.googleapis.com/maps/api/directions/"
                        + output + "?" + params;

                Timber.tag(TAG).wtf("coordenada inicial: " + coord_inicio);
                Timber.tag(TAG).wtf("coordenada destino: " + destino);
                ThreadGetRoute drawPath=new ThreadGetRoute(destino,getContext(), urlcompleta,mapBox,polyOptions,pathLine,this);
                drawPath.execute();
            }
        }
    }

    //=============================================METODOS NUEVO PATH ROUTE=======================================================
    private boolean alreadyTwelveMarkersOnMap(){
        if (paradas.size()==12){
            return true;
        }else {
            return false;
        }
    }

    private void addDestinationMarker(LatLng point){
        if (paradas.size()==12){
            mapBox.addMarker(new MarkerOptions()
                .position(new LatLng(point.getLatitude(),point.getLongitude()))
                .title("Destino"));
        }
    }

    private void addPointToParadasList(LatLng point){
        paradas.add(Position.fromCoordinates(point.getLongitude(),point.getLatitude()));
    }

    private void addFirstPointToParadasList(LatLng point){
        origin = Position.fromCoordinates(point.getLongitude(),point.getLatitude());
        paradas.add(origin);
    }

    private void getOptimizedRoute(List<Position> coordinates){
        optimizedClients = new MapboxOptimizedTrips.Builder<>()
                .setSource(FIRST)
                .setDestination(ANY)
                .setCoordinates(coordinates)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .setAccessToken(Mapbox.getAccessToken())
                .build();

        optimizedClients.enqueueCall(new Callback<OptimizedTripsResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(Call<OptimizedTripsResponse> call, Response<OptimizedTripsResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("DirectionsActivity","No se encontraron rutas, revisar usuario y llave de acceso");
                    Toast.makeText(getActivity(),"No se encontraron rutas, revisar usuario y llave de acceso",Toast.LENGTH_SHORT).show();
                }else {
                    if (response.body().getTrips().isEmpty()){
                        Log.d("DirectionsActivity","No se encontraron rutas para la localización size = "+response.body().getTrips().size());
                        Toast.makeText(getActivity(),"No se encontraron rutas para la localización",Toast.LENGTH_SHORT).show();
                    }
                }

                optimizedRoute = response.body().getTrips().get(0);
                //drawOptimizedRoute(optimizedRoute);
            }

            @Override
            public void onFailure(Call<OptimizedTripsResponse> call, Throwable t) {
                Log.d("DirectionsActivity","Error: "+t.getMessage());
            }
        });
    }
    //=============================================METODOS NUEVO PATH ROUTE=======================================================

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (fbtn_path.isSelected()){
            if(coord_touch>8){
                Toast.makeText(getContext(),"El límite son 8 coordenadas",Toast.LENGTH_SHORT).show();
            }else {
                LatLng coord_mark = new LatLng(marker.getPosition());
                Timber.tag(TAG).wtf(coord_mark.getLatitude() + " " + coord_mark.getLongitude());
                //getRoute(coord_mark);
                //tracePath(coord_mark);
                coord_touch++;
                coord_mark =new LatLng(0.0,0.0);
            }
            coord_inicio=new LatLng(marker.getPosition());
        }else {
            LatLng position=marker.getPosition();
            Holder_data.newMarkClient(position,marker.getTitle());
            mapBox.selectMarker(marker);
            //marker.showInfoWindow(mapBox,mapView);
            mapBox.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
            Timber.tag(TAG).i("Posición " + position);
        }
        return true;
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        Timber.tag(TAG).wtf("MapReady with coord X:" + Holder_data.longitud + " Y:" + Holder_data.latitud);
        coord_inicio=coordenadas;
        mapBox =mapboxMap;
        mapBox.setOnMapClickListener(this);
        mapBox.setOnMarkerClickListener(this);
        mapBox.setOnInfoWindowLongClickListener(this);
        mapBox.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 16));
        //enableLocationPlugin();
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            //mapBox.setMyLocationEnabled(true);
            mapBox.getUiSettings().setAllGesturesEnabled(true);
            mapBox.getUiSettings().setZoomControlsEnabled(true);
            mapBox.setInfoWindowAdapter(new InfoWindowModel(LayoutInflater.from(getActivity()),getContext()));
        }
        Holder_data.newMap(mapBox);
        drawClientes(mapBox);
    }

    public void drawClientes(MapboxMap map){
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
                                .icon(changeMarker("verde")));
                        markerArray.add(greenmarker);
                        break;
                    case "2":
                        Marker redmarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(changeMarker("rojo")));
                        markerArray.add(redmarker);
                        break;
                    case "Atendido":
                        Marker offlinegreenmarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(changeMarker("verde")));
                        markerArray.add(offlinegreenmarker);
                        break;
                    case "Rechazado":
                        Marker offlineredmarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(changeMarker("rojo")));
                        markerArray.add(offlineredmarker);
                        break;
                    default:
                        Marker graymarker=map.addMarker(new MarkerOptions()
                                .title("cliente")
                                .snippet(item.getCliente())
                                .position(new LatLng(Double.valueOf(item.getYCoord()),Double.valueOf(item.getXCoord())))
                                .icon(changeMarker("gris")));
                        markerArray.add(graymarker);
                        break;
                }
            }
        }
        Timber.tag(TAG).i("save markers on holder_data");
        Holder_data.newMarkerArray(markerArray);
    }

    @Override
    public void onLocationChanged(Location location) {
        //comprueba si existe marcador de la localización
        //para eliminarlo y poner uno nuevo dando la sensación de seguimiento
        if (markerPosition != null) {
            markerPosition.remove();
        }
        coordenadas = new LatLng(location.getLatitude(), location.getLongitude());
        //coord_inicio=latLng;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(coordenadas);
        markerOptions.title(Holder_data.codcamion);
        markerOptions.snippet("Coordenada: "+coordenadas.toString());
        //markerOptions.icon(icon);
        markerPosition = mapBox.addMarker(markerOptions);

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
            mapBox.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas,16));
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
                        //mapBox.setMyLocationEnabled(true);
                    }
                }else {
                    Toast.makeText(getContext(),"Permisos denegados",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient!=null){
            mGoogleApiClient.reconnect();
        }
        mapView.onResume();
        Timber.tag(TAG).i("Resume on map");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        mapView.onPause();
        Timber.tag(TAG).i("Pause on map");
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
        mapView.onStop();
        Timber.tag(TAG).i("Stop on map");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
                    .requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                Holder_data.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Marker marker: Holder_data.markerArray){
                            String codcliente=marker.getSnippet();
                            Timber.tag(TAG).i("Compare snippet " + codcliente + " with code " + Holder_data.codigocliente);
                            if (codcliente.equals(Holder_data.codigocliente)){
                                marker.setIcon(changeMarker("verde"));
                                Timber.tag(TAG).i("The marker from " + codcliente + " is reachable!!!");
                                break;
                            }else{
                                Timber.tag(TAG).i("No marker exist :c");
                            }
                        }
                    }
                });
                break;
            case "Rechazado":
                changeStateClient("2");
                Holder_data.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Marker marker: Holder_data.markerArray){
                            String codcliente=marker.getSnippet();
                            Timber.tag(TAG).i("Compare snippet " + codcliente + " with code " + Holder_data.codigocliente);
                            if (codcliente.equals(Holder_data.codigocliente)){
                                marker.setIcon(changeMarker("rojo"));
                                Timber.tag(TAG).i("The marker from " + codcliente + " is reachable!!!");
                                break;
                            }else{
                                Timber.tag(TAG).i("No marker exist :c");
                            }
                        }
                    }
                });
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
}
