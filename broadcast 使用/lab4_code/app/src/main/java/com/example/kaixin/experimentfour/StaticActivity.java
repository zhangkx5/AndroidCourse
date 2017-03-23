package com.example.kaixin.experimentfour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaixin on 2016/10/21.
 */
public class StaticActivity extends Activity {

    private List<Fruit> fruitList = new ArrayList<Fruit>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.static_layout);
        initFruits();
        FruitAdapter adapter = new FruitAdapter(StaticActivity.this, R.layout.item, fruitList);
        ListView fruit_lv = (ListView)findViewById(R.id.fruit_list);
        fruit_lv.setAdapter(adapter);
        fruit_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("com.example.kaixin.experimentfour.staticreceiver");
                intent.putExtra("name",fruitList.get(position).getName());
                intent.putExtra("imageId", fruitList.get(position).getImageId());
                sendBroadcast(intent);
            }
        });
    }

    private void initFruits() {
        Fruit apple = new Fruit("apple", R.mipmap.apple);
        fruitList.add(apple);
        Fruit banana = new Fruit("banana", R.mipmap.banana);
        fruitList.add(banana);
        Fruit cherry = new Fruit("cherry", R.mipmap.cherry);
        fruitList.add(cherry);
        Fruit coco = new Fruit("coco", R.mipmap.coco);
        fruitList.add(coco);
        Fruit kiwi = new Fruit("kiwi", R.mipmap.kiwi);
        fruitList.add(kiwi);
        Fruit orange = new Fruit("Orange", R.mipmap.orange);
        fruitList.add(orange);
        Fruit pear = new Fruit("pear", R.mipmap.pear);
        fruitList.add(pear);
        Fruit strawberry = new Fruit("strawberry", R.mipmap.strawberry);
        fruitList.add(strawberry);
        Fruit watermelon = new Fruit("watermelon", R.mipmap.watermelon);
        fruitList.add(watermelon);
    }
}
