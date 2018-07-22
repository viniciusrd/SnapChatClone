package br.com.vinicius.android.snapchatclone.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.vinicius.android.snapchatclone.R;

public class MainActivity extends AppCompatActivity{

    Button mButtonLogin;
    Button mButtonCreateAccount;

    //Firebase
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonLogin =  findViewById(R.id.buttonLogin);
        mButtonCreateAccount = findViewById(R.id.buttonCreate);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
            }
        });

        mButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateAccount();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        this.mUser = this.mAuth.getCurrentUser();

        if(mUser!=null){
            showSnapsActivity();
        }


    }

    private void showSnapsActivity() {

        Log.d("showSnapsActivity","onclick");
        Intent intent = new Intent(this,SnapsActivity.class);
        startActivity(intent);
    }

    public void showLogin()
    {
        Log.d("buttonlogin","onclick");
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


    public void showCreateAccount()
    {
        Log.d("buttonCreate","onclick");
        Intent intent = new Intent(this,CreateAccountActivity.class);
        startActivity(intent);
    }



}
