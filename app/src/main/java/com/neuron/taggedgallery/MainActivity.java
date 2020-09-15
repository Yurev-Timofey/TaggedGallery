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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

//INSERT INTO images (file_path, name) VALUES ("your file path", "name of image");

public class MainActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    DBHelper dbHelper;
    ImageView imageView;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();

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
                            imageView.setImageBitmap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

//                    try {
//                        final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageView.setImageBitmap(selectedImage);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

        dbHelper = new DBHelper(this, "db", 1);


        Button PickImage = (Button) findViewById(R.id.button);
        PickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });
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
