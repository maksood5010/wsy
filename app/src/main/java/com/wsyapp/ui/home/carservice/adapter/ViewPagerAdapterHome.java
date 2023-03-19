package com.wsyapp.ui.home.carservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.wsyapp.R;

import java.util.List;


public class ViewPagerAdapterHome extends PagerAdapter {
    private WebView mWebview;
    private Context mContext;
    private List<Integer> setBannerList;

    public ViewPagerAdapterHome(Context mContext, List<Integer> bannerList) {
        this.mContext = mContext;
        this.setBannerList=bannerList;
    }

    @Override
    public int getCount() {

        return setBannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.fragment_app_pager, container, false);
        //int index = position%setBannerList.size();
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_img);
        try {

            Glide.with(mContext).load("").placeholder(setBannerList.get(position)).into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
