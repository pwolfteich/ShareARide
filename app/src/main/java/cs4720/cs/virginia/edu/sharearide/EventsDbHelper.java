package cs4720.cs.virginia.edu.sharearide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by McNulty-PC on 9/29/2015.
 */
public class EventsDbHelper extends SQLiteOpenHelper {
    private static EventsDbHelper sInstance;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Events.db";
    public static final String TABLE_NAME = "Events";
    public static final String COLUMN_ENTRY_ID = "entryid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DESCRIPTION = "description";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_LOCATION + TEXT_TYPE + COMMA_SEP +
                    COLUMN_DESCRIPTION+
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static synchronized EventsDbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new EventsDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private EventsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(COLUMN_ENTRY_ID, event.getId());
        values.put(COLUMN_NAME, event.getName());
        values.put(COLUMN_DATE, event.getDate());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_LOCATION, event.getLocation());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
    public void updateEvent(Event event)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(COLUMN_ENTRY_ID, event.getId());
        values.put(COLUMN_NAME, event.getName());
        values.put(COLUMN_DATE, event.getDate());
        values.put(COLUMN_DESCRIPTION, event.getDescription());
        values.put(COLUMN_LOCATION, event.getLocation());

        db.update(TABLE_NAME, values, COLUMN_ENTRY_ID, new String[]{String.valueOf(event.getId())});
        db.close(); // Closing database connection
    }
    public Event getEvent(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,new String[] {COLUMN_ENTRY_ID, COLUMN_NAME, COLUMN_DATE,COLUMN_LOCATION, COLUMN_DESCRIPTION}, COLUMN_ENTRY_ID+" LIKE ?", new String[] {""+id},null,null,null,null);
        cursor.moveToFirst();
        Event tmp = new Event(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        db.close();
        return tmp;
    }
    public ArrayList<Event> getEvents()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Event> retVal = new ArrayList<Event>();
        Cursor cursor = db.query(TABLE_NAME,new String[] {COLUMN_ENTRY_ID, COLUMN_NAME, COLUMN_DATE,COLUMN_LOCATION, COLUMN_DESCRIPTION}, COLUMN_ENTRY_ID+" LIKE ?", new String[] {"%"},null,null,null,null);
        if (cursor.moveToFirst() && !(cursor.getCount() == 0)) {
            do {
                retVal.add(new Event(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
            while (cursor.moveToNext());
        }
        db.close();
        return retVal;
    }
    public void deleteEvent(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int result  = db.delete(TABLE_NAME,COLUMN_ENTRY_ID+"=?",new String[] {""+id});
        db.close();
        return;
    }

}
