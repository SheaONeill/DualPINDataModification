package ie.shea.dualpindatamodification;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // call create directory method
        CreateDir();
        // call copy assets method to copy duress.sh to external SDCard
        CopyAssets();
        //start frontend activity
        Intent intent = new Intent(this, Frontend.class);
        //startActivity(intent);
        startActivityForResult(intent, 0);

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

    //  This method creates directory on sdcard if it does not exist
    public void CreateDir() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + getString(R.string.dir_name));
        //check if directory alreday exists
        if (!folder.exists()) {
            showToastActivity(getString(R.string.dir_not_exist));
            //create directory
            folder.mkdir();
            //check if directory was created
            if (true) {
                showToastActivity("Directory Created");
            } else {
                showToastActivity("Error");
            }
            //otherwise copy script to sdcard
        } else
            Log.e("tag", "message");
        showToastActivity("Directory already exists");
        //showToastActivity(getString(R.string.dir_exists));
    }

    // This method copies files from the assets dir to external SDCard Duress directory
    private void CopyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list(getString(R.string.dir_name));
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        for (String filename : files) {

            try {
                InputStream in = assetManager.open("" + getString(R.string.dir_name) + "/" + filename);
                OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.dir_name) + "/" + filename);
                // call copyFile method and pass filename and destination directory
                copyFile(in, out, filename);
                //close streams
                in.close();
                out.close();
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
        }

    }

    private void copyFile(InputStream in, OutputStream out, String filename) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        showToastActivity("file " + filename + " copied!");
    }


}
