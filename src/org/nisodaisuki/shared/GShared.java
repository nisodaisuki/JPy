package org.nisodaisuki.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GShared
{
    //Json文件的定义
    public static String json_file_path = "";

    // 程序所在的路径
    public static String program_dir = "";
    public static String log_file_dir = "";
    public static String log_sublog_dir = "";

    // cev和中文tag对照
    public static Map<String, String> config_map = new HashMap<>();

    // 程序config的文件名
    final static public String program_config_file = "config";

    // python脚本执行后返回的结果状态
    final static public int ENV_EXIST = 0;
    final static public int ENV_NO_EXIST = 1;
    final static public int ENV_EXCP = 255;
    final static public int ENV_ERROR = 2;


    public static void program_init()
    {
        program_dir =  new File("").getAbsolutePath();
        log_file_dir = program_dir + "/logs";

        // 检查logs这个文件夹是否存中
        File log_dir = new File(log_file_dir);
        if (!log_dir.exists())
        {
            log_dir.mkdir();
        }

        // 创建本次运行的日志文件夹
        log_sublog_dir = log_file_dir + "/"+ get_cur_time("HH_mm_ss");
        File cur_log_excp = new File(log_sublog_dir);
        cur_log_excp.mkdir();

        init_cev_map();
    }

    //获取当前时间
    public static String get_cur_time(String format)
    {
        SimpleDateFormat data_format = new SimpleDateFormat(format);//设置日期格式
        return data_format.format(new Date());
    }

    private static void init_cev_map()
    {
        try
        {
            File file = new File(program_dir+"/config");

            if (file.isFile() && file.exists())
            // 判断文件是否存在
            {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), "UTF-8");// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;

                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    String[] strings = lineTxt.split("==");
                    if (strings.length == 2)
                    {
                        config_map.put(strings[0].trim().toUpperCase(), strings[1].trim());
                    }
                }
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("找不到[config]的文件");
            }
        }
        catch (Exception e)
        {
            System.out.println("读取[config]内容出错");
            e.printStackTrace();
        }
    }




}
