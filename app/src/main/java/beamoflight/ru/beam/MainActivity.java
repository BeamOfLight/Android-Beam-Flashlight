package beamoflight.ru.beam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends Activity {

    private Switch mWidgetSwitchFlash;
    private boolean mHasFlash;
    private Camera mCamera;
    private Parameters mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWidgetSwitchFlash = (Switch) findViewById(R.id.swFlash);
        mHasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!mHasFlash) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title)
                    .setPositiveButton(R.string.dialog_OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.create().show();
            return;
        }

        turnOnFlash();

        mWidgetSwitchFlash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWidgetSwitchFlash.isChecked()) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });
    }

    private void initCamera() {
        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                mParams = mCamera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera.open() error", e.getMessage());
            }
        }
    }

    private void turnOnFlash() {
        if (prepareFlash(Parameters.FLASH_MODE_TORCH)) {
            mCamera.startPreview();
        }
    }

    private void turnOffFlash() {
        if (prepareFlash(Parameters.FLASH_MODE_OFF)) {
            mCamera.stopPreview();
        }
    }

    private boolean prepareFlash(String flashMode) {
        if (mCamera == null || mParams == null) {
            return false;
        }

        mParams = mCamera.getParameters();
        mParams.setFlashMode(flashMode);
        mCamera.setParameters(mParams);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mHasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

}