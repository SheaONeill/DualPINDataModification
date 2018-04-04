package ie.shea.dualpindatamodification;

import android.content.Context;
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
            //check if userPin is abnornal PIN
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
        } else
            // try again message
            Info.setText("Please try again");
    }

}
