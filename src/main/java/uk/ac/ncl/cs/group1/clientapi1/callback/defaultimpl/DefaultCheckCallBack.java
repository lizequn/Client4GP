package uk.ac.ncl.cs.group1.clientapi1.callback.defaultimpl;

import uk.ac.ncl.cs.group1.clientapi1.DocReceive;
import uk.ac.ncl.cs.group1.clientapi1.callback.CheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.*;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultReceiptCallBack;

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
            docReceive.getFileAndReceipt(uuid,new DefaultFileStore(new File("C:\\")),new uk.ac.ncl.cs.group1.clientapi1.callback.defaultimpl.DefaultReceiptCallBack(new File("C:\\")));
        }
    }
}
