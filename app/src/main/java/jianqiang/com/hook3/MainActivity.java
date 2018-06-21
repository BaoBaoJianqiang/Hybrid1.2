package jianqiang.com.hook3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import jianqiang.com.hook3.entity.Course;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //申请SDCard读写权限
        verifyStoragePermissions();


        findViewById(R.id.btnFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareData();

                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                intent.putExtra("UserName", "建强");
                intent.putExtra("Age", 10);

                ArrayList<Course> courses = new ArrayList<Course>();
                courses.add(new Course("Math", 80));
                courses.add(new Course("English", 90));
                courses.add(new Course("Chinese", 75));
                intent.putExtra("Courses", courses);

                startActivity(intent);
            }
        });

        findViewById(R.id.btnSecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnThird).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        findViewById(R.id.btnFourth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareData();
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    void prepareData() {
        String newFilePath = Environment.getExternalStorageDirectory() + File.separator + "myH5";
        Utils.copy(this, "firstpage.html", newFilePath);
        Utils.copy(this, "secondpage.html", newFilePath);
        Utils.copy(this, "thirdpage.html", newFilePath);
        Utils.copy(this, "style.css", newFilePath);

        String h5FilePath1 = newFilePath + File.separator + "firstpage.html";
        String h5FilePath2 = newFilePath + File.separator + "thirdpage.html";

        HashMap<String, Integer> fields = new HashMap<String, Integer>();
        fields.put("UserName", 1);  //1 means string
        fields.put("Age", 2);       //2 means int
        fields.put("Courses", 3);   //3 means object

        PageInfo pageInfo1 = new PageInfo("file://" + h5FilePath1, fields);
        MyApplication.pages.put("jianqiang.com.hook3.FirstActivity", pageInfo1);

        PageInfo pageInfo2 = new PageInfo("file://" + h5FilePath2, null);
        MyApplication.pages.put("jianqiang.com.hook3.ThirdActivity", pageInfo2);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 2 && requestCode == 101) {
            int score = data.getIntExtra("score", 0);
            int a = 1;
            int b = score;
        }
    }
}
