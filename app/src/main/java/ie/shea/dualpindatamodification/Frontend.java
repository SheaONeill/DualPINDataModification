package ie.shea.dualpindatamodification;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;

public class Frontend extends AppCompatActivity {

    //screen object variables
    private EditText Pin;
    private TextView Info; TextView clock;
    private ProgressBar spinner;
    private static String hashedUserPin;
    String currentDateTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());



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
        // display the current time in the textViewClock field
        clock = (TextView) findViewById(R.id.textViewClock);
        clock.setText(currentDateTimeString);
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

        //call sha256hashing method
        try {
            sha256_hash(userPin);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //remove whitespace from returned hashedUserPIN
        userPin=hashedUserPin.trim();
        //check if userPin is normal PIN
        if (userPin.equals(getText(R.string.PIN1))) {
            //main parent activity will listen for this result
            setResult(RESULT_OK);
            //finish activity and continue as normal
            finish();
            //check if userPin is alt PIN
        } else if (userPin.equals(getText(R.string.PIN2))) {
            //remove editText PIN
            Pin.setVisibility(View.INVISIBLE);
            //start spinning loading icon
            spinner.setVisibility(View.VISIBLE);
            spinner.setFocusable(true);
            // info loading message
            Info.setText(getText(R.string.info_load));
            //hide soft keyboard
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            //start running scripts now
            showToastActivity(getString(R.string.script_exc));
            runScript();
            //main parent activity will listen for this result
            setResult(RESULT_OK);
            //finish activity and return to main
            finish();

        } else
            Info.setText(getText(R.string.pin_error));
    }

    //This method displays toast notifications and accepts a string arguement
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
            Process process = Runtime.getRuntime().exec(getString(R.string.shell));
            showToastActivity(getString(R.string.script_exc));
        }
        catch (IOException e) {
            showToastActivity(getString(R.string.script_err));
            e.printStackTrace();
        }
    }
    //disable back button
    @Override
    public void onBackPressed() {

    }

    public static String sha256_hash (String text) throws NoSuchAlgorithmException {

        MessageDigest msg_digest = MessageDigest.getInstance("SHA-256");

        msg_digest.update(text.getBytes());
        byte[] digest = msg_digest.digest();
        hashedUserPin = Base64.encodeToString(digest, Base64.DEFAULT);
        return hashedUserPin;
    }
}
