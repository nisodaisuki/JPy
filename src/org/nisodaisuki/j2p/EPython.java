package org.nisodaisuki.j2p;

import org.nisodaisuki.log.Log;
import org.nisodaisuki.print.PRINT;
import org.nisodaisuki.shared.GShared;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EPython
{
    static private String _python2_path = "";
    static private String _python3_path = "";
    static ArrayList<String> _py_scrip_name = new ArrayList<>();

    public static void set_python2_path(String python2_path)
    {
        _python2_path = python2_path;
    }
    public static void set_python3_path(String python3_path)
    {
        _python2_path = python3_path;
    }

    //static String _json_file_path = "";

    // 执行命令，返回结果
    static int exec_unit(String[] cmd, String host)
    {
        int exc_number = -1;
        while (true)
        {
            try
            {
                Process process = Runtime.getRuntime().exec(cmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                ArrayList<String> log_tmp = new ArrayList<>();
                while ( (line = in.readLine()) != null)
                {
                    log_tmp.add(line + "\r\n");
                }
                in.close();
                int ret = process.waitFor();

                if (ret == GShared.ENV_EXCP && exc_number > -3)
                {
                    exc_number--;
                    continue;
                }
                Log.insert_log_pool(log_tmp);
                return ret;
            } catch (Exception e) {
                e.printStackTrace();
                return GShared.ENV_ERROR;
            }
        }
    }

    private static void exec_net(String host, String py_scrip_search_dir)
    {
        for (String file_name : _py_scrip_name)
        {
            // 解析cve文件名 CVE_2019_1201_P2
            // 不是以cve开头，不执行脚本
            String[] file_name_splits = file_name.split("_");
            if (!file_name_splits[0].toUpperCase().equals("CVE")) {
                continue;
            }

            String cve_name = file_name_splits[0] + "_" + file_name_splits[1] + "_" + file_name_splits[2];
            cve_name = cve_name.toUpperCase();

            String[] arguments;
            // 用python2 执行
            if (file_name_splits[3].toUpperCase().contains("P2")) {
                arguments = new String[]{
                        _python2_path,
                        GShared.program_dir+"/"+py_scrip_search_dir+"/"+file_name,
                        GShared.json_file_path,
                        host
                };
            }
            // 用python3 执行
            else if (file_name_splits[3].toUpperCase().contains("P3"))
            {
                arguments = new String[]{
                        _python3_path,
                        GShared.program_dir+"/"+py_scrip_search_dir+"/"+file_name,
                        GShared.json_file_path,
                        host
                };
            }
            else
            {
                System.out.println("[E] " + file_name + "文件名解析错误");
                continue;
            }
            Log.clear_log_pool();
            Log.insert_log_pool("\r\n[" + GShared.get_cur_time("HH:mm:ss") + "] " + GShared.config_map.get(cve_name) + "\r\n");
            PRINT.print_result(exec_unit(arguments, host), host, cve_name);
            Log.write_log(host);
        }
    }

    static public void exec_nets(String host_start, String host_end, String py_scrip_search_dir, ArrayList<String> py_scrip_name)
    {
        _py_scrip_name = py_scrip_name;

        if (host_end.equals("x")| host_end.equals("X"))
        // 单设备检查
        {
            exec_net(host_start, py_scrip_search_dir);
        }
        else
        //IP 段检查
        {
            String[] ips_start = host_start.split("\\.");
            String[] ips_end = host_end.split("\\.");
            int ip_start = Integer.valueOf(ips_start[3]);
            int ip_end = Integer.valueOf(ips_end[3]);
            for (; ip_start <= ip_end; ip_start++)
            {
                exec_net(ips_start[0]+"."+ips_start[1]+"."+ips_start[2]+"."+ip_start, py_scrip_search_dir);
            }
        }
    }


}
