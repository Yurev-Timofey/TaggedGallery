package com.neuron.taggedgallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;  //А зачем это? Хер знает..

    DBHelper dbHelper;
    SQLiteDatabase database;

    DataAdapter adapter;

    Button btnCreate;
    Button btnClear;
    SearchView searchView;

    List<Image> images = new ArrayList<>();

    //Загрузка всех изображений из бд
    public void loadAllPictures(){
        images.clear();
        Cursor cursor = database.query(DBHelper.TABLE_IMAGES, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            images.add(new Image(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.KEY_IMAGE_ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PATH))));
        }
        cursor.close();
        adapter.setImages(images);
    }

    //Не мой код
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
            Log.e("getPath", "getRealPathFromURI Exception : " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> {
            //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            //Тип получаемых объектов - image:
            photoPickerIntent.setType("image/*");
            //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
            startActivityForResult(photoPickerIntent, PICK_IMAGE);
        });

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            dbHelper.onUpgrade(database, DBHelper.DB_VERSION, ++DBHelper.DB_VERSION);
            images.clear();
            adapter.setImages(images);
            Toast.makeText(MainActivity.super.getBaseContext(), "Удалено", Toast.LENGTH_SHORT).show(); //Не уверен насчет контекста
        });

        searchView = findViewById(R.id.searchView1);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    loadAllPictures();
                    return false;
                }

                images.clear();
                Cursor cursor = database.query(DBHelper.TABLE_IMAGES, null, DBHelper.KEY_IMAGE_ID + " = " + newText, null,
                        null, null, null);
                while (cursor.moveToNext()) {
                    images.add(new Image(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_IMAGE_ID)),
                            cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PATH))));
                }
                cursor.close();
                adapter.setImages(images);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("HELLO THERE2");
                return false;
            }
        });

        dbHelper = new DBHelper(this, "db");
        database = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.imagegallery);
        adapter = new DataAdapter(this, images);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);

        loadAllPictures();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {

                    String path = getRealPathFromURI(this, data.getData());
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_NAME, "");
                    contentValues.put(DBHelper.KEY_PATH, path);

                    database.insert(DBHelper.TABLE_IMAGES, null, contentValues);

                    Cursor cursor = database.query(DBHelper.TABLE_IMAGES, null, null, null,
                            null, null, null);

                    if (cursor.moveToLast())
                        adapter.addImage(new Image("", "", path));

                    cursor.close();
                }
        }
    }
}
