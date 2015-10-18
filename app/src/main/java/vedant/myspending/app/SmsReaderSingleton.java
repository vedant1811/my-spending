package vedant.myspending.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Date;

import rx.Observable;
import vedant.myspending.app.models.Sms;

/**
 * Created by vedant on 18/10/15.
 */
public class SmsReaderSingleton {
    private static final String TAG = "SmsReaderSingleton";
    private static final String[] COLUMN_NAMES = {"_id", "address",  "date", "body"};

    private static final String FROM_CITIBANK = "citibk";
    private static SmsReaderSingleton ourInstance = new SmsReaderSingleton();

    public Observable<Sms> fetchSmses(Context context) {
        return Observable.<Sms>create(subscriber -> {
            Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"),
                    COLUMN_NAMES, // select
                    String.format("LOWER(%s) LIKE LOWER(?) ", COLUMN_NAMES[1]), // where column address contains ?
                    new String[] {"%" + FROM_CITIBANK + "%"},
                    COLUMN_NAMES[2] + " DESC");
            if (cursor != null && cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    Sms sms = parseSms(cursor);
                    Log.d(TAG, String.valueOf(sms));
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
