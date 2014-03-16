package uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl;

import uk.ac.ncl.cs.group1.clientapi.callback.ReceiptCallBack;

import java.io.*;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
public class DefaultReceiptCallBack implements ReceiptCallBack {
    private final File path;
    public DefaultReceiptCallBack(File path){
        this.path = path;
    }
    @Override
    public void getReceipt(byte[] file, String name) {
        File file1 = new File(path,name);
        if(file1.exists()){
            throw new IllegalArgumentException("file already exist");
        }
        try {
            file1.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file1);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(file);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
