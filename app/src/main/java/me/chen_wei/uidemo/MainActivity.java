package me.chen_wei.uidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.chen_wei.uidemo.coordinator.CoordinatorLayoutActivity;
import me.chen_wei.uidemo.scroll.MultiScrollActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.item_slide_view)
    public void onClickSlideView(View view) {
        startActivity(new Intent(MainActivity.this, MultiScrollActivity.class));
    }

    @OnClick(R.id.item_coordinator_layout)
    public void onClickCoordinatorLayout(View view) {
        startActivity(new Intent(MainActivity.this, CoordinatorLayoutActivity.class));
    }
}
