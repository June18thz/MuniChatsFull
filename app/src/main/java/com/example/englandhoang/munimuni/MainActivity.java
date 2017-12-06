package com.example.englandhoang.munimuni;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.joaquimley.faboptions.FabOptions;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private DatabaseReference mUserRef;

    private TabLayout mTabLayout;

    private FabOptions fabOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Muni Chats");

        if (mAuth.getCurrentUser() != null) {

            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }

        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        //Fab
        fabOptions = (FabOptions) findViewById(R.id.faboptions);
        fabOptions.setButtonsMenu(R.menu.fab_menu);
        fabOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                switch (view.getId()) {
                    case R.id.faboptions_settings:
                        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);
                        break;

                    case R.id.faboptions_users:
                        Intent usersIntent = new Intent(MainActivity.this, UsersActivity.class);
                        startActivity(usersIntent);
                        break;

                    case R.id.faboptions_maps:
                        CharSequence options[] = new CharSequence[]{"Chức năng đang được thử nghiệm"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                        builder.show();
                        break;

                    case R.id.faboptions_logout:

                        CharSequence options2[] = new CharSequence[]{"Có", "Không"};
                        builder.setTitle("Bạn muốn đăng xuất?");
                        builder.setItems(options2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    LoginManager.getInstance().logOut();
                                    FirebaseAuth.getInstance().signOut();
                                    mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
                                    sendToStart();
                                }
                                if (i == 1) {
                                    return;
                                }
                            }
                        });
                        builder.show();
                        break;

                    default:

                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            sendToStart();

        } else {

            mUserRef.child("online").setValue("true");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }
    }


    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

}
