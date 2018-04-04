package ie.shea.dualpindatamodification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

                    return true;
                }
                return false;
            }
        };
    }

}
