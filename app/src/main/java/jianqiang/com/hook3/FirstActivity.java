package jianqiang.com.hook3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import jianqiang.com.hook3.entity.Course;
import jianqiang.com.hook3.entity.Room;

public class FirstActivity extends Activity {

    private static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first);

        findViewById(R.id.btnGotoSecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                intent.putExtra("HotelId", 44);
                intent.putExtra("HotelName", "郭郭大酒店");

                ArrayList<Room> rooms = new ArrayList<Room>();
                rooms.add(new Room("大床房", 100));
                rooms.add(new Room("双床房", 200));
                intent.putExtra("Rooms", rooms);

                startActivity(intent);
            }
        });
    }
}
