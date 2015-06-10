package it.spot.android.calendarsample.calendar;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.spot.android.calendarsample.shared.BaseAsyncQueryHandler;

/**
 * @author a.rinaldi
 */
public class CalendarsQueryHandler extends BaseAsyncQueryHandler<CalendarModel> {

    private final ArrayList<Listener> mListeners;

    // region Construction

    public CalendarsQueryHandler(ContentResolver cr) {
        super(cr);
        this.mListeners = new ArrayList<Listener>();
    }

    // endregion

    // region AsyncQueryHandler implementation

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        super.onUpdateComplete(token, cookie, result);
        Log.e("CALENDAR_UPDATE", "Result is " + result);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        if (uri != null) {
            this.notifyCalendarCreated();
        }
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        this.notifyCalendarDeleted();
        Log.e("CALENDAR_DELETE", "Result is " + result);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        ArrayList<CalendarModel> calendars = new ArrayList<CalendarModel>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                calendars.add(CalendarModel.createFromCursor(cursor));
            }
        }

        this.notifyCalendarsRetrieved(calendars);
    }

    // endregion

    // region Public methods

    public void registerListener(Listener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public void unregisterListener(Listener listener) {
        if (this.mListeners.contains(listener)) {
            this.mListeners.remove(listener);
        }
    }

    // endregion

    // region Private methods

    private void notifyCalendarCreated() {
        for (Listener listener : this.mListeners) {
            listener.onCalendarCreated();
        }
    }

    private void notifyCalendarDeleted() {
        for (Listener listener : this.mListeners) {
            listener.onCalendarDeleted();
        }
    }

    private void notifyCalendarsRetrieved(ArrayList<CalendarModel> calendars) {
        for (Listener listener : this.mListeners) {
            listener.onCalendarsRetrieved(calendars);
        }
    }

    // endregion

    // region Inner interface

    public interface Listener {

        void onCalendarsRetrieved(List<CalendarModel> calendars);

        void onCalendarCreated();

        void onCalendarDeleted();
    }

    // endregion
}
