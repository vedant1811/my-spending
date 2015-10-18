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
    private static final String FROM_CITIBANK = "VM-Citibk";
    private static SmsReaderSingleton ourInstance = new SmsReaderSingleton();

    public Observable<Sms> fetchSmses(Context context) {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"),
                COLUMN_NAMES,
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                Log.d(TAG, String.valueOf(parseSms(cursor)));
//                cursor.getCol
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            // empty box, no SMS
        }
        return null;
    }

    public static SmsReaderSingleton getInstance() {
        return ourInstance;
    }

    private static final String[] COLUMN_NAMES = {"_id", "address",  "date", "body"};

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
