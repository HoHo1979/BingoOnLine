package com.iotarch.bingoonline;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.iotarch.bingoonline.firebase.DataBaseHelper;
import com.iotarch.bingoonline.livedata.MyViewModelFactory;
import com.iotarch.bingoonline.livedata.StatusViewModel;
import com.iotarch.bingoonline.livedata.StatusViewModelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BingoActivity extends AppCompatActivity {

    private static final String TAG = BingoActivity.class.getSimpleName() ;
    private String roomId;
    private RecyclerView bingoRecycler;
    private TextView tvRoomName;
    List<MyButton> buttons;
    private StatusViewModel numberViewModel;
    private ButtonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttons = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {

            MyButton button = new MyButton(this);
            button.setNumber(i);
            button.setSelected(false);
            buttons.add(button);
        }

        Collections.shuffle(buttons);

        findViews();

        String roomName = getIntent().getStringExtra("ROOM_NAME");
        String roomId = getIntent().getStringExtra("ROOM_ID");


        tvRoomName.setText(roomName);


        numberViewModel = ViewModelProviders.of(this,new StatusViewModelFactory(getApplication(),roomId))
                .get(StatusViewModel.class);


        numberViewModel.getFirebaseData().observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {


                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    String key = data.getKey();
                    Boolean status = data.getValue(Boolean.class);


                    for (MyButton button : buttons) {

                       Integer number=button.getNumber();
                       Integer intKey = Integer.parseInt(key);

                       if(number.equals(intKey)&&status!=null) {
                           button.setSelected(status);
                           button.setSel(status);
                       }

                    }


                }

                adapter.setButtons(buttons);
                adapter.notifyDataSetChanged();

            }

        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    private void findViews() {
        tvRoomName = findViewById(R.id.tvRoomName);
        roomId = getIntent().getStringExtra("ROOM_ID");
        bingoRecycler = findViewById(R.id.bingoRecycler);
        bingoRecycler.setHasFixedSize(true);
        bingoRecycler.setLayoutManager(new GridLayoutManager(this,5));

        adapter = new ButtonAdapter(buttons);
        bingoRecycler.setAdapter(adapter);

    }


    class ButtonAdapter extends RecyclerView.Adapter<ButtonHolder>{

        List<MyButton> buttons;

        public ButtonAdapter(List<MyButton> buttons) {
            this.buttons = buttons;
        }

        public void setButtons(List<MyButton> buttons) {
            this.buttons = buttons;
        }

        @NonNull
        @Override
        public ButtonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BingoActivity.this).inflate(R.layout.button_layout,parent,false);
            return new ButtonHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ButtonHolder holder, int position) {
            holder.bindNumber(buttons.get(position).getNumber(),buttons.get(position).isSel());
        }

        @Override
        public int getItemCount() {
            return buttons.size();
        }
    }


    class ButtonHolder extends RecyclerView.ViewHolder{

        private final MyButton button;


        public ButtonHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.circleButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyButton button= (MyButton) v;
                    DataBaseHelper.getInstance().updateRoomStatus(roomId,button.getNumber(),true);

                }
            });
        }

        public void bindNumber(int number,Boolean isSelected){
            button.setText(number+"");
            button.setNumber(number);
            button.setSelected(isSelected);
            button.setSel(isSelected);

            if(button.isSelected()==true){
                Log.d(TAG, "bindNumber: "+button.getNumber()+button.isSelected());
            }

            if(button.isSel()==true){
                Log.d(TAG, "bindNumber: sel "+button.getNumber()+button.isSel());
            }

        }
    }

}
