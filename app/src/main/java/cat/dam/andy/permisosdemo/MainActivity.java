package cat.dam.andy.permisosdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // Permission codes
    final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    final int MY_PERMISSIONS_CONTACTS=1002;
    final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;


    // Members
    private Button btn_camera;
    private Button btn_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setupListeners();

    }

    private void setupViews() {
        btn_camera = (Button)findViewById(R.id.btn_camera);
        btn_contacts = (Button)findViewById(R.id.btn_contacts);
    }

    private void setupListeners() {
        btn_camera.setOnClickListener(v -> {
            if (checkingPermissions(Manifest.permission.CAMERA)){
                //Es té permís de càmera
                Toast.makeText(getApplicationContext(), "Tens permís de càmera",
                        Toast.LENGTH_LONG).show();
                //Accions a fer si es disposa de permís de càmera
            }else{
                //No es té permís de càmera
                Toast.makeText(getApplicationContext(), "NO tens permís de càmera",
                        Toast.LENGTH_LONG).show();
                requestMyPermissions(Manifest.permission.CAMERA);
            }
        });

        btn_contacts.setOnClickListener(v -> {
            if (checkingPermissions(Manifest.permission.WRITE_CONTACTS)){
                //Es té permís per gestionar els contactes
                Toast.makeText(getApplicationContext(), "Tens permís per gestionar els contactes",
                        Toast.LENGTH_LONG).show();
                //Accions a fer si es disposa de permís per gestionar contactes
            }else{
                //No es té permís per gestionar els contactes
                Toast.makeText(getApplicationContext(), "NO tens permís per gestionar els contactes",
                        Toast.LENGTH_LONG).show();
                requestMyPermissions(Manifest.permission.WRITE_CONTACTS);
            }
        });

    }

    //*****************************************
    // Checking if permissions were granted
    //*****************************************
    private boolean checkingPermissions(String permission){
        // if permission is granted, return true otherwise false
        return checkSelfPermission(permission) == PERMISSION_GRANTED;

    }

    //**************************************
    // if permissions were not granted
    // then I request the permissions again
    //**************************************
    private void requestMyPermissions(String permission) {
        if (checkingPermissions(permission)) {
            //Permission was granted
            switch (permission) {
                case Manifest.permission.CAMERA: {
                    //  show a dialog
                    Toast.makeText(getApplicationContext(), "Ha donat permis de camera. Gràcies!",
                            Toast.LENGTH_LONG).show();
                }
                case Manifest.permission.WRITE_CONTACTS: {
                    //  show a dialog
                    Toast.makeText(getApplicationContext(), "Ha donat permis per gestionar els contactes'. Gràcies!",
                            Toast.LENGTH_LONG).show();
                }
            }
        } else {
            // Request the permission
            if (shouldShowRequestPermissionRationale(permission)) {
                switch (permission) {
                    case Manifest.permission.CAMERA: {
                        //  show a dialog
                        new AlertDialog.Builder(this)
                                .setMessage("L'aplicació necessita el permís de càmera per...")
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                                    }
                                })
                                .show();
                        break;
                    }
                    case Manifest.permission.WRITE_CONTACTS: {
                        //  show a dialog
                        new AlertDialog.Builder(this)
                                .setMessage("L'aplicació necessita el permís de gestió de contactes per...")
                                .setCancelable(true)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSIONS_CONTACTS);
                                    }
                                })
                                .show();
                        break;
                    }
                }
            } else {
                // Show this request again and again until user reject or grant
                switch (permission) {
                    case Manifest.permission.CAMERA: {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        break;
                    }
                    case Manifest.permission.WRITE_CONTACTS: {
                        requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSIONS_CONTACTS);
                        break;
                    }
                }
            }
        }
    }

    //*****************************************
    // Checking the permission request result
    //*****************************************

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //crida mètode original que es sobreescriu
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //control permisos
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // Si sol·licitud de permís ha estat denegada, l'array resultant és buit.
                if (grantResults.length > 0
                        && grantResults[0] == PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Ha donat permís de càmera. Gràcies!",
                            Toast.LENGTH_LONG).show();

                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        // Permission is not granted (Permanently)
                        new AlertDialog.Builder(this)
                                .setMessage("S'ha denegat permanentment el permís de càmera, si us plau habiliti aquest permís a la configuració per poder utilitzar correctament l'aplicació.")
                                .setCancelable(true)
                                .setPositiveButton("Anar a Configuració", (dialogInterface, i) -> goToApplicationSettings())
                                .setNegativeButton("Cancel", null)
                                .show();

                    }
                }
                break;
            }

            case MY_PERMISSIONS_CONTACTS: {
                // Si sol·licitud de permís ha estat denegada, l'array resultant és buit.
                if (grantResults.length > 0
                        && grantResults[0] == PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Ha donat permís per gestionar els contactes. Gràcies!",
                            Toast.LENGTH_LONG).show();

                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                        // Permission is not granted (Permanently)
                        new AlertDialog.Builder(this)
                                .setMessage("S'ha denegat permanentment el permís per gestionar els contactes', si us plau habiliti aquest permís a la configuració per poder utilitzar correctament l'aplicació.")
                                .setCancelable(true)
                                .setPositiveButton("Anar a Configuració", (dialogInterface, i) -> goToApplicationSettings())
                                .setNegativeButton("Cancel", null)
                                .show();

                    }
                }
                break;
            }
        }
    }

    //*************************************************
    // if user denied permanently the permissions,
    //  he should go to setting to granted the permissions
    //*************************************************
    private void goToApplicationSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
