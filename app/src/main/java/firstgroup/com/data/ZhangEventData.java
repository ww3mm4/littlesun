package firstgroup.com.data;

/**
 * Created by mugua on 2015/11/18.
 */
public class ZhangEventData {
    private int wath;
    private String ms;

    public int getWath() {
        return wath;
    }

    public String getMs() {
        return ms;
    }

    public void setWath(int wath) {
        this.wath = wath;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public ZhangEventData(int wath) {
        this.wath = wath;
    }

    public ZhangEventData(String ms) {
        this.ms = ms;
    }

    public ZhangEventData() {
    }

    public ZhangEventData(int wath, String ms) {
        this.wath = wath;
        this.ms = ms;
    }
}
