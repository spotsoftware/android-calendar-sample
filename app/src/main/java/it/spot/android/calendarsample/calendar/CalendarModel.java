package it.spot.android.calendarsample.calendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import it.spot.android.calendarsample.shared.ContentProviderEntityModel;

/**
 * @author a.rinaldi
 */
public class CalendarModel extends ContentProviderEntityModel {

    private String mName;
    private String mDisplayName;
    private String mAccountName;
    private String mAccountType;

    private int mAccessLevel;

    private boolean mIsVisible;
    private boolean mHasChangedVisibility;
    private boolean mSyncEvents;
    private boolean mHasChangedSyncEvents;

    // region Construction

    protected CalendarModel() {
        super();

        this.mName = null;
        this.mDisplayName = null;
        this.mAccountName = null;
        this.mAccountType = null;
        this.mAccessLevel = -1;

        this.mIsVisible = false;
        this.mHasChangedVisibility = false;
        this.mSyncEvents = false;
        this.mHasChangedSyncEvents = false;
    }

    public static CalendarModel create() {
        return new CalendarModel();
    }

    public static CalendarModel createFromCursor(final Cursor cursor) {
        CalendarModel calendar = new CalendarModel();
        calendar.fillFromCursor(cursor);
        return calendar;
    }

    // endregion

    // region Public methods

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
        this.mHasChangedVisibility = true;
        this.mIsVisible = visible;
        return this;
    }

    public boolean syncEvents() {
        return this.mSyncEvents;
    }

    public CalendarModel enableSyncEvents(boolean syncEvents) {
        this.mHasChangedSyncEvents = true;
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

    // region ContentProviderEntityModel implementation

    @Override
    public Uri getEntityUri() {
        return CalendarContract.Calendars.CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.CALENDAR_LOCATION,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.SYNC_EVENTS,
                CalendarContract.Calendars.VISIBLE
        };
    }

    @Override
    public void fillFromCursor(Cursor cursor) {

        int index = cursor.getColumnIndex(CalendarContract.Calendars._ID);
        this.setId(cursor.getInt(index));

        index = cursor.getColumnIndex(CalendarContract.Calendars.NAME);
        if (index > -1) {
            this.setName(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME);
        if (index > -1) {
            this.setDisplayName(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.SYNC_EVENTS);
        if (index > -1) {
            this.enableSyncEvents(cursor.getInt(index) == 1);
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.VISIBLE);
        if (index > -1) {
            this.setVisibility(cursor.getInt(index) == 1);
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL);
        if (index > -1) {
            this.setAccessLevel(cursor.getInt(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME);
        if (index > -1) {
            this.setAccountName(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE);
        if (index > -1) {
            this.setAccountType(cursor.getString(index));
        }
    }

    @Override
    public ContentValues fillContentValues() {
        ContentValues values = new ContentValues();

        if (this.mId != -1) {
            values.put(CalendarContract.Calendars._ID, this.mId);
        }

        if (this.mAccountName != null) {
            values.put(CalendarContract.Calendars.ACCOUNT_NAME, this.mAccountName);
        }

        if (this.mAccountType != null) {
            values.put(CalendarContract.Calendars.ACCOUNT_TYPE, this.mAccountType);
        }

        if (this.mAccessLevel > -1) {
            values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, this.mAccessLevel);
        }

        if (this.mHasChangedSyncEvents) {
            values.put(CalendarContract.Calendars.SYNC_EVENTS, this.mSyncEvents ? 1 : 0);
        }

        if (this.mHasChangedVisibility) {
            values.put(CalendarContract.Calendars.VISIBLE, this.mIsVisible ? 1 : 0);
        }

        if (this.mName != null) {
            values.put(CalendarContract.Calendars.NAME, this.mName);
        }

        if (this.mDisplayName != null) {
            values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, this.mDisplayName);
        }

        return values;
    }

    // endregion
}
