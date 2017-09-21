package cn.app.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.app.library.R;


/**
 * @description： 自定义组合控件实现 数量加减控件
 */

public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private int min_amount = 0; //最低购买数量
    private int amount = 1; //当前购买数量
    private int stock = 0; //商品库存
    private ImageView mIvCountDelete;
    private ImageView mIvCountAdd;
    private EditText mEdProductCount;
    private boolean isInput = false;//是否可以手动输入数量  默认不能手动输入false
    private OnAmountChangeListener mListener;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initCustomAttrs(context, attrs);
    }


    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        mIvCountDelete = (ImageView) findViewById(R.id.product_count_delete);
        mEdProductCount = (EditText) findViewById(R.id.product_count);
        mIvCountAdd = (ImageView) findViewById(R.id.product_count_add);
        mIvCountDelete.setOnClickListener(this);
        mIvCountAdd.setOnClickListener(this);
        mEdProductCount.addTextChangedListener(this);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AmountView);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.AmountView_amount_textsize, sp2px(context, 16));
        isInput = typedArray.getBoolean(R.styleable.AmountView_amount_isInput, false);
        min_amount = typedArray.getInteger(R.styleable.AmountView_min_amonut, min_amount);
        typedArray.recycle();

        if (textSize != 0) {
            mEdProductCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        //设置是否可手动输入
        mEdProductCount.setEnabled(isInput);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.product_count_delete == id) {
            if (amount > min_amount) {
                amount--;
                mEdProductCount.setText(String.valueOf(amount));
            }

        } else if (R.id.product_count_add == id) {
            if (amount < stock) {
                amount++;
                mEdProductCount.setText(String.valueOf(amount));
            }
        }
        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
        mEdProductCount.clearFocus();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().isEmpty())
            return;
        amount = Integer.valueOf(charSequence.toString());
        if (amount > stock) {
            mEdProductCount.setText(String.valueOf(stock));
            mEdProductCount.setSelection(mEdProductCount.getText().toString().length());
            return;
        }
        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置最低购买数量
     *
     * @param min_amount
     */
    public void setMin_amount(int min_amount) {
        this.min_amount = min_amount;
    }

    /**
     * 设置库存数量
     *
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * 设置是否支持手动输入数量
     *
     * @param input
     */
    public void setInput(boolean input) {
        mEdProductCount.setEnabled(input);
    }

    public int getAmount() {
        return amount;
    }

    /**
     * 设置当前购买数量
     *
     * @param amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
        mEdProductCount.setText(String.valueOf(amount));
    }

    public int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public void setListener(OnAmountChangeListener listener) {
        mListener = listener;
    }

    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }
}
