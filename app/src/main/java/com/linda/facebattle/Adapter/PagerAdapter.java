package com.linda.facebattle.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.linda.facebattle.Activity.MainActivity;
import com.linda.facebattle.Fragment.HistoryFragment;
import com.linda.facebattle.Fragment.MeFragment;
import com.linda.facebattle.Fragment.PublicFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private final int COUNT=3;
    private Fragment fg1,fg2,fg3;

    public PagerAdapter(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                if(fg1==null)
                {
                    fg1 = new PublicFragment();
                }
                return fg1;
            case 1:
                if(fg2==null)
                {
                    fg2 = new HistoryFragment();
                }
               return  fg2;
            case 2:
                if(fg3==null) {
                       fg3 = new MeFragment();
                   }
                return fg3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
          return  null;
    }



}
