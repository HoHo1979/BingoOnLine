package com.iotarch.bingoonline;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.iotarch.bingoonline.entity.User;
import com.iotarch.bingoonline.fragment.InputFragment;
import com.iotarch.bingoonline.fragment.ReceiveFragment;
import com.iotarch.bingoonline.livedata.MyViewModel;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener,InputFragment.OnFragmentInteractionListener,ReceiveFragment.OnFragmentInteractionListener {

    private static final int LOGIN_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView tvName = findViewById(R.id.tvName);

        //Decouple the DataSnapShort here to MyViewModel class so the MainActivity do not need to know anything
        //about where the data is coming from.
        MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        viewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                tvName.setText(user.getAdmin());
                Toast.makeText(MainActivity.this,user.getPassword(),Toast.LENGTH_LONG).show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==LOGIN_CODE){
            Toast.makeText(this,"Login Success",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user!=null){


        }else{

            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    )
                            )
                    .build()
                    ,LOGIN_CODE
            );
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
