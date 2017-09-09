package com.example.hagertarek.cubiiblut;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by HagEr TaReK on 27/08/2017.
 */
public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_ENABLE_BT = 1;

    private final UUID myUUID =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothAdapter mBluetoothAdapter = null;
    Button btnConn,btnOn,btnOff;
    String deviceMac,deviceName;
    ArrayList<String> pairedDeviceArrayList;
    ArrayAdapter<String> pairedDeviceAdapter;
    private static final String TAG = "LumaMini";
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private static String address ;
    //ThreadConnectBTdevice myThreadConnectBTdevice;
  //  ThreadConnected myThreadConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConn =(Button)findViewById(R.id.bttnConn);
        btnOn =(Button)findViewById(R.id.turnOn);
        btnOff = (Button)findViewById(R.id.turnOff);
        mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
        checkBTState();


        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    // Send "a" via Bluetooth
                    sendData("a");
                    Toast.makeText(getBaseContext(), "Turn On", Toast.LENGTH_SHORT).show();


            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    // Send "a" via Bluetooth
                    sendData("b");
                    Toast.makeText(getBaseContext(), "Turn Off", Toast.LENGTH_SHORT).show();


            }
        });
    }
    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(mBluetoothAdapter==null) {
            Toast.makeText(getBaseContext(), "not supported", Toast.LENGTH_SHORT).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {

            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }
    public void showDialog(View v) {
        //dialog with style:CustomDialogTheme
        final Dialog dialogLang = new Dialog(this, R.style.CustomDialogTheme);
        //use custom design for dialog
        dialogLang.setContentView(R.layout.custom_dialog);
        //set dialog size
        dialogLang.getWindow().setLayout(1000, 1000);

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            pairedDeviceArrayList = new ArrayList<>();

            for (BluetoothDevice device : pairedDevices) {
                deviceMac = device.getAddress();
                deviceName =device.getName();
                pairedDeviceArrayList.add(deviceName+"\n"+deviceMac);
            }

            pairedDeviceAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, pairedDeviceArrayList);
            ListView listViewPairedDevice = (ListView)dialogLang.findViewById(R.id.list);
            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                      String add = (String) parent.getItemAtPosition(position);
                    address = add.substring(add.length() - 17);
                    onResume(address);
                    Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();

                   //  myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
                    // myThreadConnectBTdevice.start();
                    dialogLang.dismiss();
                }
            });
        }else{
            Toast.makeText(MainActivity.this,
                    "No Paired Devices..Pair Your Device ",
                    Toast.LENGTH_LONG).show();
        }
        //define Cancel Button and Listener
        Button dialogButton = (Button) dialogLang.findViewById(R.id.dialogButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLang.dismiss();
            }
        });

        dialogLang.show();

    }

    public void onResume(String address) {
        super.onResume();

        Log.d(TAG, "...In onResume - Attempting client connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
          //  btSocket = device.createRfcommSocketToServiceRecord(myUUID);
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            btSocket = (BluetoothSocket) m.invoke(device, 1);
        } /*catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }*/ catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        mBluetoothAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting to Remote...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection established and data link opened...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Creating Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }


        if (btSocket != null) {
            try {btSocket.close();} catch (Exception e) {}
            btSocket = null;
        }
    }
    public void onStop(){
        super.onStop();
        if (btSocket != null) {
            try {btSocket.close();} catch (Exception e) {}
            btSocket = null;
        }

        if (outStream != null) {
            try {outStream.close();} catch (Exception e) {}
            outStream = null;
        }

    }

   /* private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;


        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();


                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if(success){
                //connect successful
                final String msgconnected = "connect successful:\n"
                        + "BluetoothSocket: " + bluetoothSocket + "\n"
                        + "BluetoothDevice: " + bluetoothDevice;

                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), msgconnected, Toast.LENGTH_SHORT).show();
                    }});

                startThreadConnected(bluetoothSocket);
            }else{
                //fail
            }
        }

        public void cancel() {

            Toast.makeText(getApplicationContext(),
                    "close bluetoothSocket",
                    Toast.LENGTH_LONG).show();

            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }*/
    //Called in ThreadConnectBTdevice once connect successed
    //to start ThreadConnected
    /*private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }


   private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    String strReceived = new String(buffer, 0, bytes);
                    final String msgReceived = String.valueOf(bytes) +
                            " bytes received:\n"
                            + strReceived;

                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                          //  textStatus.setText(msgReceived);
                        }});

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String msgConnectionLost = "Connection lost:\n"
                            + e.getMessage();
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                           // textStatus.setText(msgConnectionLost);
                        }});
                }
            }
        }

        public void write(String s) {
            try {
                byte[] msgBuffer = s.getBytes();
                connectedOutputStream.write(msgBuffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }*/
   private void sendData(String message) {
       byte[] msgBuffer = message.getBytes();

       Log.d(TAG, "...Sending data: " + message + "...");

       try {
           outStream.write(msgBuffer);
       } catch (IOException e) {
           String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
           msg = msg +  ".\n\nCheck that the SPP UUID: " + myUUID.toString() + " exists on server.\n\n";

           errorExit("Fatal Error", msg);
       }
   }
    private void errorExit(String title, String message){
        Toast msg = Toast.makeText(getBaseContext(),
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        finish();
    }
}



