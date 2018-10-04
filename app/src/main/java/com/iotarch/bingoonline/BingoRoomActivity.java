package com.iotarch.bingoonline;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.iotarch.bingoonline.entity.GameRoom;
import com.iotarch.bingoonline.entity.User;
import com.iotarch.bingoonline.firebase.DataBaseHelper;
import com.iotarch.bingoonline.livedata.MyViewModel;
import com.iotarch.bingoonline.livedata.MyViewModelFactory;

public class BingoRoomActivity extends AppCompatActivity {

    private static final int LOGIN_CODE = 100;
    private static final String TAG = BingoRoomActivity.class.getSimpleName() ;

    private String uId;
    private TextView tvName;
    private View ivAvatar;
    private LinearLayout layoutAvatar;
    FirebaseRecyclerAdapter adapter;

    Boolean isImageDisplayed = false;

    int [] avatars = {R.drawable.avatar_0,R.drawable.avatar_1,R.drawable.avatar_2,R.drawable.avatar_3,R.drawable.avatar_4,R.drawable.avatar_5,R.drawable.avatar_6};
    private RecyclerView roomRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo_room);
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void findViews() {

        tvName = findViewById(R.id.tvName);
        ivAvatar = findViewById(R.id.ivAvatar);
        layoutAvatar = findViewById(R.id.layoutAvatar);
        layoutAvatar.setVisibility(View.INVISIBLE);
        roomRecyclerView = findViewById(R.id.roomRecyclerView);

        roomRecyclerView.setHasFixedSize(true);
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseDatabase.getInstance().getReference().child("Rooms").limitToFirst(50);

        FirebaseRecyclerOptions<GameRoom> options = new FirebaseRecyclerOptions
                .Builder<GameRoom>()
                .setQuery(query,GameRoom.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<GameRoom,RoomViewHolder>(options) {

            @NonNull
            @Override
            public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_info,parent,false);
                return new RoomViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RoomViewHolder holder, int position, @NonNull GameRoom model) {

                holder.bindRoom(model);

            }
        };

        roomRecyclerView.setAdapter(adapter);


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


    public void addRoom(View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                //final View layout = getLayoutInflater().inflate(R.layout.user_info_alert,null);
               EditText tvRoomName = new EditText(this);

                alert.setView(tvRoomName)
                        .setMessage("Please Enter Game Room Name")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                GameRoom room = new GameRoom();
                                room.setOwnerId(uId);
                                room.setRoomName(tvRoomName.getText().toString());
                                DataBaseHelper.getInstance().saveRoom(room);

                            }
                        }).show();



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
                        startActivity(new Intent(BingoRoomActivity.this,MainActivity.class));
                        finish();
                    }
                });

                break;
        }


        return super.onOptionsItemSelected(item);
    }


    class RoomViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvRoomName;
        private final ImageView ivRoomAvatar;
        private final TextView tvOwner;
        String roomId;
        Boolean isCreator=false;

        public RoomViewHolder(View itemView) {
            super(itemView);

            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            ivRoomAvatar = itemView.findViewById(R.id.ivRoomAvatar);
            tvOwner = itemView.findViewById(R.id.tvOwnerName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(BingoRoomActivity.this,BingoActivity.class);
                    intent.putExtra("ROOM_NAME",tvRoomName.getText().toString());

                    if(roomId!=null) {
                            intent.putExtra("ROOM_ID", roomId);
                            Log.d(TAG, "onClick: "+roomId);
                    }

                    if(isCreator){
                        intent.putExtra("IS_CREATOR",true);
                        Log.d(TAG, "onClick: creator true");
                    }

                    startActivity(intent);

                }
            });

        }

        public void bindRoom(GameRoom room){

            tvRoomName.setText(room.getRoomName());

            DataBaseHelper.getInstance().updateRoomUser(room.getOwnerId(),tvOwner,ivRoomAvatar,avatars);

            roomId = room.getRoomId();


            if(room.getOwnerId().equals(uId)){
                isCreator=true;
            }


        }
    }


}
