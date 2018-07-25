package br.com.vinicius.android.snapchatclone.ViewController.acitivity;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.vinicius.android.snapchatclone.R;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";

    EditText mFieldEmailLogin;
    EditText mFieldName;
    EditText mFieldPassword;
    EditText mFieldConfirmPassword;
    Button   mButtonCreateAccount;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;


    private String email;
    private String name;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initFindViewByIds();

        mAuth = FirebaseAuth.getInstance();

        mButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void initFindViewByIds() {
        mFieldEmailLogin = findViewById(R.id.textFieldEmailLogin);
        mFieldName = findViewById(R.id.textFieldPassword);
        mFieldPassword = findViewById(R.id.textFieldPassword);
        mFieldConfirmPassword = findViewById(R.id.textFieldConfirmPassword);
        mButtonCreateAccount = findViewById(R.id.buttonCreate);
    }

    private boolean validateFields() {

        this.email = mFieldEmailLogin.getText().toString();
        this.name = mFieldName.getText().toString();
        this.password = mFieldPassword.getText().toString();
        this.confirmPassword = mFieldConfirmPassword.getText().toString();

        if(email.isEmpty()) {

            Toast.makeText(this,"Informe seu email",Toast.LENGTH_LONG).show();
            return  false;
        }

        if(name.isEmpty()) {

            Toast.makeText(this,"Informe seu nome",Toast.LENGTH_LONG).show();
            return  false;
        }

        if(password.isEmpty()) {

            Toast.makeText(this,"Informe sua senha",Toast.LENGTH_LONG).show();
            return  false;
        }

        if(confirmPassword.isEmpty()) {

            Toast.makeText(this,"Informe sua confirmação de senha",Toast.LENGTH_LONG).show();
            return  false;
        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(this,"Confirme sua senha corretamente",Toast.LENGTH_LONG).show();
            return  false;
        }
        return true;
    }

    private void createAccount(){

        if(validateFields()){

            this.mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");

                        mUser = mAuth.getCurrentUser();
                        saveUserDatabase();
                        showSnapActivity();
                        Toast.makeText(CreateAccountActivity.this, "Conta criada com sucesso !",Toast.LENGTH_SHORT).show();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(CreateAccountActivity.this, "Falha ao criar conta, tente novamente!",Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }


    }

    private void saveUserDatabase() {

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("usuarios").child(mUser.getUid()).child("name").setValue(name);
        mDatabase.child("usuarios").child(mUser.getUid()).child("email").setValue(email);

    }

    private void showSnapActivity() {
        Intent intent = new Intent(this,SnapsActivity.class);
        startActivity(intent);
        this.finish();
    }


}
