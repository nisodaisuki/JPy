package org.nisodaisuki.log;

import org.nisodaisuki.shared.GShared;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Log
{
    static private ArrayList<String> _log_tmp_pool = new ArrayList<>();

    static public void insert_log_pool(String log)
    {
        _log_tmp_pool.add(log);
    }
    static public void insert_log_pool(ArrayList<String> logs)
    {
        _log_tmp_pool.addAll(logs);
    }
    static public void clear_log_pool()
    {
        _log_tmp_pool.clear();
    }
    static public void write_log(String dest_file_name)
    {
        File log_file = new File(GShared.log_sublog_dir +"/"+dest_file_name);
        try
        {
            if (!log_file.exists())
            {
                log_file.createNewFile();
            }

            FileWriter resultFile = new FileWriter(log_file, true);
            for (String log : _log_tmp_pool)
            {
                resultFile.write(log);
            }
            resultFile.close();
        }
        catch (Exception e) {
            System.out.println("org.nisodaisuki.Log.write_log:新建文件操作出错");
            e.printStackTrace();
        }
    }

}
