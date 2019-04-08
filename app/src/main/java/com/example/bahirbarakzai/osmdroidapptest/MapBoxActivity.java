package com.example.bahirbarakzai.osmdroidapptest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MapBoxActivity extends AppCompatActivity {

    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_box);

        Mapbox.getInstance(this,"sk.eyJ1IjoiYmFoaXJiYXJhayIsImEiOiJjanJweWxwbm8wYTNiM3ltcjVkYmx3YWE4In0.xK0aLiiTkkLbS5Io4MnpcQ");

        mMapView = findViewById(R.id.mapview_mapbox);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.SATELLITE, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {


                        //Map is set up and the style has loaded.
                        //Set up the offlineManger
                        OfflineManager offlineManager = OfflineManager.getInstance(MapBoxActivity.this);

                        //A Bounding box fo the office
                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                .include(new LatLng(33.9347,67.7034))
                                .include(new LatLng(33.9347,67.7034))
                                .build();

                        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                                mapboxMap.getStyle().getUrl(),
                                latLngBounds,
                                10,
                                18,
                                MapBoxActivity.this.getResources().getDisplayMetrics().density
                        );

                        byte[]  metadata ;
                        try{
                            String JSON_FIELD_NAME = null;
                            String JSON_CHARSET = "UTF-8";
                            JSONObject jsonObject = new JSONObject();
                            String json = jsonObject.toString();
                            metadata = json.getBytes(JSON_CHARSET);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}
