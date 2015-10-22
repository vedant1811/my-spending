package vedant.myspending.app;

import android.util.Log;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import vedant.myspending.app.models.Expense;
import vedant.myspending.app.models.Sms;

/**
 * Created by vedant on 18/10/15.
 */
public class SmsToExpenseTransformer implements Observable.Transformer<Sms, Expense> {
    private static final String TAG = "SmsToExpenseTransformer";

    private static final Pattern MONEY_PATTERN = Pattern.compile("(Rs )([\\d,.]+)");
    private static final Pattern SPENT_AT_PATTERN = Pattern.compile("(at )(.*)(.$)");

    @Override
    public Observable<Expense> call(Observable<Sms> smsObservable) {
        return smsObservable.map(sms -> {
            BigDecimal amount = null;
            Matcher moneyMatcher = MONEY_PATTERN.matcher(sms.body);
            if (moneyMatcher.find()) {
                String moneyString = moneyMatcher.group(2).replaceAll(",", "");
                amount = new BigDecimal(moneyString);
            }

            String spentOn = "";
            if (sms.from.toLowerCase().contains(SmsReaderSingleton.CITIBANK)) {
                Matcher spentAtMatcher = SPENT_AT_PATTERN.matcher(sms.body);
                if (spentAtMatcher.find()) {
                    spentOn = spentAtMatcher.group(2);
                    Log.d(TAG, "spentAtMatcher.group(2): " + spentOn);
//                    Log.d(TAG, "money: " + new BigDecimal(moneyString));
//                    amount = new BigDecimal(spentAt);
                } else {
                    Log.d(TAG, "no spentAtMatcher for " + sms.body);
                }
            }
            if (amount == null) {
                Log.e(TAG, "amount null for sms " + sms);
                return null;
            } else {
                return new Expense(sms.id, // it is unique anyway
                        spentOn,
                        sms.receivedAt,
                        amount);
            }
        });
    }
}
