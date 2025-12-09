package gulfer.emine.diary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import gulfer.emine.diary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // toolbar removed; AppBarConfiguration not needed
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Force window background to white to avoid black surfaces in some emulators
        try {
            getWindow().getDecorView().setBackgroundColor(android.graphics.Color.WHITE);
            getWindow().setStatusBarColor(android.graphics.Color.WHITE);
            getWindow().setNavigationBarColor(android.graphics.Color.WHITE);
        } catch (Exception ignored) {
        }

        final String TAG = "MainActivityDebug";
        Log.d(TAG, "onCreate - inflate complete, setting up NavController");
        
        // FragmentContainerView kullanıldığında NavHostFragment'i bu şekilde almalıyız
        FragmentContainerView navHostFragmentView = findViewById(R.id.nav_host_fragment_content_main);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            Log.d(TAG, "NavController currentDest (initial): " + navController.getCurrentDestination());
            
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                Log.d(TAG, "Destination changed -> id:" + destination.getId() + " label:" + destination.getLabel());
                try {
                    View bottom = findViewById(R.id.bottomNav);
                    if (bottom != null) {
                        int id = destination.getId();
                        // Hide bottom nav for new entry (or other screens that should be full-screen)
                        if (id == R.id.newJournalFragment) {
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
        View btnNew = findViewById(R.id.btnNew);

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