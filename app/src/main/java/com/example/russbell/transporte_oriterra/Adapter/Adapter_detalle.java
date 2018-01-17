package com.example.russbell.transporte_oriterra.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.russbell.transporte_oriterra.Class.Feed_detdocu;
import com.example.russbell.transporte_oriterra.Class.NClass_carga;
import com.example.russbell.transporte_oriterra.Class.NClass_detalle;
import com.example.russbell.transporte_oriterra.Holder.DetdocuHolder;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_helper;
import com.example.russbell.transporte_oriterra.Threads.ThreadCarga;
import com.example.russbell.transporte_oriterra.Threads.ThreadDetalle;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Russbell on 28/08/2017.
 */

public class Adapter_detalle extends RecyclerView.Adapter<DetdocuHolder>{

    private ArrayList<Feed_detdocu> feeddetdocu= new ArrayList<>();

    public Adapter_detalle(String documento, Context context){
        if (!ThreadDetalle.tmpDetalle.isEmpty()){
            for (NClass_detalle detalle:ThreadDetalle.tmpDetalle){
                if (detalle.getIdcliente().equals(Holder_data.codigocliente) &&
                        detalle.getNropla().equals(Holder_data.nropla) &&
                        detalle.getFletero().equals(Holder_data.codcamion) &&
                        detalle.getNrodoc().equals(documento)){

                    String codart=detalle.getCodart();

                    for (NClass_carga carga:ThreadCarga.tmpCarga){
                        if (carga.getaCodart().equals(codart)){
                            String descrip=carga.getaDescrip();

                            double lineaPrecio=Double.valueOf(detalle.getLinea());
                            String unidades=detalle.getUnidades();

                            NumberFormat formatter = new DecimalFormat("#0.00");
                            String prec= formatter.format(lineaPrecio);

                            Feed_detdocu item=new Feed_detdocu();
                            item.setFeedcodart(codart);
                            item.setFeeddesart(descrip);
                            item.setFeedimporte(prec);
                            item.setFeedcantart(unidades);

                            feeddetdocu.add(item);

                            notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }
        }else {
            drawDetDocumento(context,documento);
        }
    }

    private void drawDetDocumento(Context context,String documento){
        SQL_helper helper=new SQL_helper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        Log.d("Adap detdocu","valor de planilla "+Holder_data.nropla);
        Cursor cursor = db.rawQuery("select distinct d.codart,c.aDescrip,d.resto," +
                "d.cant,d.unidades,d.linea " +
                "from detalle d, carga c " +
                "where d.codart=c.aCodart " +
                "and d.codprov=c.codprov and d.tipopla=c.tipopla and d.seriepla=c.seriepla " +
                "and d.nropla=c.nropla and d.fletero=c.fletero " +
                "and d.idcliente="+ Holder_data.codigocliente+" " +
                "and d.nropla="+Holder_data.nropla+" " +
                "and d.fletero="+Holder_data.codcamion+" " +
                "and d.nrodoc="+documento+" " +
                "order by d.idLinea asc", null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                do{
                    double lineaPrecio=cursor.getDouble(5);
                    int resto=cursor.getInt(2);
                    int cant=cursor.getInt(3);
                    int resto_l=cursor.getInt(4);

                    //double precio=((cajaPrecio/resto)*(resto*cant+resto_l)*(100-bonif)/100);
                    int unidades=(resto*cant)+resto_l;
                    NumberFormat formatter = new DecimalFormat("#0.00");
                    String prec= formatter.format(lineaPrecio);

                    Feed_detdocu item=new Feed_detdocu();
                    item.setFeedcodart(cursor.getString(0));
                    item.setFeeddesart(cursor.getString(1));
                    item.setFeedimporte(prec);
                    item.setFeedcantart(String.valueOf(unidades));

                    feeddetdocu.add(item);
                    notifyDataSetChanged();
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    @Override
    public DetdocuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_documento,parent,false);
        return new DetdocuHolder(view);
    }

    @Override
    public void onBindViewHolder(DetdocuHolder holder, int position) {
        final Feed_detdocu item=feeddetdocu.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return feeddetdocu.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }
}
