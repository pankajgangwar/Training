package com.electrobit.trainingsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    long ans = 0;
    char operator = '+';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "OnCreate called");
        TextView calculationText = (TextView)findViewById(R.id.calculationText);
        Button button = (Button)findViewById(R.id.calculateButton);
        EditText editText = (EditText) findViewById(R.id.editTextView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCalculatedValue();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String operations = s.toString();
                int len = operations.length();
                if(len <= 0) {
                    ans = 0;
                    calculationText.setText("ans " + ans);
                    return;
                }
                // 1 - 3
                Log.d(TAG,"operation " + operations);
                if(!Character.isDigit(operations.charAt(len - 1))){
                    operator = operations.charAt(len - 1);
                    Log.d(TAG, "operator " + operator);
                }else{
                    int digit = operations.charAt(len - 1) - '0';
                    Log.d(TAG, " digit " + digit + " operator " + operator);
                    switch (operator){
                        case '+':
                            ans = ans + digit;
                            break;
                        case '-':
                            ans = ans - digit;
                            break;
                        case '*':
                            ans = ans * digit;
                            break;
                        case '/':
                            ans = ans / digit;
                            break;
                    }
                }
                calculationText.setText("ans " + ans);
            }
        });
    }

    private void sendCalculatedValue(){
        Bundle bundle = new Bundle();
        Log.d(TAG,"Value being sent " + ans);
        bundle.putLong(SecondActivity.BUNDLE_KEY, ans);
        Intent intent = new Intent();
        intent.setClass(this, SecondActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy called");
    }
}