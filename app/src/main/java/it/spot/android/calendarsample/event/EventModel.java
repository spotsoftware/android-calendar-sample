package it.spot.android.calendarsample.event;

import java.util.Calendar;

/**
 * @author a.rinaldi
 */
public class EventModel {

    private int mId;
    private String mTitle;
    private String mDescription;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private int mDuration = -1;
    private boolean mIsRecurrent;
    private EventRecurrence mRecurrence;
    private EventRecurrence mExcRecurrence;

    // region Construction

    public EventModel(int id, String title, String description, Calendar startDate, Calendar endDate) {
        super();
        this.mId = id;
        this.mTitle = title;
        this.mDescription = description;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mIsRecurrent = false;
    }

    // endregion

    // region Public methods

    public int getId() {
        return this.mId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Calendar getStartDate() {
        return this.mStartDate;
    }

    public void setStartDate(Calendar startDate) {
        this.mStartDate = startDate;
    }

    public Calendar getEndDate() {
        return this.mEndDate;
    }

    public void setEndDate(Calendar endDate) {
        this.mEndDate = endDate;
    }

    public boolean isRecurrent() {
        return this.mIsRecurrent;
    }

    public void enableRecurrence(boolean isRecurrent) {
        this.mIsRecurrent = isRecurrent;
    }

    public void setRecurrence(EventRecurrence recurrence) {
        this.mRecurrence = recurrence;
    }

    public EventRecurrence getRecurrence() {
        return this.mRecurrence;
    }

    public void setExcRecurrence(EventRecurrence recurrence) {
        this.mExcRecurrence = recurrence;
    }

    public EventRecurrence getExcRecurrence() {
        return this.mExcRecurrence;
    }

    /**
     * Sets the duration of the recurrence.<br/>
     * Calling this method without enabling recurrences with {@link #enableRecurrence(boolean)} is useless.
     *
     * @param duration the duration of the recurrence, in seconds
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    /**
     * Returns the duration of the recurrence.<br/>
     *
     * @return the duration in seconds. If not set, {@code -1} is returned
     */
    public int getDuration() {
        return this.mDuration;
    }

    // endregion
}
