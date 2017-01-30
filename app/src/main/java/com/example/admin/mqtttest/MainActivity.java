package com.example.admin.mqtttest;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.sf.xenqtt.client.AsyncClientListener;
import net.sf.xenqtt.client.AsyncMqttClient;
import net.sf.xenqtt.client.MqttClient;
import net.sf.xenqtt.client.PublishMessage;
import net.sf.xenqtt.client.Subscription;
import net.sf.xenqtt.message.ConnectReturnCode;
import net.sf.xenqtt.message.QoS;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


public class MainActivity extends AppCompatActivity {


    private String mqttBrokerURL = "tcp://sysnet.utcc.ac.th:1883";
    private String mqttUser = "admin";
    private String mqttPwd = "admin";
    private Hashtable<String, MqttThread> mqttThreadHT = new Hashtable<String, MqttThread>();
    private Handler handler = null;
    private String sssn = "admin";
    private String topic = "aparcas_raw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("ben", "onCreate");
        MqttThread mqttThread = createMQTTThread(sssn, topic);
        Log.i("ben", "onCreate (1)");
        mqttThread.start();
        Log.i("ben", "onCreate (2)");
        mqttThreadHT.put(sssn, mqttThread);
        Log.i("ben", "onCreate (3)");
    }
    private MqttThread createMQTTThread(final String sssn, final String topic) {
        return new MqttThread() {
            @Override
            public void createListener() {
                Log.i("ben", "createListener");

                // TODO Auto-generated method stub
                final CountDownLatch connectLatch = new CountDownLatch(1);
                final AtomicReference<ConnectReturnCode> connectReturnCode = new AtomicReference<ConnectReturnCode>();
                mqttListener = new AsyncClientListener() {
                    @Override
                    public void publishReceived(MqttClient client, final PublishMessage message) {

                        final PublishMessage msg = message;
                        String mMsg = msg.getPayloadString().toString();
                        try {
                            Data data = new Data();
                            Log.i("ben", "publishReceived: " +mMsg);
                            data.setData(mMsg);
                            Log.i("ben", "publishReceived2: " + data.getLat());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        message.ack();
                    }
                    @Override
                    public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {

                        if (cause != null) {
                            Log.i("MQT2", "Disconnected from the broker due to an exception - " + cause);
                        } else {

                            Log.i("MQT2", "Disconnected from the broker.");
                        }
                        if (reconnecting) {

                            Log.i("MQT2", "Attempting to reconnect to the broker.");
                        }
                    }

                    @Override
                    public void connected(MqttClient client, ConnectReturnCode returnCode) {
                        Log.i("ben", "connected");
                        connectReturnCode.set(returnCode);
                        connectLatch.countDown();
                    }

                    @Override
                    public void published(MqttClient arg0, PublishMessage arg1) {
                        // TODO Auto-generated method stub
                        Log.i("ben", "published");

                    }

                    @Override
                    public void subscribed(MqttClient arg0,
                                           Subscription[] arg1, Subscription[] arg2,
                                           boolean arg3) {
                        // TODO Auto-generated method stub
                        Log.i("ben", "subscribed");

                    }

                    @Override
                    public void unsubscribed(MqttClient arg0, String[] arg1) {
                        // TODO Auto-generated method stub
                        Log.i("ben", "unsubscribed");

                    }
                };
            }//end createListener
            public void createClient() {
                // TODO Auto-generated method stub
                Log.i("ben", "createClient");
                mqttClient = new AsyncMqttClient(mqttBrokerURL, mqttListener, mqttHandlerThreadPoolSize);
                try {
                    Log.i("ben", "createClient in try");
                    mqttClient.connect(topic, true, mqttUser, mqttPwd);
                    Log.i("ben", "createClient after connect");
                    List<Subscription> subscriptions = new ArrayList<Subscription>();
                    subscriptions.add(new Subscription(topic, QoS.AT_MOST_ONCE));
                    mqttClient.subscribe(subscriptions);
                    Log.i("ben", "createClient after subscribe");

                } catch (Exception e) {
                    Log.i("MQT2", "An exception prevented the publishing of the full catalog." + e);
                }
            }//end createClient

        };//end new MqttThread

    }//end createMQTTThread()



}

