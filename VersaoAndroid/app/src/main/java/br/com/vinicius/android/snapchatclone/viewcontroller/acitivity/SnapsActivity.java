package br.com.vinicius.android.snapchatclone.ViewController.acitivity;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.TextView;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.ArrayList;
        import java.util.List;

        import br.com.vinicius.android.snapchatclone.Model.Snap;
        import br.com.vinicius.android.snapchatclone.Model.User;
        import br.com.vinicius.android.snapchatclone.R;
        import br.com.vinicius.android.snapchatclone.Util.RecyclerItemClickListener;
        import br.com.vinicius.android.snapchatclone.ViewController.adapter.SnapAdapter;
        import br.com.vinicius.android.snapchatclone.ViewController.adapter.UserAdapter;

public class SnapsActivity extends AppCompatActivity {

    private TextView mText;
    private RecyclerView mRecyclerView;

    private Toolbar toolbar;

    private List<Snap> snaps;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mNodeUsers;
    private DatabaseReference mSnaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaps);

        initFindViewsByIds();

        setSupportActionBar(toolbar);
//        getActionBar().setTitle(getResources().getString(R.string.title_snaps));

        snaps = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mNodeUsers = mDatabase.child("usuarios");
        String idUserLogged = mAuth.getCurrentUser().getUid();
        mSnaps = mNodeUsers.child(idUserLogged).child("snaps");

        mSnaps.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Snap snapForUserLogged = dataSnapshot.getValue(Snap.class);
                snapForUserLogged.setId(dataSnapshot.getKey());
                snaps.add(snapForUserLogged);
                defineLayoutRecyclerView();
                defineAdapter();

                if (snaps.size()==0) {
                    mText.setVisibility(View.GONE);
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

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Snap snapSelected = snaps.get(position);
                showSnapReceivedActivity(snapSelected);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    private void showSnapReceivedActivity(Snap snapSelected) {

        Intent intent = new Intent(this,DetailSnapActivity.class);
        intent.putExtra("SnapSelected",snapSelected);
        startActivity(intent);
    }


    private void initFindViewsByIds(){
        toolbar = findViewById(R.id.toolbar);
        mText = findViewById(R.id.textSnapEmpty);
        mRecyclerView = findViewById(R.id.recyclerSnap);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_add:
                showPhotoActivity();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    private void showPhotoActivity(){
        Intent intent = new Intent(this,PhotoActivity.class);
        startActivity(intent);
    }

    private void defineAdapter() {
        try
        {
            Log.d("NauticaBr", "definirAdapter ListaVeleiro");
            SnapAdapter comentarioAdapter = new SnapAdapter(this,snaps);
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
