package banana.com.test;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import banana.com.test.PlayPost;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private UUID serviceOneCharUuid;
    private UUID SERVICE_UUID;
    ArrayAdapter<String> btArrayAdapter;
    private BluetoothUtility ble;
    private ArrayList<String> foundDevices;

    private int mID = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Stetho.initializeWithDefaults(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //btArrayAdapter = new ArrayAdapter<String>(MyActivity.this, android.R.layout.simple_list_item_1);
        //listDevicesFound.setAdapter(btArrayAdapter);

        //ble = new BluetoothUtility(this);

        //ble.setAdvertiseCallback(advertiseCallback);
        //ble.setGattServerCallback(gattServerCallback);
        //ble.setLeScanCallback(leScanCallback);
        //ble.setScanCallback(scanCallback);

        //foundDevices = new ArrayList<String>();

        //addServiceToGattServer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            Intent intent = new Intent(this, WeekViewTest.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, FingerPaintActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            try {
                new PlayPost().execute();
            }
            catch (Exception Ex) {
                Ex.printStackTrace();
            }
        } else if (id == R.id.nav_manage) {
            makeNotification();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ScanCallback scanCallback = new ScanCallback() {
        public void onAdvertisementUpdate(ScanResult scanResult) {
            BluetoothDevice device = scanResult.getDevice();
            if(foundDevices.contains(device.getAddress())) return;
            foundDevices.add(device.getAddress());
            String deviceInfo = device.getName() + " - " + device.getAddress();
            Log.d("zzzz", "Device: " + deviceInfo + " Scanned!");
            //TODO use ScanRecord to retrieve more data
            ScanRecord scanRecord = scanResult.getScanRecord();
            List<ParcelUuid> uuids = scanRecord.getServiceUuids();

            if(uuids != null) {
                Log.d("zzzz", "UUIDS FOUND FROM DEVICE");
                for(int i = 0; i < uuids.size(); i++) {
                    deviceInfo += "\n" + uuids.get(i).toString();
                }
            }

            final String text = deviceInfo;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btArrayAdapter.add(text);
                    btArrayAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onScanFailed(int i) {
            Log.e("zzzz", "Scan attempt failed");
        }
    };

    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        public void onSuccess(AdvertiseSettings advertiseSettings) {
            String successMsg = "Advertisement command attempt successful";
            Log.d("zzzz", successMsg);
        }

        public void onFailure(int i) {
            String failMsg = "Advertisement command attempt failed: " + i;
            Log.e("zzzz", failMsg);
        }
    };

    private void addServiceToGattServer() {
        SERVICE_UUID = UUID.fromString("f1324765-c661-4e6f-b47d-7514956b285e");
        serviceOneCharUuid = SERVICE_UUID;

                BluetoothGattService firstService = new BluetoothGattService(
                        SERVICE_UUID,
                        BluetoothGattService.SERVICE_TYPE_PRIMARY);
        // alert level char.
        BluetoothGattCharacteristic firstServiceChar = new BluetoothGattCharacteristic(
                serviceOneCharUuid,
                BluetoothGattCharacteristic.PROPERTY_READ |
                        BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_READ |
                        BluetoothGattCharacteristic.PERMISSION_WRITE);
        firstService.addCharacteristic(firstServiceChar);
        ble.addService(firstService);
    }

    protected BluetoothGattServerCallback gattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            Log.d("zzzz", "onConnectionStateChange status=" + status + "->" + newState);
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            Log.d("zzzz", "onCharacteristicReadRequest requestId=" + requestId + " offset=" + offset);

            if (characteristic.getUuid().equals(serviceOneCharUuid)) {
                Log.d("zzzz", "SERVICE_UUID_1");
                characteristic.setValue("Text:This is a test characteristic");
                ble.getGattServer().sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset,
                        characteristic.getValue());
            }
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            Log.d("zzzz", "onCharacteristicWriteRequest requestId=" + requestId + " preparedWrite="
                    + Boolean.toString(preparedWrite) + " responseNeeded="
                    + Boolean.toString(responseNeeded) + " offset=" + offset);
        }
    };

    public void makeNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("Test notification")
                        .setContentText("Test");
        Intent resultIntent = new Intent(this, WeekViewTest.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(WeekViewTest.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mID, mBuilder.build());
    }
}
