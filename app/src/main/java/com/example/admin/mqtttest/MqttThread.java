package com.example.admin.mqtttest;
import android.util.Log;
import net.sf.xenqtt.client.AsyncClientListener;
import net.sf.xenqtt.client.AsyncMqttClient;
import net.sf.xenqtt.client.MqttClient;
import net.sf.xenqtt.client.PublishMessage;
import net.sf.xenqtt.client.Subscription;
import net.sf.xenqtt.message.ConnectReturnCode;
import net.sf.xenqtt.message.QoS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;


public abstract class MqttThread implements Runnable {

    private BlockingQueue<JSONObject> messageQueue;
    private Thread innerThread;
    private boolean stopInnerThread = false;
    private String mqttBrokerURL = "tcp://sysnet.utcc.ac.th:1883";
    public AsyncClientListener mqttListener = null;
    public MqttClient mqttClient = null;
    public int mqttHandlerThreadPoolSize = 5;

    public MqttThread() {
    }
    public void start() {
        try {
            createListener();
            createClient();
            innerThread = new Thread(this);
            innerThread.start();
        } catch (Exception e) {

            Log.i("MQT2","start exception (MqttThread): " + e);
        }
    }

    public void stop() {
        try {
            stopInnerThread = true;
            destroyPublisher();
        } catch (Exception e) {
            Log.i("MQT2","stop exception (MqttThread): " + e);
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(!stopInnerThread){
            try{
                Thread.sleep(100);
            }catch(Exception e){

                Log.i("MQT2","run exception (MqttThread): " + e);
            }//end try
        }//end while
    }


    public boolean isStopInnerThread() {
        return stopInnerThread;
    }

    public void setStopInnerThread(boolean stopInnerThread) {
        this.stopInnerThread = stopInnerThread;
    }
    public abstract void createListener();
    public abstract void createClient();
    public void destroyPublisher() {
        if (!mqttClient.isClosed()) {
            mqttClient.disconnect();
        }
    }

}
