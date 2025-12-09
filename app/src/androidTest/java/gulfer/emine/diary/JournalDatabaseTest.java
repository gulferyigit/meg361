package gulfer.emine.diary.androidtest;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import gulfer.emine.diary.JournalDatabase;
import gulfer.emine.diary.JournalDao;
import gulfer.emine.diary.JournalEntity;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class JournalDatabaseTest {
    
    private JournalDatabase db;
    private JournalDao journalDao;
    
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, JournalDatabase.class).build();
        journalDao = db.journalDao();
    }
    
    @After
    public void closeDb() {
        db.close();
    }
    
    @Test
    public void testInsertAndRetrieveJournal() throws Exception {
        JournalEntity entity = new JournalEntity("😊", "Test günlük", "#test", "2025-12-09");
        
        journalDao.insert(entity);
        
        List<JournalEntity> entries = journalDao.getAll().get();
        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertEquals("Test günlük", entries.get(0).content);
    }
    
    @Test
    public void testUpdateJournal() throws Exception {
        JournalEntity entity = new JournalEntity("😢", "Original", "#test", "2025-12-09");
        long id = journalDao.insert(entity);
        
        JournalEntity updated = new JournalEntity("😊", "Updated", "#updated", "2025-12-09");
        updated.id = id;
        journalDao.update(updated);
        
        List<JournalEntity> entries = journalDao.getAll().get();
        assertEquals(1, entries.size());
        assertEquals("Updated", entries.get(0).content);
    }
    
    @Test
    public void testDeleteJournal() throws Exception {
        JournalEntity entity = new JournalEntity("😐", "Silinecek", "#delete", "2025-12-09");
        long id = journalDao.insert(entity);
        
        journalDao.deleteById(id);
        
        List<JournalEntity> entries = journalDao.getAll().get();
        assertEquals(0, entries.size());
    }
}

