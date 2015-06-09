package it.spot.android.calendarsample.event;

import java.util.Calendar;

/**
 * @author a.rinaldi
 */
public class EventModel {

    private int mId;
    private String mTitle;
    private Calendar mStartDate;
    private Calendar mEndDate;

    // region Construction

    public EventModel(int id, String title, Calendar startDate, Calendar endDate) {
        super();
        this.mId = id;
        this.mTitle = title;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
    }

    // endregion

    // region Public methods

    public int getId() {
        return this.mId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public Calendar getStartDate() {
        return this.mStartDate;
    }

    public Calendar getEndDate() {
        return this.mEndDate;
    }

    // endregion
}
