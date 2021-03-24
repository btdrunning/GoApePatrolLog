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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

    private RelativeLayout mainMenu, startUpMenu, accidentMenu,settingsMenu;
    private RadioGroup siteCondition;
    private RadioButton okayOrClear, rakedOrGood;
    private Button btnSubmit, btnExit, btnSkip, btnUpdateName,settingsButton, settingsBack, createLog;
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
            case R.id.okayOrClear: //For the Condition buttons, checks if button 1 or button 0 are checked. Depending on if it is a Landing Site or just a Site, the string to be input will be determined elsewhere
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
    public void onClick(View v) { // Override for ALL button onClick readers
        switch (v.getId()) {
            case R.id.btnSubmit: // Logic to gather necessary info and save on submit button click
                Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
                if (inputQueue.getText().toString().trim().length() == 0) {
                    queue = "-"; //No queue is entered (due to not being allowed or by choice)
                } else {
                    queue = inputQueue.getText().toString().trim(); //Whatever is entered into Queue will be entered
                }
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss"); //Might have to change to lower API level needed, there are other ways to get timestamp
                String now = LocalTime.now().format(timeFormat);
                if (conditionStatus == 1) //Uses logic from above along with the current site/landing site to determine string to be saved
                {
                    condition = okayOrClear.getText().toString(); //Okay on Landing Site and Clear on Site
                }
                else
                {
                    condition = rakedOrGood.getText().toString(); //Raked on Landing Site and Good on Site
                }

                System.out.println(now); //USED FOR TESTING, TO BE REMOVED
                System.out.println(queue);
                System.out.println(condition);
                System.out.println(currentInstructorName);
                System.out.println(currentSiteName);

                fileContent = currentSiteName + "\t| " + queue + "\t| " + condition + "\t| " + now + "\t| " + currentInstructorName + "\n"; //Determines string to be written based off of given info
                writeToTxt(); //Goes to write function which will open text editor, save to the txt file and close the text editor

                if (currentStationNumber == 6) { //Determines what the next Site/Landing Site will be in the loop (Currently hard-coded for Lums Pond Site, might broaden)
                    currentStationNumber = 0;
                } else {
                    currentStationNumber++;
                }
                nextStation(); //Does logic to determine the text to be shown to the user based on the new currentStationNumber value
                clearEditTexts(); //Clears on text input blocks as to not allow accidental repeat entries for future submits
                break;
            case R.id.btnSkip: //Just moves to the next Site/Landing Site without saving the value (Logic could be potentially be put into own function)
                if (currentStationNumber == 6) {
                    currentStationNumber = 0;
                } else {
                    currentStationNumber++;
                }
                nextStation(); //Changes Visible Text
                clearEditTexts(); //Clears text blocks
                break;
            case R.id.btnExit: //NO USE CURRENTLY
                //May change to MENU button, will see (debating about allowing the user to go back to menu during patrol (to add instructor name and other settings changes) Debating about not allowing but I DONT KNOW if it is needed or will work
                mainMenu.setVisibility(View.GONE);
                startUpMenu.setVisibility(View.VISIBLE);
            case R.id.btnUpdateName: //Takes Instructor Name from Text box and saves
                currentInstructorName = inputName.getText().toString().trim();
                instructorName.setText("Instructor: " + currentInstructorName);
                inputName.setText("");
                break;
            case R.id.createLog: //Goes from Menu screen to Main Patrol Log
                startUpMenu.setVisibility(View.GONE);
                mainMenu.setVisibility(View.VISIBLE);
                break;

            case R.id.settingsButton:
                startUpMenu.setVisibility(View.GONE);
                settingsMenu.setVisibility(View.VISIBLE);
                break;

            case R.id.settingsBack:
                settingsMenu.setVisibility(View.GONE);
                startUpMenu.setVisibility(View.VISIBLE);
                break;
            default: //Any other buttons currently
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
                mainMenu = findViewById(R.id.mainMenu);
                startUpMenu = findViewById(R.id.startUpMenu);
                settingsButton = findViewById(R.id.settingsButton);
                settingsMenu = findViewById(R.id.settingsMenu);
                settingsBack = findViewById(R.id.settingsBack);
                createLog = findViewById(R.id.createLog);

                btnExit.setOnClickListener(this);
                btnSkip.setOnClickListener(this);
                btnSubmit.setOnClickListener(this);
                btnUpdateName.setOnClickListener(this);
                settingsButton.setOnClickListener(this);
                settingsBack.setOnClickListener(this);
                createLog.setOnClickListener(this);

                siteCondition.setOnCheckedChangeListener(this);
        
                currentSiteName = "LS 2";
        
                SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy");
                Date today = new Date();
                filename = "LumsPond" + formatter.format(today) + ".txt";
                filepath = "MyFileDir";
        
                if(!isExternalStorageAvailableForRW()){ //If for some reason the device will not allow Read/Write to external/internal storage, the submit button won't be available. Don't know if it actually does anything right now
                    btnSubmit.setEnabled(false);
                }

    }

    public void nextStation() { //Lums Pond rotation logic
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

    private boolean isExternalStorageAvailableForRW(){ //CHECK IF ACTUALLY WORKS/DOES ANYTHING
        String extStorageState = Environment.getExternalStorageState();
        if(extStorageState.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    public void writeToTxt() { //Writes given text to file to be saved
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