package com.example.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText time;
    private TextView finalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Edge-to-Edge functionality
        setContentView(R.layout.activity_main);

        // Initialize UI components
        time = findViewById(R.id.in_time);
        button = findViewById(R.id.btn_run);
        finalResult = findViewById(R.id.tv_result);

        // Set up WindowInsets for padding adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set button click listener to run AsyncTask
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = time.getText().toString();
                runner.execute(sleepTime);
            }
        });
    }

    // AsyncTask inner class for background processing
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]) * 1000; // Convert to milliseconds
                Thread.sleep(time); // Simulate delay
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // Dismiss the ProgressDialog and update UI with result
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            finalResult.setText(result);
        }

        @Override
        protected void onPreExecute() {
            // Show ProgressDialog before task starts
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog",
                    "Wait for " + time.getText().toString() + " seconds");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // Update UI with progress message
            finalResult.setText(text[0]);
        }
    }
}
