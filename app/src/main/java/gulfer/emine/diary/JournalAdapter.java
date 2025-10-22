package gulfer.emine.diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.VH> {

    private final List<JournalEntity> items = new ArrayList<>();
    private OnItemClickListener listener;

    public JournalAdapter() { }

    public void setItems(List<JournalEntity> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
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
    holder.ivMood.setText(it.mood != null ? it.mood : "");
    holder.tvTitle.setText(it.content != null ? (it.content.length() > 50 ? it.content.substring(0, 50) + "..." : it.content) : "");
    holder.tvDate.setText(it.date != null ? it.date : "");
        // item click -> forward id to listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(it.id);
        });
        // menu click can be wired later
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView ivMood, tvTitle, tvDate;
        View ivMenu;

        VH(@NonNull View itemView) {
            super(itemView);
            ivMood = itemView.findViewById(R.id.ivMood);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivMenu = itemView.findViewById(R.id.ivMenu);
        }
    }
}
