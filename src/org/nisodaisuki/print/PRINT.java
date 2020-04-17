package org.nisodaisuki.print;

import org.nisodaisuki.shared.GShared;

public class PRINT
{
    // 颜色的设置
    final static String COLOUR_BLUE = "\033[34;4m";
    final static String COLOUR_RED = "\033[31;4m";
    final static String COLOUR_GREEN = "\033[36;4m";
    final static String COLOUR_END = "\033[0m";

    // cev和中文tag对照
    //static Map<String, String> _config_map = new HashMap<>();

    static public void print_result(int result, String host, String cve_name)
    {
        String cur_time = GShared.get_cur_time("HH:mm:ss");
        if (result == GShared.ENV_NO_EXIST)
        {
            System.out.println(COLOUR_GREEN + "[-] [" + cur_time + "] INFO HOST:"+host+" 不存在" + GShared.config_map.get(cve_name.toUpperCase()) + COLOUR_END);
        }
        else if (result == GShared.ENV_EXIST)
        {
            System.out.println(COLOUR_RED + "[*] [" + cur_time + "] INFO HOST:"+host+" 存在"+ GShared.config_map.get(cve_name) +COLOUR_END);
        }
        else if (result == GShared.ENV_EXCP)
        {
            System.out.println("[?] [" + cur_time + "] INFO HOST:"+host+" 不存在"+ GShared.config_map.get(cve_name)+"环境");
        }
        else
        {
            System.out.println("[x] [" + cur_time + "] INFO HOST:"+host+"执行脚本["+ GShared.config_map.get(cve_name)+"]时发出错误");
        }
    }
}
