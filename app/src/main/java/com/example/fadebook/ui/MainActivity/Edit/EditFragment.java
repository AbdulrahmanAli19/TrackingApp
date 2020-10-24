package com.example.fadebook.ui.MainActivity.Edit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.fadebook.R;
import com.example.fadebook.pojo.modules.Users;
import com.example.fadebook.ui.MainActivity.Home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

@SuppressLint("NonConstantResourceId")
public class EditFragment extends Fragment {
    private static final String TAG = "EditFragment";
    private final int IMAGE_PICKER_CODE = 105;
    private Users users;
    private Uri file;
    @BindView(R.id.input_edit_name)
    TextInputLayout inputName;
    @BindView(R.id.img_edit_user)
    CircleImageView imgUser;
    @BindView(R.id.edit_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.txt_select_image)
    TextView textView;
    @BindView(R.id.main_edit_layout)
    ConstraintLayout mainLayout;
    @BindView(R.id.main_edit_progressbar)
    ProgressBar editProgressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, v);
        prepareFragment();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_PICKER_CODE:
                if (resultCode == RESULT_OK) {
                    file = data.getData();
                    Log.d(TAG, "onActivityResult: " + file.toString());
                    imgUser.setImageURI(data.getData());
                }
                break;
        }
    }

    @OnClick(R.id.card_save)
    public void onSaveClick() {
        mainLayout.setVisibility(View.GONE);
        editProgressbar.setVisibility(View.VISIBLE);
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference ref = reference.child("image/" + UUID.randomUUID().toString());
        Log.d(TAG, "onSaveClick: " + file.toString());
        ref.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgUser.setImageURI(file);
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateUserInfo(uri.toString());
                                mainLayout.setVisibility(View.VISIBLE);
                                editProgressbar.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mainLayout.setVisibility(View.VISIBLE);
                                editProgressbar.setVisibility(View.GONE);
                                Log.d(TAG, "onFailure: " + e.getMessage());
                                showSnackBar("Error");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackBar("Error");
            }
        });
    }

    @OnClick(R.id.img_edit_user)
    public void chooseAnImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICKER_CODE);
    }

    @OnClick(R.id.card_close)
    public void onCloseClick() {
        getActivity().onBackPressed();
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

    private void prepareFragment() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(HomeFragment.CURRENT_USER_ID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    users = documentSnapshot.toObject(Users.class);
                    inputName.getEditText().setText(users.getName());

                    if (users.getImageUrl().isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        imgUser.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        imgUser.setImageDrawable(getActivity().getDrawable(R.drawable.ic_person));
                    } else {

                        Picasso.get().load(users.getImageUrl())
                                .into(imgUser, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBar.setVisibility(View.GONE);
                                        imgUser.setVisibility(View.VISIBLE);
                                        textView.setVisibility(View.VISIBLE);

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        showSnackBar(e.getMessage());
                                    }
                                });
                    }

                } else if (!task.isSuccessful()) {
                    showSnackBar(task.getException().getMessage());
                }
            }
        });


    }

    public void updateUserInfo(String imgUrl) {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(HomeFragment.CURRENT_USER_ID)
                .update("imageUrl", imgUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showSnackBar("Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackBar("Error");
            }
        });
        String name = inputName.getEditText().getText().toString();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(HomeFragment.CURRENT_USER_ID)
                .update("name", name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showSnackBar("Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackBar("Error");
            }
        });
    }

}