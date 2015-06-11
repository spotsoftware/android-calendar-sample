package it.spot.android.calendarsample.lib.event;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;

import it.spot.android.calendarsample.lib.shared.ContentProviderEntityModel;

/**
 * @author a.rinaldi
 */
public class EventModel extends ContentProviderEntityModel {

    private int mCalendarId;

    private int mId;
    private String mTitle;
    private String mDescription;
    private String mLocation;
    private String mTimeZone;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private int mAccessLevel;
    private int mDuration;
    private boolean mIsRecurrent;
    private boolean mHasRecurrenceStateChanged;
    private EventRecurrence mRecurrence;
    private EventRecurrence mExcRecurrence;


    // region Construction

    protected EventModel() {
        super();

        this.mCalendarId = -1;

        this.mId = -1;
        this.mTitle = null;
        this.mDescription = null;
        this.mLocation = null;
        this.mTimeZone = null;
        this.mStartDate = null;
        this.mEndDate = null;
        this.mAccessLevel = -1;
        this.mDuration = -1;
        this.mHasRecurrenceStateChanged = false;
        this.mIsRecurrent = false;
        this.mRecurrence = null;
        this.mExcRecurrence = null;
    }

    public static EventModel create() {
        return new EventModel();
    }

    // endregion

    // region Public methods

    public int getCalendarId() {
        return this.mCalendarId;
    }

    public EventModel setCalendarId(int calendarId) {
        this.mCalendarId = calendarId;
        return this;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public EventModel setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public EventModel setDescription(String description) {
        this.mDescription = description;
        return this;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public EventModel setLocation(String location) {
        this.mLocation = location;
        return this;
    }

    public String getTimeZone() {
        return this.mTimeZone;
    }

    public EventModel setTimeZone(String timeZone) {
        this.mTimeZone = timeZone;
        return this;
    }

    public Calendar getStartDate() {
        return this.mStartDate;
    }

    public EventModel setStartDate(Calendar startDate) {
        this.mStartDate = startDate;
        return this;
    }

    public Calendar getEndDate() {
        return this.mEndDate;
    }

    public EventModel setEndDate(Calendar endDate) {
        this.mEndDate = endDate;
        return this;
    }

    public int getAccessLevel() {
        return this.mAccessLevel;
    }

    public EventModel setAccessLevel(int accessLevel) {
        this.mAccessLevel = accessLevel;
        return this;
    }

    /**
     * Returns the duration of the recurrence.<br/>
     *
     * @return the duration in seconds. If not set, {@code -1} is returned
     */
    public int getDuration() {
        return this.mDuration;
    }

    /**
     * Sets the duration of the recurrence.<br/>
     * Calling this method without enabling recurrences with {@link #enableRecurrence(boolean)} is useless.
     *
     * @param duration the duration of the recurrence, in seconds
     */
    public EventModel setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    public boolean isRecurrent() {
        return this.mIsRecurrent;
    }

    public EventModel enableRecurrence(boolean isRecurrent) {
        this.mHasRecurrenceStateChanged = true;
        this.mIsRecurrent = isRecurrent;
        return this;
    }

    public EventModel setRecurrence(EventRecurrence recurrence) {
        this.mRecurrence = recurrence;
        return this;
    }

    public EventRecurrence getRecurrence() {
        return this.mRecurrence;
    }

    public EventModel setExcRecurrence(EventRecurrence recurrence) {
        this.mExcRecurrence = recurrence;
        return this;
    }

    public EventRecurrence getExcRecurrence() {
        return this.mExcRecurrence;
    }

    // endregion

    // region ContentProviderEntityModel implementation

    @Override
    public Uri getEntityUri() {
        return CalendarContract.Events.CONTENT_URI;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_TIMEZONE,
                CalendarContract.Events.ACCESS_LEVEL,
                CalendarContract.Events.RRULE,
                CalendarContract.Events.EXRULE,
                CalendarContract.Events.DURATION,
                CalendarContract.Events.EVENT_LOCATION
        };
    }

    @Override
    public ContentValues fillContentValues() {
        ContentValues values = new ContentValues();

        if (this.mId != -1) {
            values.put(CalendarContract.Events._ID, this.mId);
        }

        if (this.mCalendarId != -1) {
            values.put(CalendarContract.Events.CALENDAR_ID, this.mCalendarId);
        }

        if (this.mTitle != null) {
            values.put(CalendarContract.Events.TITLE, this.mTitle);
        }

        if (this.mDescription != null) {
            values.put(CalendarContract.Events.DESCRIPTION, this.mDescription);
        }

        if (this.mStartDate != null) {
            values.put(CalendarContract.Events.DTSTART, this.mStartDate.getTimeInMillis());
        }

        if (this.mHasRecurrenceStateChanged && this.mIsRecurrent) {
            if (this.mRecurrence != null) {
                values.put(CalendarContract.Events.RRULE, this.mRecurrence.toString());
            }

            if (this.mExcRecurrence != null) {
                values.put(CalendarContract.Events.EXRULE, this.mExcRecurrence.toString());
            }

            if (this.mDuration > -1) {
                values.put(CalendarContract.Events.DURATION, String.format("P%sS", String.valueOf(this.mDuration)));
            }
        } else if (this.mEndDate != null) {
            values.put(CalendarContract.Events.DTEND, this.mEndDate.getTimeInMillis());
        }

        if (this.mLocation != null) {
            values.put(CalendarContract.Events.EVENT_LOCATION, this.mLocation);
        }

        if (this.mTimeZone != null) {
            values.put(CalendarContract.Events.EVENT_TIMEZONE, this.mTimeZone);
        }

        if (this.mAccessLevel != -1) {
            values.put(CalendarContract.Events.ACCESS_LEVEL, this.mAccessLevel);
        }

        return values;
    }

    @Override
    public void fillFromCursor(Cursor cursor) {

        int index = cursor.getColumnIndex(CalendarContract.Events._ID);
        if (index != -1) {
            this.setId(cursor.getInt(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID);
        if (index > -1) {
            this.setCalendarId(cursor.getInt(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.TITLE);
        if (index > -1) {
            this.setTitle(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
        if (index > -1) {
            this.setDescription(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
        if (index > -1) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(cursor.getLong(index));
            this.setStartDate(date);
        }

        index = cursor.getColumnIndex(CalendarContract.Events.DTEND);
        if (index > -1) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(cursor.getLong(index));
            this.setEndDate(date);
        }

        index = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
        if (index > -1) {
            this.setLocation(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.EVENT_TIMEZONE);
        if (index > -1) {
            this.setTimeZone(cursor.getString(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.ACCESS_LEVEL);
        if (index > -1) {
            this.setAccessLevel(cursor.getInt(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ACCESS_LEVEL);
        if (index > -1) {
            this.setAccessLevel(cursor.getInt(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.DURATION);
        if (index > -1) {
            this.setDuration(cursor.getInt(index));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.RRULE);
        if (index > -1) {
            this.setRecurrence(EventRecurrence.fromString(cursor.getString(index)));
        }

        index = cursor.getColumnIndex(CalendarContract.Events.EXRULE);
        if (index > -1) {
            this.setExcRecurrence(EventRecurrence.fromString(cursor.getString(index)));
        }
    }

    // endregion
}
