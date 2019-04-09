package com.example.bahirbarakzai.osmdroidapptest;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;

public class DataDrivenActivity extends AppCompatActivity {



    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYmFoaXJiYXJhayIsImEiOiJjanJweWxwbm8wYTNiM3ltcjVkYmx3YWE4In0.xK0aLiiTkkLbS5Io4MnpcQ");
        setContentView(R.layout.activity_data_driven);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        RasterSource rasterSource = new RasterSource("herat-source","http://api.mapbox.com/v4/bahirbarak.93fb3509.json?access_token=");
                        RasterLayer rasterLayer = new RasterLayer("kabul-source","http://api.mapbox.com/v4/bahirbarak.7i55j43u.json?access_token=");
                        VectorSource vectorSource = new VectorSource(
                                "trees-source",
            // replace examples.8mj5l1r9 with the map ID for the tileset
            // you created by uploading data to your Mapbox account
                                "http://api.mapbox.com/v4/bahirbarak.98o7lr6h.json?access_token="
                        );
                        style.addLayer(rasterLayer);
                       // style.addSource(rasterLayer);
                        CircleLayer circleLayer = new CircleLayer("trees-style", "trees-source");
            // replace street-trees-DC-9gvg5l with the name of your source layer
                        circleLayer.setSourceLayer("13OCT02064922-S3DS_R1C4-05804-bf7t5d");
                        circleLayer.withProperties(
                                circleOpacity(2.6f),
                                circleColor(Color.parseColor("#000000")),
                                circleRadius(
                                        interpolate(exponential(1.0f), get("DBH"),
                                                stop(0, 0f),
                                                stop(1, 1f),
                                                stop(110, 11f)
                                        )
                                )
                        );
                        style.addLayer(circleLayer);
                    }
                });
            }
        });
    }


    // Add the mapView's own lifecycle methods to the activity's lifecycle methods
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
