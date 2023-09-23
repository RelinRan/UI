package androidx.ui.app;

public class AppPendingTransition {

    public static final int START = 1;
    public static final int FINISH = 2;

    private int type;
    private int enterAnim;
    private int exitAnim;

    public AppPendingTransition(int type, int enterAnim, int exitAnim) {
        this.type = type;
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public void setEnterAnim(int enterAnim) {
        this.enterAnim = enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public void setExitAnim(int exitAnim) {
        this.exitAnim = exitAnim;
    }
}
