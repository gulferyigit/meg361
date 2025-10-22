package gulfer.emine.diary;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import gulfer.emine.diary.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    // toolbar removed; AppBarConfiguration not needed
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final String TAG = "MainActivityDebug";
        Log.d(TAG, "onCreate - inflate complete, setting up NavController");
        NavController navControllerLocal = null;
        try {
            navControllerLocal = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        } catch (Exception ex) {
            Log.e(TAG, "NavController alınamadı: " + ex.getMessage(), ex);
        }

        if (navControllerLocal != null) {
            final NavController navControllerFinal = navControllerLocal; // effectively final for listeners
            Log.d(TAG, "NavController currentDest (initial): " + String.valueOf(navControllerFinal.getCurrentDestination()));
            navControllerFinal.addOnDestinationChangedListener((controller, destination, arguments) -> {
                Log.d(TAG, "Destination changed -> id:" + destination.getId() + " label:" + destination.getLabel());
                try {
                    View bottom = findViewById(R.id.bottomNav);
                    if (bottom != null) {
                        int id = destination.getId();
                        // Hide bottom nav for auth/new screens, show otherwise
                        if (id == R.id.welcomeFragment || id == R.id.loginFragment || id == R.id.registerFragment || id == R.id.newJournalFragment) {
                            bottom.setVisibility(View.GONE);
                        } else {
                            bottom.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception ex) {
                    Log.w(TAG, "Failed to update bottom nav visibility: " + ex.getMessage());
                }
            });
        } else {
            android.widget.Toast.makeText(this, "NavController bulunamadı - Logcat'e bakın", android.widget.Toast.LENGTH_LONG).show();
        }
        // Hook up bottom navigation buttons (found in included layout)
        View btnHome = findViewById(R.id.btnHome);
        View btnCalendar = findViewById(R.id.btnCalendar);
        View btnNew = findViewById(R.id.btnNew);
        View btnProfile = findViewById(R.id.btnProfile);

        if (btnHome != null) btnHome.setOnClickListener(v -> {
            try {
                NavController nc = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                if (nc.getCurrentDestination() == null || nc.getCurrentDestination().getId() != R.id.diaryListFragment) {
                    nc.navigate(R.id.diaryListFragment);
                }
            } catch (Exception ex) {
                android.widget.Toast.makeText(this, "Geçiş yapılamadı: " + ex.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        if (btnCalendar != null) btnCalendar.setOnClickListener(v -> {
            try {
                NavController nc = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                if (nc.getCurrentDestination() == null || nc.getCurrentDestination().getId() != R.id.diaryListFragment) {
                    nc.navigate(R.id.diaryListFragment);
                }
            } catch (Exception ex) {
                android.widget.Toast.makeText(this, "Geçiş yapılamadı: " + ex.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        if (btnNew != null) btnNew.setOnClickListener(v -> {
            try {
                NavController nc = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                if (nc.getCurrentDestination() == null || nc.getCurrentDestination().getId() != R.id.newJournalFragment) {
                    nc.navigate(R.id.newJournalFragment);
                }
            } catch (Exception ex) {
                android.widget.Toast.makeText(this, "Geçiş yapılamadı: " + ex.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        if (btnProfile != null) btnProfile.setOnClickListener(v -> {
            try {
                NavController nc = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                if (nc.getCurrentDestination() == null || nc.getCurrentDestination().getId() != R.id.profileFragment) {
                    nc.navigate(R.id.profileFragment);
                }
            } catch (Exception ex) {
                android.widget.Toast.makeText(this, "Geçiş yapılamadı: " + ex.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
        // FloatingActionButton removed from layout; no FAB click handler needed.
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}