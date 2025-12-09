package gulfer.emine.diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.VH> {

    private final List<JournalEntity> items = new ArrayList<>();
    private OnItemClickListener listener;

    public JournalAdapter() { }

    public void setItems(List<JournalEntity> list) {
        int oldSize = items.size();
        if (list == null || list.isEmpty()) {
            if (oldSize > 0) {
                items.clear();
                notifyItemRangeRemoved(0, oldSize);
            }
            return;
        }
        items.clear();
        items.addAll(list);
        int newSize = items.size();
        if (oldSize == 0) {
            notifyItemRangeInserted(0, newSize);
        } else {
            int max = Math.max(oldSize, newSize);
            notifyItemRangeChanged(0, max);
            if (newSize > oldSize) notifyItemRangeInserted(oldSize, newSize - oldSize);
            else if (newSize < oldSize) notifyItemRangeRemoved(newSize, oldSize - newSize);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        JournalEntity it = items.get(position);
        holder.ivMood.setText(it.mood != null ? it.mood : "😌");
        
        // Title from content (first line or truncated)
        String title = it.content != null ? it.content : "";
        if (title.contains("\n")) {
            title = title.substring(0, title.indexOf("\n"));
        }
        if (title.length() > 30) {
            title = title.substring(0, 30);
        }
        holder.tvTitle.setText(title.isEmpty() ? "Başlıksız" : title);
        
        // Date formatting
        holder.tvDate.setText(it.date != null ? formatDate(it.date) : "");
        
        // Preview text (rest of content)
        String preview = it.content != null ? it.content : "";
        if (preview.length() > 100) {
            preview = preview.substring(0, 100) + "...";
        }
        holder.tvPreview.setText(preview);

        // Tags rendering
        if (holder.chipTags != null) {
            holder.chipTags.removeAllViews();
            if (it.tags != null && !it.tags.trim().isEmpty()) {
                String[] parts = it.tags.split(",");
                for (String p : parts) {
                    String tag = p.trim();
                    if (tag.isEmpty()) continue;
                    Chip chip = new Chip(holder.itemView.getContext(), null, com.google.android.material.R.style.Widget_Material3_Chip_Assist);
                    chip.setText(tag);
                    chip.setCheckable(false);
                    chip.setClickable(false);
                    chip.setEnsureMinTouchTargetSize(false);
                    holder.chipTags.addView(chip);
                }
            }
        }
        
        // item click -> forward id to listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(it.id);
        });
        // menu click can be wired later
    }

    private String formatDate(String date) {
        // Simple format: convert "2024-05-15" to "15 MAY"
        if (date != null && date.length() >= 10) {
            String[] parts = date.split("-");
            if (parts.length == 3) {
                String[] months = {"OCA", "ŞUB", "MAR", "NİS", "MAY", "HAZ", "TEM", "AĞU", "EYL", "EKİ", "KAS", "ARA"};
                int monthIdx = Integer.parseInt(parts[1]) - 1;
                if (monthIdx >= 0 && monthIdx < 12) {
                    return parts[2] + " " + months[monthIdx];
                }
            }
        }
        return date;
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView ivMood, tvTitle, tvDate, tvPreview;
        View ivMenu;
        ChipGroup chipTags;

        VH(@NonNull View itemView) {
            super(itemView);
            ivMood = itemView.findViewById(R.id.ivMood);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPreview = itemView.findViewById(R.id.tvPreview);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            chipTags = itemView.findViewById(R.id.chipTags);
        }
    }
}
