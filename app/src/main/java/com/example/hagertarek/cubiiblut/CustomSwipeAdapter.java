package com.example.hagertarek.cubiiblut;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chabbal.slidingdotsplash.SlidingSplashView;

/**
 * Created by HagEr TaReK on 30/08/2017.
 */
public class CustomSwipeAdapter extends PagerAdapter {
    public static int [] image_resources = {R.drawable.image1,R.drawable.image2};
    private Context ctx;
    private LayoutInflater layoutInflater;
    public CustomSwipeAdapter (Context ctx){
        this.ctx = ctx ;
    }
    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.image_view);
        SlidingSplashView slidingSplashView = (SlidingSplashView) item_view.findViewById(R.id.splash);
        imageView.setImageResource(image_resources[position]);
        container.addView(item_view);
        return item_view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }

}
