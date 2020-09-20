package com.neuron.taggedgallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_IMAGE = 1;

    DBHelper dbHelper;
    SQLiteDatabase database;

    LinearLayout llMain;
    Button btnCreate;
    Button btnClear;
    ImageView lastImg;

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llMain = findViewById(R.id.llPictures);

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);

        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        dbHelper = new DBHelper(this, "db");
        database = dbHelper.getWritableDatabase();

//        Button PickImage = (Button) findViewById(R.id.button);
//        PickImage.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                //Тип получаемых объектов - image:
//                photoPickerIntent.setType("image/*");
//                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
//                startActivityForResult(photoPickerIntent, PICK_IMAGE);
//            }
//        });
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
                llMain.addView(lastImg, lParams);

                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
                break;

            case R.id.btnClear:
                llMain.removeAllViews();
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
                    contentValues.put(DBHelper.KEY_PATH, getPath(data.getData()));
                    contentValues.put(DBHelper.KEY_NAME, data.getDataString());

                    database.insert(DBHelper.TABLE_IMAGES, null, contentValues);

                    Cursor cursor = database.query(DBHelper.TABLE_IMAGES, null, null, null,
                            null, null, null);

                    if (cursor.moveToLast()) {
                        try {
                            final Uri imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)));
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            lastImg.setImageBitmap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    cursor.close();
                }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


}
