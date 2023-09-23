package androidx.ui.util;

import java.util.List;
import java.util.Map;

/**
 * 大小计算
 */
public class Size {

    private int width;
    private int height;

    public Size() {
        
    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    /**
     * 宽度大小
     *
     * @param width  宽度
     * @param height 高度
     * @return
     */
    public static Size of(int width, int height) {
        return new Size(width, height);
    }

    /**
     * 列表大小
     *
     * @param list
     * @return
     */
    public static int of(List<?> list) {
        return list == null ? 0 : list.size();
    }

    /**
     * 数组长度
     *
     * @param arr
     * @return
     */
    public static int of(String[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return arr.length;
    }

    /**
     * 数组长度
     *
     * @param arr
     * @return
     */
    public static int of(byte[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return arr.length;
    }

    /**
     * 数组长度
     *
     * @param arr
     * @return
     */
    public static int of(char[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return arr.length;
    }


    /**
     * 键值对大小
     *
     * @param map 键值对
     * @return
     */
    public static int of(Map map) {
        if (map == null) {
            return 0;
        }
        return map.size();
    }

}
