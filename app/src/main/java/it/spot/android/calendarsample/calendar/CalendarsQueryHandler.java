package it.spot.android.calendarsample.calendar;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author a.rinaldi
 */
public class CalendarsQueryHandler extends AsyncQueryHandler {

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
        Log.e("CAL_QUERY_HANDLER", "query completed with " + cursor.getCount() + " records");

        int idIndex = cursor.getColumnIndex(CalendarContract.Calendars._ID);
        int nameIndex = cursor.getColumnIndex(CalendarContract.Calendars.NAME);
        int accountNameIndex = cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME);
        ArrayList<CalendarModel> calendars = new ArrayList<CalendarModel>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                calendars.add(new CalendarModel(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        accountNameIndex > -1 ? cursor.getString(accountNameIndex) : ""));
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
