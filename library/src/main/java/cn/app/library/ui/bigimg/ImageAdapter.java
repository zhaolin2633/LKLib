package cn.app.library.ui.bigimg;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.app.library.R;
import cn.app.library.utils.ImageUtils;
import cn.app.library.widget.glideimageview.GlideImageLoader;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Describe：详情页查看大图适配器
 */
public class ImageAdapter extends PagerAdapter {

    private ArrayList<String> imageList;
    private Context mContext;
    private onImageLayoutOnClickListener mOnClickListener;
    private boolean isWarpImgUrl;

    public ImageAdapter(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        mContext = context;
    }

    public ImageAdapter(ArrayList<String> imageList, Context context, boolean isWarpImgUrl) {
        this.imageList = imageList;
        this.isWarpImgUrl = isWarpImgUrl;
        mContext = context;
    }

    @Override
    public int getCount() {
        return imageList != null ? imageList.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        View imageLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_big_image, null);
        viewGroup.addView(imageLayout);
        assert imageLayout != null;
        PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.iv_product_big_image);
        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (mOnClickListener != null) {
                    mOnClickListener.OnImageOnClik();
                }
            }
        });
        Picasso.with(mContext).load(isWarpImgUrl ? imageList.get(position) : ImageUtils.getBigImageUrl(imageList.get(position))).into(imageView);
        return imageLayout;
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int arg1, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    public void setOnClickListener(onImageLayoutOnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface onImageLayoutOnClickListener {
        void OnImageOnClik();
    }
}
