package com.dryfire.sold.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dryfire.sold.Adapter.SlideStatePager;
import com.dryfire.sold.Fragments.AddProductFragment;
import com.dryfire.sold.Fragments.BidFragment;
import com.dryfire.sold.Fragments.PaymentFragment;
import com.dryfire.sold.Fragments.ProfileFragment;
import com.dryfire.sold.Fragments.SignupFragment;
import com.dryfire.sold.R;

import java.util.ArrayList;
import java.util.List;

public class SoldHowtoUse extends AppCompatActivity {

    private ViewPager pager;
    private SlideStatePager adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_use_sold);

        List<Fragment> list = new ArrayList<>();
        list.add(new SignupFragment());
        list.add(new AddProductFragment());
        list.add(new BidFragment());
        list.add(new PaymentFragment());
        list.add(new ProfileFragment());


        pager = findViewById(R.id.sold_viewPager);
        adapter = new SlideStatePager(getSupportFragmentManager(),list);
        pager.setAdapter(adapter);

    }
}
