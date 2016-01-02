package me.davidrios.android.flash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private Camera.Parameters parameters;
    private boolean turnedOn = false;
    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (ImageButton) findViewById(R.id.buttonToogleFlash);
    }

    /**
     * Enciende la luz
     * @param view
     */
    public void toogleFlash(View view) {

        /*
        Detecta si la cámara tiene flash
         */
        if (!this.hashFlash())
            popupMessage(getString(R.string.hasNoFlash));
        else {

            if (turnedOn)
                turnFlash(false);
            else
                turnFlash(true);
        }
    }

    /**
     * Determina si tiene o no flash
     * @return
     */
    private boolean hashFlash() {

        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }


    /**
     * Enciende o apaga el flash
     * @param state Indica si se enciende o se apaga
     */
    private void turnFlash(boolean state) {

        /*
        Intenta conectarse al servicio de la cámara
         */
        if (camera == null) {

            try {
                camera = Camera.open();
                parameters = camera.getParameters();
            } catch (RuntimeException error) {
                popupMessage(getString(R.string.errorMessage) + error.getMessage());
                return;
            }
        }

        /*
        Enciende o apaga el flash
         */
        if (state) {

            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            turnedOn = true;
            //button.setText(getString(R.string.buttonToogleFlashOff));
            popupMessage(getString(R.string.activatedLight));
        }
        else {

            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            turnedOn = false;
            //button.setText(getString(R.string.buttonToogleFlashOn));
            popupMessage(getString(R.string.deactivatedLight));
        }
    }


    /**
     * Muestra un mensaje de texto en forma de Toast
     * @param messageText Mensaje a mostrar en el Toast
     */
    private void popupMessage(String messageText) {

        Context context;
        Toast toast;

        context = getApplicationContext();

        toast = Toast.makeText(context, messageText, Toast.LENGTH_SHORT);
        toast.show();
    }
}
