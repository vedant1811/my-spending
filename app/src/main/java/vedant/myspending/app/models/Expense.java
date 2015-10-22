package vedant.myspending.app.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by vedant on 18/10/15.
 */
public class Expense {
    private static final String TAG = "Sms";

    public final int id;
    public final String spentOn;
    public final Date at;
    public final BigDecimal amount;

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", spentOn='" + spentOn + '\'' +
                ", at=" + at +
                ", amount=" + amount +
                '}';
    }

    public Expense(int id, String spentOn, Date at, BigDecimal amount) {
        this.id = id;
        this.spentOn = spentOn;
        this.at = at;
        this.amount = amount;
    }
}
