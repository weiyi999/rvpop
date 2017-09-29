package com.uniqueyi.rvpop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.uniqueyi.poplib.RvPop;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rv;
    private RVAdapter rvAdapter;
    private List<String> aList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
//        rv.setOnTouchListener(this);
        initData();


        initEvent();
    }

    private void initEvent() {
        rvAdapter.setmClick(new RVAdapter.MClick() {

            @Override
            public void longClick(View v, final int pos, final String title) {
                RvPop.getInstance(MainActivity.this).showPopupWindow(v).setmClick(new RvPop.MClick() {
                    @Override
                    public void delClick() {
                        super.delClick();
                        Toast.makeText(MainActivity.this, "点击:" + title + ",pos=" + pos, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private void initData() {
        aList = new ArrayList<>();
        addData();
        rvAdapter = new RVAdapter(aList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(rvAdapter);
    }

    private void addData() {
        aList.clear();
        for (int i = 0; i < 35; i++) {
            aList.add("测试" + i);
        }
    }

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.rv);
    }
}
