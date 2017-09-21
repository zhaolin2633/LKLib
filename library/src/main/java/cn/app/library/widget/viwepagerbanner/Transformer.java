package cn.app.library.widget.viwepagerbanner;

import android.support.v4.view.ViewPager.PageTransformer;

import cn.app.library.widget.viwepagerbanner.transformer.AccordionTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.BackgroundToForegroundTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.CubeInTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.CubeOutTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.DefaultTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.DepthPageTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.FlipHorizontalTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.FlipVerticalTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.ForegroundToBackgroundTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.RotateDownTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.RotateUpTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.ScaleInOutTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.StackTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.TabletTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.ZoomInTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.ZoomOutSlideTransformer;
import cn.app.library.widget.viwepagerbanner.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
