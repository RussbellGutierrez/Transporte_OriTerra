package com.example.russbell.transporte_oriterra.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Class.NClass_carga;
import com.example.russbell.transporte_oriterra.Class.NClass_detalle;
import com.example.russbell.transporte_oriterra.Class.NClass_documento;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Russbell on 11/04/2017.
 */

public class SQL_sentences {

    private Context context;
    private SQL_helper helper;
    private String TAG="SQL_sentences";
    private List<String> lista=new LinkedList<>();

    public SQL_sentences(Context context){
        this.context=context;
        helper=new SQL_helper(context);
    }

    public void guardarEstado(String estado,String idcliente,String planilla){
        SQLiteDatabase db;
        db=helper.getWritableDatabase();
        if (db!=null){
            ContentValues es=new ContentValues();
            es.put("estado",estado);

            db.update("documentos",es,"cliente="+idcliente+" and nropla="+planilla,null);
            Log.wtf(TAG,"Guardo estado");
        }
    }

    public int existeRegistro(String codigo){
        int cursor=0;
        SQLiteDatabase db;
        db=helper.getReadableDatabase();

        if (db!=null){
            Cursor mcursor = db.rawQuery("SELECT count(*) FROM carga where fletero="+codigo, null);
            mcursor.moveToFirst();
            cursor = mcursor.getInt(0);
            mcursor.close();
        }else {
            Log.i(TAG,"No existen registros");
        }
        return cursor;
    }

    public boolean saveDocumento(NClass_documento doc){
        boolean respuesta=false;
        SQLiteDatabase db;
        db=helper.getWritableDatabase();
        if (db!=null){
            Cursor cursor=db.rawQuery("select nrodoc from documentos where nrodoc="+doc.getNrodoc(),null);
            if (!cursor.moveToFirst()){
                ContentValues indoc=new ContentValues();

                indoc.put("sucursal",doc.getSucursal());
                indoc.put("esquema",doc.getEsquema());
                indoc.put("codprov",doc.getCodprov());
                indoc.put("tipopla",doc.getTipopla());
                indoc.put("seriepla",doc.getSeriepla());
                indoc.put("nropla",doc.getNropla());
                indoc.put("fletero",doc.getFletero());
                indoc.put("cliente",doc.getCliente());
                indoc.put("nomcli",doc.getNomcli());
                indoc.put("dircli",doc.getDircli());
                indoc.put("telefono",doc.getTelefono());
                indoc.put("negocio",doc.getNegocio());
                indoc.put("XCoord",doc.getXCoord());
                indoc.put("YCoord",doc.getYCoord());
                indoc.put("documento",doc.getDocumento());
                indoc.put("nrodoc",doc.getNrodoc());
                indoc.put("total",doc.getTotal());
                indoc.put("estado",doc.getEstado());
                indoc.put("fecha",doc.getFecha());
                indoc.put("ruta",doc.getRuta());
                indoc.put("tipopago",doc.getTipopago());

                db.insert("documentos",null,indoc);
                cursor.close();
            }else {
                Log.i(TAG,"Documento "+doc.getNrodoc()+" guardado anteriormente");
            }
            respuesta=true;
        }
        return respuesta;
    }

    public boolean saveDetalle(NClass_detalle det){
        boolean respuesta=false;
        SQLiteDatabase db;
        db=helper.getWritableDatabase();
        if (db!=null){
            ContentValues indet=new ContentValues();

            indet.put("codprov",det.getCodprov());
            indet.put("tipopla",det.getTipopla());
            indet.put("seriepla",det.getSeriepla());
            indet.put("nropla",det.getNropla());
            indet.put("fletero",det.getFletero());
            indet.put("idcliente",det.getIdcliente());
            indet.put("iddocumento",det.getIddocumento());
            indet.put("serie",det.getSerie());
            indet.put("nrodoc",det.getNrodoc());
            indet.put("idlinea",det.getIdlinea());
            indet.put("codart",det.getCodart());
            indet.put("cant",det.getCant());
            indet.put("resto",det.getResto());
            indet.put("unidades",det.getUnidades());
            indet.put("precio",det.getPrecio());
            indet.put("impuesto",det.getImpuesto());
            indet.put("bruto",det.getBruto());
            indet.put("descuento",det.getDescuento());
            indet.put("linea",det.getLinea());
            indet.put("fecha",det.getFecha());

            db.insert("detalle",null,indet);
            respuesta=true;
        }
        return respuesta;
    }

    public boolean saveCarga(NClass_carga car){
        boolean respuesta=false;
        SQLiteDatabase db;
        db=helper.getWritableDatabase();
        if (db!=null){
            ContentValues incar=new ContentValues();

            incar.put("codprov",car.getCodprov());
            incar.put("seriepla",car.getSeriepla());
            incar.put("tipopla",car.getTipopla());
            incar.put("nropla",car.getNropla());
            incar.put("fletero",car.getFletero());
            incar.put("aCodart",car.getaCodart());
            incar.put("aDescrip",car.getaDescrip());
            incar.put("aResto",car.getaResto());
            incar.put("aAlmacen",car.getaAlmacen());
            incar.put("aCodbarra",car.getaCodbarra());
            incar.put("aCodbarrauni",car.getaCodbarrauni());
            incar.put("aProveedor",car.getaProveedor());
            incar.put("aLinea",car.getaLinea());
            incar.put("aGenerico",car.getaGenerico());
            incar.put("cant",car.getCant());
            incar.put("resto",car.getResto());
            incar.put("total",car.getTotal());
            incar.put("observacion",car.getObservacion());
            incar.put("fecha",car.getFecha());

            db.insert("carga",null,incar);
            respuesta=true;
        }
        return respuesta;
    }

    public void saveObservacion(String camion, String articulo, String planilla, String observacion){
        SQLiteDatabase db;
        db=helper.getWritableDatabase();
        if (db!=null){
            ContentValues obs=new ContentValues();
            obs.put("observacion",observacion);
            db.update("carga",obs,"fletero="+camion+" and aCodart="+articulo+" and nropla="+planilla,null);
        }else {
            Toast.makeText(context,"No se pudo guardar observación del producto",Toast.LENGTH_SHORT).show();
        }
    }

    public String searchScan(String barra,String planilla){
        String codigo="";
        SQLiteDatabase db;
        db=helper.getReadableDatabase();
        if (db!=null){
            //String[] arg=new String[] {barra};
            Cursor cursor=db.rawQuery("select aCodart " +
                    "from carga " +
                    "where aCodbarrauni="+barra+" and nropla="+planilla,null);
            if (cursor.moveToFirst()){
                codigo=cursor.getString(0);
                cursor.close();
            }else {
                Toast.makeText(context,"No existe el producto",Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        }else {
            Toast.makeText(context,"No tiene datos de producto para escanear",Toast.LENGTH_SHORT).show();
        }
        return codigo;
    }

    public void truncateTB(){
        SQLiteDatabase db;
        db=helper.getWritableDatabase();
        if (db!=null){
            db.delete("documentos",null,null);
            db.delete("detalle",null,null);
            db.delete("carga",null,null);
            Toast.makeText(context,"Datos eliminados",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"No existe base de datos",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkBD(){
        boolean resultado=false;
        SQLiteDatabase db;
        db=helper.getReadableDatabase();
        if (db!=null){
            Cursor carga=db.rawQuery("select count(*) from carga",null);
            Cursor detalle=db.rawQuery("select count(*) from detalle",null);
            Cursor documentos=db.rawQuery("select count(*) from documentos",null);

            carga.moveToFirst();
            detalle.moveToFirst();
            documentos.moveToFirst();

            if (carga.getInt(0)!=0 || detalle.getInt(0)!=0 || documentos.getInt(0)!=0){
                resultado=true;
            }

        }else {
            resultado=false;
            Toast.makeText(context,"No existe base de datos",Toast.LENGTH_SHORT).show();
        }
        return resultado;
    }

    public String[] detalleDia(){
        String[] resultado=new String[4];
        SQLiteDatabase db;
        db=helper.getReadableDatabase();
        if (db!=null){
            Cursor documentos=db.rawQuery("select count(*) from documentos",null);
            Cursor clientes=db.rawQuery("select count(distinct cliente) from documentos",null);
            Cursor contados=db.rawQuery("select count(*) from documentos where tipopago='CONTADO'",null);
            Cursor corrientes=db.rawQuery("select count(*) from documentos where tipopago='CUENTA CORRIENTE'",null);

            documentos.moveToFirst();
            clientes.moveToFirst();
            contados.moveToFirst();
            corrientes.moveToFirst();

            resultado[0]=String.valueOf(documentos.getInt(0));
            resultado[1]=String.valueOf(clientes.getInt(0));
            resultado[2]=String.valueOf(contados.getInt(0));
            resultado[3]=String.valueOf(corrientes.getInt(0));
        }
        return resultado;
    }
}
