package com.example.negotiation.utils;

public class HexUtils {

    /**
     * Convert byte to hex string
     */
    public static String byteToHexString(byte src) {
        int v = src & 0xFF;
        String hv = Integer.toHexString(v);
        return hv;
    }

    /**
     * Convert byte[] to hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;

            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * Convert byte[] to hex int
     */
    public static int bytesToIntBig(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    /**
     * Convert byte[] to hex int
     */
    public static int bytesToIntSmall(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }
    /**
     * Convert byte[] to hex Smallshort
     */
    public static short bytesToIntShortSmall(byte[] src) {
        return (short) (((src[1] << 8) | src[0] & 0xff));
    }

    /**
     * Convert byte[] to hex Bigshort
     */
    public static short bytesToIntShortBig(byte[] src) {
        return (short) (((src[0] << 8) | src[1] & 0xff));
    }
    /**
     * Convert hex string to byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * Convert int to byte
     */
    public static byte[] IntToByteBig(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((num >> 24) & 0xff);
        bytes[1] = (byte) ((num >> 16) & 0xff);
        bytes[2] = (byte) ((num >> 8) & 0xff);
        bytes[3] = (byte) (num & 0xff);
        return bytes;
    }

    /**
     * Convert int to byte
     */
    public static byte[] IntToByteSmall(int num) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) ((num >> 24) & 0xff);
        bytes[2] = (byte) ((num >> 16) & 0xff);
        bytes[1] = (byte) ((num >> 8) & 0xff);
        bytes[0] = (byte) (num & 0xff);
        return bytes;
    }

    /**
     * Convert short to byte
     */
    public static byte[] shortToByte(short s) {
        byte[] bytes = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (bytes.length - 1 - i) * 8;
            bytes[1 - i] = (byte) ((s >>> offset) & 0xff);
        }
        return bytes;
    }


    public static boolean byteEquals(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) return false;
        if (b1 == null || b2 == null) return false;
        for (int i = 0; i < b1.length; i++)
            if (b1[i] != b2[i])
                return false;
        return true;
    }

}
