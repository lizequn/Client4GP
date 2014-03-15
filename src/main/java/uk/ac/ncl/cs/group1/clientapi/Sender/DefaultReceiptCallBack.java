package uk.ac.ncl.cs.group1.clientapi.Sender;

import uk.ac.ncl.cs.group1.clientapi.callback.ReceiptCallBack;

import java.io.*;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
public class DefaultReceiptCallBack implements ReceiptCallBack {
    private String Path;
    private String fileName;
    @Override
    public void getReceipt(byte[] bytes) {
        File file = new File(Path+"\\"+fileName);
        if(file.exists()){
            throw new IllegalArgumentException("File already exist");
        }
        try {
            file.createNewFile();
            OutputStream stream = new FileOutputStream(file);
            stream.write(bytes);
            stream.flush();
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
