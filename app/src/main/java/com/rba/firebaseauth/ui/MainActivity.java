package com.rba.firebaseauth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rba.firebaseauth.R;
import com.rba.firebaseauth.ui.base.BaseActivity;
import com.rba.firebaseauth.view.fragment.ChangePasswordDialogFragment;
import com.rba.firebaseauth.view.fragment.ProductFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView lblUserName, lblUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }else{

                    Log.i("x- user", firebaseUser.getEmail()+" - "+firebaseUser.getDisplayName());
                    String name = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();


                    View headerView =  navigationView.getHeaderView(0);
                    lblUserName = (TextView) headerView.findViewById(R.id.lblUserName);
                    lblUserEmail = (TextView) headerView.findViewById(R.id.lblUserEmail);

                    if(name!=null){
                        lblUserName.setText(name);
                    }

                    if(email!=null){
                        lblUserEmail.setText(email);
                    }
                }
            }
        };

        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (item.getItemId()){
            case R.id.opHome:
                fragment = new ProductFragment();
                title = getString(R.string.app_name);
                break;
            case R.id.opLogout:
                logout();
                break;
            case R.id.opChangePassword:
                FragmentManager fm = this.getSupportFragmentManager();
                ChangePasswordDialogFragment changePasswordDialogFragment = new ChangePasswordDialogFragment();
                changePasswordDialogFragment.show(fm, "layout_filter_checkbox_dialog");
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frmContainer, fragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(title);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        firebaseAuth.signOut();
        //finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
        }
    }
}
