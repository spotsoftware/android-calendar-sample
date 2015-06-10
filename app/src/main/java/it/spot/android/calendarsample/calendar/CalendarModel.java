package it.spot.android.calendarsample.calendar;

import android.database.Cursor;
import android.provider.CalendarContract;

/**
 * @author a.rinaldi
 */
public class CalendarModel {

    private int mId;
    private String mName;
    private String mDisplayName;
    private String mAccountName;
    private String mAccountType;

    private int mAccessLevel;

    private boolean mIsVisible;
    private boolean mSyncEvents;

    // region Construction

    protected CalendarModel() {
        super();

        this.mId = -1;
        this.mName = "";
        this.mDisplayName = "";
        this.mAccountName = "private";
        this.mAccountType = CalendarContract.ACCOUNT_TYPE_LOCAL;

        this.mAccessLevel = CalendarContract.Calendars.CAL_ACCESS_OWNER;

        this.mIsVisible = false;
        this.mSyncEvents = false;
    }

    public static CalendarModel create() {
        return new CalendarModel();
    }

    public static CalendarModel createFromCursor(final Cursor cursor) {
        CalendarModel calendar = new CalendarModel();

        int index = cursor.getColumnIndex(CalendarContract.Calendars._ID);
        calendar.setId(cursor.getInt(index));

        index = cursor.getColumnIndex(CalendarContract.Calendars.NAME);
        if (index > -1) {
            calendar.setName(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME);
        if (index > -1) {
            calendar.setDisplayName(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.SYNC_EVENTS);
        if (index > -1) {
            calendar.enableSyncEvents(cursor.getInt(index) == 1);
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.VISIBLE);
        if (index > -1) {
            calendar.setVisibility(cursor.getInt(index) == 1);
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL);
        if (index > -1) {
            calendar.setAccessLevel(cursor.getInt(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME);
        if (index > -1) {
            calendar.setAccountName(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE);
        if (index > -1) {
            calendar.setAccountType(cursor.getString(index));
        }

        return calendar;
    }

    // endregion

    // region Public methods

    public int getId() {
        return this.mId;
    }

    public CalendarModel setId(int id) {
        this.mId = id;
        return this;
    }

    public String getName() {
        return this.mName;
    }

    public CalendarModel setName(String name) {
        this.mName = name;
        return this;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public CalendarModel setDisplayName(String displayName) {
        this.mDisplayName = displayName;
        return this;
    }

    public String getAccountName() {
        return this.mAccountName;
    }

    public CalendarModel setAccountName(String accountName) {
        this.mAccountName = accountName;
        return this;
    }

    public String getAccountType() {
        return this.mAccountType;
    }

    public CalendarModel setAccountType(String accountType) {
        this.mAccountType = accountType;
        return this;
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public CalendarModel setVisibility(boolean visible) {
        this.mIsVisible = visible;
        return this;
    }

    public boolean syncEvents() {
        return this.mSyncEvents;
    }

    public CalendarModel enableSyncEvents(boolean syncEvents) {
        this.mSyncEvents = syncEvents;
        return this;
    }

    public int getAccessLevel() {
        return this.mAccessLevel;
    }

    public CalendarModel setAccessLevel(int accessLevel) {
        this.mAccessLevel = accessLevel;
        return this;
    }

    // endregion
}
