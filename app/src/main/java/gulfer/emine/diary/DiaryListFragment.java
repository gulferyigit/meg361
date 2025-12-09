package gulfer.emine.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class DiaryListFragment extends Fragment {

    private RecyclerView rv;
    private TextView emptyView;
    private JournalAdapter adapter;
    private List<JournalEntity> allItems = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary_list, container, false);
        rv = v.findViewById(R.id.rvJournalList);
        emptyView = v.findViewById(R.id.tvEmpty);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new JournalAdapter();
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

        // observe ViewModel (all journals on device)
        JournalViewModel vm = new androidx.lifecycle.ViewModelProvider(requireActivity()).get(JournalViewModel.class);
        vm.getAll().observe(getViewLifecycleOwner(), list -> {
            allItems.clear();
            if (list != null) allItems.addAll(list);
            adapter.setItems(allItems);
            if (emptyView != null) {
                emptyView.setVisibility((allItems == null || allItems.isEmpty()) ? View.VISIBLE : View.GONE);
            }
        });

        // FAB button click
        View fabAdd = v.findViewById(R.id.fabAdd);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(view -> {
                try {
                    androidx.navigation.Navigation.findNavController(v).navigate(R.id.newJournalFragment);
                } catch (Exception ex) {
                    com.google.android.material.snackbar.Snackbar.make(v, "Navigasyon hatası: " + ex.getMessage(), com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show();
                }
            });
        }

        // bottom nav clicks: use the shared bottom_nav included in the activity (ids: btnNew)
        View navAdd = null;
        try {
            navAdd = requireActivity().findViewById(R.id.btnNew);
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

        // Search button
        View searchBtn = v.findViewById(R.id.ivSearch);
        if (searchBtn != null) {
            searchBtn.setOnClickListener(vBtn -> showSearchDialog());
        }

        return v;
    }

    private void showSearchDialog() {
        final EditText input = new EditText(requireContext());
        input.setHint("Arama...");
        input.setSingleLine(true);
        int pad = (int) (12 * getResources().getDisplayMetrics().density);
        input.setPadding(pad, pad, pad, pad);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Günlük Ara")
                .setView(input)
                .setPositiveButton("Ara", (d, which) -> filterItems(String.valueOf(input.getText())))
                .setNegativeButton("İptal", null)
                .show();
    }

    private void filterItems(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setItems(allItems);
            return;
        }
        String q = query.trim().toLowerCase();
        List<JournalEntity> filtered = new ArrayList<>();
        for (JournalEntity e : allItems) {
            if ((e.content != null && e.content.toLowerCase().contains(q)) ||
                (e.tags != null && e.tags.toLowerCase().contains(q)) ||
                (e.date != null && e.date.toLowerCase().contains(q))) {
                filtered.add(e);
            }
        }
        adapter.setItems(filtered);
        if (emptyView != null) {
            emptyView.setText("Sonuç bulunamadı");
            emptyView.setVisibility((filtered == null || filtered.isEmpty()) ? View.VISIBLE : View.GONE);
        }
    }
}
