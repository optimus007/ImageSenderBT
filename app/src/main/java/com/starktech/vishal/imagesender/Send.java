package com.starktech.vishal.imagesender;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.Set;

import static android.R.attr.path;

public class Send extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 12;
    private TextView out;
    private BluetoothAdapter adapter;
ToggleButton blueONOff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        blueONOff = (ToggleButton) findViewById(R.id.toggleButton);
        out = (TextView) findViewById(R.id.tvBluetoothInfo);
        adapter = BluetoothAdapter.getDefaultAdapter();
        out.setText("\n\n\n\n\n\n\n\n\n\n\n\n                             TURN ON BLUETOOTH");
        if (adapter.isEnabled()) {
            blueONOff.setChecked(true);
            setBluetoothData();
        }
            blueONOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Intent enableBtIntent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        setBluetoothData();

                    } else {
                        adapter.disable();
                        out.setText("\n\n\n\n\n\n\n\n\n\n\n\n                             TURN ON BLUETOOTH");
                    }
                }
            });


            // if (Connections.blueTooth()) {
            //  }


            final Button send = (Button) findViewById(R.id.sendbutton);
            send.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    senddata();
                }
            });
        }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        out.setText("");
        setBluetoothData();

    }

    private void setBluetoothData() {
        out.setText("");
        // Getting the Bluetooth adapter
        adapter = BluetoothAdapter.getDefaultAdapter();
        String macAddress = android.provider.Settings.Secure.getString(getContentResolver(), "bluetooth_address");
        out.append("\nAdapter: " + adapter.toString() + "\n\nName: "
                + adapter.getName() + "\nAddress: " + macAddress);

        // Check for Bluetooth support in the first place
        // Emulator doesn't support Bluetooth and will return null

        if (adapter == null) {
            Toast.makeText(this, "Bluetooth NOT supported. Aborting.",
                    Toast.LENGTH_LONG).show();
        }

        // Starting the device discovery
        out.append("\n\nStarting discovery...");
        adapter.startDiscovery();
        out.append("\nDone with discovery...\n");

        // Listing paired devices
        out.append("\nDevices Paired:");
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            out.append("\n\nFound device: " + device.getName() + " Add: "
                    + device.getAddress());
        }


    }

    public void senddata() {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);


        String filePath = Environment.getExternalStorageDirectory().toString() + path;
        String fileName = "IMG.jpg";
        File file = new File(filePath, fileName);


        sharingIntent.setType("image/*");
        sharingIntent.setPackage("com.android.bluetooth");
//you can also pass a ArrayList<Uri> uriList for multiple files.
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(Intent.createChooser(sharingIntent, "Share file"));

    }
}
