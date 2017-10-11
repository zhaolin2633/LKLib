package cn.app.library.utils;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @describe: textView 工具类
 */

public class TextViewUtils {

    /**
     * 设置文本
     *
     * @param context
     * @param textView
     * @param leftText   左侧文本
     * @param rightText  右侧文本
     * @param leftColor  左侧颜色
     * @param rightColor 右侧颜色
     * @param leftSize   左侧字体大小
     * @param rightSize  右侧字体大小
     */
    public static void setText(Context context, TextView textView, String leftText, String rightText, int leftColor, int rightColor, int leftSize, int rightSize) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(leftText + rightText);
        //设置左侧字体颜色
        ForegroundColorSpan leftColorSpan = new ForegroundColorSpan(context.getResources().getColor(leftColor));
        builder.setSpan(leftColorSpan, 0, leftText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置右侧字体颜色
        ForegroundColorSpan rightColorSpan = new ForegroundColorSpan(context.getResources().getColor(rightColor));
        builder.setSpan(rightColorSpan, leftText.length(), leftText.length() + rightText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置左侧字体大小
        AbsoluteSizeSpan leftSizeSpan = new AbsoluteSizeSpan(leftSize, true);
        builder.setSpan(leftSizeSpan, 0, leftText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置右侧字体大小
        AbsoluteSizeSpan rightSizeSpan = new AbsoluteSizeSpan(rightSize, true);//设置价格字体大小
        builder.setSpan(rightSizeSpan, leftText.length(), leftText.length() + rightText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(builder);
    }

    /**
     * 关键字高亮显示
     *
     * @param target    需要高亮的关键字
     * @param text      需要显示的文字
     * @param textColor 高亮显示的字体颜色
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target, int textColor) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableStringBuilder();
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        if (TextUtils.isEmpty(target)) {
            return spannable;
        }
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(textColor);
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;

        // 调用
        // SpannableStringBuilder textString = TextViewUtils.highlight(item.getItemName(), KnowledgeActivity.searchKey);
        // vHolder.tv_itemName_search.setText(textString);
    }

    /**
     * 设置View中间横线
     *
     * @param textView
     */
    public static void setTextViewMidlleLine(TextView textView) {
        if (textView == null)
            return;
        textView.setEllipsize(TextUtils.TruncateAt.END); // 收缩
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * 设置View底部横线
     *
     * @param textView
     */
    public static void setTextViewBottomLine(TextView textView) {
        if (textView == null)
            return;
        textView.setEllipsize(TextUtils.TruncateAt.END); // 收缩
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

}
