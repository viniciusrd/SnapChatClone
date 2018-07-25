package br.com.vinicius.android.snapchatclone.ViewController.acitivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.vinicius.android.snapchatclone.R;
import br.com.vinicius.android.snapchatclone.Model.Image;

public class PhotoActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_GALLERY = 1;
    private static final int PICK_IMAGE_CAMERA = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private Toolbar toolbar;
    private ImageView mPhotoSnap;
    private EditText mFieldDesc;
    private Button mButtonNext;

    private Uri filePath;
    private UUID idImage = UUID.randomUUID();
    private Bitmap photo;
    private boolean imageSelected = false;


    //Firebase
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        initFindViewsByIds();
        setSupportActionBar(toolbar);
        enableButton(false);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                next();
                Toast.makeText(PhotoActivity.this,"click",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void enableButton(boolean enable) {

        mButtonNext.setEnabled(enable);
        int color  = (enable ) ? getResources().getColor(R.color.buttonLogar) : Color.GRAY;
        mButtonNext.setBackgroundColor(color);

    }

    private void configButtonUpload(boolean enable){
        mButtonNext.setEnabled(enable);
        String text = (enable) ? getResources().getString(R.string.hint_next) : getResources().getString(R.string.hint_load);
        mButtonNext.setText(text);
    }

    private void initFindViewsByIds(){
        toolbar = findViewById(R.id.toolbar);
        mPhotoSnap = findViewById(R.id.imageSnap);
        mFieldDesc = findViewById(R.id.textFieldDesc);
        mButtonNext = findViewById(R.id.buttonNextSnap);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_take_photo:
                showOptionsForUser();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    private void showOptionsForUser() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhotoActivity.this);
        alertDialog.setTitle("Fotos para Snap");
        alertDialog.setMessage("Como deseja selecionar sua foto?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openCamera();
            }
        });
        alertDialog.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openGallery();
            }
        });
        alertDialog.show();

    }

    private void openCamera(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
            }
        }

    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE_GALLERY
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {
            //TODO: action
            this.filePath = data.getData();

            try {
                this.photo = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Log.d(TAG, String.valueOf(bitmap));
                mPhotoSnap.setImageBitmap(photo);
                imageSelected = true;
                enableButton(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {
            this.filePath = data.getData();
            try {
                this.photo = (Bitmap) data.getExtras().get("data");
                mPhotoSnap.setImageBitmap(photo);
                imageSelected = true;
                enableButton(true);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    private void next() {

        configButtonUpload(false);
//        this.mDatabase = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference nodeImage = this.mDatabase.child("imagens");
//        StorageReference ref = mDatabase.child("images/"+ UUID.randomUUID().toString());

        if(imageSelected){

            if(filePath != null){

                storage = FirebaseStorage.getInstance();
                StorageReference reference = storage.getReference().child("imagens");
                StorageReference nodeimage = reference.child(this.idImage.toString()+".jpg");

                UploadTask uploadTask = nodeimage.putFile(filePath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(PhotoActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String url = taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(PhotoActivity.this,url,Toast.LENGTH_LONG).show();
                        configButtonUpload(true);

                        Image image = new Image();
                        image.setUid(idImage.toString());
                        image.setDesc("");
                        image.setUrl(url);

                        showUsersAcitivy(image);

                    }
                });
            }


        }else {
            Toast.makeText(PhotoActivity.this,"False",Toast.LENGTH_LONG).show();
        }

    }

    private void showUsersAcitivy(Image image) {

        Intent intent = new Intent(this,UsersActivity.class);
        intent.putExtra("Snap", image);
        startActivity(intent);
        finish();

    }

    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
                                int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }
}
