package com.project.util;

public class Constant {

	public static final String ORGNIZATION_TYPE_COMPANY = "2";
	public static final String ORGNIZATION_TYPE_OFFICE = "3";

	public static final String DATA_AVAILABLE_YES = "1";
	public static final String DATA_AVAILABLE_NO = "0";

	public static final String DATA_DISPLAY_SHOW = "1";
	public static final String DATA_DISPLAY_HIDE = "0";

	public static final String RESOURCE_TYPE_PARENT = "0";
	public static final String RESOURCE_TYPE_MENU = "1";
	public static final String RESOURCE_TYPE_BUTTON = "2";

	public static final String DIC_TYPE_CATALOG = "catalog";
	public static final String DIC_DEL_FLAG_YES = "1";
	public static final String DIC_DEL_FLAG_NO = "0";
	public static final String DIC_ROOT_ID = "0";
	public static final String CACHE_DICT_MAP = "dictMap";

	public static final String PRIVILEGE_AVAILABLE_YES = "1";
	public static final String PRIVILEGE_AVAILABLE_NO = "0";

	/**
	 * 行政区域根节点代码
	 */
	public static final String PARENT_TYPE_AREA = "0";
	public static final String ISEXPEND_AREA_NO = "0";
	public static final String ISEXPEND_AREA_FIRST = "1";
	public static final String ISEXPEND_AREA_ALL = "2";

	// 芜湖市
	public static final String AREA_LVL_4 = "4";
	// 区、县级
	public static final String AREA_LVL_3 = "3";
	// 街道级
	public static final String AREA_LVL_2 = "2";
	// 村级
	public static final String AREA_LVL_1 = "1";

	// 访客的角色
	public static final String ROLE_TYPE_VISITOR = "visitor";
	// 系统超户角色
	public static final String ROLE_TYPE_SUPPER = "supper";
	public static final String SUPPER_LOGIN_NAME = "supper";

	public static final String ROLE_TYPE_ADMIN = "admin";
	public static final String ROLE_TYPE_RECEPTION = "reception";
	public static final String ROLE_TYPE_OPERATOR = "operator";

	// 用户来源
	public static final String USER_ORIGN_OUTTER = "outter";
	public static final String USER_ORIGN_INNER = "inner";
	// CA访客默认账号
	public static final String VISITOR_LOGINNAME_DEFAULT = "visitor";
	public static final String VISITOR_PASSWORD_DEFAULT = "123456";

	public static final String PERM_TYPE_MENU = "menu";
	public static final String PERM_TYPE_BUTTON = "button";
	public static final String PERM_TYPE_URL = "url";

	// 惩戒渠道
	public static final String LHCJ_CHANNEL_YIQIWANG = "1";
	public static final String LHCJ_CHANNEL_YIZHANTONG = "2";
	public static final String LHCJ_CHANNEL_LHCJPINTTAI = "3";

	public static final String INTERFACE_URL_LHCJ = "/lhcj/";
	public static final String INTERFACE_URL_TJFX = "/lhcjtjfx/";

	// 南京全市
	public static final String STATION_SPOT_NJ = "NJ";
	// 迈皋桥监控点
	public static final String STATION_SPOT_1151A = "1151A";
	// 草场门监控点
	public static final String STATION_SPOT_1152A = "1152A";
	// 山西路监控点
	public static final String STATION_SPOT_1153A = "1153A";
	// 中华门监控点
	public static final String STATION_SPOT_1154A = "1154A";
	// 瑞金路监控点
	public static final String STATION_SPOT_1155A = "1155A";
	// 玄武湖监控点
	public static final String STATION_SPOT_1156A = "1156A";
	// 奥体中心监控点
	public static final String STATION_SPOT_1158A = "1158A";
	// 仙林大学城监控点
	public static final String STATION_SPOT_1159A = "1159A";
	// 浦口监控点
	public static final String STATION_SPOT_1157A = "1157A";

	// 今日
	public static final String THIS_DAY = "0";
	// 本周
	public static final String THIS_WEEK = "1";
	// 本月
	public static final String THIS_MONTH = "2";
	// 本年
	public static final String THIS_YEAR = "3";
	
    // 自定义分隔符
    public static final String SEPARATOR = "&&&";
    // 用户session
    public static final String SESSION_USER_KEY = "user";
    // 用户部门session
    public static final String SESSION_USER_ORG = "userOrgInfo";
    // 用户所有角色session
    public static final String SESSION_USER_ROLE = "userRoles";
    
    // PM10实时
 	public static final String PM10_ACTUAL = "1";
 	// PM1024小时
 	public static final String PM10_24HOUR = "2";
 	// PM2.5实时
 	public static final String PM25_ACTUAL = "3";
 	// Pm2.524小时
 	public static final String PM25_24HOUR = "4";
    // 噪声实时
  	public static final String ZS_ACTUAL = "5";
    // 噪声24小时
  	public static final String ZS_24HOUR = "6";
  	
  	// 扬尘
  	public static final String ALARM_TYPE_YC = "1";
 	// 噪声
 	public static final String ALARM_TYPE_ZS = "2";
 	
 	// 云台控制地址
 	public static final String VIDEO_CLOUD_URL = "http://218.2.221.251:8034";
 	// 云台控制地址(暂时HK3用)
  	public static final String VIDEO_CLOUD_NJZHGD_URL = "http://www.njzhgd.cn:84/interface/";
}
