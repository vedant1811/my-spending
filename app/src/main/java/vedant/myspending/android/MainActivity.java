package vedant.myspending.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vedant.myspending.R;
import vedant.myspending.app.SmsReaderSingleton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmsReaderSingleton.getInstance().fetchExpenses(this)
                .subscribe();
    }
}
