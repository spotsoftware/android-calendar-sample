package it.spot.android.calendarsample.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.spot.android.calendarsample.R;

/**
 * @author a.rinaldi
 */
public class EventsArrayAdapter extends ArrayAdapter<EventModel> {

    private final int mCalendarLayoutId;
    private final LayoutInflater mLayoutInflater;

    // region Construction

    public EventsArrayAdapter(Context context, int resource) {
        this(context, resource, new ArrayList<EventModel>());
    }

    public EventsArrayAdapter(Context context, int resource, List<EventModel> objects) {
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
        ((TextView) convertView.findViewById(R.id.name)).setText(this.getItem(position).getTitle());

        SimpleDateFormat formatter = new SimpleDateFormat("d", Locale.getDefault());
        String from = formatter.format(this.getItem(position).getStartDate().getTime());
        String to = formatter.format(this.getItem(position).getEndDate().getTime());
        ((TextView) convertView.findViewById(R.id.duration)).setText(String.format(this.getContext().getString(R.string.event_duration), from, to));

        return convertView;
    }

    // endregion
}
