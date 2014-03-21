package uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl;

import uk.ac.ncl.cs.group1.clientapi.callback.SessionStoreCallBack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 21/03/14
 */
public class SessionStoreCallBackImpl implements SessionStoreCallBack {
    @Override
    public void storeUUID(File idFile, UUID uuid,boolean finish) {
        try {
            RandomAccessFile file = new RandomAccessFile(idFile,"rw");
            if(finish){
                file.writeChars(uuid.toString()+","+finish+"\n");
            } else {
                file.writeChars(uuid.toString()+"\n");
            }

        } catch (Exception ignored) {

        }
    }

    @Override
    public List<UUID> getUnfinishedUUID(File id) {
        try {
            List<UUID> list = new ArrayList<>();
            BufferedReader buffered = new BufferedReader(new FileReader(id));
            String line;
            while ((line = buffered.readLine()) != null){
                String [] result = line.split(",");
                if(result.length == 2){
                    continue;
                }
                if(result.length == 1){
                    list.add(UUID.fromString(result[0]));
                }
            }
            return list;
        } catch (Exception ignored) {
        }
        return null;
    }
}
