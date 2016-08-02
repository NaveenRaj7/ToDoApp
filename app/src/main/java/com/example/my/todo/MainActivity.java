package com.example.my.todo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "MainActivity";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private String mPhotoUrl;
    private DatabaseReference mDatabase;
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseRecyclerAdapter<CommonUtilities, MessageViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    private Button mSendButton;
    public static final String MESSAGES_CHILD = "messages";
    public static final String NEW_USER_UID = "users";


    public CircleImageView messengerImageView2;



    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;
        public TextView mName;
        public TextView mTime;
        public LinearLayout lt;

        //public CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            mTime = (TextView) itemView.findViewById(R.id.timeGiven);
            mName = (TextView) itemView.findViewById(R.id.assignedTo);
            lt = (LinearLayout) itemView.findViewById(R.id.bcolor);
           // messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set default username is anonymous.
        mUsername = ANONYMOUS;


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // [START initialize_database_ref]
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]





        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            /*if(mFirebaseDatabaseReference.orderByChild(NEW_USER_UID).equalTo(mFirebaseUser.getUid()).equals(mFirebaseUser.getUid())){
                Log.d(TAG, "user exists");
            }
            else*/
                //Log.d(TAG, "user does not exists");
                mFirebaseDatabaseReference.child(NEW_USER_UID).child(mUsername).setValue(mFirebaseUser.getUid());
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
                mFirebaseDatabaseReference.orderByChild(NEW_USER_UID).equalTo(mFirebaseUser.getUid());
            }
        }

        Log.d(TAG, "url"+ mFirebaseUser.getPhotoUrl());



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();



        //mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);



        mFirebaseAdapter = new FirebaseRecyclerAdapter<CommonUtilities, MessageViewHolder>(
                CommonUtilities.class,
                R.layout.item_message,
                MessageViewHolder.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, CommonUtilities friendlyMessage, int position) {
                //mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                if(friendlyMessage.getName().equals(mUsername)) {
                    //viewHolder.lt.setBackgroundColor(Color.rgb(143, 188, 143));
                    viewHolder.lt.setBackgroundColor(Color.rgb(128,128,128));

                    viewHolder.messageTextView.setText(friendlyMessage.getTask());
                   //viewHolder.messageTextView.setTextColor(Color.rgb(52,152,219));
                    viewHolder.messageTextView.setTextColor(Color.rgb(255,255,255));

                    viewHolder.messengerTextView.setText(friendlyMessage.getAdate());
                    //viewHolder.messengerTextView.setTextColor(Color.rgb(52,152,219));
                    viewHolder.messengerTextView.setTextColor(Color.rgb(255,255,255));

                    viewHolder.mTime.setText(friendlyMessage.getAtime());
                    //viewHolder.mTime.setTextColor(Color.rgb(52,152,219));
                    viewHolder.mTime.setTextColor(Color.rgb(255,255,255));

                    viewHolder.mName.setText(friendlyMessage.getName());
                    //viewHolder.mName.setTextColor(Color.rgb(52,152,219));
                    viewHolder.mName.setTextColor(Color.rgb(255,255,255));
                }
                else {
                    //viewHolder.lt.setBackgroundColor(Color.rgb(250,128,114));
                    viewHolder.lt.setBackgroundColor(Color.rgb(255,255,255));

                    viewHolder.messageTextView.setText(friendlyMessage.getTask());
                    //viewHolder.messageTextView.setTextColor(Color.rgb(178,0,253));
                    viewHolder.messageTextView.setTextColor(Color.rgb(0,0,0));

                    viewHolder.messengerTextView.setText(friendlyMessage.getAdate());
                    //viewHolder.messengerTextView.setTextColor(Color.rgb(178,0,253));
                    viewHolder.messengerTextView.setTextColor(Color.rgb(0,0,0));

                    viewHolder.mTime.setText(friendlyMessage.getAtime());
                    //viewHolder.mTime.setTextColor(Color.rgb(178,0,253));
                    viewHolder.mTime.setTextColor(Color.rgb(0,0,0));

                    viewHolder.mName.setText(friendlyMessage.getName());
                    //viewHolder.mName.setTextColor(Color.rgb(178,0,253));
                    viewHolder.mName.setTextColor(Color.rgb(0,0,0));



                }
                /*if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(MainActivity.this)
                            .load(friendlyMessage.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }*/
            }
        };



        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);






        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, AddTask.class));




                /*CommonUtilities friendlyMessage = new CommonUtilities(mMessageEditText.getText().toString(), mUsername);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(friendlyMessage);
                mMessageEditText.setText("");*/
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView textView1 = (TextView)header.findViewById(R.id.textView1);
        textView1.setText(mUsername);

        messengerImageView2 = (CircleImageView) header.findViewById(R.id.messengerImageView);
        if (mFirebaseUser.getPhotoUrl() == null) {
            messengerImageView2.setImageResource(R.drawable.ic_account_circle_black_36dp);
        } else {
            Glide.with(this)
                    .load(mFirebaseUser.getPhotoUrl())
                    .into(messengerImageView2);
        }

        /*email = (TextView)header.findViewById(R.id.email);
        email.setText(personEmail);*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                mUsername = ANONYMOUS;
                mPhotoUrl = null;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_person) {
            // Handle the camera action
        } else if (id == R.id.create_group) {

        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }















    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}
