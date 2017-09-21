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

    public int code;

    private String msg;

    private ResponseData<T> response_data;


    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    /**
     * 连接服务器是否成功
     *
     * @return
     */
    public boolean isHttpSuccess() {
        return code == 200;
    }

    public ResponseData getResponse_data() {
        return response_data;
    }

    public class ResponseData<T> {

        private int code;

        private String msg;

        private T data;


        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public T getData() {
            return data;
        }

        /**
         * 连接接口是否成功，返回码为2开头则表示成功
         *
         * @return
         */
        public boolean isSuccess() {
            return String.valueOf(code).startsWith("2");
        }
    }
}
