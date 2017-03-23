package com.example.kaixin.lab3_code;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by kaixin on 2016/10/14.
 */
public class DetailActivity extends AppCompatActivity {

    int flag = 0;
    String[] operations = new String[] {
            "编辑联系人", "分享联系人", "加入黑名单", "删除联系人"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String[] detail = intent.getStringArrayExtra("extra_data");

        TextView name = (TextView)findViewById(R.id.name);
        name.setText(detail[1]);
        TextView num = (TextView)findViewById(R.id.num);
        num.setText(detail[2]);
        TextView type = (TextView)findViewById(R.id.type);
        type.setText(detail[3]);
        TextView address = (TextView)findViewById(R.id.address);
        address.setText(detail[4]);
        RelativeLayout top = (RelativeLayout)findViewById(R.id.top);
        top.setBackgroundColor(Color.parseColor("#"+detail[5]));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailActivity.this, android.R.layout.simple_list_item_1, operations);
        ListView op = (ListView)findViewById(R.id.operations);
        op.setAdapter(adapter);

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });
        final ImageButton star = (ImageButton)findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    star.setImageResource(R.mipmap.full_star);
                    flag = 1;
                } else {
                    star.setImageResource(R.mipmap.empty_star);
                    flag = 0;
                }
            }
        });
    }
}
