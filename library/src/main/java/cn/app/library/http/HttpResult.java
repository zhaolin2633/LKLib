package cn.app.library.http;

import java.io.Serializable;

/**
 * Author: Mr.xiao on 2017/3/15
 *
 * @mail:xhb_199409@163.com
 * @github:https://github.com/xiaohaibin
 * @describe: 网络请求的实体基类
 */
public class HttpResult<T> implements Serializable {

    public int status;

    private String info;

    private T data;


    public String getMsg() {
        return info;
    }

    public int getCode() {
        return status;
    }

    /**
     * 连接服务器是否成功
     *
     * @return
     */
    public boolean isHttpSuccess() {
        return status == 1;
    }

    public T getData() {
        return data;
    }
}
