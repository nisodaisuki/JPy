import org.nisodaisuki.j2p.EPython;
import org.nisodaisuki.shared.GShared;

import java.io.*;
import java.util.ArrayList;

public class main
{
    //如果知识分析一个ip。参数个数为4
    final static int _args_number_net = 4;

    //python路径的设置
    final static String PYTHON2 = "python2 == ";
    final static String PYTHON3 = "python3 == ";

    // 当前路径下文件的集合
    static ArrayList<String> _files_names = new ArrayList<>();

    final static int CMD_JSON_IDX = 0;
    final static int CMD_IPSTART_IDX = 1;
    final static int CMD_IPEND_IDX = 2;
    final static int CMD_DIRS_IDX = 3;


    static void set_python_path()
    {
        try
        {
            File file = new File(GShared.program_config_file);

            if (file.isFile() && file.exists())
            // 判断文件是否存在
            {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                lineTxt = bufferedReader.readLine();
                if (lineTxt.contains(PYTHON2))
                    EPython.set_python2_path(lineTxt.substring(PYTHON2.length()));
                else
                    System.out.println("未设置python2路径或设置错误。");

                lineTxt = bufferedReader.readLine();
                if (lineTxt.contains(PYTHON3))
                    EPython.set_python3_path(lineTxt.substring(PYTHON3.length()));
                else
                    System.out.println("未设置python3路径或设置错误。");
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("找不到指定的文件");
            }
        }
        catch (Exception e)
        {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }


    static ArrayList<String> get_file_names(String search_path)
    {
        ArrayList<String> file_names = new ArrayList<>();
        File file = new File(search_path);

        if (file.exists())
        {
            if (null == file.listFiles()) {
                return file_names;
            }

            String[] files = file.list();
            for(String file_name:files)
            {
                if (file_name.toUpperCase().startsWith("CVE_"))
                    file_names.add(file_name);
            }
        }
        else
        {
            System.out.println("文件不存在!");
        }
        return file_names;
    }

    // java json start end  dir
    public static void main(String[] args)
    {
        // 初始化一些参数
        GShared.program_init();

        // 读取配置文件，获取python路径
        set_python_path();

        if (args.length < _args_number_net )
        {
            System.out.print("Usage:");
            System.out.println("<json dir> <ip start> <ip end> <search dirs>");
            return;
        }

        // json 文件路径
        GShared.json_file_path = args[CMD_JSON_IDX];

        // 搜索文件夹
        String work_dir = args[CMD_DIRS_IDX];

        // 得到工作路径下所有的文件
        _files_names = get_file_names(work_dir);

        EPython.exec_nets(args[CMD_IPSTART_IDX], args[CMD_IPEND_IDX], work_dir, _files_names);
    }
}
