package com.example.fadebook.ui.MainActivity.Home;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fadebook.R;
import com.example.fadebook.pojo.modules.Users;
import com.example.fadebook.pojo.services.TrackingService;
import com.example.fadebook.ui.MainActivity.Edit.EditFragment;
import com.example.fadebook.ui.MainActivity.SignIn.SigninFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private List<Users> usersList = new ArrayList<>();
    public static String CURRENT_USER_ID = FirebaseAuth.getInstance().getUid();
    public static final String NOTIFICATION_CHANNEL_ID = "Tracking";
    private static final int LOCATION_PERMISSION_CODE = 500;
    private UsersAdapter adapter;
    @BindView(R.id.home_recycler)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        changeState(true);
        ButterKnife.bind(this, v);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("TrackingApp");
        crateNotificationChannel();
        prepareFragment();
        isPermissionsGrunted();

        return v;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTracking();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new SigninFragment())
                        .commit();
                break;
            case R.id.action_edit_profile:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new EditFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.action_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchView.setQueryHint("Full name");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        adapter.getFilter().filter(query);
                        if (adapter.getItemCount() == 0) {
                            ///TODO : set Empty layout
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //adapter.getFilter().filter(newText);
                        return false;
                    }
                });
                break;
        }
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        changeState(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        changeState(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        changeState(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        changeState(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        changeState(false);
    }

    private void changeState(boolean val) {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(CURRENT_USER_ID).update("state", val)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: success");
            }
        });
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseFirestore.getInstance()
                .collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null)
                            showSnackBar(error.getMessage());
                        else {
                            if (usersList.size() > 0)
                                usersList.clear();
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Users users = snapshot.toObject(Users.class);
                                if (!users.getUid().contains(CURRENT_USER_ID))
                                    usersList.add(users);
                            }
                        }
                        adapter = new UsersAdapter(getContext(), usersList);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    private void isPermissionsGrunted() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        int isGrunted = ContextCompat.checkSelfPermission(getContext(), permissions[0]);
        if (isGrunted == PackageManager.PERMISSION_GRANTED) {
            startTracking();
        } else {
            requestPermissions(permissions, LOCATION_PERMISSION_CODE);
        }
    }

    private void crateNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    getActivity().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void startTracking() {
        Log.d(TAG, "startTracking: started");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(new Intent(getActivity(), TrackingService.class));
        } else {
            getActivity().startService(new Intent(getActivity(), TrackingService.class));
        }
    }
}