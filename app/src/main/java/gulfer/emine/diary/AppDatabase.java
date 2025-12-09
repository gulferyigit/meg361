package gulfer.emine.diary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {JournalEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract JournalDao journalDao();

    private static volatile AppDatabase INSTANCE;
    private static final androidx.room.migration.Migration MIGRATION_1_2 = new androidx.room.migration.Migration(1, 2) {
        @Override
        public void migrate(@androidx.annotation.NonNull androidx.sqlite.db.SupportSQLiteDatabase database) {
            // Add new column 'photoUri' to journals table
            database.execSQL("ALTER TABLE journals ADD COLUMN photoUri TEXT");
        }
    };
    // no migration for ownerEmail (app no longer separates journals by user)

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "diary.db")
                            .addMigrations(MIGRATION_1_2)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
