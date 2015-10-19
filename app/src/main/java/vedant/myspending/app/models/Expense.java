package vedant.myspending.app.models;

import java.util.Date;

/**
 * Created by vedant on 18/10/15.
 */
public class Expense {
    private static final String TAG = "Sms";

    public final int id;
    public final String spentOn;
    public final Date at;
    public final int amount;

    public Expense(int id, String spentOn, Date at, int amount) {
        this.id = id;
        this.spentOn = spentOn;
        this.at = at;
        this.amount = amount;
    }
}
