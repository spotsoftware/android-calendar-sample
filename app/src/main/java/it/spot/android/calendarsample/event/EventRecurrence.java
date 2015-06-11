package it.spot.android.calendarsample.event;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author a.rinaldi
 */
public class EventRecurrence {

    public static final String RECURRENCE_ON_MONDAY = "MO";
    public static final String RECURRENCE_ON_TUESDAY = "TU";
    public static final String RECURRENCE_ON_WEDNESDAY = "WE";
    public static final String RECURRENCE_ON_THURSDAY = "TH";
    public static final String RECURRENCE_ON_FRIDAY = "FR";
    public static final String RECURRENCE_ON_SATURDAY = "SA";
    public static final String RECURRENCE_ON_SUNDAY = "SU";

    public static final String RECURRENCE_WINDOW_DAILY = "DAILY";
    public static final String RECURRENCE_WINDOW_WEEKLY = "WEEKLY";
    public static final String RECURRENCE_WINDOW_MONTHLY = "MONTHLY";
    public static final String RECURRENCE_WINDOW_YEARLY = "YEARLY";

    private int mRecursEvery = -1;
    private String mRecursIn = null;
    private Calendar mRecursUntil = null;

    private ArrayList<String> mRecursOn;

    // region Construction

    protected EventRecurrence() {
        super();
        this.mRecursOn = new ArrayList<String>();
    }

    public static EventRecurrence create() {
        return new EventRecurrence();
    }

    public static EventRecurrence fromString(String recStr) {
        EventRecurrence recurrence = new EventRecurrence();

        // TODO - parse string

        return recurrence;
    }

    // endregion

    // region Public methods

    public EventRecurrence recursOn(String dayOfWeek) {
        // TODO - check repetitions?
        this.mRecursOn.add(dayOfWeek);
        return this;
    }

    public EventRecurrence recursEvery(int interval) {
        this.mRecursEvery = interval;
        return this;
    }

    public EventRecurrence recursIn(String window) {
        this.mRecursIn = window;
        return this;
    }

    public EventRecurrence recursUntil(Calendar date) {


        return this;
    }

    @Override
    public String toString() {
        String recurrence = "";

        switch (this.mRecursIn) {
            case RECURRENCE_WINDOW_DAILY:
            case RECURRENCE_WINDOW_WEEKLY:
            case RECURRENCE_WINDOW_MONTHLY:
            case RECURRENCE_WINDOW_YEARLY:
                recurrence += "FREQ=" + this.mRecursIn;
                break;
            default:
                return recurrence;
        }

        if (this.mRecursEvery > -1) {
            recurrence += ";INTERVAL=" + this.mRecursEvery;
        }

        if (this.mRecursOn.size() > 0) {
            recurrence += ";BYDAY=";
            for (String day : this.mRecursOn) {
                switch (day) {
                    case RECURRENCE_ON_MONDAY:
                    case RECURRENCE_ON_TUESDAY:
                    case RECURRENCE_ON_WEDNESDAY:
                    case RECURRENCE_ON_THURSDAY:
                    case RECURRENCE_ON_FRIDAY:
                    case RECURRENCE_ON_SATURDAY:
                    case RECURRENCE_ON_SUNDAY:
                        recurrence += day;
                        if (this.mRecursOn.indexOf(day) < this.mRecursOn.size() - 1) {
                            recurrence += ",";
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return recurrence;
    }

    // endregion
}
