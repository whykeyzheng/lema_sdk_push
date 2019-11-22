package com.rance.chatui.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.lema.imsdk.client.LMClient;
import com.rance.chatui.R;
import com.rance.chatui.adapter.TestFragmentAdapter;
import com.rance.chatui.widget.MainBottomTabLayout;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LMClient.logout(); //退出登录
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private TestFragmentAdapter mAdapter;
        private ViewPager mPager;
        private MainBottomTabLayout mTabLayout;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            setupViews(rootView);
            return rootView;
        }

        private void setupViews(View view) {
            mAdapter = new TestFragmentAdapter(getFragmentManager());
            mPager = (ViewPager) view.findViewById(R.id.tab_pager);
            mPager.setAdapter(mAdapter);


            mTabLayout = (MainBottomTabLayout) view.findViewById(R.id.main_bottom_tablayout);
            mTabLayout.setViewPager(mPager);
        }
    }
}
