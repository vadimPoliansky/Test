
package banana.com.test;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonPoints {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("points")
    @Expose
    private List<Point> points = new ArrayList<Point>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The points
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * 
     * (Required)
     * 
     * @param points
     *     The points
     */
    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public JsonPoints(List<Point> points){
        this.points = points;
    }

}
