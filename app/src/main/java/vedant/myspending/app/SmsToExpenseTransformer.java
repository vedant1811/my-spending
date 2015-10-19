package vedant.myspending.app;

import rx.Observable;
import vedant.myspending.app.models.Expense;
import vedant.myspending.app.models.Sms;

/**
 * Created by vedant on 18/10/15.
 */
public class SmsToExpenseTransformer implements Observable.Transformer<Sms, Expense> {
    private static final String TAG = "SmsToExpenseTransformer";

    @Override
    public Observable<Expense> call(Observable<Sms> smsObservable) {
        return smsObservable.map(sms -> {
            
        });
    }
}
