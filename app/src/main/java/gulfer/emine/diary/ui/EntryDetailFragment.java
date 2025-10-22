package gulfer.emine.diary.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import gulfer.emine.diary.R;
import gulfer.emine.diary.JournalEntity;
import gulfer.emine.diary.JournalViewModel;

import java.util.List;

public class EntryDetailFragment extends Fragment {
    private JournalViewModel vm;
    private long entryId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    vm = new ViewModelProvider(requireActivity()).get(JournalViewModel.class);
        if (getArguments() != null) entryId = getArguments().getLong("entryId", -1);

    TextView tEmoji = view.findViewById(R.id.textEmoji);
    TextView tDate = view.findViewById(R.id.textDate);
    TextView tFull = view.findViewById(R.id.textFull);
    // We'll lazily create EditText counterparts if edit mode is entered
    android.widget.EditText editFull = view.findViewById(R.id.editFull);
        ImageView photo = view.findViewById(R.id.photo);

        vm.getAll().observe(getViewLifecycleOwner(), list -> {
            JournalEntity e = findById(list, entryId);
                if (e != null) {
                tEmoji.setText(e.mood == null ? "🙂" : e.mood);
                tFull.setText(e.content == null ? "" : e.content);
                // date: JournalEntity stores a date string
                tDate.setText(e.date == null ? "" : e.date);
                // set dynamic title based on mood/emoji
                TextView tTitle = view.findViewById(R.id.textTitle);
                if (tTitle != null) {
                    String mood = e.mood == null ? "" : e.mood.toLowerCase();
                    String title = "Güzel bir gün"; // default
                    if (mood.contains("😄") || mood.contains("😊") || mood.contains("🙂") || mood.contains(":)") || mood.contains("mutlu") ) {
                        title = "Harika bir gün";
                    } else if (mood.contains("😢") || mood.contains("☹") || mood.contains(":(") || mood.contains("üzgün") ) {
                        title = "Kötü geçen bir gün";
                    } else if (mood.contains("😐") || mood.contains("meh") || mood.contains("orta") ) {
                        title = "Orta halli bir gün";
                    } else if (mood.contains("😴") || mood.contains("yorgun") ) {
                        title = "Sakin bir gün";
                    } else if (mood.contains("😃") || mood.contains("😁") ) {
                        title = "Neşeli bir gün";
                    }
                    tTitle.setText(title);
                }
                if (editFull != null) editFull.setText(e.content == null ? "" : e.content);
                // photo: JournalEntity doesn't include photoPath in current schema
                photo.setVisibility(View.GONE);

                // delete with confirmation
                View btnDelete = view.findViewById(R.id.btnDelete);
                if (btnDelete != null) {
                    btnDelete.setOnClickListener(v -> {
                        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setTitle("Sil")
                                .setMessage("Bu girdi silinecek. Emin misin?")
                                .setPositiveButton("Sil", (dialog, which) -> {
                                    vm.deleteById(e.id);
                                    requireActivity().onBackPressed();
                                })
                                .setNegativeButton("Vazgeç", null)
                                .show();
                    });
                }

                // wire bottom delete action (if present in new layout)
                View btnDeleteAction = view.findViewById(R.id.btnDeleteAction);
                if (btnDeleteAction != null) {
                    btnDeleteAction.setOnClickListener(v -> {
                        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                .setTitle("Sil")
                                .setMessage("Bu girdi silinecek. Emin misin?")
                                .setPositiveButton("Sil", (dialog, which) -> {
                                    vm.deleteById(e.id);
                                    requireActivity().onBackPressed();
                                })
                                .setNegativeButton("Vazgeç", null)
                                .show();
                    });
                }

                // wire bottom edit action (if present) - enter in-place edit mode
                View btnEditAction = view.findViewById(R.id.btnEditAction);
                View btnSaveAction = view.findViewById(R.id.btnSaveAction);
                View btnCancelAction = view.findViewById(R.id.btnCancelAction);
                if (btnEditAction != null) {
                    btnEditAction.setOnClickListener(v -> {
                        // show edit field, hide text view, show save/cancel
                        if (editFull != null) {
                            tFull.setVisibility(View.GONE);
                            editFull.setVisibility(View.VISIBLE);
                            editFull.requestFocus();
                            // show save/cancel if available
                            if (btnSaveAction != null) btnSaveAction.setVisibility(View.VISIBLE);
                            if (btnCancelAction != null) btnCancelAction.setVisibility(View.VISIBLE);
                            btnEditAction.setVisibility(View.GONE);
                        } else {
                            // fallback: navigate to newJournalFragment for editing
                            Bundle b = new Bundle();
                            b.putLong("entryId", e.id);
                            Navigation.findNavController(v).navigate(R.id.newJournalFragment, b);
                        }
                    });
                }

                if (btnSaveAction != null) {
                    btnSaveAction.setOnClickListener(v -> {
                        if (editFull != null) {
                            String newContent = editFull.getText().toString();
                            e.content = newContent;
                            vm.update(e, () -> requireActivity().runOnUiThread(() -> {
                                // exit edit mode
                                if (editFull != null) editFull.setVisibility(View.GONE);
                                tFull.setVisibility(View.VISIBLE);
                                tFull.setText(newContent);
                                if (btnSaveAction != null) btnSaveAction.setVisibility(View.GONE);
                                if (btnCancelAction != null) btnCancelAction.setVisibility(View.GONE);
                                if (btnEditAction != null) btnEditAction.setVisibility(View.VISIBLE);
                                // show confirmation snackbar
                                View root = requireActivity().findViewById(android.R.id.content);
                                if (root != null) {
                                    Snackbar.make(root, "Güncelleme kaydedildi", Snackbar.LENGTH_SHORT).show();
                                }
                            }));
                        }
                    });
                }

                if (btnCancelAction != null) {
                    btnCancelAction.setOnClickListener(v -> {
                        // discard changes, exit edit mode
                        if (editFull != null) {
                            editFull.setText(tFull.getText());
                            editFull.setVisibility(View.GONE);
                            tFull.setVisibility(View.VISIBLE);
                        }
                        if (btnSaveAction != null) btnSaveAction.setVisibility(View.GONE);
                        if (btnCancelAction != null) btnCancelAction.setVisibility(View.GONE);
                        if (btnEditAction != null) btnEditAction.setVisibility(View.VISIBLE);
                    });
                }

                View btnShare = view.findViewById(R.id.btnShare);
                if (btnShare != null) {
                    boolean hasContent = (e.content != null && !e.content.isEmpty()) || (e.mood != null && !e.mood.isEmpty());
                    btnShare.setVisibility(hasContent ? View.VISIBLE : View.GONE);
                    btnShare.setOnClickListener(v -> {
                        // share text (mood + content)
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        String body = (e.mood == null ? "" : e.mood + " ") + (e.content == null ? "" : e.content);
                        share.putExtra(Intent.EXTRA_TEXT, body);
                        startActivity(Intent.createChooser(share, "Paylaş"));
                    });
                }

                // header back (top-left) - navigate up
                View btnBack = view.findViewById(R.id.btnBack);
                if (btnBack != null) {
                    btnBack.setOnClickListener(v -> {
                        try {
                            Navigation.findNavController(v).navigateUp();
                        } catch (Exception ex) {
                            requireActivity().onBackPressed();
                        }
                    });
                }
            }
        });
    }

    private JournalEntity findById(List<JournalEntity> list, long id) {
        if (list == null) return null;
        for (JournalEntity e : list) if (e.id == id) return e;
        return null;
    }
}
