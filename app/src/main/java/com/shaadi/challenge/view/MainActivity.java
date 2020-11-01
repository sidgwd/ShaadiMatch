package com.shaadi.challenge.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.shaadi.challenge.BR;
import com.shaadi.challenge.R;
import com.shaadi.challenge.Util.Utility;
import com.shaadi.challenge.database.DatabaseClient;
import com.shaadi.challenge.database.RoomHelper;
import com.shaadi.challenge.databinding.ActivityMainBinding;
import com.shaadi.challenge.retrofit.Helper.FetchData;
import com.shaadi.challenge.viewmodel.DataViewModel;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class MainActivity extends AppCompatActivity {
    private DataViewModel dataViewModel;
    private static MainActivity mainActivity;
    private Toolbar toolbar;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static synchronized MainActivity getInstance(){
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = bind();
        mainActivity =this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRecyclerView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        dataViewModel.tearDown();
    }

    private void fetchData(){
        try {

            if(Utility.isInternetConnected(MainActivity.this)){
                FetchData.callWebMethod(MainActivity.this,"10");
            }else {
                RoomHelper.databaseWriteExecutor.execute(() -> {
                    DatabaseClient dbClient= DatabaseClient.getInstance(MainActivity.this);
                    int dbCount = dbClient.getAppDatabase().UserDao().getUserCount();
                    if(dbCount>0){
                        populateData();
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utility.showPopup(MainActivity.this,"","Unable to fetch your match. please refresh!");
                            }
                        });
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View bind() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dataViewModel = new DataViewModel();
        binding.setViewModel(dataViewModel);
        return binding.getRoot();
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        Handler mHandler;
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                        .setPrimaryText("Refresh Your Matches")
                        .setSecondaryText("You can always refresh your matches by clicking here.")
                        .setAnimationInterpolator(new FastOutSlowInInterpolator())
                        .setMaxTextWidth(R.dimen.tap_target_menu_max_width).setCaptureTouchEventOnFocal(true)
                        .setCaptureTouchEventOutsidePrompt(false)
                        .setBackgroundColour(getResources().getColor(R.color.black))
                        .setFocalColour(getResources().getColor(R.color.focul_color1))
                        .setCaptureTouchEventOutsidePrompt(false)
                        .setTarget(toolbar.getChildAt(1));
                tapTargetPromptBuilder.setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fetchData();
                            }
                        },500);
                    }

                    @Override
                    public void onHidePromptComplete() {

                    }
                });

                tapTargetPromptBuilder.show();

            }
        }, 1000);
    }

    public void populateData(){
        dataViewModel.setUp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            try {
                fetchData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
