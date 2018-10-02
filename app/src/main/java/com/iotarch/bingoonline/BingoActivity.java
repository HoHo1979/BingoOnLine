package com.iotarch.bingoonline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BingoActivity extends AppCompatActivity {

    private String roomId;
    private RecyclerView bingoRecycler;
    private TextView tvRoomName;
    List<MyButton> buttons = new ArrayList<>();
    private FirebaseRecyclerAdapter<Boolean, ButtonHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        for (int i = 0; i < 25; i++) {

            MyButton button = new MyButton(this);
            button.setNumber(i+1);
            buttons.add(button);
        }

        Collections.shuffle(buttons);

        findViews();

        String roomName = getIntent().getStringExtra("ROOM_NAME");

        tvRoomName.setText(roomName);

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
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void findViews() {
        tvRoomName = findViewById(R.id.tvRoomName);
        roomId = getIntent().getStringExtra("ROOM_ID");
        bingoRecycler = findViewById(R.id.bingoRecycler);
        bingoRecycler.setHasFixedSize(true);
        bingoRecycler.setLayoutManager(new GridLayoutManager(this,5));

        Query query = FirebaseDatabase.getInstance().getReference().child("Rooms").child(roomId).child("status").orderByKey();

        FirebaseRecyclerOptions<Boolean> options = new FirebaseRecyclerOptions.Builder<Boolean>()
                .setQuery(query,Boolean.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Boolean, ButtonHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ButtonHolder holder, int position, @NonNull Boolean model) {
                //This has problem , should create own adapter and add viewHolder to listen on the change of the data
                holder.bindNumber(model);
                holder.button.setText(buttons.get(position).number+"");
            }

            @NonNull
            @Override
            public ButtonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(BingoActivity.this).inflate(R.layout.button_layout,parent,false);
                return new ButtonHolder(view);
            }
        };


        bingoRecycler.setAdapter(adapter);

    }


    class ButtonHolder extends RecyclerView.ViewHolder{

        private final MyButton button;

        public ButtonHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.circleButton);
        }

        public void bindNumber(Boolean isSelected){
            button.setSelected(isSelected);
        }
    }

}
