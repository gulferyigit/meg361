package gulfer.emine.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiaryListFragment extends Fragment {

    private RecyclerView rv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary_list, container, false);
        rv = v.findViewById(R.id.rvJournalList);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        JournalAdapter adapter = new JournalAdapter();
        rv.setAdapter(adapter);

        // item click -> navigate to detail with entryId
        adapter.setOnItemClickListener(id -> {
            try {
                Bundle b = new Bundle();
                b.putLong("entryId", id);
                androidx.navigation.Navigation.findNavController(requireView()).navigate(R.id.entryDetailFragment, b);
            } catch (Exception ex) {
                com.google.android.material.snackbar.Snackbar.make(rv, "Detay açılamadı: " + ex.getMessage(), com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show();
            }
        });

        // observe ViewModel
        JournalViewModel vm = new androidx.lifecycle.ViewModelProvider(requireActivity()).get(JournalViewModel.class);
        // show only current user's journals
        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
        String owner = prefs.getString("pref_email", "");
        vm.getAllByOwner(owner).observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
        });

        // bottom nav clicks: use the shared bottom_nav included in the activity (ids: btnNew, btnProfile)
        View navAdd = null;
        View navProfile = null;
        try {
            navAdd = requireActivity().findViewById(R.id.btnNew);
            navProfile = requireActivity().findViewById(R.id.btnProfile);
        } catch (Exception ignored) {
        }

        if (navAdd != null) {
            navAdd.setOnClickListener(view -> {
                try {
                    androidx.navigation.Navigation.findNavController(v).navigate(R.id.newJournalFragment);
                } catch (Exception ex) {
                    com.google.android.material.snackbar.Snackbar.make(v, "Navigasyon hatası: " + ex.getMessage(), com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show();
                }
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(view -> {
                try {
                    androidx.navigation.Navigation.findNavController(v).navigate(R.id.profileFragment);
                } catch (Exception ex) {
                    com.google.android.material.snackbar.Snackbar.make(v, "Navigasyon hatası: " + ex.getMessage(), com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show();
                }
            });
        }

        return v;
    }
}
