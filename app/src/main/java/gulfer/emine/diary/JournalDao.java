package gulfer.emine.diary;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JournalDao {
    @Insert
    long insert(JournalEntity journal);

    @Update
    void update(JournalEntity journal);

    @Query("SELECT * FROM journals ORDER BY id DESC")
    LiveData<List<JournalEntity>> getAll();

    @Query("SELECT * FROM journals WHERE ownerEmail = :owner ORDER BY id DESC")
    LiveData<List<JournalEntity>> getAllByOwner(String owner);

    @Query("DELETE FROM journals WHERE id = :id")
    void deleteById(long id);
}
