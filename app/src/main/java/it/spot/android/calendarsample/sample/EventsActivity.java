package it.spot.android.calendarsample.sample;

import android.app.Activity;
import android.app.AlertDialog;
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
import it.spot.android.calendarsample.lib.event.EventModel;
import it.spot.android.calendarsample.lib.event.EventRecurrence;
import it.spot.android.calendarsample.lib.event.EventsArrayAdapter;
import it.spot.android.calendarsample.lib.event.EventsProxy;


public class EventsActivity
        extends Activity
        implements EventsProxy.Listener {

    public static final String EXTRA_CALENDAR_ID = "calendar_id";

    private EventsProxy mEventsQueryHandler;

    private ArrayList<EventModel> mEvents;
    private EventsArrayAdapter mEventsAdapter;
    private ListView mEventsListView;

    private FloatingActionButton mCreateEventButton;
    private FloatingActionButton mDeleteAllEventsButton;

    private int mCalendarId;

    // region Activity life cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_events);

        this.mCalendarId = this.getIntent().getExtras().getInt(EXTRA_CALENDAR_ID, -1);

        this.mDeleteAllEventsButton = (FloatingActionButton) this.findViewById(R.id.button_delete_all_events);
        this.mDeleteAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventsQueryHandler.deleteAllEventsForCalendar(EventModel.create(), mCalendarId);
            }
        });

        this.mCreateEventButton = (FloatingActionButton) this.findViewById(R.id.button_add_event);
        this.mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EventsActivity.this)
                        .setTitle(R.string.add_event)
                        .setPositiveButton(R.string.create_recurrent, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                Calendar beginTime = Calendar.getInstance();
                                beginTime.set(Calendar.HOUR_OF_DAY, 16);
                                beginTime.set(Calendar.MINUTE, 25);
                                beginTime.setTimeZone(TimeZone.getDefault());
                                Calendar endTime = Calendar.getInstance();
                                endTime.set(Calendar.MONTH, 8);
                                endTime.set(Calendar.DAY_OF_MONTH, 25);
                                endTime.set(Calendar.HOUR_OF_DAY, 22);
                                endTime.set(Calendar.MINUTE, 50);
                                endTime.setTimeZone(TimeZone.getDefault());
                                Calendar endExTime = Calendar.getInstance();
                                endExTime.set(Calendar.MONTH, 6);
                                endExTime.set(Calendar.DAY_OF_MONTH, 25);
                                endExTime.set(Calendar.HOUR_OF_DAY, 22);
                                endExTime.set(Calendar.MINUTE, 50);
                                endExTime.setTimeZone(TimeZone.getDefault());

                                mEventsQueryHandler.create(EventModel.create()
                                        .setCalendarId(mCalendarId)
                                        .setTitle("Evento ricorrente di prova")
                                        .setDescription("Descrizione dell'evento ricorrente di prova")
                                        .setAccessLevel(CalendarContract.Events.ACCESS_PUBLIC)
                                        .setStartDate(beginTime)
                                        .enableRecurrence(true)
                                        .setRecurrence(EventRecurrence.create()
                                                .recursIn(EventRecurrence.RECURRENCE_WINDOW_WEEKLY)
                                                .recursOn(EventRecurrence.RECURRENCE_ON_WEDNESDAY)
                                                .recursOn(EventRecurrence.RECURRENCE_ON_FRIDAY)
                                                .recursUntil(endTime))
                                        .setExcRecurrence(EventRecurrence.create()
                                                .recursIn(EventRecurrence.RECURRENCE_WINDOW_WEEKLY)
                                                .recursOn(EventRecurrence.RECURRENCE_ON_WEDNESDAY)
                                                .recursUntil(endExTime))
                                        .setDuration(3600)
                                        .setTimeZone("GMT"));
                            }
                        })
                        .setNeutralButton(R.string.create, new DialogInterface.OnClickListener() {
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

                                mEventsQueryHandler.create(EventModel.create()
                                        .setCalendarId(mCalendarId)
                                        .setTitle("Evento di prova")
                                        .setDescription("Descrizione dell'evento di prova")
                                        .setStartDate(beginTime)
                                        .setEndDate(endTime)
                                        .setAccessLevel(CalendarContract.Events.ACCESS_PUBLIC)
                                        .setTimeZone("GMT"));
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                        .setMessage(R.string.delete_event_alert)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Log.e("EVENTS_ACTIVITY_DELETE", "id is " + mEvents.get(position).getId());
                                mEventsQueryHandler.delete(mEvents.get(position));
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        this.mEventsQueryHandler = new EventsProxy(this.getContentResolver());
        this.mEventsQueryHandler.registerListener(this);
    }

    // endregion

    // region Public methods

    public void queryEvents(View v) {
        Log.e("CALENDARS_QUERY", "starting the query");

        this.mEventsQueryHandler.getAll(
                EventModel.create(),
                CalendarContract.Events.CALENDAR_ID + " = ?",
                new String[]{String.valueOf(this.mCalendarId)});
    }

    // endregion

    // region EventsProxy.Listener implementation

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
