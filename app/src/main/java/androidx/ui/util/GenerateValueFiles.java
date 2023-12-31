package androidx.ui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by zhy on 15/5/3.
 */
public class GenerateValueFiles {

    private int baseW;
    private int baseH;

    private String dirStr = "./app/src/main/res";

    private final static String WTemplate = "<dimen name=\"x{0}\">{1}px</dimen>\n";
    private final static String HTemplate = "<dimen name=\"y{0}\">{1}px</dimen>\n";
    public static boolean IS_TV_DIMESION = false;

    /**
     * {0}-HEIGHT
     */
    private final static String VALUE_TEMPLATE = "values-{0}x{1}";

    private static final String SUPPORT_DIMESION = "320,480;" +
            "480,800;" +
            "600,800;" +
            "480,854;" +
            "540,854;" +
            "540,960;" +
            "640,960;" +
            "600,1024;" +
            "720,1184;" +
            "720,1196;" +
            "720,1280;" +
            "750,1334;" +
            "768,1024;" +
            "800,1280;" +
            "1080,1700;" +
            "1080,1812;" +
            "1080,1776;" +
            "1080,1800;" +
            "1080,1812;" +
            "1080,1920;" +
            "1440,2560;" +
            "1600,2560;";


    private static final String TV_SUPPORT_DIMESION =
            "3840,2160;" +
                    "2560,1600;" +
                    "2560,1440;" +
                    "2048,1536;" +
                    "1920,1440;" +
                    "1920,1200;" +
                    "1920,1080;" +
                    "1920,1032;" +
                    "1920,1200;" +
                    "1900,1000;" +
                    "1600,1050;" +
                    "1600,1200;" +
                    "1600,1204;" +
                    "1600,900;" +
                    "1440,900;" +
                    "1366,768;" +
                    "1360,768;" +
                    "1280,1024;" +
                    "1280,960;" +
                    "1280,800;" +
                    "1280,768;" +
                    "1280,720;" +
                    "1280,672;" +
                    "1176,664;" +
                    "1152,864;" +
                    "1024,768;" +
                    "800,600;";

    private String supportStr;

    public GenerateValueFiles(int baseX, int baseY, String supportStr) {
        this.baseW = baseX;
        this.baseH = baseY;
        this.supportStr = supportStr;
        if (!supportStr.contains(baseX + "," + baseY)) {
            supportStr += baseX + "," + baseY + ";";
        }
        supportStr += validateInput(supportStr);
        System.out.println(supportStr);
        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdir();

        }
        System.out.println(dir.getAbsoluteFile());
    }

    /**
     * @param supportStr w,h_...w,h;
     * @return
     */
    private String validateInput(String supportStr) {
        StringBuffer sb = new StringBuffer();
        String[] vals = supportStr.split("_");
        int w = -1;
        int h = -1;
        String[] wh;
        for (String val : vals) {
            try {
                if (val == null || val.trim().length() == 0)
                    continue;
                wh = val.split(",");
                w = Integer.parseInt(wh[0]);
                h = Integer.parseInt(wh[1]);
            } catch (Exception e) {
                System.out.println("skip invalidate params : w,h = " + val);
                continue;
            }
            sb.append(w + "," + h + ";");
        }
        return sb.toString();
    }

    public void generate() {
        String[] vals = supportStr.split(";");
        for (String val : vals) {
            String[] wh = val.split(",");
            generateXmlFile(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
        }
    }

    private void generateXmlFile(int w, int h) {
        StringBuffer sbForWidth = new StringBuffer();
        sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForWidth.append("<resources>");
        float cellw = w * 1.0f / baseW;
        System.out.println("width : " + w + "," + baseW + "," + cellw);
        for (int i = 1; i < baseW; i++) {
            sbForWidth.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
        sbForWidth.append(WTemplate.replace("{0}", baseW + "").replace("{1}",
                w + ""));
        sbForWidth.append("</resources>");
        StringBuffer sbForHeight = new StringBuffer();
        sbForHeight.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForHeight.append("<resources>");
        float cellh = h * 1.0f / baseH;
        System.out.println("height : " + h + "," + baseH + "," + cellh);
        for (int i = 1; i < baseH; i++) {
            sbForHeight.append(HTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }
        sbForHeight.append(HTemplate.replace("{0}", baseH + "").replace("{1}",
                h + ""));
        sbForHeight.append("</resources>");

        File fileDir = new File(dirStr + File.separator
                + VALUE_TEMPLATE.replace("{0}", w + "")//
                .replace("{1}", h + ""));
        fileDir.mkdir();
        File layxFile = new File(fileDir.getAbsolutePath(), "lay_x.xml");
        File layyFile = new File(fileDir.getAbsolutePath(), "lay_y.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sbForWidth.toString());
            pw.close();
            pw = new PrintWriter(new FileOutputStream(layyFile));
            pw.print(sbForHeight.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

    public static void main(String[] args) {
        int baseW = 1080;
        int baseH = 1920;
        if (IS_TV_DIMESION) {
            baseW = 1920;
            baseH = 1080;
        }
        String addition = IS_TV_DIMESION ? TV_SUPPORT_DIMESION : SUPPORT_DIMESION;
        try {
            if (args.length >= 3) {
                baseW = Integer.parseInt(args[0]);
                baseH = Integer.parseInt(args[1]);
                addition = args[2];
            } else if (args.length >= 2) {
                baseW = Integer.parseInt(args[0]);
                baseH = Integer.parseInt(args[1]);
            } else if (args.length >= 1) {
                addition = args[0];
            }
        } catch (NumberFormatException e) {

            System.err.println("right input params : java -jar xxx.jar width height w,h_w,h_..._w,h;");
            e.printStackTrace();
            System.exit(-1);
        }
        new GenerateValueFiles(baseW, baseH, addition).generate();
    }

}