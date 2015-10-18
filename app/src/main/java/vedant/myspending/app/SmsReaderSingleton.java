package vedant.myspending.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

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
                String msgData = "";
                for(int idx=0; idx<cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }
                // use msgData
                Log.d(TAG, msgData);
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

//    private Sms parseSms(Cursor cursor) {
//        return new Sms(
//                cursor
//        );
//    }

    private SmsReaderSingleton() {
    }
}
