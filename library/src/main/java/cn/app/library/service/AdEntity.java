package cn.app.library.service;

import java.io.Serializable;

/**
 * Author：xiaohaibin
 * Time：2017/3/31
 * Emil：xhb_199409@163.com
 * Github：https://github.com/xiaohaibin/
 * Describe：交易首页广告实体
 */

public class AdEntity implements Serializable {

    /**
     * type : 1
     * title : 网易音乐
     * pic : 533251-591188171b740
     * uri : http://music.163.com/#/discover/toplist?id=71384707
     * page_key : null
     * page_param : null
     * status : 1
     */
    private static final long serialVersionUID = 7382351359868556980L;//这里需要写死 序列化Id
    public int type; // 跳转类型  1 ：网页   2：原生页面跳转
    public String title = "";
    public String pic = "";
    public String uri = "";//网页地址
    public String page_key = "";// 原生跳转参数: 出售详情页：trade_supply_detail   求购详情页：trade_demand_detail   店铺详情页：trade_shop_detail
    public PageParam page_param;
    public int status;//广告状态

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getPic() {
        return pic;
    }

    public String getUri() {
        return uri;
    }

    public String getPage_key() {
        return page_key;
    }

    public PageParam getPage_param() {
        return page_param;
    }

    public int getStatus() {
        return status;
    }

    public class PageParam {
        private int id;

        public int getId() {
            return id;
        }
    }
}
