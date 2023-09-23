package androidx.ui.util;

import java.io.ByteArrayOutputStream;

/**
 * 16进制工具
 */
public class Hex {

    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public Hex() {
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param data
     * @return
     */
    public static String encodeHex(byte[] data) {
        return encodeHex(data, 0);
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param data  字节数组
     * @param group 分组大小
     * @return
     */
    public static String encodeHex(byte[] data, int group) {
        int l = data.length;
        char[] out = new char[(l << 1) + (group > 0 ? l / group : 0)];
        int i = 0;
        for (int j = 0; i < l; ++i) {
            if (group > 0 && i % group == 0 && j > 0) {
                out[j++] = '-';
            }
            out[j++] = DIGITS[(240 & data[i]) >>> 4];
            out[j++] = DIGITS[15 & data[i]];
        }
        return new String(out);
    }

    /**
     * 十六进制字符串转换为字节数组
     *
     * @param hexString 十六进制字符串
     * @return
     */
    public static byte[] decodeHex(String hexString) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        for (int i = 0; i < hexString.length(); i += 2) {
            int b = Integer.parseInt(hexString.substring(i, i + 2), 16);
            bas.write(b);
        }
        return bas.toByteArray();
    }

}