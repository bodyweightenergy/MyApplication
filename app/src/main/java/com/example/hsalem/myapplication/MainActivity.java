package com.example.hsalem.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class MainActivity extends Activity {
    //declare Views
    public static final String PREF_FILE = "MyPrefFile";
    Switch slider1;
    Switch slider2;
    Switch slider3;
    Switch slider4;
    TextView textlog;//Log for outputs
    Button buttonConnect;//(dis)connect Button
    TextView seekBarValue;//Textfield displaing the Value of the seekbar
    ProgressBar progressbar;//progressbar showing the poti rotation
    Button buttonToggle1;
    Button buttonToggle2;
    Button buttonToggle3;
    Button buttonToggle4;
    public String[] inComingStringArray = {""};
    public String inComingString = "$";

    boolean connected=false;//stores the connectionstatus

    NetworkTask networktask;//networktask is the included class to handle the socketconnection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //initialize debug textView
        textlog = (TextView) findViewById(R.id.debugTextView);
        textlog.setText("Everything OK.\n");


        //initialize switch indicators
        slider1 = (Switch) findViewById(R.id.switch1);
        slider2 = (Switch) findViewById(R.id.switch2);
        slider3 = (Switch) findViewById(R.id.switch3);
        slider4 = (Switch) findViewById(R.id.switch4);


        //initialize toggle button states
        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonToggle1 = (Button) findViewById(R.id.toggleButton1);
        buttonToggle2 = (Button) findViewById(R.id.toggleButton2);
        buttonToggle3 = (Button) findViewById(R.id.toggleButton3);
        buttonToggle4 = (Button) findViewById(R.id.toggleButton4);

        //update toggle indicators
        updateToggleIndicators(getApplicationContext());

        //set onClickListeners for toggle buttons


        textlog.setText("Starting Client");//log that the App launched
        changeConnectionStatus(false);//change connectionstatus to "disconnected"

        //add EventListeners
        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        buttonToggle1.setOnClickListener(buttonToggle1OnClickListener);
        buttonToggle2.setOnClickListener(buttonToggle2OnClickListener);
        buttonToggle3.setOnClickListener(buttonToggle3OnClickListener);
        buttonToggle4.setOnClickListener(buttonToggle4OnClickListener);

        networktask = new NetworkTask();//Create initial instance so SendDataToNetwork doesn't throw an error.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.my, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openSettings(){
        if(networktask!=null){//In case the task is currently running
            networktask.closeSocket();
            networktask.cancel(true);//cancel the task
        }
        Intent openSettings_intent = new Intent(this, SettingsActivity.class);

        startActivity(openSettings_intent);
    }

    public void updateToggleIndicators(Context context){
        boolean done = false;
        boolean t1 = false; //place holders for preference values
        boolean t2 = false;
        boolean t3 = false;
        boolean t4 = false;
        if(connected) {
                outputText("updating indicators with string="+inComingString);
                for (int i = 0; i < inComingString.length(); i++) {
                    if (inComingString.charAt(i) == '<') {
                        if (inComingString.charAt(i + 5) == '>') {
                            t1 = (inComingString.charAt(i + 1) != '0');
                            t2 = (inComingString.charAt(i + 2) != '0');
                            t3 = (inComingString.charAt(i + 3) != '0');
                            t4 = (inComingString.charAt(i + 4) != '0');
                            done = true;
                        }

                    }
                }
        }

        if(done){
            Prefs.setRelayStatus1(getApplicationContext(), t1);
            Prefs.setRelayStatus2(getApplicationContext(), t2);
            Prefs.setRelayStatus3(getApplicationContext(), t3);
            Prefs.setRelayStatus4(getApplicationContext(), t4);
        }
        slider1.setChecked(Prefs.getRelayStatus1(context));
        slider2.setChecked(Prefs.getRelayStatus2(context));
        slider3.setChecked(Prefs.getRelayStatus3(context));
        slider4.setChecked(Prefs.getRelayStatus4(context));
        slider1.setBackgroundColor((Prefs.getRelayStatus1(context) ? 0xFF00FF00 : 0xFF777777));
        slider2.setBackgroundColor((Prefs.getRelayStatus2(context) ? 0xFF00FF00 : 0xFF777777));
        slider3.setBackgroundColor((Prefs.getRelayStatus3(context) ? 0xFF00FF00 : 0xFF777777));
        slider4.setBackgroundColor((Prefs.getRelayStatus4(context) ? 0xFF00FF00 : 0xFF777777));
    }
    // ----------------------- CONNECT BUTTON EVENTLISTENER - begin ----------------------------
    private View.OnClickListener buttonConnectOnClickListener = new View.OnClickListener() {
        public void onClick(View v){
            if(!connected){//if not connected
                outputText("connecting to Server");
                networktask = new NetworkTask(); //New instance of NetworkTask
                networktask.execute();
            }else{
                outputText("disconnecting from Server...");
                if(networktask!=null){
                    networktask.closeSocket();
                    networktask.cancel(true);
                }
            }
        }
    };
    private View.OnClickListener buttonToggle1OnClickListener = new View.OnClickListener() {
        public void onClick(View v){
            networktask.SendDataToNetwork("1");
            outputText("Toggle 1.");
        }
    };
    private View.OnClickListener buttonToggle2OnClickListener = new View.OnClickListener() {
        public void onClick(View v){
            networktask.SendDataToNetwork("2");
            outputText("Toggle 2.");
        }
    };
    private View.OnClickListener buttonToggle3OnClickListener = new View.OnClickListener() {
        public void onClick(View v){
            networktask.SendDataToNetwork("3");
            outputText("Toggle 3.");
        }
    };
    private View.OnClickListener buttonToggle4OnClickListener = new View.OnClickListener() {
        public void onClick(View v){
            networktask.SendDataToNetwork("4");
            outputText("Toggle 4.");
        }
    };
    // ----------------------- CONNECT BUTTON EVENTLISTENER - end ----------------------------

    // ----------------------- THE NETWORK TASK - begin ----------------------------
    public class NetworkTask extends AsyncTask<Void, String, Boolean> {
        Socket nsocket; //Network Socket
        InputStream nis; //Network Input Stream
        OutputStream nos; //Network Output Stream
        BufferedReader inFromServer;//Buffered reader to store the incoming bytes
        final String prefAddress = Prefs.getIPAddress(getApplicationContext());

        protected void onPreExecute() {
            //change the connection status to "connected" when the task is started
            changeConnectionStatus(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) { //This runs on a different thread
            boolean result = false;
            try {
                //create a new socket instance
                SocketAddress sockaddr = new InetSocketAddress(prefAddress, 23);
                nsocket = new Socket();
                nsocket.connect(sockaddr, 5000);//connect and set a 10 second connection timeout
                if (nsocket.isConnected()) {//when connected
                    nis = nsocket.getInputStream();//get input
                    nos = nsocket.getOutputStream();//and output stream from the socket
                    inFromServer = new BufferedReader(new InputStreamReader(nis));//"attach the inputstreamreader"
//                    String msgFromServer = inFromServer.readLine();//read the lines coming from the socket
//                    byte[] theByteArray = msgFromServer.getBytes();//store the bytes in an array
//                    publishProgress(msgFromServer);//update the publishProgress
                    while(true){//while connected
                        String msgFromServer = inFromServer.readLine();//read the lines coming from the socket
//                        byte[] theByteArray = msgFromServer.getBytes();//store the bytes in an array
                        publishProgress(msgFromServer);//update the publishProgress
                    }
                }
                //catch exceptions
            } catch (IOException e) {
                e.printStackTrace();
                result = true;
            }
            catch (Exception e) {
                e.printStackTrace();
                result = true;
                }
                finally {
                closeSocket();
            }
            return result;
        }

        //Method closes the socket
        public void closeSocket(){
            try {
                nis.close();
                nos.close();
                nsocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Method tries to send Strings over the socket connection
        public void SendDataToNetwork(String cmd) { //You run this from the main thread.
            try {
                if (nsocket.isConnected()) {
                    nos.write(cmd.getBytes());
                } else {
                    outputText("SendDataToNetwork: Cannot send message. Socket is closed");
                }
            } catch (Exception e) {
                outputText("SendDataToNetwork: Message send failed. Caught an exception");
            }
        }

        //Methods is called every time a new String is recieved from the socket connection
        @Override
        protected void onProgressUpdate(String... values) {
           String in = values[0];  //get last string
            inComingString = in;
            updateToggleIndicators(getApplicationContext());
        }

        //Method is called when task is cancelled
        @Override
        protected void onCancelled() {
            changeConnectionStatus(false);//change the connection to "disconnected"
        }

        //Method is called after task execution
        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {
                outputText("onPostExecute: Completed with an Error.");
            } else {
                outputText("onPostExecute: Completed.");
            }
            changeConnectionStatus(false);//change connection status to disconnected
        }
    }


    // ----------------------- THE NETWORK TASK - end ----------------------------
// Method changes the connection status
    public void changeConnectionStatus(boolean sConnected) {
        connected=sConnected;//change variable
        if(sConnected){//if connection established
            outputText("successfully connected to server");//log
            buttonConnect.setText("disconnect");//change Buttontext
        }
        else if(!sConnected){
            outputText("disconnected from Server!");//log
            buttonConnect.setText("connect");//change Buttontext
        }
    }

    //Method appends text to the textfield and adds a newline character
    public void outputText(String msg) {
        int scrollAmount;
        // append the new string
        textlog.append(msg + "\n");
        // find the amount we need to scroll.  This works by
        // asking the TextView's internal layout for the position
        // of the final line and then subtracting the TextView's height
        try {
            scrollAmount = textlog.getLayout().getLineTop(textlog.getLineCount()) - textlog.getHeight();
        } catch (NullPointerException e){
            scrollAmount = 0;
        }
        // if there is no need to scroll, scrollAmount will be <=0
        if (scrollAmount > 0)
            textlog.scrollTo(0, scrollAmount);
        else
            textlog.scrollTo(0, 0);
    }

    //Method is called when app is closed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(networktask!=null){//In case the task is currently running
            networktask.closeSocket();
            networktask.cancel(true);//cancel the task
        }
    }
}
