package com.example.kaixin.experimentnine;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText City;
    private TextView name, time_update, wendu, shidu, air, degrees, wind;
    private Button Search;
    private ListView listView;
    private GridLayout gridLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<String> response;
    private ArrayList<HashMap<String,String>> listItems;
    private ArrayList<Weather> weather_list;
    private String[] list_exponents = new String[] {
            "紫外线指数", "感冒指数", "穿衣指数", "洗车指数", "运动指数", "空气污染指数"
    };
    private static final int UPDATE_CONTENT = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONTENT:
                    response = (ArrayList<String>)msg.obj;
                    String result = response.get(0);
                    if ("查询结果为空".equals(result)) {
                        Toast.makeText(MainActivity.this, "当前城市不存在，请重新输入", Toast.LENGTH_SHORT).show();
                    } else if ("发现错误：免费用户不能使用高速访问。http://www.webxml.com.cn/".equals(result)){
                        Toast.makeText(MainActivity.this, "您的点击速度过快，二次查询间隔<600ms", Toast.LENGTH_SHORT).show();
                    } else if ("发现错误：免费用户 24 小时内访问超过规定数量。http://www.webxml.com.cn/".equals(result)) {
                        Toast.makeText(MainActivity.this, "免费用户24小时内访问超过规定数量50次", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.size() > 28) {
                            gridLayout.setVisibility(View.VISIBLE);
                            String where = response.get(1);
                            name.setText(where);
                            String timestr = response.get(3);
                            time_update.setText(timestr.substring(11, timestr.length()) + " 更新");
                            showWeather();
                            showAir();
                            degrees.setText(response.get(8));
                            showList();
                            showNextFiveDay();
                        } else {
                            Toast.makeText(MainActivity.this, response.get(0), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private ArrayList<String> list;
    private static final String web = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        City = (EditText)findViewById(R.id.et_city);
        name = (TextView)findViewById(R.id.name_search);
        Search = (Button)findViewById(R.id.btn_search);
        listView = (ListView)findViewById(R.id.zs_list);
        time_update = (TextView)findViewById(R.id.time_update);
        gridLayout = (GridLayout)findViewById(R.id.gridlayout);
        wendu = (TextView)findViewById(R.id.wendu);
        shidu = (TextView)findViewById(R.id.shidu);
        air = (TextView)findViewById(R.id.air_quality);
        wind = (TextView)findViewById(R.id.wind);
        degrees = (TextView)findViewById(R.id.degrees);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(MainActivity.this)) {
                    postRequest();
                } else {
                    Toast.makeText(MainActivity.this, "当前网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void postRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    Log.i("Key", "Begin the connection");
                    URL url = new URL(web);
                    connection = (HttpURLConnection)(url.openConnection());
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoOutput(true);

                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String request = City.getText().toString();
                    request = URLEncoder.encode(request, "utf-8");
                    out.writeBytes("theCityCode="+ request + "&theUserID="+"489bf01aa5624255ac630fb50b076520");

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    message.what = UPDATE_CONTENT;
                    message.obj = parseXMLWithPull(response.toString());
                    handler.sendMessage(message);
                    Log.i("Key", response.toString());
                } catch (Exception e) {
                    Log.i("Key", "Fail to connect");
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                        Log.i("Key", "Disconnect the connection");
                    }
                }
            }
        }).start();
    }
    public ArrayList<String> parseXMLWithPull(String str_xml) {
        list = new ArrayList<String>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(str_xml));
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("string".equals(parser.getName())) {
                            String str = parser.nextText();
                            list.add(str);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void showWeather() {
        String weather = response.get(4);
        if (!"今日天气实况：暂无实况".equals(weather)) {
            Pattern p2 = Pattern.compile("(：+?)(.*?)((；+?)|$)");
            Matcher m2 = p2.matcher(weather);
            ArrayList<String> weathers = new ArrayList<>();
            while (m2.find()) {
                weathers.add(m2.group());
            }
            wendu.setText(weathers.get(0).substring(4,weathers.get(0).length()-1));
            wind.setText(weathers.get(1).substring(1, weathers.get(1).length()-1));
            shidu.setText("湿度：" + weathers.get(2).substring(1, weathers.get(2).length()));
        } else {
            wendu.setText("暂无");
            wind.setText("风力：暂无");
            shidu.setText("湿度：暂无");
        }
    }
    public void showAir() {
        String airQ = response.get(5);
        if (!"空气质量：暂无预报；紫外线强度：暂无预报".equals(airQ)) {
            Pattern p3 = Pattern.compile("(：+?)(.*?)(。+?)|$");
            Matcher m3 = p3.matcher(airQ);
            m3.find();
            m3.find();
            air.setText("空气质量：" + m3.group().substring(1, m3.group().length()-1));
        } else {
            air.setText("空气质量：暂无预报");
        }
    }
    public void showList() {
        String exponent = response.get(6);
        if (!"暂无预报".equals(exponent)) {
            Pattern p1 = Pattern.compile("(：+?)(.*?)(。+?)");
            Matcher m1 = p1.matcher(exponent);
            ArrayList<String> list_content = new ArrayList<>();
            while (m1.find()) {
                list_content.add(m1.group());
            }
            listItems = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < list_exponents.length; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("exponent", list_exponents[i]);
                map.put("content", list_content.get(i).substring(1, list_content.get(i).length()-1));
                listItems.add(map);
            }
        } else {
            listItems = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < list_exponents.length; i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("exponent", list_exponents[i]);
                map.put("content", "暂无");
                listItems.add(map);
            }
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, listItems, R.layout.list_item,
                new String[]{"exponent", "content"}, new int[] {R.id.exponent, R.id.content});
        listView.setAdapter(simpleAdapter);
    }
    public void showNextFiveDay() {
        weather_list = new ArrayList<Weather>();
        Weather first = new Weather(response.get(7).substring(0,6),
                response.get(7).substring(7, response.get(7).length()),
                response.get(8));
        weather_list.add(first);
        Weather second = new Weather(response.get(12).substring(0,6),
                response.get(12).substring(7, response.get(12).length()),
                response.get(13));
        weather_list.add(second);
        Weather third = new Weather(response.get(17).substring(0,6),
                response.get(17).substring(7, response.get(17).length()),
                response.get(18));
        weather_list.add(third);
        Weather fourth = new Weather(response.get(22).substring(0,6),
                response.get(22).substring(7, response.get(22).length()),
                response.get(23));
        weather_list.add(fourth);
        Weather fifth = new Weather(response.get(27).substring(0,6),
                response.get(27).substring(7, response.get(27).length()),
                response.get(28));
        weather_list.add(fifth);
        mRecyclerView.setAdapter(new WeatherAdapter(MainActivity.this, weather_list));
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
