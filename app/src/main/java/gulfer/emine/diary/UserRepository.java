package gulfer.emine.diary;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao dao;
    private final ExecutorService io;

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        dao = db.userDao();
        io = Executors.newSingleThreadExecutor();
    }

    public void insert(UserEntity u, InsertCallback cb) {
        io.execute(() -> {
            long id = dao.insert(u);
            if (cb != null) cb.onInserted(id);
        });
    }

    public UserEntity findByEmailSync(String email) {
        return dao.findByEmail(email);
    }

    public void findByEmail(String email, FindCallback cb) {
        io.execute(() -> {
            UserEntity u = dao.findByEmail(email);
            if (cb != null) cb.onResult(u);
        });
    }

    public interface FindCallback { void onResult(UserEntity u); }

    public interface InsertCallback { void onInserted(long id); }
}
