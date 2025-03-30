package com.example.ex11023_externalfiles;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 400;
    private final String FILENAME = "textfile.txt";

    TextView tVText;
    EditText eT;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tVText = findViewById(R.id.tVText);
        eT = findViewById(R.id.eT);
        readToTV();
    }

    /**
     * writes to file the text
     * @param	text String
     * @return	none
     */
    public void writeToFile(String text)
    {
        if(isExternalStorageAvailable()) {
            try {
                if(!checkPermission()) requestPermission();
                File externalDir = Environment.getExternalStorageDirectory();
                File file = new File(externalDir, FILENAME);
                file.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(file);
                writer.write(text);
                writer.close();
            } catch (IOException e) {
                Log.e("writeToFile", "Error writing to file: " + FILENAME, e);
            }
        }
    }

    /**
     * reads to textview the text from file
     * @return	none
     */
    public void readToTV()
    {
        if(isExternalStorageAvailable()) {
            try {
                if(!checkPermission()) requestPermission();
                File externalDir = Environment.getExternalStorageDirectory();
                File file = new File(externalDir, FILENAME);
                file.getParentFile().mkdirs();
                FileReader reader = new FileReader(file);
                BufferedReader bR = new BufferedReader(reader);
                StringBuilder SB = new StringBuilder();
                String line = bR.readLine();
                while (line != null) {
                    SB.append(line + '\n');
                    line = bR.readLine();
                }
                tVText.setText(SB);

                bR.close();
                reader.close();
            } catch (IOException e) {
                Log.e("readToTV", "Error reading file: " + FILENAME, e);
            }
        }
    }

    /**
     * onClike save to file func
     * @param	view View
     * @return	none
     */
    public void save(View view) {
        writeToFile(tVText.getText().toString() + eT.getText().toString());
        readToTV();
    }

    /**
     * onClike reset func
     * @param	view View
     * @return	none
     */
    public void reset(View view) {
        writeToFile("");
        tVText.setText("");
    }

    /**
     * onClike exit func
     * @param	view View
     * @return	none
     */
    public void exit(View view) {
        save(view);
        finish();
    }


    /**
     * creats menu
     * @param	menu Menu
     * @return	none
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * handles menu item selection
     * @param	item MenuItem
     * @return	none
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        String st = item.getTitle().toString();
        if(st.equals("Credits"))
        {
            Intent creditesIntent = new Intent(this, Credits.class);
            startActivity(creditesIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android. Manifest.permission. WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission to access external storage granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();
            }
        }
    }

}