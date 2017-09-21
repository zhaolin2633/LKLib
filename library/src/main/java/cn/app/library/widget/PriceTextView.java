package cn.app.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import cn.app.library.R;

/**
 * Created by Tiger on 2016-04-28.
 */
public class PriceTextView extends LinearLayout {

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private String symbol = "￥";
    private int symbolSize = 18;
    private int integerSize = 24;
    private int decimalSize = 18;
    private double price = 0;
    private int textColor = Color.BLACK;

    private TextView symbolTextView;
    private TextView integerTextView;
    private TextView decimalTextView;

    public PriceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PriceTextView(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);

        symbolTextView = new TextView(context);
        symbolTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, symbolSize);
        symbolTextView.setTextColor(textColor);

        integerTextView = new TextView(context);
        integerTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, integerSize);
        integerTextView.setTextColor(textColor);

        decimalTextView = new TextView(context);
        decimalTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, decimalSize);
        decimalTextView.setTextColor(textColor);

        addView(symbolTextView);
        addView(integerTextView);
        addView(decimalTextView);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PriceTextView);
            symbolSize = typedArray.getInt(R.styleable.PriceTextView_symbolSize, 18);
            integerSize = typedArray.getInt(R.styleable.PriceTextView_leftSize, 24);
            decimalSize = typedArray.getInt(R.styleable.PriceTextView_rightSize, 18);
            price = typedArray.getFloat(R.styleable.PriceTextView_price, 0);
            textColor = typedArray.getColor(R.styleable.PriceTextView_priceColor, Color.BLACK);

            symbolTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, symbolSize);
            symbolTextView.setTextColor(textColor);

            integerTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, integerSize);
            integerTextView.setTextColor(textColor);

            decimalTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, decimalSize);
            decimalTextView.setTextColor(textColor);

            setPrice(price);

            typedArray.recycle();
        }

    }

    public void setPrice(double price) {
        this.price = price;
        if (Double.isNaN(price)) {
            price = 0;
        }
        symbolTextView.setText(symbol);
        String priceString = decimalFormat.format(price);
        if (priceString.contains(".")) {
            int index = priceString.indexOf(".");
            integerTextView.setText(priceString.substring(0, index));
            decimalTextView.setText(priceString.substring(index));
        }
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
        symbolTextView.setText(symbol);
    }
}
