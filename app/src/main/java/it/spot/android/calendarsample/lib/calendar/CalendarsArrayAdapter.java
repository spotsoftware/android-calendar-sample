package it.spot.android.calendarsample.lib.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.spot.android.calendarsample.R;

/**
 * @author a.rinaldi
 */
public class CalendarsArrayAdapter extends ArrayAdapter<CalendarModel> {

    private final int mCalendarLayoutId;
    private final LayoutInflater mLayoutInflater;

    // region Construction

    public CalendarsArrayAdapter(Context context, int resource) {
        this(context, resource, new ArrayList<CalendarModel>());
    }

    public CalendarsArrayAdapter(Context context, int resource, List<CalendarModel> objects) {
        super(context, resource, objects);

        this.mCalendarLayoutId = resource;
        this.mLayoutInflater = LayoutInflater.from(context);

        this.setNotifyOnChange(true);
    }

    // endregion

    // region ArrayAdapter implementation

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = this.mLayoutInflater.inflate(this.mCalendarLayoutId, parent, false);
        ((TextView) convertView.findViewById(R.id.name)).setText(this.getItem(position).getName());
        ((TextView) convertView.findViewById(R.id.account_name)).setText(this.getItem(position).getAccountName());

        return convertView;
    }

    // endregion
}
