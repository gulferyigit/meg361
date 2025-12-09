package gulfer.emine.diary.test;

import gulfer.emine.diary.JournalEntity;
import org.junit.Test;
import static org.junit.Assert.*;

public class JournalEntityTest {
    @Test
    public void testJournalEntity_isCreated() {
        JournalEntity entity = new JournalEntity("😊", "Test content", "#test #diary", "2025-12-09");
        
        assertEquals("Test content", entity.content);
        assertEquals("#test #diary", entity.tags);
        assertEquals("2025-12-09", entity.date);
        assertEquals("😊", entity.mood);
    }

    @Test
    public void testJournalEntity_tagsNotEmpty() {
        JournalEntity entity = new JournalEntity("😃", "Test", "#mutluluk #enerji", "2025-12-09");
        
        assertNotNull(entity.tags);
        assertTrue(entity.tags.contains("#mutluluk"));
    }

    @Test
    public void testJournalEntity_contentEmpty() {
        JournalEntity entity = new JournalEntity("😐", "", "#empty", "2025-12-09");
        
        assertTrue(entity.content.isEmpty());
    }
}
