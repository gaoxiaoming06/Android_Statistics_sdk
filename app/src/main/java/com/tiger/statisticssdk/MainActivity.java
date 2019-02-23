package com.tiger.statisticssdk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tiger.tigerstatisticssdk.TkAgent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        TkAgent.onPageStart(this,"aaaaaaa");
//        TkAgent.onPageStart(this,"ffffffff");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TkAgent.onPageEnd(this,"aaaaaaa");

//        //测试报错日志收集
//        EditText et_test = (EditText) findViewById(R.id.et_test);
//        et_test.setText("aaaa");
//        et_test.setSelection(5);


//        TkAgent.onPageEnd(this,"ffffffff");
//        TkAgent.onPageStart(this,"aaaaaaa1111");
//        TkAgent.onPageEnd(this,"aaaaaaa1111");
//        TkAgent.onPageStart(this,"aaaaaaa");
//        TkAgent.onPageStart(this,"ffffffff");
    }

    public void testClick(View view){
        startActivity(new Intent(this,WebActivity.class));
        TkAgent.onEvent(this,"clickckckc");
    }
}
