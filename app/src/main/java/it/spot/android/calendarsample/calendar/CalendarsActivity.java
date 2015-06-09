package it.spot.android.calendarsample.calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import it.spot.android.calendarsample.R;
import it.spot.android.calendarsample.event.EventsActivity;


public class CalendarsActivity
        extends Activity
        implements CalendarsQueryHandler.Listener {

    private CalendarsQueryHandler mCalendarsQueryHandler;

    private ArrayList<CalendarModel> mCalendars;
    private CalendarsArrayAdapter mCalendarsAdapter;
    private ListView mCalendarsListView;

    private FloatingActionButton mCreateButton;

    // region Activity life cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_calendars);

        this.mCreateButton = (FloatingActionButton) this.findViewById(R.id.button_add_calendar);
        this.mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View dialogBody = LayoutInflater.from(CalendarsActivity.this).inflate(R.layout.dialog_calendar_create, null);
                final EditText nameEditText = (EditText) dialogBody.findViewById(R.id.name);
                final EditText dispNameEditText = (EditText) dialogBody.findViewById(R.id.display_name);

                new AlertDialog.Builder(CalendarsActivity.this)
                        .setTitle("Aggiungi calendario")
                        .setView(dialogBody)
                        .setPositiveButton("Crea", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                ContentValues values = new ContentValues();
                                values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
                                values.put(CalendarContract.Calendars.VISIBLE, 1);
                                values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
                                values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, dispNameEditText.getText().toString());
                                values.put(CalendarContract.Calendars.NAME, nameEditText.getText().toString());
                                values.put(CalendarContract.Calendars.ACCOUNT_NAME, "private");
                                values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);

                                mCalendarsQueryHandler.startInsert(1, null,
                                        CalendarContract.Calendars.CONTENT_URI.buildUpon()
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

        this.mCalendars = new ArrayList<CalendarModel>();
        this.mCalendarsAdapter = new CalendarsArrayAdapter(this, R.layout.list_item_calendar, this.mCalendars);
        this.mCalendarsListView = (ListView) this.findViewById(R.id.list_calendars);
        this.mCalendarsListView.setAdapter(this.mCalendarsAdapter);
        this.mCalendarsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(CalendarsActivity.this)
                        .setMessage("Cosa vuoi fare?")
                        .setPositiveButton("Apri", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(CalendarsActivity.this, EventsActivity.class)
                                        .putExtra(EventsActivity.EXTRA_CALENDAR_ID, mCalendars.get(position).getId()));
                            }
                        })
                        .setNegativeButton("Elimina", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mCalendarsQueryHandler.startDelete(
                                        1,
                                        null,
                                        CalendarContract.Calendars.CONTENT_URI.buildUpon().appendPath(String.valueOf(mCalendars.get(position).getId()))
                                                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "private")
                                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                                                .build(),
                                        null,
                                        null);
//                                ContentValues values = new ContentValues();
//                                values.put(CalendarContract.Calendars.NAME, "My bello Calendar");
//                                values.put(CalendarContract.Calendars.ACCOUNT_NAME, "private");
//                                values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
//
//                                mCalendarsQueryHandler.startUpdate(
//                                        1,
//                                        null,
//                                        CalendarContract.Calendars.CONTENT_URI.buildUpon().appendPath(String.valueOf(mCalendars.get(position).getId()))
//                                                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER, "true")
//                                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "private")
//                                                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
//                                                .build(),
//                                        values,
//                                        null,
//                                        null);
                            }
                        })
                        .create()
                        .show();
            }
        });

        this.mCalendarsQueryHandler = new CalendarsQueryHandler(this.getContentResolver());
        this.mCalendarsQueryHandler.registerListener(this);
    }

    // endregion

    // region Public methods

    public void queryCalendars(View v) {
        Log.e("CALENDARS_QUERY", "starting the query");

        String[] projection = {
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.CALENDAR_LOCATION,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE
        };

        this.mCalendarsQueryHandler.startQuery(
                1,
                null,
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                null,
                null,
                CalendarContract.Calendars.DEFAULT_SORT_ORDER);
    }

    // endregion

    // region CalendarsQueryHandler.Listener implementation

    @Override
    public void onCalendarCreated() {
        this.queryCalendars(null);
    }

    @Override
    public void onCalendarDeleted() {
        this.queryCalendars(null);
    }

    @Override
    public void onCalendarsRetrieved(List<CalendarModel> calendars) {
        this.mCalendars.clear();
        this.mCalendars.addAll(calendars);
        this.mCalendarsAdapter.notifyDataSetChanged();
    }

    // endregion
}
