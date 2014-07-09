package com.example.hsalem.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.hsalem.myapplication.IPAddressValidator;

import java.net.InetAddress;
import java.net.Socket;

public class SettingsActivity extends Activity {

    //declare Views and variables
    public static final String PREF_FILE = "MyPrefFile";
    private boolean isValidAccess = false;
    private boolean isValidIP = false;
    private boolean isValidPort = false;
    private EditText Access_textBox = null;
    private EditText IP_textBox = null;
    private EditText port_textBox = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //update prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        String MACAddress = Utils.getMACAddress("wlan0");
        String ipAddress = Utils.getIPAddress(true); // IPv4
        /*WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();*/

        //Match Views with IDs and update
        Access_textBox = (EditText) findViewById(R.id.editTextAccess);
        Access_textBox.setText(String.valueOf(Prefs.getAccessCode(getApplicationContext())));
        IP_textBox = (EditText) findViewById(R.id.editTextIP);
        IP_textBox.setText(String.valueOf(Prefs.getIPAddress(getApplicationContext())));
        port_textBox = (EditText) findViewById(R.id.editTextPort);
        port_textBox.setText(String.valueOf(Prefs.getPortNumber(getApplicationContext())));

        //create TextWatcher for Access code editText
        Access_textBox.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){
                int code = Integer.valueOf(Access_textBox.getText().toString());
                isValidAccess = ((code>=0 && code<=9999) ? true : false);
                if(!isValidAccess) {
                    //Access_textBox.setText("");
                    Toast invalidAccessToast = Toast.makeText(getApplicationContext(), getString(R.string.invalidAccessToast), Toast.LENGTH_SHORT);
                    invalidAccessToast.show();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        //create TextWatcher for IP editText
        IP_textBox.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){

                isValidAccess = IPAddressValidator.validate(IP_textBox.getText().toString());
                if(!isValidAccess) {
                    //Access_textBox.setText("");
                    Toast invalidIPToast = Toast.makeText(getApplicationContext(), getString(R.string.invalidIPToast), Toast.LENGTH_SHORT);
                    invalidIPToast.show();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                saveSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void saveSettings(){

        if(isValidAccess) {
            int access_code = Integer.valueOf(Access_textBox.getText().toString());
            Prefs.setAccessCode(getApplicationContext(), access_code);
            String ip_address = IP_textBox.getText().toString();
            Prefs.setIPAddress(getApplicationContext(), ip_address);
            int port_number = Integer.valueOf(port_textBox.getText().toString());
            Prefs.setPortNumber(getApplicationContext(), port_number);

            //switch to MainActivity
            Intent saveSettings_intent = new Intent(this, MainActivity.class);
            startActivity(saveSettings_intent);
        }
        else {
            if(!isValidAccess){
                Toast invalidAccessToast = Toast.makeText(getApplicationContext(), getString(R.string.invalidAccessToast), Toast.LENGTH_SHORT);
                invalidAccessToast.show();
            }
            if(!isValidIP){
                Toast invalidIPToast = Toast.makeText(getApplicationContext(), getString(R.string.invalidIPToast), Toast.LENGTH_SHORT);
                invalidIPToast.show();
            }

        }
    }


}
