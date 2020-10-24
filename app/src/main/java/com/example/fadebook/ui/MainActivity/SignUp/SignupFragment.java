package com.example.fadebook.ui.MainActivity.SignUp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.fadebook.R;
import com.example.fadebook.ui.MainActivity.Home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class SignupFragment extends Fragment {
    private static final String TAG = "SignupFragment";
    @BindView(R.id.input_pass)
    TextInputLayout inputPass;
    @BindView(R.id.input_con_pass)
    TextInputLayout inputConPass;
    @BindView(R.id.txt_f_name)
    TextInputLayout inputFName;
    @BindView(R.id.txt_l_name)
    TextInputLayout inputLName;
    @BindView(R.id.select_img)
    CircleImageView selectImage;
    @BindView(R.id.input_email)
    TextInputLayout inputEmail;
    @BindView(R.id.sing_up_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.sing_up_layout)
    ConstraintLayout mainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @OnClick(R.id.card_save)
    public void signUp() {
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        Log.d(TAG, "signUp: clicked");
        String email = inputEmail.getEditText().getText().toString();
        String fName = inputFName.getEditText().getText().toString();
        String lName = inputLName.getEditText().getText().toString();
        String name = fName + " " + lName;
        String pass = inputPass.getEditText().getText().toString();
        String conPass = inputConPass.getEditText().getText().toString();
        if (!lName.isEmpty() && !fName.isEmpty() && !pass.isEmpty() && !conPass.isEmpty() && !email.isEmpty() ) {
            if (pass.equals(conPass)) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addTheDateToFirebase(name, email, authResult.getUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        progressBar.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                        showSnackBar(e.getMessage());
                    }
                });
            } else {
                inputPass.setError("");
                inputConPass.setError("password dose not match");
            }
        } else if (email.isEmpty()) {

            inputEmail.setError("Email cant be empty");

        } else if (fName.isEmpty()) {

            inputFName.setError("Name cant be empty");

        } else if (pass.isEmpty()) {

            inputPass.setError("Password cant be empty");

        } else if (lName.isEmpty()) {

            inputConPass.setError("Password cant be empty");

        } else if (conPass.isEmpty()) {

            inputConPass.setError("Password cant be empty");

        }
    }

    public void addTheDateToFirebase(String name, String email, String uid) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("uid", uid);
        user.put("state", true);
        user.put("imageUrl", "");
        FirebaseFirestore.getInstance().collection("users")
                .document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations
                                (R.anim.left_to_right, R.anim.right_to_left,
                                        R.anim.left_to_right, R.anim.right_to_left)
                        .replace(R.id.container, new HomeFragment())
                        .commit();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                showSnackBar(e.getMessage());
            }
        });
    }

    @OnClick(R.id.card_close)
    public void cardClose() {
        getActivity().onBackPressed();
    }

    private void showSnackBar (String message){
        View parentLayout = getActivity().findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
}