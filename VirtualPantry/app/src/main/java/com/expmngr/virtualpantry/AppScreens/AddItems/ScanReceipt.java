package com.expmngr.virtualpantry.AppScreens.AddItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.MainActivity;
import com.expmngr.virtualpantry.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Element;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScanReceipt extends AppCompatActivity {
    SurfaceView mCameraView;
    TextView mTextView;
    CameraSource mCameraSource;

    Set<String> potentialFoods;
    Set<String> foundFoods;

    Button pauseButton;
    private boolean isCameraOn;

    private static final int requestPermissionID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);

        mCameraView = findViewById(R.id.surfaceView);
        mTextView = findViewById(R.id.text_view);

        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCameraOn = !isCameraOn;
                if(isCameraOn){
                    mCameraSource.stop();
                    pauseButton.setText("Resume");
                }else{
                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            mCameraSource.start(mCameraView.getHolder());
                            pauseButton.setText("Pause");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        potentialFoods = new HashSet<>();
        foundFoods = new HashSet<>();

        startCameraSource();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d("ScanReciept", "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraSource.start(mCameraView.getHolder());
                isCameraOn = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w("ScanReciept", "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(5.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(ScanReceipt.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();

                                Integer centerY = 0;
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    List<Line> lines = (List<Line>) item.getComponents();
                                    for (int j=0;j<lines.size();j++){
                                        Line line = lines.get(j);
                                        if(!containsDigit(line.getValue())) {
                                            //search database for line
                                            potentialFoods.add(line.getValue().toLowerCase());

//                                            stringBuilder.append(centerY.toString() + ":\t>>>" + line.getValue());
//                                            stringBuilder.append("\n");
                                        }
                                        List<Element> elements = (List<Element>) line.getComponents();
                                        for(int k=0;k<elements.size();k++) {
                                            Element element = elements.get(k);
                                            centerY = element.getBoundingBox().centerY();

                                            if(!containsDigit(element.getValue())) {
                                                //search database for line
                                                potentialFoods.add(element.getValue().toLowerCase());
//                                                stringBuilder.append(centerY.toString() + ":\t>>>" + element.getValue());
//                                                stringBuilder.append("\n");
                                            }

//                                            stringBuilder.append(centerY.toString() + ":\t>>>" + element.getValue());
//                                            stringBuilder.append("\n");
                                        }
//                                        stringBuilder.append("\n");
                                    }
//                                    stringBuilder.append("\n");
                                }
                                //check potential foods against the database
                                for(String s : potentialFoods){
                                    List<ExpiryFood> found = MainMenuPlaceholder.database.expiryFoodDAO().findByName(s);
                                    if(found.size() > 0) {
                                        System.out.println(s);
                                        foundFoods.add(s);
                                    }
                                }

//                                System.out.println(stringBuilder.toString());
                                System.out.println(potentialFoods.toString());
                                System.out.println("Found: " + foundFoods.toString());
                                String str = "Found " + foundFoods.size() + " foods";
                                mTextView.setText(str);
                                System.out.println(str);

                            }
                        });
                    }
                }
            });
        }
    }

    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    break;
                }
            }
        }

        return containsDigit;
    }
}
