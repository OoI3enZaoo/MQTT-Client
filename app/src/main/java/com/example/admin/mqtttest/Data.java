package com.example.admin.mqtttest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 31/1/2560.
 */

public class Data {

    JSONObject jsonObject;
    Data(){

    }
    public void setData(String data) throws JSONException {
        JSONObject mainObject = new JSONObject(data);
        this.jsonObject = mainObject;
    }
    public String getLat(){
        try {
            return jsonObject.getString("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
