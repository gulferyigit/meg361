package gulfer.emine.diary;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JournalRepository {
    private final JournalDao dao;
    private final ExecutorService io;

    public JournalRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        dao = db.journalDao();
        io = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<JournalEntity>> getAll() {
        return dao.getAll();
    }

    public void insert(JournalEntity e, InsertCallback cb) {
        io.execute(() -> {
            long id = dao.insert(e);
            if (cb != null) cb.onInserted(id);
        });
    }

    public void deleteById(long id) {
        io.execute(() -> dao.deleteById(id));
    }

    public void update(JournalEntity e, UpdateCallback cb) {
        io.execute(() -> {
            dao.update(e);
            if (cb != null) cb.onUpdated();
        });
    }

    public interface InsertCallback { void onInserted(long id); }
    public interface UpdateCallback { void onUpdated(); }
}
