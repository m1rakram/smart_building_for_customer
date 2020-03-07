package com.example.admin.widesmart;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class ProjActivity extends AppCompatActivity {


    private String server_uri = "tcp://io.adafruit.com:1883";
    private String id = "0e5ed63a00884f63b14900234cbcfb93";
    private String username = "mirakram";
    private String publish_topic;
    private String publish_topic5 = "mirakram/feeds/garagesub";
    private String publish_topic4 = "mirakram/feeds/elevatorsub";
    private String publish_topic3 = "mirakram/feeds/motorsub";
    private String publish_topic2 = "mirakram/feeds/lightsub";
    private String publish_topic1 = "mirakram/feeds/generalsub";
    private String subscribe_topic;
    private String subscribe_topic1 = "mirakram/feeds/generalinfo";
    private String subscribe_topic2 = "mirakram/feeds/lightDetections";
    private String subscribe_topic3 = "mirakram/feeds/motors";
    private String subscribe_topic4 = "mirakram/feeds/elevator";
    private String subscribe_topic5 = "mirakram/feeds/garage";
    private int qos = 0;
    public String seekvalue;
    private MqttAndroidClient client;
    ProjActivity.MyMqttCallBack mqtt = new  ProjActivity.MyMqttCallBack();
    public SharedPreferences info;

    LinearLayout rgb1, rgb2, rgb3, rgb4, rgb5, rgb6, rgb7, rgb8, rgb9, cf2;
    String color="27#0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj);
        client = new MqttAndroidClient(getApplicationContext(), server_uri, id);
        connectServer();
        rgb1=(LinearLayout) findViewById(R.id.rgb1);
        rgb2=(LinearLayout) findViewById(R.id.rgb2);
        rgb3=(LinearLayout) findViewById(R.id.rgb3);
        rgb4=(LinearLayout) findViewById(R.id.rgb4);
        rgb5=(LinearLayout) findViewById(R.id.rgb5);
        rgb6=(LinearLayout) findViewById(R.id.rgb6);
        rgb7=(LinearLayout) findViewById(R.id.rgb7);
        rgb8=(LinearLayout) findViewById(R.id.rgb8);
        rgb9=(LinearLayout) findViewById(R.id.rgb9);
        cf2=(LinearLayout) findViewById(R.id.cf2);

        rgb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color="27#100";
            }
        });

        rgb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color="26#100";
            }
        });

        rgb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color="824#100";
            }
        });

        rgb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rgb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rgb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rgb7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        rgb8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rgb9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color="000";
            }
        });

        cf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishData(color, publish_topic2);
            }
        });
    }
    public void connectServer()
    {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        char[] password =  id.toCharArray();
        options.setPassword(password);
        options.setUserName(username);

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("Wotah", "Connected");
                    subscribeData(subscribe_topic1);
                    subscribeData(subscribe_topic2);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("Wotah", "NOT Connected");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void subscribeData(String topic)
    {
        subscribe_topic=topic;
        try {
            IMqttToken subToken = client.subscribe(subscribe_topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Log.d("Wotah","Subscribed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Log.d("Wotah","NOT Subscribed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(mqtt);

    }
    public void publishData(String data, String publish_topic)
    {

        String payload = data;
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);
            message.setRetained(true);
            message.setPayload(encodedPayload);

            IMqttToken token = client.publish(publish_topic, message);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Log.d("Wotah","Published");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("Wotah","NOT Published");
                }
            });
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
    /**********************************************************************************************/


    public class MyMqttCallBack implements MqttCallback {
        private String message;
        public String getMessage()
        {
            return message;
        }

        @Override
        public void connectionLost(Throwable cause) {
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            this.message = message.toString();
            identifytopic(topic.trim(), this.message);
            Log.d("Wotah", topic + " " + message);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }

    }

    public void identifytopic(String text, String mesaj){
        if(text.equals(subscribe_topic1)) {
            topic1work(mesaj);
        }

        if (text.equals(subscribe_topic2)) {
            StringBuilder builder = new StringBuilder(mesaj);
            int middle = builder.indexOf("%%");
            int end = mesaj.length();
            String number = builder.substring(0, middle).trim();
            String perc = builder.substring(middle + "%%".length(), end).trim();
            topic2work(Integer.parseInt(number), Integer.parseInt(perc));
        }
        if(text.equals(subscribe_topic4)) {
            topic4work(mesaj);
        }



    }

    public void topic1work(String mesaj){
        SharedPreferences.Editor editing=info.edit();
        StringBuilder builder = new StringBuilder(mesaj);
        if(mesaj.trim().contains("#")) {
            int middle = builder.indexOf("#");
            int end = mesaj.length();
            String number = builder.substring(0, middle).trim();
            String gas = builder.substring(middle + "#".length(), end).trim();
            editing.putInt("gas", Integer.parseInt(gas));
        }

        if(mesaj.trim().contains("&")){
            int middle = builder.indexOf("*&*");
            int end = builder.indexOf("(&)");
            String extemp = builder.substring(0, middle).trim();
            String exphoto = builder.substring(middle + "*&*".length(), end).trim();
            String inphoto=builder.substring(end+"(&)".length(), mesaj.length());
            editing.putInt("exphoto", Integer.parseInt(exphoto));
            editing.putInt("extemp", Integer.parseInt(extemp));
            editing.putInt("inphoto", Integer.parseInt(inphoto));
        }

        if(mesaj.trim().contains("$")){
            int middle = builder.indexOf("($)");
            int end = builder.indexOf("*$");
            String buf21 = builder.substring(0, middle).trim();
            String buf22 = builder.substring(middle + "($)".length(), end).trim();
            String buf31 = builder.substring(builder.indexOf("*$")+"*$".length(), builder.indexOf("$*") ).trim();
            String buf32 = builder.substring("$*".length()+builder.indexOf("$*"), mesaj.length() ).trim();
            editing.putInt("2Left", Integer.parseInt(buf21));
            editing.putInt("2Right", Integer.parseInt(buf22));
            editing.putInt("2Left", Integer.parseInt(buf31));
            editing.putInt("3Right", Integer.parseInt(buf32));

        }

        editing.commit();
    }

    public void topic2work(int n, int value){
        SharedPreferences.Editor editing=info.edit();
        if(n==5){
            editing.putInt("2Left", value);
        }
        else{
            if(n==2) {
                editing.putInt("2Right", value);
            }
        }
        editing.commit();
    }

    public void topic3work(){

    }

    public void topic4work(String mesaj){
        SharedPreferences.Editor editing=info.edit();
        editing.putInt("floor", Integer.parseInt(mesaj));
        editing.commit();
    }

    public void topic5work(){

    }
}

