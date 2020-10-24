package com.example.fadebook.ui.MainActivity.SignIn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.fadebook.R;
import com.example.fadebook.ui.MainActivity.Home.HomeFragment;
import com.example.fadebook.ui.MainActivity.SignUp.SignupFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SigninFragment extends Fragment {
    private static final String TAG = "SigninFragment";
    @BindView(R.id.input_email)
    TextInputLayout inputEmail;
    @BindView(R.id.input_pass)
    TextInputLayout inputPass;
    @BindView(R.id.signin_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.main_signin_layout)
    ConstraintLayout mainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, v);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        return v;
    }

    @OnClick(R.id.card_sing_in)
    public void singIn() {
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        String email = inputEmail.getEditText().getText().toString();
        String pass = inputPass.getEditText().getText().toString();
        if (!email.isEmpty() && !pass.isEmpty()) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new HomeFragment())
                                .commit();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onComplete: " + task.getException().toString());
                        showSnackBar(task.getException().getMessage());
                    }
                }
            });
        } else if (email.isEmpty() && pass.isEmpty()) {
            inputEmail.setError("Email can't be empty");
            inputPass.setError("Password can't be empty");
        } else if (pass.isEmpty()) {
            inputPass.setError("Password can't be empty");
        } else if (email.isEmpty()) {
            inputEmail.setError("Email can't be empty");
        }

    }

    @OnClick(R.id.txt_sign_up)
    public void signUp() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left,
                        R.anim.left_to_right, R.anim.right_to_left)
                .add(R.id.container, new SignupFragment())
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.txt_forget_password)
    public void forgetPassword() {
        ///TODO : navigate to forget password
    }

    @OnClick(R.id.img_apple_sign_in)
    public void appleSignIn() {
        ///TODO : Sign in using apple auth
    }

    @OnClick(R.id.img_google_sign_in)
    public void googleSignIn() {
        ///TODO : Sign in using google auth
    }

    @OnClick(R.id.img_facebook_sign_in)
    public void facebookSignIn() {
        ///TODO : Sign in using facebook auth
    }

    private void showSnackBar(String message) {
        View parentLayout = getActivity().findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }
}