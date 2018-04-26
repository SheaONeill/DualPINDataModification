package ie.shea.dualpindatamodification;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Frontend extends AppCompatActivity {
    //Instantiate global variables
    private static final String PIN1 = "0000";
    private static final String PIN2 = "1234";
    //screen object variables
    private EditText Pin;
    private TextView Info;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontend);
        //auto show soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // editTextPin
        Pin = (EditText) findViewById(R.id.editTextPIN);
        Pin.setOnEditorActionListener(getTextViewPINListener());
        Pin.setFocusable(true);
        Info = (TextView) findViewById(R.id.textViewInfo);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }

    //method for action listener for send button
    private TextView.OnEditorActionListener getTextViewPINListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //clear info text
                    Info.setText("");
                    //convert user input to string
                    String userPin = Pin.getText().toString();
                    //clear editText
                    Pin.setText("");
                    //call validatePin method
                    validatePin(userPin, textView);
                    return true;
                }
                return false;
            }
        };
    }
    //this method validates userPin
    void validatePin(String userPin, View v) {

        //check if userPin is normal PIN
        if (userPin.equals(PIN1)) {
            //main parent activity will listen for this result
            setResult(RESULT_OK);
            //finish activity and continue as normal
            finish();
            //check if userPin is abby-normal PIN
        } else if (userPin.equals(PIN2)) {
            //remove editText PIN
            Pin.setVisibility(View.INVISIBLE);
            //start spinning loading icon
            spinner.setVisibility(View.VISIBLE);
            spinner.setFocusable(true);
            // info loading message
            Info.setText("Loading....");
            //hide soft keyboard
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            //start running scripts now
            showToastActivity("About to run Script");
            runScript();
            //main parent activity will listen for this result
            setResult(RESULT_OK);
            SystemClock.sleep(7000);
            //finish activity and continue as normal
            finish();

        } else
            // try again message
            Info.setText("Please try again");
    }

    //This method diplays toast notifications and accepts a string arguement
    public void showToastActivity(String toast_string) {
//        ref: https://stackoverflow.com/a/29977516
        Context context = getApplicationContext();
        CharSequence toast_message = toast_string;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toast_message, duration);
        toast.show();
    }

    public void runScript() {

        try {
            Process process = Runtime.getRuntime().exec("su /system/bin/sh /sdcard/AssetsSubDir/bash.sh");
            showToastActivity("Script Running");
        }
        catch (IOException e) {
            showToastActivity("Script error");
            e.printStackTrace();
        }
    }
    //disable back button
    @Override
    public void onBackPressed() {
        //TODO Stop soft keyboard disappearing on back keypress
    }
}
