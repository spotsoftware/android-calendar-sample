package it.spot.android.calendarsample.calendar;

/**
 * @author a.rinaldi
 */
public class CalendarModel {

    private int mId;
    private String mName;
    private String mAccountName;

    // region Construction

    public CalendarModel(int id, String name, String accountName) {
        super();
        this.mId = id;
        this.mName = name;
        this.mAccountName = accountName;
    }

    // endregion

    // region Public methods

    public int getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public String getAccountName() {
        return this.mAccountName;
    }

    // endregion
}
