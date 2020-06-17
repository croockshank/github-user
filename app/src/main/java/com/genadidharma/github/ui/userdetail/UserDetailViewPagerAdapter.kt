package com.genadidharma.github.ui.userdetail

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.genadidharma.github.R
import com.genadidharma.github.ui.userfollowers.FollowersFragment
import com.genadidharma.github.ui.userfollowings.FollowingsFragment

class UserDetailViewPagerAdapter (private val mContext: Context, fm: FragmentManager, private val username: String?): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    @StringRes
    private  val tabTitles = intArrayOf(R.string.tab_followers, R.string.tab_following)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? =  null
        when(position){
            0 -> fragment = FollowersFragment().newInstance(username)
            1 -> fragment = FollowingsFragment().newInstance(username)
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }

    override fun getCount(): Int {
        return 2
    }

}