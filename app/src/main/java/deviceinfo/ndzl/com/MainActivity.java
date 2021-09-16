    package deviceinfo.ndzl.com;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.content.IntentFilter;
    import android.location.LocationManager;
    import android.os.Build;
    import android.os.Bundle;
    import android.provider.Settings;
    import android.telephony.TelephonyManager;
    import android.view.KeyEvent;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.zebra.sdk.comm.BluetoothConnectionInsecure;
    import com.zebra.sdk.comm.Connection;

    import java.util.Timer;

    //(C)2021 by cxnt48
    public class MainActivity extends Activity{

    Timer tim;


    TextView tvOut;
    TextView tvBattery;

    // DZLReceiver mIntent_Receiver;
    IntentFilter mIntentFilter;
    Intent iBattStat;

    Button btLaunch;
    Button virtKeyb;
    static public boolean bvirtualoff = true;

    Button btCamera;
    public static Intent service_is;
    public static Context main_context;
    Button btSpeak;
    Button btWait;
    EditText etPTT;
    Button btCPUtest;
    Button btReboot;

    String androidId;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_context = this;
        service_is = new Intent(this, SpeakerService.class);

        String DeviceSERIALNumber = Build.SERIAL;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        String DeviceIMEI = null;
        try {
            DeviceIMEI = telephonyManager.getDeviceId();
        } catch (SecurityException se) {
            DeviceIMEI="NO_IMEI";
        }
        androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        tvOut = (TextView) findViewById(R.id.tvOutput);

        etPTT = (EditText) findViewById(R.id.etPTT);

        //tvOut.setText("S/N: " + DeviceSERIALNumber + "\nIMEI: " + DeviceIMEI + "\nSecure.ANDROID_ID: "+androidId  );  //rem on lollipop!
        tvOut.setText("Attivare il BT di questo terminale");


        btCPUtest= (Button) findViewById(R.id.cputest);
        btCPUtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String targetBTaddr = etPTT.getText().toString();

                String zplData = "! U1 setvar \"device.friendly_name\" \"\"" ;
                zplData +="\r\n";
                zplData+= "! U1 setvar \"device.reset\" \"\"";
                zplData +="\r\n";

                if (targetBTaddr.length() != 12) {
                    Toast.makeText(getApplicationContext(), "BTADDRESS NON VALIDO", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "RESETTING " + etPTT.getText().toString(), Toast.LENGTH_SHORT).show();
                    execSGDoverInsecureBluetooth(targetBTaddr, zplData);

                }
            }
        });





    }


    /*  ET1:
    *   Battery Pack Rechargeable Lithium Ion 3.7V, 4620 mAh or 5640 mAh Smart battery.
        Backup Battery NiMH battery (rechargeable) 15 mAh 3.6 V (not user accessible).
    * */

    private void execSGDoverInsecureBluetooth(final String theBtMacAddress, final String zplData) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                    Connection  thePrinterConn = new BluetoothConnectionInsecure(theBtMacAddress);


                    // Open the connection - physical connection is established here.
                    thePrinterConn.open();
/*
                    String zplData = "! U1 setvar \"weblink.ip.conn2.location\" \"https://demo-zebra-printer.link-os.com:443/linkos/weblink/connect/\"" ;
                    zplData +="\r\n";
                    zplData+= "! U1 setvar \"device.reset\" \"\"";
                    zplData +="\r\n";

*/

                    if(thePrinterConn.isConnected()) {
                        thePrinterConn.write(zplData.getBytes());
                        //Toast.makeText(MainActivity.this, "CLASSICBT-Data written.", Toast.LENGTH_SHORT).show();

                        // Thread.sleep(500);
                    }


                    // Make sure the data got to the printer before closing the connection
                    Thread.sleep(2000);

                    thePrinterConn.close();

                } catch (Exception e) {
                    // Handle communications error here.
                    //Toast.makeText(MainActivity.this, "CLASSICBT-EXCP.", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.

    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.



    }




}





