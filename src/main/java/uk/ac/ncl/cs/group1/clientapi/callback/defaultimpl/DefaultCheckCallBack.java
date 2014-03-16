package uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl;

import uk.ac.ncl.cs.group1.clientapi.DocReceive;
import uk.ac.ncl.cs.group1.clientapi.callback.CheckCallBack;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @author ZequnLi
 *         Date: 14-3-16
 */
public class DefaultCheckCallBack implements CheckCallBack {
    private final DocReceive docReceive;

    public DefaultCheckCallBack(DocReceive docReceive) {
        this.docReceive = docReceive;
    }

    @Override
    public void getUUID(List<UUID> lists) {
        for (UUID uuid:lists){
            docReceive.getFileAndReceipt(uuid,new DefaultFileStore(new File("C:\\")),new DefaultReceiptCallBack(new File("C:\\")));
        }
    }
}
