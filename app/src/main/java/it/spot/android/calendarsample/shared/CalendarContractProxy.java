package it.spot.android.calendarsample.shared;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.provider.CalendarContract;

/**
 * @author a.rinaldi
 */
public class CalendarContractProxy<TModel extends ContentProviderEntityModel> extends AsyncQueryHandler {

    protected final CalendarContractContext mContext;

    // region Construction

    public CalendarContractProxy(ContentResolver cr) {
        this(cr, CalendarContractContext.createWithDefaults());
    }

    public CalendarContractProxy(ContentResolver cr, CalendarContractContext context) {
        super(cr);
        this.mContext = context;
    }

    // endregion

    // region Public methods

    public void getAll(TModel model, String clause, String[] args) {
        this.startQuery(1, null, model.getEntityUri(), model.getProjection(), clause, args, null);
    }

    public void get(TModel model) {
        this.startQuery(1, null, model.getEntityUri(), model.getProjection(), "_id = ?", new String[]{String.valueOf(model.getId())}, null);
    }

    public void create(TModel model) {
        this.startInsert(1, null, model.getEntityUri(), model.fillContentValues());
    }

    public void edit(TModel model) {
        this.startUpdate(1, null, model.getEntityUri(), model.fillContentValues(), null, null);
    }

    public void delete(TModel model) {
        this.startDelete(1, null, model.getEntityUri().buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, this.mContext.getAccountName())
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, this.mContext.getAccountType())
                .build(), "_id = ?", new String[]{String.valueOf(model.getId())});
    }

    public CalendarContractContext getProxyContext() {
        return this.mContext;
    }

    // endregion
}
