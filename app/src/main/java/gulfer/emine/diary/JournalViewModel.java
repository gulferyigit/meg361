package gulfer.emine.diary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class JournalViewModel extends AndroidViewModel {
    private final JournalRepository repo;
    private final LiveData<List<JournalEntity>> all;

    public JournalViewModel(@NonNull Application application) {
        super(application);
        repo = new JournalRepository(application);
        all = repo.getAll();
    }

    public LiveData<List<JournalEntity>> getAll() { return all; }

    public LiveData<List<JournalEntity>> getAllByOwner(String owner) { return repo.getAllByOwner(owner); }

    public void insert(JournalEntity e, JournalRepository.InsertCallback cb) {
        repo.insert(e, cb);
    }

    public void deleteById(long id) { repo.deleteById(id); }

    public void update(JournalEntity e, JournalRepository.UpdateCallback cb) {
        repo.update(e, cb);
    }
}
