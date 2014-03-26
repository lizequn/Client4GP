package uk.ac.ncl.cs.group1.clientapi1.util;

import org.apache.commons.codec.binary.Base64;

/**
 * @author ZequnLi
 *         Date: 14-2-24
 */
public class Base64Coder {

    public static byte [] decode(String s) {
        return Base64.decodeBase64(s);
    }
    public static String encode(byte[] s) {
        return Base64.encodeBase64String(s);
    }
}
