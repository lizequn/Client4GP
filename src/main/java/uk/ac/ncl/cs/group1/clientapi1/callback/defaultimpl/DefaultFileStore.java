package uk.ac.ncl.cs.group1.clientapi1.callback.defaultimpl;

import uk.ac.ncl.cs.group1.clientapi1.callback.FileStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author ZequnLi
 *         Date: 14-3-16
 */
public class DefaultFileStore implements FileStore {
    private final File path;
    public DefaultFileStore(File path){
        this.path = path;
    }
    @Override
    public void storeFile(byte[] file, String name) {
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
