package com.llw.goodtrash;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.llw.goodtrash.common.CameraSurfaceView;
import com.llw.goodtrash.common.Utils;
import com.llw.goodtrash.yolo_detection.Native;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Searchers2Activity extends Activity implements View.OnClickListener, CameraSurfaceView.OnTextureChangedListener {
    private static final String TAG = com.llw.goodtrash.Searchers2Activity.class.getSimpleName();

    CameraSurfaceView svPreview;
    TextView tvStatus;
    ImageButton btnSwitch;
    ImageButton btnShutter;
    ImageButton btnSettings;

    String savedImagePath = "";
    int lastFrameIndex = 0;
    long lastFrameTime;

    Native predictor = new Native();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_searchers2);

        // Clear all setting items to avoid app crashing due to the incorrect settings
        initSettings();

        // Init the camera preview and UI components
        initView();

        // Check and request CAMERA and WRITE_EXTERNAL_STORAGE permissions
        if (!checkAllPermissions()) {
            requestAllPermissions();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_switch:
                svPreview.switchCamera();
                break;
            case R.id.btn_shutter:
                SimpleDateFormat date = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                synchronized (this) {
                    savedImagePath = Utils.getDCIMDirectory() + File.separator + date.format(new Date()).toString() + ".png";
                }
                Toast.makeText(com.llw.goodtrash.Searchers2Activity.this, "Save snapshot to " + savedImagePath, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_settings:
                startActivity(new Intent(com.llw.goodtrash.Searchers2Activity.this, Settings2Activity.class));
                break;
        }
    }

    @Override
    public boolean onTextureChanged(int inTextureId, int outTextureId, int textureWidth, int textureHeight) {
        String savedImagePath = "";
        synchronized (this) {
            savedImagePath = com.llw.goodtrash.Searchers2Activity.this.savedImagePath;
        }
        boolean modified = predictor.process(inTextureId, outTextureId, textureWidth, textureHeight, savedImagePath);
        if (!savedImagePath.isEmpty()) {
            synchronized (this) {
                com.llw.goodtrash.Searchers2Activity.this.savedImagePath = "";
            }
        }
        lastFrameIndex++;
        if (lastFrameIndex >= 30) {
            final int fps = (int) (lastFrameIndex * 1e9 / (System.nanoTime() - lastFrameTime));
            runOnUiThread(new Runnable() {
                public void run() {
                    tvStatus.setText(Integer.toString(fps) + "fps");
                }
            });
            lastFrameIndex = 0;
            lastFrameTime = System.nanoTime();
        }
        return modified;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload settings and re-initialize the predictor
        checkAndUpdateSettings();
        // Open camera until the permissions have been granted
        if (!checkAllPermissions()) {
            svPreview.disableCamera();
        }
        svPreview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        svPreview.onPause();
    }

    @Override
    protected void onDestroy() {
        if (predictor != null) {
            predictor.release();
        }
        super.onDestroy();
    }

    public void initView() {
        svPreview = (CameraSurfaceView) findViewById(R.id.sv_preview);
        svPreview.setOnTextureChangedListener(this);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        btnSwitch = (ImageButton) findViewById(R.id.btn_switch);
        btnSwitch.setOnClickListener(this);
        btnShutter = (ImageButton) findViewById(R.id.btn_shutter);
        btnShutter.setOnClickListener(this);
        btnSettings = (ImageButton) findViewById(R.id.btn_settings);
        btnSettings.setOnClickListener(this);
    }

    public void initSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Settings2Activity.resetSettings();
    }

    public void checkAndUpdateSettings() {
        if (Settings2Activity.checkAndUpdateSettings(this)) {
            String realModelDir = getCacheDir() + "/" + Settings2Activity.modelDir;
            Utils.copyDirectoryFromAssets(this, Settings2Activity.modelDir, realModelDir);
            String realLabelPath = getCacheDir() + "/" + Settings2Activity.labelPath;
            Utils.copyFileFromAssets(this, Settings2Activity.labelPath, realLabelPath);
            predictor.init(
                    realModelDir,
                    realLabelPath,
                    Settings2Activity.cpuThreadNum,
                    Settings2Activity.cpuPowerMode,
                    Settings2Activity.inputWidth,
                    Settings2Activity.inputHeight,
                    Settings2Activity.inputMean,
                    Settings2Activity.inputStd,
                    Settings2Activity.scoreThreshold);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(com.llw.goodtrash.Searchers2Activity.this)
                    .setTitle("Permission denied")
                    .setMessage("Click to force quit the app, then open Settings->Apps & notifications->Target " +
                            "App->Permissions to grant all of the permissions.")
                    .setCancelable(false)
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            com.llw.goodtrash.Searchers2Activity.this.finish();
                        }
                    }).show();
        }
    }

    private void requestAllPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, 0);
    }

    private boolean checkAllPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
}
