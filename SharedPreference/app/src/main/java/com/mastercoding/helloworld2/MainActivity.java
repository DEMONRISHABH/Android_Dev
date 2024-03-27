package com.mastercoding.helloworld2;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    EditText editText;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        button   = findViewById(R.id.button);

        DisplaySavedText();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredText = editText.getText().toString();
                DisplayAndSaveText(enteredText);
            }
        });




    }

    private void DisplaySavedText() {
        // Retrieving the value from SharedPref
        SharedPreferences sharedPreferences =
                getSharedPreferences("MySharedPref",MODE_PRIVATE);

        String s1 = sharedPreferences.getString("name"," ");

        textView.setText(s1);


    }

    private void DisplayAndSaveText(String enteredText) {

        // Display the Text
        textView.setText(enteredText);

        // Saving the Text into SharedPref
        SharedPreferences sharedPreferences =
                getSharedPreferences("MySharedPref",MODE_PRIVATE);

        // Writing data to shared pref
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name",enteredText);

        editor.commit();
    }
}