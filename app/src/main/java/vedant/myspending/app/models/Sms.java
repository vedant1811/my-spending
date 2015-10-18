package vedant.myspending.app.models;

import java.util.Date;

/**
 * Created by vedant on 18/10/15.
 */
public class Sms {
    private static final String TAG = "Sms";

    public final int id;
    public final String from;
    public final Date receivedAt;
    public final String body;

    public Sms(int id, String from, Date receivedAt, String body) {
        this.id = id;
        this.from = from;
        this.receivedAt = receivedAt;
        this.body = body;
    }
}
