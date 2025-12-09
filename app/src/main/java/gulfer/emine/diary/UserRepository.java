package gulfer.emine.diary;

import android.content.Context;

public class UserRepository {
    // App no longer uses user accounts. This lightweight stub keeps existing call-sites compilable.
    public UserRepository(Context context) { }

    public void findByEmail(String email, FindCallback cb) {
        if (cb != null) cb.onResult(null);
    }

    public void insert(Object u, InsertCallback cb) {
        if (cb != null) cb.onInserted(-1);
    }

    public interface FindCallback { void onResult(Object u); }
    public interface InsertCallback { void onInserted(long id); }
}
