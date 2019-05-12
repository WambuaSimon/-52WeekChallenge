package com.wizag.a52weekssavingchallenge.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.wizag.a52weekssavingchallenge.R;
import com.wizag.a52weekssavingchallenge.model.Amount;
import com.wizag.a52weekssavingchallenge.ui.adapter.AmountAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.starting_amount)
    EditText starting_amount;

    private List<Amount> amountList = new ArrayList<>();
    AmountAdapter amountAdapter;
    List<String> amount;
    String starting_amount_txt;
    Disposable d2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("#52WeekChallenge");
        ButterKnife.bind(this);

        amount = new ArrayList<>();
        amountAdapter = new AmountAdapter(this, amountList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(amountAdapter);


        starting_amount_txt = starting_amount.getText().toString();
//        if (starting_amount_txt.isEmpty()) {
//            starting_amount.setError("Enter Starting Amount to proceed");
//        } else {
//            prepareMovieData();
//        }


        d2 = RxTextView.textChanges(starting_amount)
//                .filter(s -> s.toString().length() > 6)
//                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        amountList.clear();

                        for (int i = 1; i <= 52; i++) {

                            Amount movie = new Amount();

                            movie.setWeek(i);


                            /*calculate deposit*/
                            int result = 0;
                            String num = charSequence.toString();
                            if (!num.isEmpty()) {
                                result = Integer.parseInt(num) * i;
                            }
                            movie.setDeposit(String.valueOf(result));

                            /*Total amount*/
                            movie.setTotal(String.valueOf(result));
//                            if()

                            amountList.add(movie);

                            Log.d("ArraySize", String.valueOf(amountList.size()));
//
                            for (int n = 0; n < amountList.size(); n++) {

                                String nextDepo = amountList.get(n++).getDeposit();


                                Log.d("AmountValue", nextDepo);

                                int totalValue = Integer.parseInt(nextDepo + amountList.get(n).getTotal());
                                movie.setTotal(String.valueOf(totalValue));
                                amountList.add(movie);

                            }

//                            Toast.makeText(MainActivity.this, String.valueOf(amountList.indexOf(movie.getTotal())), Toast.LENGTH_LONG).show();


                        }

                        amountAdapter.notifyDataSetChanged();


                    }
                });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        d2.dispose();
    }
}
