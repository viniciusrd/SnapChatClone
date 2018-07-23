package br.com.vinicius.android.snapchatclone.view.acitivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.com.vinicius.android.snapchatclone.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    EditText mFieldEmail;
    EditText mFieldPassword;
    Button   mButtonLogin;

    //Firebase
    private FirebaseAuth mAuth;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFindViewByIds();

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }
    private void initFindViewByIds() {
        mFieldEmail = findViewById(R.id.textFieldEmailLogin);
        mFieldPassword = findViewById(R.id.textFieldPassword);
        mButtonLogin = findViewById(R.id.buttonLogin);
    }

    private boolean validateFields(){

        this.email = mFieldEmail.getText().toString();
        this.password = mFieldPassword.getText().toString();

        if(this.email.isEmpty()){
            Toast.makeText(this,"Informe o email para login",Toast.LENGTH_LONG).show();
            return false;
        }

        if(password.isEmpty()){
            Toast.makeText(this,"Informe a senha para login",Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    private void login() {

        if (validateFields()){


            mAuth =  FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(LoginActivity.this, "Login efetuado com sucesso !",Toast.LENGTH_SHORT).show();
                        showSnapActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Falha ao tentar logar com essa conta, tente novamente!", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    }

    private void showSnapActivity() {
        Intent intent = new Intent(this,SnapsActivity.class);
        startActivity(intent);
        this.finish();
    }

}

