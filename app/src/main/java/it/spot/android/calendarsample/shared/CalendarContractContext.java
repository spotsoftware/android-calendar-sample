package it.spot.android.calendarsample.shared;

import android.provider.CalendarContract;

/**
 * @author a.rinaldi
 */
public class CalendarContractContext {

    public static final String DEFAULT_ACCOUNT_NAME = "private";
    public static final String DEFAULT_ACCOUNT_TYPE = CalendarContract.ACCOUNT_TYPE_LOCAL;

    private String mAccountName;
    private String mAccountType;

    // region Construction

    protected CalendarContractContext(String accountName, String accountType) {
        super();
        this.mAccountName = accountName;
        this.mAccountType = accountType;
    }

    public static CalendarContractContext create(String accountName, String accountType) {
        return new CalendarContractContext(accountName, accountType);
    }

    public static CalendarContractContext createWithDefaults() {
        return new CalendarContractContext(DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_TYPE);
    }

    // endregion

    // region Public methods

    public String getAccountName() {
        return this.mAccountName;
    }

    public void setAccountName(String accountName) {
        this.mAccountName = accountName;
    }

    public String getAccountType() {
        return this.mAccountType;
    }

    public void setAccountType(String accountType) {
        this.mAccountType = accountType;
    }

    // endregion
}
