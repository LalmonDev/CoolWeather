# CoolWeather
安卓天气预报（作业）--根据《第一行代码》第二版的酷欧天气进行改进

改进点：
	1.采取第三方天气API--详情参照：https://www.sojson.com/blog/305.html
    2.修改显示城市列表的方式，以达到适配第三方天气API返回的json数据
    3.增加搜索城市功能（根据输入的城市cityCode进行搜素，详情参见API网站文档）
    4.增加城市收藏功能（只收藏城市名字和城市id，点击收藏的城市后会根据城市id进行联网获取天气信息），城市列表可长按删除
    5.修改背景显示，不采用必应的每日一图为背景，采用了第三方API实现每刷新一次界面更换一次背景照片（该功能存在一定的时延，刷新后需等待几秒方可更换背景，有待改进）
    
待解决问题：
软件存在一定的缺陷，问题出在和API适配城市列表的解析json上，其中个别城市的解析和显示的不对应，还有就是澳门地区的下属地区加载不成功。

APP全程需要联网，期间只有一次SharedPreferences的缓存，用于在退出APP后重新进入的时候读取存储信息显示一个城市的天气情况。

By Lalmon
