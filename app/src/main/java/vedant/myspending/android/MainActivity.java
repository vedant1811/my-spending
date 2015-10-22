package vedant.myspending.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import vedant.myspending.R;
import vedant.myspending.app.SmsReaderSingleton;
import vedant.myspending.app.models.Expense;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Bind(R.id.listView) ListView mListView;

    ExpensesAdapter mListViewAdapter;
    BigDecimal mTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mListViewAdapter = new ExpensesAdapter(this);
        mListView.setAdapter(mListViewAdapter);

        mTotal = new BigDecimal(0);
        SmsReaderSingleton.getInstance().fetchExpenses(this)
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        expense -> {
                            if (expense != null) {
                                mListViewAdapter.add(expense);
                                Log.d(TAG, "Adding expense " + expense);
                                mTotal = mTotal.add(expense.amount);
                            } else {
                                Log.d(TAG, "NULL expense");
                            }

                        },
                        throwable -> Log.e(TAG, "", throwable),
                        () -> addMonthTotal("October", mTotal));
    }

    public void addMonthTotal(String month, BigDecimal total) {
        View view = getLayoutInflater().inflate(R.layout.list_item_header, mListView, false);
        HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
        headerViewHolder.month.setText(month);
        headerViewHolder.amount.setText("₹ " + NumberFormat.getInstance().format(total));
        mListView.addHeaderView(view);
    }

    class ExpensesAdapter extends ArrayAdapter<Expense> {
        LayoutInflater mInflater;

        public ExpensesAdapter(Context context) {
            super(context, -1);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_expense, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Expense expense = getItem(position);
            viewHolder.spentAt.setText(expense.spentOn);
//                viewHolder.amount.setText(String.format("₹ ",  ));
            viewHolder.amount.setText("₹ " + NumberFormat.getInstance().format(expense.amount));
            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.spentAt) TextView spentAt;
            @Bind(R.id.amount) TextView amount;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    class HeaderViewHolder {
        @Bind(R.id.month) TextView month;
        @Bind(R.id.amount) TextView amount;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
