package sample.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sample.model.ProdSalesRankingBo;

/**
 * description: <一句话功能简述>
 *
 * @author Chenz
 * @date 2020/6/15
 */
public class UsbTest2 {

    /**
     * 获取设备信息(连接状态、序列号、产品名、型号、名称、系统版本号)
     *        * @return 
     *        * @throws Exception
     *       
     */
    public static List<ProdSalesRankingBo> getAllDevices(int tag) throws Exception {
        List<ProdSalesRankingBo> map;
        ArrayList<String> list;
        Process process;
        BufferedReader reader;
        String line;
        String device_tpye;
        try {
            list = new ArrayList<>();
            map = new ArrayList<>();
            //设置adb.exe存放路径，如果设置了环境变量，直接输入adb即可
            String adb_path = "adb";
            //执行adb device操作，查看pc当前连接手机或模拟器设备列表
            //注意：一定要先配置好sdk环境变量，否则无法直接执行adb命令
            process = excuteShell(adb_path + " devices -l");
            if (process != null) {
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    if (line.length() > 1) {
                        list.add(line);
                    }
                }
                if (!list.contains("* daemon started successfully *")) {
                    if (list.size() > 1) {
                        if (!list.contains("device")) {
                            for (int i = 1; i < list.size(); i++) {
                                for (int j = 0; j < list.get(i).split(" ").length; j++) {
                                    //获取手机设备连接状态，目前状态有：device(正常)、offline、unauthorized
                                    device_tpye = list.get(i).split(" ")[j];
                                    //判断当前设备状态是否正常
                                    if (device_tpye.equals("device")) {
                                        //获取设备序列号
                                        String device_sn = list.get(i).split(" ")[0];
                                        //获取设备产品名
                                        String device_product = list.get(i).split(" ")[8];
                                        //获取设备型号
                                        String device_model = list.get(i).split(" ")[9];
                                        //获取设备名称
                                        String device_name = list.get(i).split(" ")[10];
                                        System.out.println("当前设备序列号:" + device_sn);
                                        System.out.println("当前设备产品名:" + device_product);
                                        System.out.println("当前设备型号:" + device_model);
                                        System.out.println("当前设备名称:" + device_name);
                                        ProdSalesRankingBo prodSalesRankingBo = new ProdSalesRankingBo();
                                        prodSalesRankingBo.setDevice_sn(device_sn);
                                        prodSalesRankingBo.setDevice_product(device_product);
                                        prodSalesRankingBo.setDevice_model(device_model);
                                        prodSalesRankingBo.setDevice_name(device_name);
                                        map.add(prodSalesRankingBo);
                                        if (tag == 1) {
                                            // 1 公众号
                                            excuteShellTag(device_sn, "ExampleInstrumentedTest");
                                        } else if (tag == 2) {
                                            // 2搜索信息
                                            excuteShellTag(device_sn, "ExampleInstrumentedTest");
                                        } else if (tag == 3) {
                                            // 3搜索群发送素材
                                            excuteShellTag(device_sn, "SearchGroupTest");
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println("当前设备列表中，没有device类型连接设备，请检查！");
                        }
                    } else {
                        System.out.println("当前设备列表没有连接的设备，请检查！");
                    }
                } else {
                    getAllDevices(tag);
                }
            } else {
                System.out.println("当前执行adb命令异常，请检查adb环境！");
            }
        } catch (IOException e) {
            System.err.println("IOException" + e.getMessage());
            return null;
        }
        return map;
    }

    /**
     *   * 执行adb命令
     *   * 
     *   * @param s 要执行的命令  参数
     *   * 
     *  
     */
    public static Process excuteShell(String s) {
        Process proc;
        Runtime runtime = Runtime.getRuntime();
        try {
            proc = runtime.exec(s);
        } catch (Exception e) {
            System.out.print("执行命令:" + s + "出错啦！");
            return null;
        }
        return proc;
    }

    /**
     *   * 通过设备ID执行adb命令
     *   * adb -s ac377077 shell am instrument -w -r -e debug false -e class 'sample.test.ExampleInstrumentedTest' sample.test/androidx.test.runner.AndroidJUnitRunner
     *  
     */
    public static Process excuteShellTag(String s, String text) {
        Process proc;
        Runtime runtime = Runtime.getRuntime();
        try {
            String shellString = "adb -s " + s + " shell am instrument -w -r -e debug false -e class 'sample.test." + text + "' sample.test/androidx.test.runner.AndroidJUnitRunner";
            proc = runtime.exec(shellString);
            System.out.print("执行命令:" + shellString.toString());
        } catch (Exception e) {
            System.out.print("执行命令:" + s + "出错啦！");
            return null;
        }
        return proc;
    }


    public static void main(String[] args) throws Exception {
        getAllDevices(1);
    }
}
