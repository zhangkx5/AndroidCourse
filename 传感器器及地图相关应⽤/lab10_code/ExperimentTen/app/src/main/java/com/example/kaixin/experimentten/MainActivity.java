package com.example.kaixin.experimentten;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private ToggleButton toggleButton;
    private LocationManager locationManager;
    private Location location;
    private float rotation = 0;
    private Vibrator vibrator = null;
    private String locationProvider;
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            ChangeLocation();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        float[] accValues = null;
        float[] magValues = null;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    if (accValues != null && location != null) {
                        for (int i = 0; i < 3; i++) {
                            if (Math.abs(accValues[i] - event.values[i]) > 18) {
                                Toast.makeText(MainActivity.this, "您所在的纬度为：" + location.getLatitude() + "\n" +
                                        "您所在的经度为：" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                                vibrator.vibrate(500);
                                break;
                            }
                        }
                    }
                    accValues = event.values.clone();
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    if (accValues != null && magValues != null) {
                        float[] R = new float[9];
                        float[] values = new float[3];
                        SensorManager.getRotationMatrix(R, null, accValues, magValues);
                        SensorManager.getOrientation(R, values);
                        float newRotationDegree = (float) Math.toDegrees(values[0]);
                        if (Math.abs(newRotationDegree - rotation) > 0.5) {
                            rotation = newRotationDegree;
                        }
                    }
                    magValues = event.values.clone();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "App will finish in 3 secs...", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mMapView = (TextureMapView) findViewById(R.id.bmapView);
        toggleButton = (ToggleButton)findViewById(R.id.tb_center);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        //设置箭头为当前位置的显示
        setLocationPointer();

        //获取当前位置的具体信息 location
        getMyLocationMessage();

        //将当前位置设为地图的中心位置
        ChangeLocation();

        //开关按钮随地图的手动平移、放大、缩小而自动切换状态
        mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        toggleButton.setChecked(false);
                        break;
                    default:
                        break;
                }
            }
        });

        //开关切换状态时重新定位
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    getMyLocationMessage();
                    ChangeLocation();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mSensorManager.registerListener(mSensorEventListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorEventListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    private void getMyLocationMessage() {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
            Toast.makeText(this, "GPS_proveder using...", Toast.LENGTH_SHORT).show();
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Toast.makeText(this, "Network_provider using...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            location = locationManager.getLastKnownLocation(locationProvider);
            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void ChangeLocation() {
        if (location != null) {
            MyLocationData.Builder builder = new MyLocationData.Builder();
            CoordinateConverter coordinateConverter = new CoordinateConverter();
            coordinateConverter.from(CoordinateConverter.CoordType.GPS);
            coordinateConverter.coord(new LatLng(location.getLatitude(),location.getLongitude()));
            LatLng desLatLng = coordinateConverter.convert();
            builder.latitude(desLatLng.latitude);
            builder.longitude(desLatLng.longitude);
            builder.direction(rotation);
            mMapView.getMap().setMyLocationData(builder.build());

            if (toggleButton.isChecked()) {
                MapStatus mapStatus = new MapStatus.Builder().target(desLatLng).build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                mMapView.getMap().setMapStatus(mapStatusUpdate);
            }
        }
    }

    private void setLocationPointer() {
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.pointer), 100, 100, true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        mMapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor);
        mMapView.getMap().setMyLocationConfigeration(myLocationConfiguration);
    }
}
