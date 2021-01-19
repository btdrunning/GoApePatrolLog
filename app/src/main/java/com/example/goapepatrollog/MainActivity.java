// Hello, Welcome to My App!
// The app will run you through the rotation course Instructors will walk allowing you to input the
// condition and queue of each section at that time.
// The final document can be found at:
// Storage (or SDCARD) -> emulated -> 0 -> Android -> data -> com.example.goapepatrollog -> files -> MyFileDir ->
// Apologise for the Sloppiness of the Code, will be working to continue to clean it up!

package com.example.goapepatrollog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup siteCondition;
    private RadioButton okayOrClear, rakedOrGood;
    private Button btnSubmit, btnExit, btnSkip, btnUpdateName;
    private TextView instructorName, siteName;
    private EditText inputQueue, inputName;
    private String currentInstructorName, condition, queue, currentSiteName;
    private int currentStationNumber;
    private int conditionStatus;
    String filename = "";
    String filepath = "";
    String fileContent = "";


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.okayOrClear:
                conditionStatus = 1;
                break;
            case R.id.rakedOrGood:
                conditionStatus = 0;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
                if (inputQueue.getText().toString().trim().length() == 0) {
                    queue = "-";
                } else {
                    queue = inputQueue.getText().toString().trim();
                }
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
                String now = LocalTime.now().format(timeFormat);
                if (conditionStatus == 1)
                {
                    condition = okayOrClear.getText().toString();
                }
                else
                {
                    condition = rakedOrGood.getText().toString();
                }

                System.out.println(now);
                System.out.println(queue);
                System.out.println(condition);
                System.out.println(currentInstructorName);
                System.out.println(currentSiteName);
//
                fileContent = currentSiteName + "\t| " + queue + "\t| " + condition + "\t| " + now + "\t| " + currentInstructorName + "\n";
                writeToTxt();

                if (currentStationNumber == 6) {
                    currentStationNumber = 0;
                } else {
                    currentStationNumber++;
                }
                nextStation();
                clearEditTexts();
                break;
            case R.id.btnSkip:
                if (currentStationNumber == 6) {
                    currentStationNumber = 0;
                } else {
                    currentStationNumber++;
                }
                nextStation();
                clearEditTexts();
                break;
            case R.id.btnExit:

            case R.id.btnUpdateName:
                currentInstructorName = inputName.getText().toString().trim();
                instructorName.setText("Instructor: " + currentInstructorName);
                inputName.setText("");
                break;
            default:
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSkip = findViewById(R.id.btnSkip);
        btnExit = findViewById(R.id.btnExit);
        btnUpdateName = findViewById(R.id.btnUpdateName);
        instructorName = findViewById(R.id.instructorName);
        siteName = findViewById(R.id.siteName);
        inputQueue = findViewById(R.id.inputQueue);
        inputName = findViewById(R.id.inputName);
        okayOrClear = findViewById(R.id.okayOrClear);
        rakedOrGood = findViewById(R.id.rakedOrGood);
        siteCondition = findViewById(R.id.siteCondition);

        btnExit.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnUpdateName.setOnClickListener(this);

        siteCondition.setOnCheckedChangeListener(this);

        currentSiteName = "LS 2";

        SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy");
        Date today = new Date();
        filename = "LumsPond" + formatter.format(today) + ".txt";
        filepath = "MyFileDir";

        if(!isExternalStorageAvailableForRW()){
            btnSubmit.setEnabled(false);
        }

    }

    public void nextStation() {
        if (currentStationNumber == 1 || currentStationNumber == 2 || currentStationNumber == 6) {
            okayOrClear.setText("Clear");
            rakedOrGood.setText("Good");
            inputQueue.setVisibility(View.VISIBLE);
            if (currentStationNumber == 1) {
                currentSiteName = "Site 3";
                siteName.setText("Site 3");
            } else if (currentStationNumber == 2) {
                currentSiteName = "Site 5";
                siteName.setText("Site 5");
            } else {
                currentSiteName = "Site 4";
                siteName.setText("Site 4");
            }
        } else {
            okayOrClear.setText("Okay");
            rakedOrGood.setText("Raked");
            inputQueue.setVisibility(View.GONE);
            if (currentStationNumber == 0) {
                currentSiteName = "LS 2";
                siteName.setText("Landing Site 2");
            } else if (currentStationNumber == 3) {
                currentSiteName = "LS 4";
                siteName.setText("Landing Site 4");
            } else if (currentStationNumber == 4) {
                currentSiteName = "LS 3";
                siteName.setText("Landing Site 3");
            } else {
                currentSiteName = "LS 5";
                siteName.setText("Landing Site 5");
            }
        }
    }

    public void clearEditTexts() {
        inputName.setText("");
        inputQueue.setText("");
    }

    private boolean isExternalStorageAvailableForRW(){
        String extStorageState = Environment.getExternalStorageState();
        if(extStorageState.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    public void writeToTxt() {
        File myExternalFile = new File(getExternalFilesDir(filepath), filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream((myExternalFile), true);
            if(myExternalFile.length() == 0)
            {
                SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy");
                Date today = new Date();
                String startUp = "___________________________________________\nStart of Day: iPatrol #1 Lums Pond " + formatter.format(today) + "\n___________________________________________\n";
                fos.write(startUp.getBytes());
            }
            fos.write(fileContent.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}