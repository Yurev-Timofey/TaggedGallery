package com.neuron.taggedgallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_IMAGE = 1;

    DBHelper dbHelper;
    SQLiteDatabase database;

    DataAdapter adapter;

    //    LinearLayout llMain;
    Button btnCreate;
    Button btnClear;
    ImageView lastImg;

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
    List<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 123);

//        llMain = findViewById(R.id.llPictures);
//
        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        dbHelper = new DBHelper(this, "db");
        database = dbHelper.getWritableDatabase();

//        images.add(new Image("1", "1", Uri.fromFile(Res));
        Cursor cursor = database.query(DBHelper.TABLE_IMAGES, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
//            final Uri imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URI)));
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            images.add(new Image("1", "1", cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))));
        }
        cursor.close();

        RecyclerView recyclerView = findViewById(R.id.imagegallery);
        // создаем адаптер
        adapter = new DataAdapter(this, images);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                // Создание LayoutParams c шириной и высотой по содержимому
                LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        300, 300);
                lParams.gravity = Gravity.START;
//                lParams.width = 150;
//                lParams.height = 150;

                lastImg = new ImageView(this);
//                llMain.addView(lastImg, lParams);

                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
                break;

            case R.id.btnClear:
//                llMain.removeAllViews();
                dbHelper.onUpgrade(database, 1, 2);
                Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {

                    ContentValues contentValues = new ContentValues();
//                    System.out.println(data.getDataString());
                    contentValues.put(DBHelper.KEY_NAME,getRealPathFromURI(this, data.getData()));
                    contentValues.put(DBHelper.KEY_URI, data.getDataString());

                    database.insert(DBHelper.TABLE_IMAGES, null, contentValues);

                    Cursor cursor = database.query(DBHelper.TABLE_IMAGES, null, null, null,
                            null, null, null);

                    if (cursor.moveToLast()) {
                        final Uri imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URI))); //Убрат загрузку из бд
                        adapter.addImage(new Image("1", "1", getRealPathFromURI(this, imageUri)));
                    }
                    cursor.close();
                }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        String path = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("gaga", "getRealPathFromURI Exception : " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        System.out.println(path);
        return path;
    }

    private void requestPermission(String permission, int requestCode) {
        // запрашиваем разрешение
        ActivityCompat.requestPermissions(this,
                new String[]{permission}, requestCode);
    }


}
