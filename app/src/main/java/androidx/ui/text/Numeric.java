package androidx.ui.text;

/**
 * 数字格式化
 */
public class Numeric {

    /**
     *
     * @param value 文字
     * @return 是否为空
     */
    public static boolean isEmpty(String value) {
        return value == null || value.equals("null") || value.length() == 0;
    }

    /**
     * @param value 文字
     * @return Double格式
     */
    public static double parseDouble(String value) {
        if (isEmpty(value)) {
            return 0.00d;
        }
        if (!value.contains(".")) {
            value = value + ".00";
        }
        return Double.parseDouble(value);
    }

    /**
     * @param value 文字
     * @return Integer 格式
     */
    public static int parseInt(String value) {
        if (isEmpty(value)) {
            return 0;
        }
        if (value.contains(".")) {
            value = value.substring(0, value.lastIndexOf("."));
        }
        return Integer.parseInt(value);
    }

    /**
     * @param value 文字
     * @return Long格式
     */
    public static long parseLong(String value) {
        if (isEmpty(value)) {
            return 0;
        }
        return Long.parseLong(value);
    }

    /**
     * @param value 文字
     * @return Float格式
     */
    public static float parseFloat(String value) {
        if (isEmpty(value)) {
            return 0.00f;
        }
        if (!value.contains(".")) {
            value = value + ".00";
        }
        return Float.parseFloat(value);
    }

    /**
     * @param value 文字
     * @return Int格式文字
     */
    public static String toInt(String value) {
        if (isEmpty(value)) {
            return "0";
        }
        return value;
    }

    /**
     * @param value 文字
     * @return Long格式文字
     */
    public static String toLong(String value) {
        if (isEmpty(value)) {
            return "0";
        }
        return value;
    }

    /**
     * @param value 文字
     * @return Double格式文字
     */
    public static String toDouble(String value) {
        if (isEmpty(value)) {
            return "0.00";
        }
        return value;
    }

    /**
     * @param value 文字
     * @return Float格式文字
     */
    public static String toFloat(String value) {
        if (isEmpty(value)) {
            return "0.00";
        }
        return value;
    }

}
