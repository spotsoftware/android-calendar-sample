package it.spot.android.calendarsample.calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
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
                        .setTitle(R.string.add_calendar)
                        .setView(dialogBody)
                        .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                mCalendarsQueryHandler.create(CalendarModel.create()
                                        .enableSyncEvents(true)
                                        .setVisibility(true)
                                        .setAccessLevel(CalendarContract.Calendars.CAL_ACCESS_OWNER)
                                        .setAccountName("private")
                                        .setAccountType(CalendarContract.ACCOUNT_TYPE_LOCAL)
                                        .setName(nameEditText.getText().toString())
                                        .setDisplayName(dispNameEditText.getText().toString()));
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

        this.mCalendars = new ArrayList<CalendarModel>();
        this.mCalendarsAdapter = new CalendarsArrayAdapter(this, R.layout.list_item_calendar, this.mCalendars);
        this.mCalendarsListView = (ListView) this.findViewById(R.id.list_calendars);
        this.mCalendarsListView.setAdapter(this.mCalendarsAdapter);
        this.mCalendarsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(CalendarsActivity.this)
                        .setMessage(R.string.choose_what_to_do)
                        .setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(CalendarsActivity.this, EventsActivity.class)
                                        .putExtra(EventsActivity.EXTRA_CALENDAR_ID, mCalendars.get(position).getId()));
                            }
                        })
                        .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mCalendarsQueryHandler.delete(mCalendars.get(position));
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
        this.mCalendarsQueryHandler.getAll(CalendarModel.create());
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
