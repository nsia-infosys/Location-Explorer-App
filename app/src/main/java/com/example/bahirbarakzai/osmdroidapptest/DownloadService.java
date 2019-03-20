package com.example.bahirbarakzai.osmdroidapptest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadService extends Service {

    private AssetManager assetManager;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String fileName,dirPath;
    private Context context;
    private File file,newFile;
    private int read;
    public DownloadService(String fName,String dPath,Context mContext) {
        fileName = fName;
        dirPath = dPath;
        context = mContext;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Copying the tiles.zip from asset to internal storage of device
        Toast.makeText(context, "Service has been started!", Toast.LENGTH_SHORT).show();
        if (intent.getAction() =="com.example.bahirbarakzai.osmdroidapptest") {
            file = new File(dirPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            //Initializing Asset Manager
            assetManager = context.getAssets();

            try {
                inputStream = assetManager.open(fileName);
                newFile = new File(dirPath, fileName);
                outputStream = new FileOutputStream(newFile);

                if (inputStream != null) {
                    copyFile(inputStream, outputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.d("ServiceLog","No intent found");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.


        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void copyFile(InputStream in,OutputStream out){
        try {
            byte[] buffer = new byte[in.available()];

            while ((read =in.read()) != -1){
                out.write(buffer,0,read);
            }
            out.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
