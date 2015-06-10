package it.spot.android.calendarsample.event;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import it.spot.android.calendarsample.R;


public class EventsActivity
        extends Activity
        implements EventsQueryHandler.Listener {

    public static final String EXTRA_CALENDAR_ID = "calendar_id";

    private EventsQueryHandler mEventsQueryHandler;

    private ArrayList<EventModel> mEvents;
    private EventsArrayAdapter mEventsAdapter;
    private ListView mEventsListView;

    private FloatingActionButton mCreateEventButton;

    private int mCalendarId;

    // region Activity life cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_events);

        this.mCalendarId = this.getIntent().getExtras().getInt(EXTRA_CALENDAR_ID, -1);

        this.mCreateEventButton = (FloatingActionButton) this.findViewById(R.id.button_add_event);
        this.mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EventsActivity.this)
                        .setTitle("Aggiungi evento")
                        .setPositiveButton("Crea", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                Calendar beginTime = Calendar.getInstance();
                                beginTime.set(Calendar.HOUR_OF_DAY, 20);
                                beginTime.set(Calendar.MINUTE, 16);
                                beginTime.setTimeZone(TimeZone.getDefault());
                                Calendar endTime = Calendar.getInstance();
                                endTime.set(Calendar.DAY_OF_MONTH, 25);
                                endTime.set(Calendar.HOUR_OF_DAY, 22);
                                endTime.set(Calendar.MINUTE, 50);
                                endTime.setTimeZone(TimeZone.getDefault());

                                ContentValues values = new ContentValues();
                                values.put(CalendarContract.Events.CALENDAR_ID, mCalendarId);
                                values.put(CalendarContract.Events.TITLE, "stocabbello");
                                values.put(CalendarContract.Events.DESCRIPTION, "un bellissimo evento di prova e guai a chi caga la minchia");
                                values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
                                values.putNull(CalendarContract.Events.DTEND);
//                                values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
                                values.put(CalendarContract.Events.EVENT_TIMEZONE, "GMT");
                                values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PUBLIC);
                                values.put(CalendarContract.Events.RRULE, new EventRecurrence()
                                        .recursIn(EventRecurrence.RECURRENCE_WINDOW_WEEKLY)
                                        .recursOn(EventRecurrence.RECURRENCE_ON_TUESDAY)
                                        .recursOn(EventRecurrence.RECURRENCE_ON_THURSDAY)
                                        .toString());
                                values.put(CalendarContract.Events.EXRULE, "FREQ=WEEKLY;UNTIL=20150708T000000Z;BYDAY=TH");
                                values.put(CalendarContract.Events.DURATION, "P3600S");
                                values.put(CalendarContract.Events.EVENT_LOCATION, "Event Location");

                                mEventsQueryHandler.startInsert(1, null,
                                        CalendarContract.Events.CONTENT_URI.buildUpon()
                                                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "private")
                                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                                                .build(), values);
                            }
                        })
                        .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        this.mEvents = new ArrayList<EventModel>();
        this.mEventsAdapter = new EventsArrayAdapter(this, R.layout.list_item_event, this.mEvents);
        this.mEventsListView = (ListView) this.findViewById(R.id.list_events);
        this.mEventsListView.setAdapter(this.mEventsAdapter);
        this.mEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(EventsActivity.this)
                        .setMessage("Sei sicuro di voler eliminare l'evento?")
                        .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mEventsQueryHandler.startDelete(
                                        1,
                                        null,
                                        CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(String.valueOf(mEvents.get(position).getId()))
                                                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "private")
                                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                                                .build(),
                                        null,
                                        null);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        this.mEventsQueryHandler = new EventsQueryHandler(this.getContentResolver());
        this.mEventsQueryHandler.registerListener(this);
    }

    // endregion

    // region Public methods

    public void queryEvents(View v) {
        Log.e("CALENDARS_QUERY", "starting the query");

        String[] projection = {
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
        };

        String selectionClause = CalendarContract.Events.CALENDAR_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(this.mCalendarId)};

        this.mEventsQueryHandler.startQuery(
                1,
                null,
                CalendarContract.Events.CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                CalendarContract.Events.DTSTART + " ASC");
    }

    // endregion

    // region EventsQueryHandler.Listener implementation

    @Override
    public void onEventsRetrieved(List<EventModel> events) {
        this.mEvents.clear();
        this.mEvents.addAll(events);
        this.mEventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEventCreated() {
        this.queryEvents(null);
    }

    @Override
    public void onEventDeleted() {
        this.queryEvents(null);
    }

    @Override
    public void onEventUpdated() {

    }

    // endregion
}
