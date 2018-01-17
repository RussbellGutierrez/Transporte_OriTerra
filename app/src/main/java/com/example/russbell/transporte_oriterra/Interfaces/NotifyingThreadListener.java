package com.example.russbell.transporte_oriterra.Interfaces;

import org.json.JSONObject;

/**
 * Created by Russbell on 18/08/2017.
 */

public interface NotifyingThreadListener {
    void ThreadCompleteListener(String volleyName,JSONObject response);
    void AsyncCompleteListener(int count);
}
