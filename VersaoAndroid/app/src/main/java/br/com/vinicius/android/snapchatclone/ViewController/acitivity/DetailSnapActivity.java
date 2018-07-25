package br.com.vinicius.android.snapchatclone.ViewController.acitivity;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import br.com.vinicius.android.snapchatclone.Model.Snap;
import br.com.vinicius.android.snapchatclone.R;
import br.com.vinicius.android.snapchatclone.Util.BuildUrl;

public class DetailSnapActivity extends AppCompatActivity {

    private ImageView mImageSnap;
    private TextView  mTextDesc;
    private Chronometer mChronometer;


    private Snap snapReceived;
    private AsyncTask getImageFromUrlTask;


    //Firebase
    private StorageReference mStorage;
    private StorageReference mNodeImage;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mNodeUsers;
    private DatabaseReference mSnaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_snap);

        initFindViewById();
        hiddenField();
        snapReceived = (Snap) getIntent().getSerializableExtra("SnapSelected");
        donwloadImageFromUrl();


    }

    private void hiddenField() {

        mImageSnap.setVisibility(View.GONE);
        mChronometer.setVisibility(View.GONE);

    }

    private void showField(){
        mImageSnap.setVisibility(View.VISIBLE);
        mChronometer.setVisibility(View.VISIBLE);
    }

    private void donwloadImageFromUrl() {

        getImageFromUrlTask = new DownloadTask().execute(BuildUrl.stringToURL(snapReceived.getUrlImage()));

    }

    private void initFindViewById() {

        mImageSnap = findViewById(R.id.imageSnap);
        mTextDesc = findViewById(R.id.textDesc);
        mChronometer = findViewById(R.id.chronometer);

    }

    private void timerSnap() {

       final Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!

                                mChronometer.setBase(SystemClock.elapsedRealtime());
                                mChronometer.start();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

        removeSnap();
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        removeSnap();
    }

    private void removeSnap() {
        mAuth = FirebaseAuth.getInstance();
        String idUserLogged = mAuth.getCurrentUser().getUid();
        if(!idUserLogged.isEmpty()) {
            mDatabase = FirebaseDatabase.getInstance();
          mNodeUsers =   mDatabase.getReference().child("usuarios");
          mSnaps = mNodeUsers.child(idUserLogged).child("snaps");
          mSnaps.child(snapReceived.getId()).removeValue();

          mStorage = FirebaseStorage.getInstance().getReference();
          mNodeImage = mStorage.child("imagens");

          mNodeImage.child(snapReceived.getIdImage()+".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                  Toast.makeText(DetailSnapActivity.this,"Sucesso",Toast.LENGTH_LONG).show();
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(DetailSnapActivity.this,"Erro",Toast.LENGTH_LONG).show();
              }
          });
        }
    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap> {
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start

        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog


            if(result!=null){
                // Display the downloaded image into ImageView
                mImageSnap.setImageBitmap(result);
                showField();
                timerSnap();


            }else {
                Toast.makeText(DetailSnapActivity.this,"Erro",Toast.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images",MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, "UniqueFileName"+".jpg");

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }
}

