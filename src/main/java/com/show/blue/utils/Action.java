package com.show.blue.utils;

/**
 * 意图标识集合
 */
public interface Action {

    int INTENT_UPGRADE = 301;// 版本升级
    int INTENT_SUGGESTION = 302;// 意见反馈

    int LISTVIEW_DATA_MORE = 0x001;
    int LISTVIEW_ACTION_INIT = 0x002;
    int LISTVIEW_ACTION_REFRESH = 0x003;
    int LISTVIEW_ACTION_CHANGE_CATALOG = 0x004;
    int LISTVIEW_ACTION_SCROLL = 0x005;
    int LISTVIEW_DATA_EMPTY = 0x006;
    int LISTVIEW_DATA_FULL = 0x007;

    String APP_LOGIN = "XSW_APP_LOGIN";

    int SYS_TYPE = 0;
    int Chat_TYPY = 1;

    int MSG_Loaction = 0x008;
}
