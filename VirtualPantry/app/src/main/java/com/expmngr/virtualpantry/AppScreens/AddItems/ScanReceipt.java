package com.expmngr.virtualpantry.AppScreens.AddItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.expmngr.virtualpantry.AppScreens.MainMenuPlaceholder;
import com.expmngr.virtualpantry.Database.Entities.ExpiryFood;
import com.expmngr.virtualpantry.MainActivity;
import com.expmngr.virtualpantry.R;
import com.expmngr.virtualpantry.Utils.BottomNavigationViewHelper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Element;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScanReceipt extends AppCompatActivity {
    SurfaceView mCameraView;
    TextView mTextView;
    CameraSource mCameraSource;
    private static final int ACTIVITY_NUM = 1;

    Set<String> potentialFoods;
    Set<String> foundFoods;
    Set<String> blackList; //improves efficiency by remembering words that are not in the database
    Map<String, String> keywordDict;

    Button doneButton;

    private static final int requestPermissionID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);

        mCameraView = findViewById(R.id.surfaceView);
        mTextView = findViewById(R.id.foodFoundTextView);

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScanReceipt.this, ConfirmScanFoods.class);
                ArrayList<String> temp = new ArrayList<String>();
                temp.addAll(foundFoods);
                i.putExtra("found_foods", temp);
                startActivity(i);
            }
        });

        potentialFoods = new HashSet<>();
        foundFoods = new HashSet<>();
        blackList = new HashSet<>();

        addKeywords();

        startCameraSource();
        setupBottomNavigationView();

    }

    private void addKeywords(){
        ArrayList<String[]> myValues = csvToArrayBetter(R.raw.keywords);
        System.out.println(Arrays.deepToString(myValues.toArray()));

        keywordDict = new HashMap<>();

        for(String[] line : myValues){
            //every word in the expiry times needs a key pointing to itself
            keywordDict.put(line[0],line[0]);

            if(!line[1].equals("")){
                //words that are similar to a word in expiry times point to that word
                keywordDict.put(line[1],line[0]);
            }
        }

        System.out.println(keywordDict.toString());
    }

    public ArrayList<String[]> csvToArrayBetter(int resource) {

        InputStream is = getResources().openRawResource(resource);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        ArrayList<String[]> lines = new ArrayList<>();

        try {
            //row titles
            line = reader.readLine();

            line = reader.readLine();
            while(line != null) {
                lines.add(line.split(","));
                line = reader.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return lines;
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
                                        //Start By Checking Lines for digits, digits != food
                                        List<Element> elements;
                                             elements = (List<Element>) line.getComponents();
                                            //check if any of the components of the line are keywords
                                            boolean containsKeyword = false;
                                            for(Element e : elements){
                                                if(keywordDict.get(e.getValue().toLowerCase().replaceAll("\\d","")) != null) containsKeyword = true;
                                            }
                                            if(containsKeyword) {
                                                potentialFoods.add(line.getValue().toLowerCase().replaceAll("\\d",""));
                                            }

                                        elements = (List<Element>) line.getComponents();
                                        for(int k=0;k<elements.size();k++) {
                                            Element element = elements.get(k);
                                            centerY = element.getBoundingBox().centerY();
                                            String checkFood = element.getValue().toLowerCase().replaceAll("\\d","");

                                                //search database for element
                                                if(keywordDict.get(checkFood) != null) {
                                                    potentialFoods.add(checkFood);
                                                }
//                                                stringBuilder.append(centerY.toString() + ":\t>>>" + element.getValue());
//                                                stringBuilder.append("\n");

//                                            stringBuilder.append(centerY.toString() + ":\t>>>" + element.getValue());
//                                            stringBuilder.append("\n");
                                        }

//                                        stringBuilder.append("\n");
                                    }
//                                    stringBuilder.append("\n");
                                }
                                //check potential foods against the database
                                for(String s : potentialFoods){
                                    if(!blackList.contains(s)) {
                                        List<ExpiryFood> found = MainMenuPlaceholder.database.expiryFoodDAO().findByName("%" + s + "%");

                                        if (found.size() > 0) {
                                            foundFoods.add(s);
                                        }else{
                                            blackList.add(s);
                                        }
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

    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(ScanReceipt.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
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
