package gulfer.emine.diary;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.room.Ignore;

@Entity(tableName = "journals")
public class JournalEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String mood;
    public String content;
    public String tags;
    public String date;
    public String photoUri;
    public String ownerEmail;

    @Ignore
    public JournalEntity(String mood, String content, String tags, String date) {
        this(mood, content, tags, date, null, "");
    }

    public JournalEntity(String mood, String content, String tags, String date, String photoUri, String ownerEmail) {
        this.mood = mood;
        this.content = content;
        this.tags = tags;
        this.date = date;
        this.photoUri = photoUri;
        this.ownerEmail = ownerEmail;
    }
}
