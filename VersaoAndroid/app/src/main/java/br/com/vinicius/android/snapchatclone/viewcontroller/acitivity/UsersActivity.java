package br.com.vinicius.android.snapchatclone.viewcontroller.acitivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vinicius.android.snapchatclone.R;
import br.com.vinicius.android.snapchatclone.model.Image;
import br.com.vinicius.android.snapchatclone.model.User;
import br.com.vinicius.android.snapchatclone.viewcontroller.adapter.UserAdapter;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private Image image;
    private List<User> users;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth      mAuth;
    private DatabaseReference mNodeUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        users = new ArrayList<>();

        image = ( Image ) getIntent().getSerializableExtra("Snap");


        initFindViewsById();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mNodeUsers = mDatabase.child("usuarios");

        mNodeUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                mAuth = FirebaseAuth.getInstance();
                String idUserLogged = mAuth.getCurrentUser().getUid();

                User user =  dataSnapshot.getValue(User.class);
                user.setUid(dataSnapshot.getKey());

                if (user.getUid().equals(idUserLogged)){
                    users.add(user);
                    defineLayoutRecyclerView();
                    defineAdapter();
                }



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initFindViewsById() {

        mRecyclerView = findViewById(R.id.recyclerUsers);
    }


    private void defineAdapter() {
        try
        {
            Log.d("NauticaBr", "definirAdapter ListaVeleiro");
            UserAdapter comentarioAdapter = new UserAdapter(this,users);
            mRecyclerView.setAdapter(comentarioAdapter);

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }

    private void defineLayoutRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
