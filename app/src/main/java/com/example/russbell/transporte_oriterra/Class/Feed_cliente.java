package com.example.russbell.transporte_oriterra.Class;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Russbell on 17/08/2017.
 */

public class Feed_cliente{
    private String feedcodigo;
    private String feednombre;
    private String feeddireccion;
    private String feedcelular;
    private String feedestado;

    public String getFeedcodigo() {
        return feedcodigo;
    }

    public String getFeednombre() {
        return feednombre;
    }

    public String getFeeddireccion() {
        return feeddireccion;
    }

    public String getFeedcelular() {
        return feedcelular;
    }

    public String getFeedestado() {
        return feedestado;
    }

    public void setFeedcodigo(String feedcodigo) {
        this.feedcodigo = feedcodigo;
    }

    public void setFeednombre(String feednombre) {
        this.feednombre = feednombre;
    }

    public void setFeeddireccion(String feeddireccion) {
        this.feeddireccion = feeddireccion;
    }

    public void setFeedcelular(String feedcelular) {
        this.feedcelular = feedcelular;
    }

    public void setFeedestado(String feedestado) {
        this.feedestado = feedestado;
    }
}
