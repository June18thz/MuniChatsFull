package com.example.englandhoang.munimuni;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by EnglandHoang on 11/30/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            /*case 0:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;*/
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 1:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            /*case 0:
                return "Thông báo";*/
            case 0:
                return "Trò chuyện";
            case 1:
                return "Bạn bè";
            default:
                return null;
        }
    }
}
