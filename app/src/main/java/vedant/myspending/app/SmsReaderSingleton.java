package vedant.myspending.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Date;

import rx.Observable;
import vedant.myspending.app.models.Expense;
import vedant.myspending.app.models.Sms;

/**
 * Created by vedant on 18/10/15.
 */
public class SmsReaderSingleton {
    private static final String TAG = "SmsReaderSingleton";
    private static final String[] COLUMN_NAMES = {"_id", "address",  "date", "body"};

    // make sure all args are in lower case for any kind of matching
    public static final String CITIBANK = "citi";
    public static final String AXIS_BANK = "axis";
    private static SmsReaderSingleton ourInstance = new SmsReaderSingleton();

    public Observable<Sms> fetchSmses(Context context) {
        return Observable.<Sms>create(subscriber -> {
            Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"),
                    COLUMN_NAMES, // select
                    // TODO: make this automatic in terms of number of args
                    "LOWER(address) LIKE ? OR " +
                            "LOWER(address) LIKE ?", // where column address contains ?
                    new String[]{"%" + CITIBANK + "%", "%" + AXIS_BANK + "%"},
                    COLUMN_NAMES[2] + " DESC");
            if (cursor != null && cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    Sms sms = parseSms(cursor);
//                    Log.d(TAG, String.valueOf(sms));
                    if (!subscriber.isUnsubscribed())
                        subscriber.onNext(sms);
//                cursor.getCol
                } while (cursor.moveToNext());
                cursor.close();
            } else {
                // empty box, no SMS
                Log.e(TAG, "no matching smses");
            }
        });
//                .subscribeOn(Schedulers.io());
    }

    public Observable<Expense> fetchExpenses(Context context) {
        return fetchSmses(context)
                .compose(new SmsToExpenseTransformer());
    }

    public static SmsReaderSingleton getInstance() {
        return ourInstance;
    }

    private Sms parseSms(Cursor cursor) {
        return new Sms(
                cursor.getInt(0),
                cursor.getString(1),
                new Date(cursor.getInt(2)),
                cursor.getString(3)
        );
    }

    private SmsReaderSingleton() {
    }
}
