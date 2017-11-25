package cn.app.library.widget.pickertime.listener;


import cn.app.library.widget.pickertime.bean.DateBean;
import cn.app.library.widget.pickertime.bean.DateType;

/**
 * Created by codbking on 2016/9/22.
 */

public interface WheelPickerListener {
     void onSelect(DateType type, DateBean bean);
}
