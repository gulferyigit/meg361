package gulfer.emine.diary.androidtest;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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
@LargeTest
public class IntegrationTest {
    
    private JournalDatabase db;
    private JournalDao journalDao;
    
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, JournalDatabase.class).build();
        journalDao = db.journalDao();
    }
    
    @After
    public void tearDown() {
        db.close();
    }
    
    @Test
    public void testCreateReadUpdateDeleteJournal() throws Exception {
        // Create
        JournalEntity entity = new JournalEntity("🎉", "Integration Test", "#test #integration", "2025-12-09");
        long id = journalDao.insert(entity);
        assertTrue(id > 0);
        
        // Read
        List<JournalEntity> entries = journalDao.getAll().get();
        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertEquals("Integration Test", entries.get(0).content);
        
        // Update
        JournalEntity toUpdate = entries.get(0);
        JournalEntity updated = new JournalEntity("😊", "Updated Integration", "#updated", "2025-12-09");
        updated.id = toUpdate.id;
        journalDao.update(updated);
        
        List<JournalEntity> updatedEntries = journalDao.getAll().get();
        assertEquals("Updated Integration", updatedEntries.get(0).content);
        
        // Delete
        journalDao.deleteById(updated.id);
        List<JournalEntity> deletedEntries = journalDao.getAll().get();
        assertEquals(0, deletedEntries.size());
    }
    
    @Test
    public void testMultipleJournalsInsertAndSearch() throws Exception {
        // Insert multiple
        for (int i = 1; i <= 5; i++) {
            JournalEntity entity = new JournalEntity("😊", "Günlük " + i, "#günlük" + i, "2025-12-0" + i);
            journalDao.insert(entity);
        }
        
        // Verify all inserted
        List<JournalEntity> entries = journalDao.getAll().get();
        assertEquals(5, entries.size());
        
        // Check first and last
        assertEquals("Günlük 1", entries.get(4).content);
        assertEquals("Günlük 5", entries.get(0).content);
    }
    
    @Test
    public void testJournalWithSpecialCharacters() throws Exception {
        JournalEntity entity = new JournalEntity("🎈", "Türkçe: ğ, ı, ş, ç, ö, ü!", "#türkçe #özel", "2025-12-09");
        journalDao.insert(entity);
        
        List<JournalEntity> entries = journalDao.getAll().get();
        assertEquals(1, entries.size());
        assertTrue(entries.get(0).content.contains("Türkçe"));
        assertTrue(entries.get(0).tags.contains("#türkçe"));
    }
}
