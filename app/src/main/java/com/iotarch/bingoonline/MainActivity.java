package com.iotarch.bingoonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.client.AuthUiInitProvider;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.iotarch.bingoonline.entity.User;
import com.iotarch.bingoonline.firebase.DataBaseHelper;
import com.iotarch.bingoonline.livedata.MyViewModel;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    private static final int LOGIN_CODE = 100;
    private static final String TAG = MainActivity.class.getSimpleName() ;
   // private MyViewModel viewModel;
    private TextView tvName;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvName = findViewById(R.id.tvName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        auth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void goToBingoActivity() {
        Intent intent = new Intent(this,BingoActivity.class);
        intent.putExtra("USER_ID",auth.getCurrentUser().getUid());
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.singout:
                AuthUI.getInstance().signOut(this);
                break;
        }



        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        final FirebaseUser authUser = firebaseAuth.getCurrentUser();

        if(authUser !=null){


                       User userData = new User();
                       userData.setUserName(authUser.getEmail());
                       userData.setUserID(authUser.getUid());
                       userData.setDisplayName(authUser.getDisplayName());
                       DataBaseHelper.getInstance().saveUserData(userData);

                       goToBingoActivity();


        }else{

            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    )
                            )
                            .setIsSmartLockEnabled(false)
                    .build()
                    ,LOGIN_CODE
            );

        }

    }


}
