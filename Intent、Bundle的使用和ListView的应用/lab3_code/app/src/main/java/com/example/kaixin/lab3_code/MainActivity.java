package com.example.kaixin.lab3_code;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    List<Map<String,Object>> data = new ArrayList<>();
    ArrayList<String[]> info = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] s1 = new String[] {
                "A","Aaron","17715523654","手机","江苏苏州电信","BB4C3B"
        };
        final String[] s2 = new String[] {
                "E","Elvis","18825653224","手机","广东揭阳移动","c48d30"
        };
        final String[] s3 = new String[] {
                "D","David","15052116654","手机","江苏无锡移动","4469b0"
        };
        final String[] s4 = new String[] {
                "E","Edwin","18854211875","手机","山东青岛移动","20A17B"
        };
        final String[] s5 = new String[] {
                "F","Frank","13955188541","手机","安徽合肥移动","BB4C3B"
        };
        final String[] s6 = new String[] {
                "J","Joshua","13621574410","手机","江苏苏州移动","c48d30"
        };
        final String[] s7 = new String[] {
                "I","Ivan","15684122771","手机","山东烟台联通","4469b0"
        };
        final String[] s8 = new String[] {
                "M","Mark","17765213579","手机","广东珠海电信","20A17B"
        };
        final String[] s9 = new String[] {
                "J","Joseph","13315466578","手机","河北石家庄电信","BB4C3B"
        };
        final String[] s10 = new String[] {
                "P","Phoebe","17895466428","手机","山东东营移动","c48d30"
        };
        info.add(s1);
        info.add(s2);
        info.add(s3);
        info.add(s4);
        info.add(s5);
        info.add(s6);
        info.add(s7);
        info.add(s8);
        info.add(s9);
        info.add(s10);

        for (int i = 0; i < 10; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("letter", info.get(i)[0]);
            temp.put("name", info.get(i)[1]);
            data.add(temp);
        }
        ListView contacts = (ListView) findViewById(R.id.contact_list);
        final SimpleAdapter sa = new SimpleAdapter(this, data, R.layout.contacts_list,
                new String[] {"letter", "name"}, new int[] {R.id.letter, R.id.name});
        contacts.setAdapter(sa);
        contacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("确定删除联系人"+ info.get(position)[1]+"?");
                builder.setTitle("删除联系人");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.remove(position);
                        info.remove(position);
                        sa.notifyDataSetChanged();
                    }
                });
                builder.create().show();
                return true;
            }
        });
        contacts.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("extra_data", info.get(position));
                startActivity(intent);
            }
        });
    }
}
