package com.example.kirin.feedback_master;

/**
 * Created by kirin on 2016/10/18.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class FileHelper {

    private Context mContext;
    private File file = new File("");
    //private String path = getFilePathByName("Feedback.txt", );

    private String getFilePathByName(String seekFileName,File rootFile){
        List<File> files=parseFiles(rootFile);
        for (File file:files){
            if(file.getName().equals(seekFileName)){
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    private List<File> parseFiles(File file){
        List<File> listFiles=new ArrayList<>();
        File[] files = file.listFiles();
        for (File mf:files){
            if(mf.isDirectory()){
                listFiles.addAll(parseFiles(mf));
            }else{
                listFiles.add(mf);
            }

        }
        return listFiles;
    }
    public FileHelper() {
    }

    public FileHelper(Context mContext) {
        super();
        this.mContext = mContext;
    }


    public void save(String filename, String filecontent) throws Exception {
        FileOutputStream output = mContext.openFileOutput(filename, Context.MODE_APPEND);
        output.write(filecontent.getBytes());
        output.write('\n');
        output.close();
    }



    public String read(String filename) throws IOException {
        FileInputStream input = mContext.openFileInput(filename);
        byte[] temp = new byte[1024];
        StringBuilder sb = new StringBuilder("");
        int len = 0;
        while ((len = input.read(temp)) > 0) {
            sb.append(new String(temp, 0, len));
        }
        input.close();
        return sb.toString();
    }


    public void delete(String filename, String filecontent)throws IOException {
    FileOutputStream output = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
        output.write(filecontent.getBytes());
        output.close();
    }
}
