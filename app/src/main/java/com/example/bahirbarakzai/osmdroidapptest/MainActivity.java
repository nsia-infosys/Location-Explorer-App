package com.example.bahirbarakzai.osmdroidapptest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.ArchiveFileFactory;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MapTileAssetsProvider;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity{

    public static final GeoPoint KABUL = new GeoPoint(34.4933,69.1900);
    private MapView mapView;
    private Button load_map_btn,copy_map_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
        
             mapView = findViewById(R.id.mapview);
            mapView.setClickable(true);
            mapView.setBuiltInZoomControls(true);
            mapView.setMultiTouchControls(true);
            //  mapView.setUseDataConnection(true);
            mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
            IMapController mapViewController = mapView.getController();
            mapViewController.setZoom(14);
            mapViewController.setCenter(KABUL);

            copy_map_btn=findViewById(R.id.copy_map_btn);
            copy_map_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent download_intent = new Intent(MainActivity.this,IntentServiceDownload.class);
                    download_intent.setAction("com.example.bahirbarakzai.osmdroidapptest");
                    download_intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    startService(download_intent);

                }
            });

            load_map_btn=findViewById(R.id.load_map_btn);
            load_map_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TileMaps";
                    String filename = "tiles.zip";
//                    File file = new File(dirPath,filename);
//                    if (!file.exists()){
//                       copyAssetFiles(filename);
//                    }


                    //Getting File from External Storage
                    File tileFile = getTileFile("tiles.zip");

                    // save zip to sd
                    IArchiveFile[] archives = new IArchiveFile[1];
                    archives[0] = ArchiveFileFactory.getArchiveFile(tileFile);

                    // Simple implementation that extends BitmapTileSourceBase
                    CustomTileSource customTiles = new CustomTileSource("MapquestOSM", null, 10, 14, 256, ".png");  // Maverik is the name of the folder inside the zip (so zip is map.zip and inside it is a folder called Maverik)

                    MapTileModuleProviderBase[] providers = new MapTileModuleProviderBase[2];
//                    providers[0] = new MapTileAssetsProvider(new SimpleRegisterReceiver(getApplicationContext()),getAssets(),customTiles);
                    providers[0] = new MapTileFileArchiveProvider(new SimpleRegisterReceiver(getApplicationContext()), customTiles, archives);    // this one is for local tiles (zip etc.)
                    providers[1] = new MapTileDownloader(TileSourceFactory.MAPQUESTOSM);    // Osmdroid library map type /web tile source

                    mapView.setUseDataConnection(false);

                    MapTileProviderArray tileProvider = new MapTileProviderArray(customTiles,
                            new SimpleRegisterReceiver(getApplicationContext()), providers);
                    TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, getApplicationContext());
                    tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);  // this makes sure that the invisble tiles of local tiles are transparent so we can see tiles from web, transparent have a minor performance decline.

                    mapView.getOverlays().add(tilesOverlay);
                    load_map_btn.setVisibility(View.INVISIBLE);
                    mapView.invalidate();

                }
            });
            //Calling to copy asset Files to external Storage

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 100:
                if(grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "External Storage Permission Granted", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }else{
                    Toast.makeText(this, "External Storage Permission Not Granted the application may not work correctlly", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //Copy File from Assets to External Storage

    private void copyAssetFiles(String filename){
        InputStream in = null;
        OutputStream out;
        AssetManager assetManager = null;
        try {
                in = this.getAssets().open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TileMaps";
        File file = new File(dirPath);
        if (!file.exists()){
          file.mkdirs();
            try {
                File file1 = new File(dirPath,filename);
                file1.createNewFile();


                //copy process


                String [] files =assetManager.list(filename);
                for (String each_file : files){
                    in = assetManager.open(each_file,AssetManager.ACCESS_BUFFER);

                    Log.e("Inside For","Inside for loop");

                }
                Log.d("FileCheck",files.length+"");
//                if (in ==null){
//                    in=assetManager.open(filename,AssetManager.ACCESS_UNKNOWN);
//                }

                if(in !=null) {
                    Log.e("Error#2","inside if loop");
                    Toast.makeText(this, "Inside IF Statement", Toast.LENGTH_SHORT).show();
                    File outFile = new File(dirPath, filename);
                    out = new FileOutputStream(outFile);
//                copyFile(in, out);

                    byte[] buffer = new byte[in.available()];
                    int read = in.read(buffer);

                    while (read != -1) {
                        out.write(buffer, 0, read);

                    }
                    out.flush();
                    in.close();
                    out.close();
                    Toast.makeText(this, "At end of if Statement", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("Error#1","input stream is null");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }




        }



    }


    private File getTileFile(String fileName){

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TileMaps";
        File file= new File(dirPath,fileName);
        return file;
    }








}
