package com.zhuandian.barrageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BarrageView barrageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barrageView = (BarrageView) findViewById(R.id.barrage_view);
        List<String> list = new ArrayList<>();
        list.add("打算发大水发大水分打算发生的范德萨范德萨");
        list.add("fsdafds");
        list.add("哈哈的伙食费和的方式发大水");
        list.add("安顿");
        list.add("打算发大水发大水分打算");
        list.add("fsdafds");
        list.add("111111111111111111111111");
        barrageView.setBarrageItemList(list);
        barrageView.startBarrageView();
        barrageView.setOnBarrageItemClickListener(new IBarrageItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
