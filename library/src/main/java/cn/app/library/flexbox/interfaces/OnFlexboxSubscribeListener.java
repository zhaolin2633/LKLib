package cn.app.library.flexbox.interfaces;

import java.util.List;

/**
 * 作者：AS
 * 日期：2017/3/27.
 */
public interface OnFlexboxSubscribeListener<T> {

    /**
     * @param selectedItem 已选中的标签
     */
    void onSubscribe(List<T> selectedItem);
}
