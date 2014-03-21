package uk.ac.ncl.cs.group1.clientapi;

import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultCheckCallBack;
import uk.ac.ncl.cs.group1.clientapi.callback.defaultimpl.DefaultReceiptCallBack;
import uk.ac.ncl.cs.group1.clientapi.core.DocReceiveImpl;
import uk.ac.ncl.cs.group1.clientapi.core.DocSenderImpl;
import uk.ac.ncl.cs.group1.clientapi.core.KeyPairStore;
import uk.ac.ncl.cs.group1.clientapi.core.RegisterImpl;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

/**
 * @Auther: Li Zequn
 * Date: 21/03/14
 */
public class CommandTransfer {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String [] args) throws IOException {
        if(args.length != 1) {
            System.out.println("specify sender or receiver");
            System.exit(-1);
        }
        String myId;
        String otherId;
        String filePath;
        String receiptPath;
        if(args[0].equals("sender")){
            println("work as sender");
            println("input your id ");
            myId = scanner.nextLine();
            println("check if key file exists(name should be "+myId+".puk and"+myId+".pik)");
            KeyPairStore keyPairStore;
            try{
                 keyPairStore = KeyPairStore.getFromFile(myId,new File(myId+".puk"),new File(myId+".pik"));
                println("get key file");
            } catch (IllegalArgumentException e){
                println("key file not exist register new user");
                Register register = new RegisterImpl();
                keyPairStore = register.register(myId);
                keyPairStore.store2File(new File(myId),new File(myId+".puk"),new File(myId+".pik"));
                println("register success and store key pair");
            }
            DocSender docSender = new DocSenderImpl(keyPairStore);
            println("please input the receiver id");
            otherId = scanner.nextLine();
            File file;
            do{
                println("please input the file path");
                filePath = scanner.nextLine();
                file = new File(filePath);
            }while (!file.exists());
            File receipt;
            do{
                println("please input the receiptPath path");
                receiptPath = scanner.nextLine();
                receipt = new File(receiptPath);
            }while (!receipt.exists());
            println("begin send doc");
            UUID uuid = docSender.sendDoc(file,otherId);
            println("send doc finished");
            println("begin receive receipt");
            docSender.receiveReceipt(1000,1000,uuid,new DefaultReceiptCallBack(receipt));

        }else if(args[0].equals("receiver")){
            println("work as receiver");
            println("input your id ");
            otherId = scanner.nextLine();
            println("check if key file exists(name should be "+otherId+".puk and"+otherId+".pik)");
            KeyPairStore keyPairStore;
            try{
                keyPairStore = KeyPairStore.getFromFile(otherId,new File(otherId+".puk"),new File(otherId+".pik"));
                println("get key file");
            } catch (IllegalArgumentException e){
                println("key file not exist register new user");
                Register register = new RegisterImpl();
                keyPairStore = register.register(otherId);
                keyPairStore.store2File(new File(otherId),new File(otherId+".puk"),new File(otherId+".pik"));
                println("register success and store key pair");
            }
            DocReceive receive = new DocReceiveImpl(keyPairStore);
            println("input how much times it request");
            String str = scanner.nextLine();
            int t = Integer.parseInt(str);
            println("begin receive");
            receive.asyCheckExistCommunication(new DefaultCheckCallBack(receive),1000,t);
        } else {
            System.out.println("specify sender or receiver");
            System.exit(-1);
        }
    }
    private static void println(String mes){
        System.out.println(mes);
    }

}
