package cn.app.library.ui.web.html_textview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.InputStream;

import cn.app.library.utils.ScreenUtil;


/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class UrlImageGetter implements Html.ImageGetter {
    private TextView mTextView;

    public UrlImageGetter(TextView textView) {
        mTextView = textView;
    }

    @Override
    public Drawable getDrawable(final String source) {
        final HtmlTextView.URLDrawable urlDrawable = new HtmlTextView.URLDrawable();
        //暂不支持GIF，之后会添加
        ImageLoader.getInstance().loadImage(source,  new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                int tvWidth = mTextView.getMeasuredWidth();
                int width = tvWidth - ScreenUtil.dip2px(mTextView.getContext(), 12);
                int height = tvWidth * loadedImage.getHeight() / loadedImage.getWidth()- ScreenUtil.dip2px(mTextView.getContext(), 12);
                BitmapDrawable drawable = new BitmapDrawable(loadedImage);
                drawable.setBounds(0, 0, width, height);
                urlDrawable.setBounds(0, 0, width, height);
                urlDrawable.setDrawable(drawable);
                mTextView.setText(mTextView.getText());
            }
        });
        return urlDrawable;
    }

    public int[] getImageWidthHeight(InputStream inputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options); // 此时返回的bitmap为null
        return new int[]{options.outWidth, options.outHeight};
    }

}
