package com.ljl.note.collection.common.utils;

import com.ljl.note.collection.common.callback.DealFileCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class GZIPUtil {

    private static String defaultPath;
    private static Map<String,File> md5ToFileMap;

    static{
        md5ToFileMap = new HashMap<>();
        String os = System.getProperty("os.name").toLowerCase();
        if(os.startsWith("win")){
            defaultPath = "D:\\";
        }else{
            defaultPath = "/opt";
        }
    }

    private static void createDirectory(String outputDir,String subDir){
        File file = new File(outputDir);
        if(!(subDir == null || subDir.trim().equals(""))){//子目录不为空
            file = new File(outputDir + System.getProperty("file.separator") + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }


    private static void unCsvGz(File file, String outputDir) throws IOException {
        try {
            createDirectory(outputDir, null);
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream out = null;
            GZIPInputStream gis = new GZIPInputStream(fis);
            try {
                out = new FileOutputStream(outputDir + System.getProperty("file.separator") + file.getName().replace(".gz", ""));
                int count;
                byte data[] = new byte[2048];
                while ((count = gis.read(data)) != -1) {
                    out.write(data, 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    if (gis != null) {
                        gis.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File downloadFileByUrl(String urlStr,String suffix){
        File file;
        if(suffix == null || "".equals(suffix) ){
            file = new File(defaultPath+System.getProperty("file.separator")+new IdWorker().nextId()+".html");
        }else{
            file = new File(defaultPath+System.getProperty("file.separator")+new IdWorker().nextId()+suffix);
        }
        try {
            file.createNewFile();
            FileUtils.copyURLToFile(new URL(urlStr),file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }



    private static void getAndHandleAllFile(File parentPath, DealFileCallback<File> fileCallback){
        if(parentPath.isFile()) {
            if(fileCallback != null){
                fileCallback.callBack(parentPath);
            }
            return;
        }else{
            for(File f:parentPath.listFiles()){
                getAndHandleAllFile(f,fileCallback);
            }
        }
    }

    public static Boolean getVodLiveRecordCountByFileUrl(String fileUrl,DealFileCallback<File> dealFileCallback){
        Boolean result=false;
        File unGzFileDir = new File(defaultPath+System.getProperty("file.separator")+new IdWorker().nextId());
        File gzFile=null;
        try{
            gzFile = downloadFileByUrl(fileUrl,".csv.gz");
            unCsvGz(gzFile,unGzFileDir.getPath());
            getAndHandleAllFile(unGzFileDir,dealFileCallback);
            result=true;
        }catch (Exception e){
            System.out.printf("下载解压文件报错!");
        }finally {
            File[] files=unGzFileDir.listFiles();
            for(File item:files){
                item.delete();
            }
            gzFile.delete();
            unGzFileDir.delete();
            return result;
        }
    }
    public static void main(String[] args) throws IOException {
        new GZIPUtil().test1();
    }

    private void test1(){
        String urlStr="http://vodcq1251659802-10022853.cos.ap-chongqing.myqcloud.com/vodcq1251659802/1/playstat/20190821/1300029038_20190821.csv.gz?q-sign-algorithm=sha1&q-ak=AKIDIWe7AtI10PQkm8REDl4UO7I6myn6NDF7&q-sign-time=1566529628;1566572828&q-key-time=1566529628;1566572828&q-header-list=&q-url-param-list=&q-signature=8d04ef2d5c133ec06954aa2bb5ae2b5726ca404b";
        File unGzFileDir = new File(defaultPath+System.getProperty("file.separator")+new IdWorker().nextId());
        File gzFile = downloadFileByUrl(urlStr,".csv.gz");
        try{
            unCsvGz(gzFile,unGzFileDir.getPath());
            getAndHandleAllFile(unGzFileDir,file1 -> dealFile(file1));
        }catch (Exception e){
            System.out.printf("下载解压文件报错!");
        }finally {
            File[] files=unGzFileDir.listFiles();
            for(File item:files){
                item.delete();
            }
            gzFile.delete();
            unGzFileDir.delete();
        }
            //return flag;
    }

    private void test2(){
        File dirWithFile=new File("D:\\1164767564253204480");
        File dir=new File("D:\\1164767564253204481");
        File[] files=dirWithFile.listFiles();
        for(File file:files){
            file.delete();
        }
        System.out.println(dirWithFile.delete());
        System.out.println(dir.delete());
        System.out.println("");
    }

    private void dealFile(File file){
        System.out.println("处理文件");
        System.out.println(file.getAbsoluteFile());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());
        System.out.println(file.getTotalSpace());
        System.out.println("处理文件结束");

    }
}
