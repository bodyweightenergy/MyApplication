package com.example.hsalem.myapplication;

import android.app.Activity;
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

public class SettingsActivity extends Activity {

    //declare Views and variables
    public static final String PREF_FILE = "MyPrefFile";
    private EditText Access_textBox = null;
    private EditText IP_textBox = null;
    private EditText port_textBox = null;

    //temporary holders for pref values
    private int temp_AccessCode;
    private String temp_IP;
    private int temp_Port;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //update prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        temp_AccessCode = Prefs.getAccessCode(getApplicationContext());
        temp_IP = Prefs.getIPAddress(getApplicationContext());
        temp_Port = Prefs.getPortNumber(getApplicationContext());

        String MACAddress = Utils.getMACAddress("wlan0");
        String ipAddress = Utils.getIPAddress(true); // IPv4
        /*WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();*/

        //Match Views with IDs and update
        Access_textBox = (EditText) findViewById(R.id.editTextAccess);
        Access_textBox.setText(String.valueOf(temp_AccessCode));
        IP_textBox = (EditText) findViewById(R.id.editTextIP);
        IP_textBox.setText(String.valueOf(temp_IP));
        port_textBox = (EditText) findViewById(R.id.editTextPort);
        port_textBox.setText(String.valueOf(temp_Port));

        //create TextWatcher for Access code editText
        Access_textBox.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){
                temp_AccessCode = convertString2Int(Access_textBox.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        //create TextWatcher for IP editText
        IP_textBox.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){
                temp_IP = IP_textBox.getText().toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        //create TextWatcher for Port editText
        port_textBox.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s){
                temp_Port = convertString2Int(port_textBox.getText().toString());
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
            case R.id.action_discard:
                discardSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void discardSettings(){
        Toast invalidPortToast = Toast.makeText(getApplicationContext(), getString(R.string.discardSettingsToast), Toast.LENGTH_SHORT);
        invalidPortToast.show();
        //switch to MainActivity
        Intent saveSettings_intent = new Intent(this, MainActivity.class);
        saveSettings_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(saveSettings_intent);
    }
    public void saveSettings(){
        boolean isValidAccess = isValidAccess(temp_AccessCode);
        boolean isValidIP = isValidIP(temp_IP);
        boolean isValidPort = isValidPort(temp_Port);

        if((isValidAccess && isValidIP) && isValidPort) {
            Prefs.setAccessCode(getApplicationContext(), temp_AccessCode);
            Prefs.setIPAddress(getApplicationContext(), temp_IP);
            Prefs.setPortNumber(getApplicationContext(), temp_Port);

            //switch to MainActivity
            Intent saveSettings_intent = new Intent(this, MainActivity.class);
            saveSettings_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            if(!isValidPort){
                Toast invalidPortToast = Toast.makeText(getApplicationContext(), getString(R.string.invalidPortToast), Toast.LENGTH_SHORT);
                invalidPortToast.show();
            }

        }
    }
    private boolean isValidAccess(int value){
        boolean valid;
        valid = ((value>=0 && value<=9999) ? true : false);
        if(!valid) {
            Toast invalidAccessToast = Toast.makeText(getApplicationContext(), getString(R.string.invalidAccessToast), Toast.LENGTH_SHORT);
            invalidAccessToast.show();
        }
        return valid;
    }

    private boolean isValidIP(String value){
        boolean valid;
        valid = IPAddressValidator.validate(value);
        if(!valid) {
            Toast invalidIPToast = Toast.makeText(getApplicationContext(), getString(R.string.invalidIPToast), Toast.LENGTH_SHORT);
            invalidIPToast.show();
        }
        return valid;
    }

    private boolean isValidPort(int value){
        boolean valid;
        valid = ((value>=0 && value<=255) ? true : false);
        if(!valid) {
            Toast invalidPortToast = Toast.makeText(getApplicationContext(), getString(R.string.invalidPortToast), Toast.LENGTH_SHORT);
            invalidPortToast.show();
        }
        return valid;
    }

    private int convertString2Int(String value){
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
