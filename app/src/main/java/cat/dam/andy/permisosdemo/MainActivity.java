package cat.dam.andy.permisosdemo;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Members
    final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
    private Button btn_camera;
    private Button btn_contacts;
    private ActivityResultLauncher<String[]> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initPermissions();
        initListeners();
    }

    private void initViews() {
        btn_camera = findViewById(R.id.btn_camera);
        btn_contacts = findViewById(R.id.btn_contacts);
    }

    private void initPermissions() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    for (String permission : result.keySet()) {
                        if (Boolean.TRUE.equals(result.get(permission))) {
                            Toast.makeText(this, permission + " Permission granted", Toast.LENGTH_SHORT).show();
                        } else {
                            if (shouldShowRequestPermissionRationale(permission)) {
                                Toast.makeText(this, permission + " Permission denied. This app need this permission to do the job. Thanks", Toast.LENGTH_SHORT).show();
                            } else {
                                new AlertDialog.Builder(this)
                                        .setTitle("Permission denied")
                                        .setMessage("Permission "+ permission + "was permanently denied. You need to go to Permission settings to allow it. Thanks")
                                        .setPositiveButton("Go to settings", (dialog, which) -> {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.parse("package:" + getPackageName()));
                                            startActivity(intent);
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .create()
                                        .show();
                            }
                        }
                    }
                }
        );
    }

    private void initListeners() {
        btn_camera.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                activityResultLauncher.launch(new String[]{Manifest.permission.CAMERA});
            }
        });
        btn_contacts.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PERMISSION_GRANTED) {
                Toast.makeText(this, "Contacts Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                activityResultLauncher.launch(new String[]{Manifest.permission.WRITE_CONTACTS});
            }
        });
    }
}
