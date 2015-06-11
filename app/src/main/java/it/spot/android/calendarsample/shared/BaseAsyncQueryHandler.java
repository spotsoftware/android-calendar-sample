package it.spot.android.calendarsample.shared;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * @author a.rinaldi
 */
public class BaseAsyncQueryHandler<TModel extends ContentProviderEntityModel> extends AsyncQueryHandler {

    // region Construction

    public BaseAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    // endregion

    // region Public methods

    public void getAll(TModel model, String clause, String[] args) {
        this.startQuery(1, null, model.getContentProviderUri(), model.getProjection(), clause, args, null);
    }

    public void get(TModel model) {
        this.startQuery(1, null, model.getContentProviderUri(), model.getProjection(), "_id = ?", new String[]{String.valueOf(model.getId())}, null);
    }

    public void create(TModel model) {
        this.startInsert(1, null, model.getContentProviderUri(), model.fillContentValues());
    }

    public void edit(TModel model) {
        this.startUpdate(1, null, model.getContentProviderUri(), model.fillContentValues(), null, null);
    }

    public void delete(TModel model) {
        this.startDelete(1, null, model.getContentProviderUri(), "_id = ?", new String[]{String.valueOf(model.getId())});
    }

    // endregion
}
