package androidx.ui.util;

public class Phone {


    /**
     * 显示隐藏值电话号
     *
     * @param phone
     * @return
     */
    public static String secure(String phone) {
        return secure(phone, false, 0);
    }

    /**
     * 显示隐藏值电话号
     *
     * @param phone 手机号
     * @param space 空格
     * @param count 空格个数
     * @return
     */
    public static String secure(String phone, boolean space, int count) {
        int length = phone.length();
        if (length < 4) {
            return phone;
        }
        StringBuffer spaceBuffer = new StringBuffer();
        for (int i = 0; i < count; i++) {
            spaceBuffer.append(" ");
        }
        String start = phone.substring(0, 3);
        String end = phone.substring(phone.length() - 4, phone.length());
        StringBuffer sb = new StringBuffer();
        sb.append(start);
        if (space) {
            sb.append(spaceBuffer);
        }
        for (int i = 0; i < length - 7; i++) {
            sb.append("*");
        }
        if (space) {
            sb.append(spaceBuffer);
        }
        sb.append(end);
        return sb.toString();
    }


}
