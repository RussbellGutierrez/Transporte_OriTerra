package com.example.russbell.transporte_oriterra.SQL;

import android.provider.BaseColumns;

/**
 * Created by Russbell on 11/04/2017.
 */

public class SQL_columns {

    //===================NUEVAS TABLAS A EMPLEAR======================
    public static abstract class detalle implements BaseColumns{
        public static final String table_name ="detalle";
        public static final String codprov ="codprov";
        public static final String tipopla ="tipopla";
        public static final String seriepla ="seriepla";
        public static final String nropla ="nropla";
        public static final String fletero ="fletero";
        public static final String idcliente ="idcliente";
        public static final String iddocumento ="iddocumento";
        public static final String serie ="serie";
        public static final String nrodoc ="nrodoc";
        public static final String idlinea ="idlinea";
        public static final String codart ="codart";
        public static final String cant ="cant";
        public static final String resto ="resto";
        public static final String unidades ="unidades";
        public static final String precio ="precio";
        public static final String impuesto ="impuesto";
        public static final String bruto ="bruto";
        public static final String descuento ="descuento";
        public static final String linea ="linea";
        public static final String fecha ="fecha";
    }

    public static abstract class documentos implements BaseColumns{
        public static final String table_name ="documentos";
        public static final String sucursal ="sucursal";
        public static final String esquema ="esquema";
        public static final String codprov ="codprov";
        public static final String tipopla ="tipopla";
        public static final String seriepla ="seriepla";
        public static final String nropla ="nropla";
        public static final String fletero ="fletero";
        public static final String cliente ="cliente";
        public static final String nomcli ="nomcli";
        public static final String dircli ="dircli";//agregar
        public static final String telefono ="telefono";
        public static final String negocio ="negocio";
        public static final String XCoord ="XCoord";
        public static final String YCoord ="YCoord";
        public static final String documento ="documento";
        public static final String nrodoc ="nrodoc";
        public static final String total ="total";
        public static final String estado ="estado";
        public static final String fecha ="fecha";
        public static final String ruta ="ruta";
        public static final String tipopago ="tipopago";
    }

    public static abstract class carga implements BaseColumns{
        public static final String table_name ="carga";
        public static final String codprov ="codprov";
        public static final String seriepla ="seriepla";
        public static final String tipopla ="tipopla";
        public static final String nropla ="nropla";
        public static final String fletero ="fletero";
        public static final String aCodart ="aCodart";
        public static final String aDescrip ="aDescrip";
        public static final String aResto ="aResto";
        public static final String aAlmacen ="aAlmacen";
        public static final String aCodbarra ="aCodbarra";
        public static final String aCodbarrauni ="aCodbarrauni";
        public static final String aProveedor ="aProveedor";
        public static final String aLinea ="aLinea";
        public static final String aGenerico ="aGenerico";
        public static final String cant ="cant";
        public static final String resto ="resto";
        public static final String total ="total";
        public static final String observacion ="observacion";
        public static final String fecha ="fecha";//agregar
    }
}
