package com.example.fadebook.ui.MainActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fadebook.R;
import com.example.fadebook.ui.MainActivity.SelectLang.SelectLangFragment;
import com.example.fadebook.ui.MainActivity.SignIn.SigninFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new SigninFragment())
                .commit();
    }

}