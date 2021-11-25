package cat.copernic.jcadafalch.penjatfirebase.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import cat.copernic.jcadafalch.penjatfirebase.R;

public class java extends Activity {
    private Button b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = findViewById(R.id.logOutButton);
        b.setOnClickListener(view -> {
                    File file = saveImage();
                    if (file != null)
                        share(file);
                }

        );

    }

    private void share(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(this, getPackageName()+".provider", file);

        }else{
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Screenshot");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello Word");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try{
            startActivity(Intent.createChooser(intent, "Share using"));
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            b.performClick();
        } else
            Toast.makeText(this, "Permis denegat", Toast.LENGTH_SHORT).show();


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private File saveImage() {
        if (CheckPermission())
            return null;

        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/AppName";
            File fileDir = new File(path);
            if (!fileDir.exists())
                fileDir.mkdir();

            String mPath = path + "/ScreenShot " + new Date().getTime() + ".png";
            Bitmap bitmap = screenshot();
            File file = new File(mPath);
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();


            Toast.makeText(this, "Imatge guardada correctament", Toast.LENGTH_SHORT).show();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Bitmap screenshot() {
        View v = findViewById(R.id.rootView);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private boolean CheckPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            return false;
        }

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
