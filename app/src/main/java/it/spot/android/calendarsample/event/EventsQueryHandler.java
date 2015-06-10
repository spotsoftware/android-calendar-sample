package it.spot.android.calendarsample.event;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author a.rinaldi
 */
public class EventsQueryHandler extends AsyncQueryHandler {

    private final ArrayList<Listener> mListeners;

    // region Construction

    public EventsQueryHandler(ContentResolver cr) {
        super(cr);
        this.mListeners = new ArrayList<Listener>();
    }

    // endregion

    // region AsyncQueryHandler implementation

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        this.notifyCalendarCreated();
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        this.notifyCalendarDeleted();
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        this.notifyCalendarUpdated();
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        Log.e("CAL_QUERY_HANDLER", "query completed with " + cursor.getCount() + " records");

        int idIndex = cursor.getColumnIndex(CalendarContract.Events._ID);
        int titleIndex = cursor.getColumnIndex(CalendarContract.Events.TITLE);
        int descIndex = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
        int startIndex = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
        int endIndex = cursor.getColumnIndex(CalendarContract.Events.DTEND);
        ArrayList<EventModel> events = new ArrayList<EventModel>();

        Calendar startDate;
        Calendar endDate;

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // TODO - pass current app locale?
                startDate = Calendar.getInstance();
                startDate.setTimeInMillis(Long.valueOf(cursor.getString(startIndex)));
                endDate = Calendar.getInstance();
                if (endIndex > -1) {
                    try {
                        endDate.setTimeInMillis(Long.valueOf(cursor.getString(endIndex)));
                    } catch (NumberFormatException ex) {
                    }
                }

                events.add(new EventModel(cursor.getInt(idIndex), cursor.getString(titleIndex), "", startDate, endDate));
            }
        }

        this.notifyCalendarsRetrieved(events);
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
            listener.onEventCreated();
        }
    }

    private void notifyCalendarUpdated() {
        for (Listener listener : this.mListeners) {
            listener.onEventUpdated();
        }
    }

    private void notifyCalendarDeleted() {
        for (Listener listener : this.mListeners) {
            listener.onEventDeleted();
        }
    }

    private void notifyCalendarsRetrieved(ArrayList<EventModel> events) {
        for (Listener listener : this.mListeners) {
            listener.onEventsRetrieved(events);
        }
    }

    // endregion

    // region Inner interface

    public interface Listener {

        void onEventsRetrieved(List<EventModel> events);

        void onEventCreated();

        void onEventDeleted();

        void onEventUpdated();
    }

    // endregion
}
