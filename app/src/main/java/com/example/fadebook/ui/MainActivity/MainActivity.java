package com.example.fadebook.ui.MainActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fadebook.R;
import com.example.fadebook.ui.MainActivity.Home.HomeFragment;
import com.example.fadebook.ui.MainActivity.SignIn.SigninFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new SigninFragment())
                    .commit();
        else
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();

    }

}