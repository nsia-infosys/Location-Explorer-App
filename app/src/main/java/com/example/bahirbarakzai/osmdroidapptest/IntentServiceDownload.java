package com.example.bahirbarakzai.osmdroidapptest;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IntentServiceDownload extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this

    private AssetManager assetManager;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String fileName,dirPath;
    private Context context;
    private File file,newFile;
    private int read;
    public IntentServiceDownload() {

        super("IntentServiceDownload");
    }


    @Override
    protected void onHandleIntent( Intent intent) {
        fileName = "tiles.zip";
        dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TileMaps";

        if (intent.getAction() =="com.example.bahirbarakzai.osmdroidapptest") {
            file = new File(dirPath);
            if (!file.exists()) {

                file.mkdirs();


                try {
                    File newFile = new File(dirPath,fileName);
                    newFile.createNewFile();
                    assetManager = getBaseContext().getAssets();
                    String [] files =assetManager.list("zip/tiles.zip");
                    for (String file : files) {
                        loadzip(dirPath, assetManager.open(file));

                    }
                } catch (IOException e) {
                    Log.d("LoadUnsuccessful","failed");
                    e.printStackTrace();
                }

//                try {
//                    String[] files = assetManager.list("images/tiles.zip");
//                    for(String file : files){
//                        inputStream = assetManager.open(file,AssetManager.ACCESS_STREAMING);
//                    }
//
//                    newFile = new File(dirPath, fileName);
//                    outputStream = new FileOutputStream(newFile);
//
//                    if (inputStream != null) {
//                        Log.d("InputStreamFull","Full");
//                        copyFile(inputStream, outputStream);
//                    }else {
//                        Log.d("InputStreamEmpty","Empty");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            //Initializing Asset Manager

        }else {
            Log.d("ServiceLog","No intent found");
        }

    }

    public void copyFile(InputStream in, OutputStream out){
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

    private void loadzip(String folder, InputStream inputStream) throws IOException
    {
        ZipInputStream zipIs = new ZipInputStream(inputStream);
        ZipEntry ze = null;

        while ((ze = zipIs.getNextEntry()) != null) {

            FileOutputStream fout = new FileOutputStream(folder +"/"+ze.getName());

            byte[] buffer = new byte[1024];
            int length = 0;

            while ((length = zipIs.read(buffer))>0) {
                fout.write(buffer, 0, length);
            }
            zipIs.closeEntry();
            fout.close();
        }
        zipIs.close();
    }
}
