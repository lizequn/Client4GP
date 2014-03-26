package uk.ac.ncl.cs.group1.clientapi1;

/**
 * @Auther: Li Zequn
 * Date: 14/03/14
 */
public class TTPURL {
    public final static String base = "http://localhost:8080";
    //public final static String base = "http://groupproject-group1.elasticbeanstalk.com";
    public final static String registerUrl = base+"/register";
    public final static String getMyExchangeUrl = base+"/getmyexchange";
    public final static String phase1RequestUrl4Email = base+"/phase1/email";
    public final static String phase1RequestUrl4Normal = base+"/phase1/normal";
    public final static String phase5SigUrl= base+"/phase5";
    public final static String phase2Url = base+"/phase2";
    public final static String phase3Url = base+"/phase3";
    public final static String getPublicKeyUrl = base+"/getpublickey";
    public final static String senderResolveUrl = base+"/resolve/sender";
    public final static String receiverResolveUrl = base+"/resolve/receiver";
    public final static String abortUrl = base+"/abort";

}
