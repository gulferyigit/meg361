package gulfer.emine.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewJournalFragment extends Fragment {

    private TextView tvDate, mood1, mood2, mood3, mood4, mood5, btnBack;
    private EditText etEntry, etTags;
    private Button btnSave;
    private TextView selectedMood = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_journal, container, false);

        // hide shared bottom navigation when this fragment is visible
        try {
            View bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        } catch (Exception ignored) {
        }

        tvDate = v.findViewById(R.id.tvDate);
        mood1 = v.findViewById(R.id.mood1);
        mood2 = v.findViewById(R.id.mood2);
        mood3 = v.findViewById(R.id.mood3);
        mood4 = v.findViewById(R.id.mood4);
        mood5 = v.findViewById(R.id.mood5);
    etEntry = v.findViewById(R.id.etEntry);
    etTags = v.findViewById(R.id.etTags);
        btnSave = v.findViewById(R.id.btnSave);
        btnBack = v.findViewById(R.id.btnBack);

        // set today's date in Turkish locale
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", new Locale("tr"));
        tvDate.setText(sdf.format(new Date()));

        View.OnClickListener moodClick = view -> {
            if (selectedMood != null) selectedMood.setBackground(null);
            selectedMood = (TextView) view;
            // highlight selected
            selectedMood.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        };

        mood1.setOnClickListener(moodClick);
        mood2.setOnClickListener(moodClick);
        mood3.setOnClickListener(moodClick);
        mood4.setOnClickListener(moodClick);
        mood5.setOnClickListener(moodClick);

        JournalViewModel vm = new androidx.lifecycle.ViewModelProvider(requireActivity()).get(JournalViewModel.class);

        btnSave.setOnClickListener(b -> {
            try {
                String mood = selectedMood != null ? selectedMood.getText().toString() : "";
                String content = etEntry.getText() != null ? etEntry.getText().toString().trim() : "";
                String tags = etTags.getText() != null ? etTags.getText().toString().trim() : "";
                String date = tvDate.getText() != null ? tvDate.getText().toString() : "";

                android.content.SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
                String owner = prefs.getString("pref_email", "");
                JournalEntity e = new JournalEntity(mood, content, tags, date, null, owner);
                vm.insert(e, id -> {
                    // after insert, pop back on UI thread
                    requireActivity().runOnUiThread(() -> requireActivity().getSupportFragmentManager().popBackStack());
                });
            } catch (Exception ex) {
                com.google.android.material.snackbar.Snackbar.make(v, "Kaydetme hatası: " + ex.getMessage(), com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show();
            }
        });

        btnBack.setOnClickListener(bb -> requireActivity().getSupportFragmentManager().popBackStack());

        // no photo feature

        // footer buttons (local) wiring
    View fHome = v.findViewById(R.id.btnHome);
    View fNew = v.findViewById(R.id.btnNew);
    View fProfile = v.findViewById(R.id.btnProfile);

        if (fHome != null) fHome.setOnClickListener(f -> {
            try {
                androidx.navigation.Navigation.findNavController(requireView()).navigate(R.id.diaryListFragment);
            } catch (Exception ex) {
                com.google.android.material.snackbar.Snackbar.make(v, "Geçiş yapılamadı: " + ex.getMessage(), com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show();
            }
        });

        if (fNew != null) fNew.setOnClickListener(f -> {
            // already on NewJournal - maybe scroll to top
            etEntry.scrollTo(0, 0);
        });

        if (fProfile != null) fProfile.setOnClickListener(f -> {
            try {
                androidx.navigation.Navigation.findNavController(requireView()).navigate(R.id.profileFragment);
            } catch (Exception ex) {
                com.google.android.material.snackbar.Snackbar.make(v, "Geçiş yapılamadı: " + ex.getMessage(), com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    // no onActivityResult — photo feature removed

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            View bottomNav = requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        } catch (Exception ignored) {
        }
    }
}

