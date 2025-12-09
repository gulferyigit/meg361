package gulfer.emine.diary.test;

import gulfer.emine.diary.JournalEntity;
import org.junit.Test;
import static org.junit.Assert.*;

public class JournalViewModelTest {
    
    @Test
    public void testJournalEntity_basicCreation() {
        JournalEntity entity = new JournalEntity("😊", "Test content", "#test", "2025-12-09");
        
        assertEquals("😊", entity.mood);
        assertEquals("Test content", entity.content);
        assertEquals("#test", entity.tags);
        assertEquals("2025-12-09", entity.date);
    }
    
    @Test
    public void testJournalEntity_multipleCreations() {
        JournalEntity e1 = new JournalEntity("🎉", "Great!", "#happy", "2025-12-09");
        JournalEntity e2 = new JournalEntity("😐", "Neutral", "#okay", "2025-12-08");
        
        assertEquals("🎉", e1.mood);
        assertEquals("😐", e2.mood);
        assertNotEquals(e1.content, e2.content);
    }
    
    @Test
    public void testJournalEntity_tagsVerification() {
        JournalEntity entity = new JournalEntity("😢", "Sad day", "#sad #down", "2025-12-08");
        
        assertTrue(entity.tags.contains("#sad"));
        assertTrue(entity.tags.contains("#down"));
    }
}
