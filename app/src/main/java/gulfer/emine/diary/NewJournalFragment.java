package gulfer.emine.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewJournalFragment extends Fragment {

    private TextView tvDate, mood1, mood2, mood3, mood4, mood5, btnSaveTop;
    private ImageView btnClose;
    private EditText etEntry;
    private TextView selectedMood = null;
    private ChipGroup chipGroupTags;
    private View btnAddTag;

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
        btnSaveTop = v.findViewById(R.id.btnSaveTop);
        btnClose = v.findViewById(R.id.btnClose);
        chipGroupTags = v.findViewById(R.id.chipGroupTags);
        btnAddTag = v.findViewById(R.id.tvAddTag);

        // set today's date in Turkish locale
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", java.util.Locale.forLanguageTag("tr"));
        tvDate.setText(sdf.format(new Date()));

        View.OnClickListener moodClick = view -> {
            if (selectedMood != null) {
                selectedMood.setBackgroundResource(R.drawable.profile_avatar_bg);
            }
            selectedMood = (TextView) view;
            // highlight selected with a border or different background
            selectedMood.setBackgroundResource(R.drawable.profile_avatar_choice_bg);
        };

        mood1.setOnClickListener(moodClick);
        mood2.setOnClickListener(moodClick);
        mood3.setOnClickListener(moodClick);
        mood4.setOnClickListener(moodClick);
        mood5.setOnClickListener(moodClick);

        JournalViewModel vm = new androidx.lifecycle.ViewModelProvider(requireActivity()).get(JournalViewModel.class);

        View.OnClickListener saveClick = b -> {
            try {
                String mood = selectedMood != null ? selectedMood.getText().toString() : "";
                String content = etEntry.getText() != null ? etEntry.getText().toString().trim() : "";
                String date = tvDate.getText() != null ? tvDate.getText().toString() : "";
                String tags = collectTags();

                if (content.isEmpty()) {
                    com.google.android.material.snackbar.Snackbar.make(v, "Lütfen günlük içeriği girin", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show();
                    return;
                }

                JournalEntity e = new JournalEntity(mood, content, tags, date, null);
                vm.insert(e, id -> {
                    // after insert, navigate back on UI thread
                    requireActivity().runOnUiThread(() -> {
                        try {
                            androidx.navigation.Navigation.findNavController(requireView()).navigate(R.id.diaryListFragment);
                        } catch (Exception ex) {
                            requireActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
                });
            } catch (Exception ex) {
                com.google.android.material.snackbar.Snackbar.make(v, "Kaydetme hatası: " + ex.getMessage(), com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show();
            }
        };

        btnSaveTop.setOnClickListener(saveClick);

        btnClose.setOnClickListener(bb -> {
            try {
                androidx.navigation.Navigation.findNavController(requireView()).navigate(R.id.diaryListFragment);
            } catch (Exception ex) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        if (btnAddTag != null) {
            btnAddTag.setOnClickListener(vBtn -> showAddTagDialog());
        }

        return v;
    }

    private void addTagChip(String text) {
        if (chipGroupTags == null || text == null || text.trim().isEmpty()) return;
        String clean = text.trim();
        Chip chip = new Chip(requireContext(), null, com.google.android.material.R.style.Widget_Material3_Chip_Assist_Elevated);
        chip.setText(clean);
        chip.setCheckable(false);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> chipGroupTags.removeView(chip));
        chipGroupTags.addView(chip);
    }

    private void showAddTagDialog() {
        final EditText input = new EditText(requireContext());
        input.setHint("Örn: gezi, iş, duygu");
        input.setSingleLine(true);
        int pad = (int) (12 * getResources().getDisplayMetrics().density);
        input.setPadding(pad, pad, pad, pad);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Etiket ekle")
                .setView(input)
                .setPositiveButton("Ekle", (d, which) -> addTagChip(String.valueOf(input.getText())))
                .setNegativeButton("İptal", null)
                .show();
    }

    private String collectTags() {
        if (chipGroupTags == null || chipGroupTags.getChildCount() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chipGroupTags.getChildCount(); i++) {
            View child = chipGroupTags.getChildAt(i);
            if (child instanceof Chip) {
                String t = ((Chip) child).getText().toString().trim();
                if (!t.isEmpty()) {
                    if (sb.length() > 0) sb.append(",");
                    sb.append(t);
                }
            }
        }
        return sb.toString();
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

