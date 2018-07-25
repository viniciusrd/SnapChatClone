package br.com.vinicius.android.snapchatclone.ViewController.acitivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vinicius.android.snapchatclone.Model.Snap;
import br.com.vinicius.android.snapchatclone.R;
import br.com.vinicius.android.snapchatclone.Model.Image;
import br.com.vinicius.android.snapchatclone.Model.User;
import br.com.vinicius.android.snapchatclone.Util.RecyclerItemClickListener;
import br.com.vinicius.android.snapchatclone.ViewController.adapter.UserAdapter;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private Image image;
    private List<User> users;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth      mAuth;
    private DatabaseReference mNodeUsers;
    private DatabaseReference mSnaps;

    private UUID idSnap = UUID.randomUUID();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        users = new ArrayList<>();

        image = ( Image ) getIntent().getSerializableExtra("Snap");


        initFindViewsById();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mNodeUsers = mDatabase.child("usuarios");

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener()
                {

                    @Override
                    public void onItemClick(View view, int position) {

                        User userSelect = users.get(position);
                        String  idUserSelect = userSelect.getUid();

                        mAuth = FirebaseAuth.getInstance();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mNodeUsers = mDatabase.child("usuarios");
                        mSnaps = mNodeUsers.child(idUserSelect).child("snaps");

                        String idUserLogged = mAuth.getCurrentUser().getUid();

                        if (!idUserLogged.isEmpty()){

                            DatabaseReference userLogged = mNodeUsers.child(idUserLogged);
                            userLogged.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    User user =  dataSnapshot.getValue(User.class);
                                    user.setUid(dataSnapshot.getKey());
                                    Snap snapConfig = new Snap();
                                    snapConfig.setFrom(user.getEmail());
                                    snapConfig.setName(user.getName());
                                    snapConfig.setDesc(image.getDesc());
                                    snapConfig.setUrlImage(image.getUrl());
                                    snapConfig.setIdImage(image.getUid());

                                    mSnaps.child(idSnap.toString()).setValue(snapConfig);

                                    finish();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {


                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    }

                }));

        mNodeUsers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                mAuth = FirebaseAuth.getInstance();
                String idUserLogged = mAuth.getCurrentUser().getUid();

                User user =  dataSnapshot.getValue(User.class);
                user.setUid(dataSnapshot.getKey());

                if (!user.getUid().equals(idUserLogged)){
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
