package com.iotarch.bingoonline;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.iotarch.bingoonline.entity.User;
import com.iotarch.bingoonline.firebase.DataBaseHelper;
import com.iotarch.bingoonline.livedata.MyViewModel;
import com.iotarch.bingoonline.livedata.MyViewModelFactory;

import java.util.Arrays;

public class BingoActivity extends AppCompatActivity {

    private static final int LOGIN_CODE = 100;
    private static final String TAG = BingoActivity.class.getSimpleName() ;

    private String uId;
    private TextView tvName;
    private View ivAvatar;
    private LinearLayout layoutAvatar;

    Boolean isImageDisplayed = false;

    int [] avatars = {R.drawable.avatar_0,R.drawable.avatar_1,R.drawable.avatar_2,R.drawable.avatar_3,R.drawable.avatar_4,R.drawable.avatar_5,R.drawable.avatar_6};


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViews();

        uId = getIntent().getStringExtra("USER_ID");

        if(uId!=null){
            MyViewModel viewModel = ViewModelProviders
                    .of(this, new MyViewModelFactory(getApplication(), uId))
                    .get(MyViewModel.class);


            viewModel.getUserLiveData().observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {

                    tvName.setText(user.getDisplayName());

                    ivAvatar.setBackgroundResource(avatars[user.getImageId()]);

                }
            });
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void findViews() {

        tvName = findViewById(R.id.tvName);
        ivAvatar = findViewById(R.id.ivAvatar);
        layoutAvatar = findViewById(R.id.layoutAvatar);
        layoutAvatar.setVisibility(View.INVISIBLE);
        ivAvatar.setOnClickListener(e->{

            if (isImageDisplayed != true) {
                layoutAvatar.setVisibility(View.VISIBLE);
                isImageDisplayed = true;
            }else{
                layoutAvatar.setVisibility(View.INVISIBLE);
                isImageDisplayed=false;
            }

        });

        ImageView ivAvatar_0 = findViewById(R.id.ivAvatar_0);
        ImageView ivAvatar_1 = findViewById(R.id.ivAvatar_1);
        ImageView ivAvatar_2 = findViewById(R.id.ivAvatar_2);
        ImageView ivAvatar_3 = findViewById(R.id.ivAvatar_3);
        ImageView ivAvatar_4 = findViewById(R.id.ivAvatar_4);
        ImageView ivAvatar_5 = findViewById(R.id.ivAvatar_5);
        ImageView ivAvatar_6 = findViewById(R.id.ivAvatar_6);


        createOnClickListener(ivAvatar_0);
        createOnClickListener(ivAvatar_1);
        createOnClickListener(ivAvatar_2);
        createOnClickListener(ivAvatar_3);
        createOnClickListener(ivAvatar_4);
        createOnClickListener(ivAvatar_5);
        createOnClickListener(ivAvatar_6);

    }

    private void createOnClickListener(ImageView ivAvatar) {

        User user = new User();

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){

                    case R.id.ivAvatar_0:
                        user.setImageId(0);
                        break;
                    case R.id.ivAvatar_1:
                        user.setImageId(1);
                        break;
                    case R.id.ivAvatar_2:
                        user.setImageId(2);
                        break;
                    case R.id.ivAvatar_3:
                        user.setImageId(3);
                        break;
                    case R.id.ivAvatar_4:
                        user.setImageId(4);
                        break;
                    case R.id.ivAvatar_5:
                        user.setImageId(5);
                        break;
                    case R.id.ivAvatar_6:
                        user.setImageId(6);
                        break;
                    default:
                        user.setImageId(0);
                        break;

                }

                user.setUserID(uId);

                if(user.getUserID()!=null) {
                    DataBaseHelper.getInstance().saveUserImage(user);
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(BingoActivity.this,MainActivity.class));
                        finish();
                    }
                });

                break;
        }


        return super.onOptionsItemSelected(item);
    }

}