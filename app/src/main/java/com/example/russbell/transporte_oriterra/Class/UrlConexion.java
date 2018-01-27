package com.example.russbell.transporte_oriterra.Class;

/**
 * Created by Russbell on 2/06/2017.
 */

public class UrlConexion {

    private String tipo_data="";
    private String chain_url="";
    //pool de IP's
    private String ip_trabajo_lap="192.168.1.128:80";
    private String ip_trabajo_pc="192.168.1.62:80";
    private String ip_casa="192.168.0.101:80";
    private String ip_geny="10.0.3.2";
    private String ip_android="10.0.2.2";
    private String ip_get="192.168.1.203:80";
    //en proceso, cambiar deacuerdo al servidor nuevo
    //private String ip_post="192.168.1.203:3000";
    //private String ip_post="200.48.106.21";
    //private String ip_terranorte="192.168.1.130";
    //private String ip_oriunda="192.168.1.130:8080";
    private String ip_terranorte="200.110.40.58:80";
    private String ip_oriunda="200.110.40.58:8080";
    //private String ip_post="10.0.2.2:8027";
    //private String ip_post="192.168.1.232:8027";
    //private String ip_post="192.168.0.102:8027";
    //private String ip_post="10.34.238.122:8027";

    //el http para la conexion
    private String http;

    public String setIP(String basedatos){
        String http = "";
        switch(basedatos){
            case "oriunda":
                http = "http://"+ip_oriunda+"/";
                break;
            case "terranorte":
                http = "http://"+ip_terranorte+"/";
                break;
        }
        return this.http = http;
    }

    //rest antiguo
    private String rest_cod="/REST/usuarioTransporte/";
    private String rest_carga="/REST/carga/";
    private String rest_cliente="/REST/pedidos/";

    public String urlMultiple(String basedatos,String tipodato){
        if (basedatos.equals("terranorte")){
            String chain_data =dataMultiple(tipodato);
            chain_url=http+"terranorte/app/"+ chain_data;
        }else if (basedatos.equals("oriunda")){
            String chain_data =dataMultiple(tipodato);
            chain_url=http+"oriunda/app/"+ chain_data;
        }
        return chain_url;
    }

    public String urlSocket(String basedatos){
        String url="";
        if (basedatos.equals("terranorte")){
            url="http://"+ip_terranorte+"/terranorte";
        }else {
            url="http://"+ip_oriunda+"/oriunda";
        }
        return url;
    }

    private String dataMultiple(String data){
        switch (data){
            case "transporte":
                tipo_data="authenticate";
                break;
            case "carga":
                tipo_data="fleteros/carga";
                break;
            case "detalle":
                tipo_data="fleteros/detalles";
                break;
            case "documento":
                tipo_data="fleteros/documentos";
                break;
        }
        return tipo_data;
    }
}
