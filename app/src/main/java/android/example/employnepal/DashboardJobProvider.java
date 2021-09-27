package android.example.employnepal;

import android.content.Intent;
import android.example.employnepal.databinding.ActivityDashboardJobProviderBinding;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;


public class DashboardJobProvider extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDashboardJobProviderBinding binding;


    //
    private BottomNavigationView bottomNavigationView;
    private NavController bottomnavController;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;



    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityDashboardJobProviderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarDashboardJobProvider.toolbar);
        binding.appBarDashboardJobProvider.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardJobProvider.this, PostActivity.class);
                startActivity(intent);

            }

        });
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomnavController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_nav);

        mDrawer = findViewById(R.id.drawer_layout);
        nvDrawer = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.start, R.string.close);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        NavigationUI.setupWithNavController(bottomNavigationView, bottomnavController);

        nvDrawer.setNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navigation_job:
                Toast.makeText(this, "Job Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_notifications:
                Toast.makeText(this, "Notifications Clicked", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }


}