package gulfer.emine.diary;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String email;
    public String password;

    @Ignore
    public UserEntity(String name, String email, String password) {
        this(name, email, password, 0);
    }

    public UserEntity(String name, String email, String password, long id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
    }
}
