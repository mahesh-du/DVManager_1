package com.example.dvmanager_1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.dvmanager_1.Fragments.About_Fragment;
import com.example.dvmanager_1.Fragments.Alert_Fragment;
import com.example.dvmanager_1.Fragments.Analysis_Fragment;
import com.example.dvmanager_1.Fragments.Home_Fragment;
import com.example.dvmanager_1.Fragments.Manage_devices_Fragment;
import com.example.dvmanager_1.Fragments.My_Ids_Fragment;
import com.example.dvmanager_1.Fragments.My_Profile_Fragment;
import com.example.dvmanager_1.Fragments.Scan_Fragment;
import com.example.dvmanager_1.Fragments.Search_Fragment;
import com.example.dvmanager_1.Fragments.Search_Result_Fragment;
import com.example.dvmanager_1.Tasks.LogOut_background_Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CAMERA;
import static com.example.dvmanager_1.Helper.loadFragment_withBackstackEntry;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, Constants{

    private static final int PERMISSION_REQUEST_CODE = 200;
    BottomNavigationView navigation;
    NavigationView navigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth;
    Fragment alert, analysis, home, manage_devices, my_ids, my_profile, scan, search, search_result,about;
    TextView nav_header_main_txt_Email;
    static LinearLayout popUpLinearLayout;
    static Button btn_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Permissions( new String[]{ACCESS_FINE_LOCATION,ACCESS_NETWORK_STATE,CAMERA});

        alert= new Alert_Fragment();
        analysis = new Analysis_Fragment();
        home = new Home_Fragment();
        manage_devices = new Manage_devices_Fragment();
        my_ids = new My_Ids_Fragment();
        my_profile = new My_Profile_Fragment();
        scan = new Scan_Fragment();
        search = new Search_Fragment();
        search_result = new Search_Result_Fragment();
        about = new About_Fragment();

        popUpLinearLayout = findViewById(R.id.content_main_connect_linearLayout);
        btn_connect = findViewById(R.id.content_main_btn_connect);
        // first fragment not added to backstack so onbackpressed it doesnt pops out of the backstack.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, home,backstack_TAG_Home_FRAGMENT)
                .commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        nav_header_main_txt_Email = headerView.findViewById(R.id.nav_header_main_txt_Email);
        nav_header_main_txt_Email.setText(Objects.requireNonNull(user).getEmail());

        navigation = findViewById(R.id.bn_navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().getItem(2).setChecked(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(2).setChecked(true);

        //start service to listen for network changes.
        Intent backgroundService = new Intent(getApplicationContext(), NetworkService.class);
        startService(backgroundService);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MainActivity.this, NetworkService.class));
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         if(menu.findItem(R.id.action_logout)== null)
           getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_filter).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                logOut();
                return true;
            }
            case R.id.action_filter:
            case R.id.action_search: {
                return false;
            }

        }
        return true;
    }

    public void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.message_Logout);
        builder.setTitle(R.string.title_confirm);
        builder.setCancelable(false);
        builder .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("firestoreINSTANCE", db);
                        data.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        LogOut_background_Task logOut_background_task = new LogOut_background_Task(MainActivity.this, data);
                        logOut_background_task.execute();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }

    public void Permissions(String[] permissions)
    {
        List<String> permissions_disabled = new ArrayList<>();

        for(String permission : permissions)
        {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED)
            {
                permissions_disabled.add(permission);
            }
        }
        if(permissions_disabled.size() > 0) {
            String[] temp_permissions_disabled = permissions_disabled.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, temp_permissions_disabled, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    List<String> permission_still_not_enabled = new ArrayList<>();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                    {
                        for(int i= 0; i< grantResults.length;i++)
                        {
                            if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            {
                                permission_still_not_enabled.add(permissions[i]);
                            }
                        }

                        if(permission_still_not_enabled.size() > 0) {
                            String[] temp_permission_still_not_enabled = permission_still_not_enabled.toArray(new String[0]);
                            ActivityCompat.requestPermissions(this, temp_permission_still_not_enabled, PERMISSION_REQUEST_CODE);
                        }else
                            break;
                    }
                    else
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        for(int i= 0; i< grantResults.length;i++)
                        {
                            if(grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            {
                                permission_still_not_enabled.add(permissions[i]);
                            }
                        }

                        if(permission_still_not_enabled.size() > 0) {
                            String[] temp_permission_still_not_enabled = permission_still_not_enabled.toArray(new String[0]);
                            ActivityCompat.requestPermissions(this, temp_permission_still_not_enabled, PERMISSION_REQUEST_CODE);
                        }else
                            break;
                    }

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment frag : fragments) {
                if (frag != null && frag.isVisible() && frag.getTag() != backstack_TAG_Search_Result_FRAGMENT){
                    if(getNavIndexFromFragment(frag)!= -1)
                        navigationView.getMenu().getItem(getNavIndexFromFragment(frag)).setChecked(false);
                    if(getBNBIndexFromFragment(frag)!= -1)
                        navigation.getMenu().getItem(getBNBIndexFromFragment(frag)).setChecked(false);
                break;}
            }
            super.onBackPressed();
            fragments.clear();
            fragments = getSupportFragmentManager().getFragments();
            for (Fragment frag : fragments) {
                if (frag != null && frag.isVisible() && frag.getTag() != backstack_TAG_Search_Result_FRAGMENT){
                    if(getNavIndexFromFragment(frag)!= -1)
                        navigationView.getMenu().getItem(getNavIndexFromFragment(frag)).setChecked(true);
                    if(getBNBIndexFromFragment(frag)!= -1)
                        navigation.getMenu().getItem(getBNBIndexFromFragment(frag)).setChecked(true);
                break;}
            }
        }
/*

        int current_fragment_id = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getId();
        if(current_fragment_id == R.id.bn_add)
            navigation.getMenu().getItem(0).setChecked(true);
        else if(current_fragment_id == R.id.bn_analysis)
            navigation.getMenu().getItem(1).setChecked(true);
        else if(current_fragment_id == R.id.bn_home)
            navigation.getMenu().getItem(2).setChecked(true);
        else if(current_fragment_id == R.id.bn_alert)
            navigation.getMenu().getItem(3).setChecked(true);
        else if(current_fragment_id == R.id.bn_Search)
            navigation.getMenu().getItem(4).setChecked(true);
*/
        }


    public int getNavIndexFromFragment(Fragment fragment){
        String tag = fragment.getTag();
        switch(tag){
            case backstack_TAG_Scan_FRAGMENT:           {return 0;}
            case backstack_TAG_Analysis_FRAGMENT:       {return 1;}
            case backstack_TAG_Home_FRAGMENT:           {return 2;}
            case backstack_TAG_Alert_FRAGMENT:          {return 3;}
            case backstack_TAG_Search_FRAGMENT:         {return 4;}
            case backstack_TAG_Manage_Devices_FRAGMENT: {return 5;}
            case backstack_TAG_My_Ids_FRAGMENT:         {return 6;}
            case backstack_TAG_My_Profile_FRAGMENT:     {return 7;}
            case backstack_TAG_About_FRAGMENT:          {return 8;}
        }
        return -1;
    }

    public int getBNBIndexFromFragment(Fragment fragment){
        String tag = fragment.getTag();
        switch(tag){
            case backstack_TAG_Scan_FRAGMENT:           {return 0;}
            case backstack_TAG_Analysis_FRAGMENT:       {return 1;}
            case backstack_TAG_Home_FRAGMENT:           {return 2;}
            case backstack_TAG_Alert_FRAGMENT:          {return 3;}
            case backstack_TAG_Search_FRAGMENT:         {return 4;}
        }
        return -1;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String TAG = null;
        Fragment fragment = null;

        //TODO: check fragment tags are properly applied.
        if (id == R.id.nav_add || id == R.id.bn_add ) {
            fragment = scan;
            TAG = backstack_TAG_Scan_FRAGMENT;
            navigation.getMenu().getItem(0).setChecked(true);
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(0).setChecked(true);
        } else if (id == R.id.nav_analysis || id == R.id.bn_analysis ) {
            fragment = analysis;
            TAG = backstack_TAG_Analysis_FRAGMENT;
            navigation.getMenu().getItem(1).setChecked(true);
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(1).setChecked(true);
        } else if (id == R.id.nav_home || id == R.id.bn_home ) {
            fragment = home;
            TAG = backstack_TAG_Home_FRAGMENT;
            navigation.getMenu().getItem(2).setChecked(true);
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(2).setChecked(true);
        } else if (id == R.id.nav_alert || id == R.id.bn_alert ) {
            fragment = alert;
            TAG = backstack_TAG_Alert_FRAGMENT;
            navigation.getMenu().getItem(3).setChecked(true);
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(3).setChecked(true);
        } else if (id == R.id.nav_Search || id == R.id.bn_Search ) {
            fragment = search;
            TAG = backstack_TAG_Search_FRAGMENT;
            navigation.getMenu().getItem(4).setChecked(true);
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(4).setChecked(true);
        }else if (id == R.id.nav_Manage_devices) {
            TAG = backstack_TAG_Manage_Devices_FRAGMENT;
            fragment = manage_devices;
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(5).setChecked(true);
        }else if (id == R.id.nav_My_Ids) {
            TAG = backstack_TAG_My_Ids_FRAGMENT;
            fragment = my_ids;
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(6).setChecked(true);
        }else if (id == R.id.nav_my_profile) {
            TAG = backstack_TAG_My_Profile_FRAGMENT;
            fragment = my_profile;
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(7).setChecked(true);
        }else if (id == R.id.nav_About) {
            TAG = backstack_TAG_About_FRAGMENT;
            fragment = about;
            clearNavigationDrawerSelection();
            navigationView.getMenu().getItem(8).setChecked(true);
        }else if (id == R.id.nav_Log_Out) {
            logOut();
        }
        loadFragment_withBackstackEntry(getSupportFragmentManager() ,fragment, TAG);
//        showFragment(getSupportFragmentManager() ,fragment, TAG);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void clearNavigationDrawerSelection(){
        for (int i = 0; i <10 ; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    public static void showOfflinePOPup(final Context context, boolean isOffline){
        //TODO: make popup below appbar visible.

        if(isOffline) {
            popUpLinearLayout.setVisibility(View.VISIBLE);
            btn_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else
            popUpLinearLayout.setVisibility(View.GONE);

    }

    public static void showOfflineDialog(){
        //TODO: create and show custom layout.
    }

}
