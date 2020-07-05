package com.example.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //ui
    private SearchView searchView;
    private Button btn;

    // vars
    private CompositeDisposable disposables = new CompositeDisposable();
    private long timeSinceLastRequest; // for log printouts only. Not part of logic.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.search);
        btn =  findViewById(R.id.button);

        timeSinceLastRequest = System.currentTimeMillis();

        // create the Observable
         Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                        // Listen for text input into the SearchView
//                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                            @Override
//                            public boolean onQueryTextSubmit(String query) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onQueryTextChange(final String newText) {
//                                if (!emitter.isDisposed()) {
//                                    emitter.onNext(newText); // Pass the query to the emitter
//                                }
//                                return false;
//                            }
//                        });

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                emitter.onNext("a");
                            }
                        });







                    }
                })
 //               .debounce(500, TimeUnit.MILLISECONDS) // Apply Debounce() operator to limit requests
        .buffer(4, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
.subscribe(new Observer <List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(List<String> s) {
                Log.d(TAG, "onNext: time  since last request: " + (System.currentTimeMillis() - timeSinceLastRequest));
                Log.d(TAG, "onNext: search query: " + s);
                timeSinceLastRequest = System.currentTimeMillis();

                // method for sending a request to the server
                sendRequestToServer(s);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    // Fake method for sending a request to the server
    private void sendRequestToServer(List<String> s) {
        Log.d(TAG, "sendRequestToServer: " + s.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear(); // clear disposables
    }
}