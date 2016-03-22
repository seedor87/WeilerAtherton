import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Created by robertseedorf on 3/10/16.
 */
class IntersectioniFinder {

    private static Point As;
    private static Point Ae;
    private static Point Bs;
    private static Point Be;
    private static Line LineA;
    private static Line LineB;
    private static Double Am;
    private static Double Bm;
    private static Double Ab;
    private static Double Bb;
    private static Double x;
    private static Double y;

    /**
     *Constructor of this class whihc encapsualtes all the of the fucntionality nex=cessary to find if two lines intersect
     * and if so where they intersect
     * @param LineA the line of the polygon of which we will be clipping        (subject)
     * @param LineB the line of the polygon against which we will be clipping   (view finder)
     */
    public IntersectioniFinder(Line LineA, Line LineB) {
        this.LineA = LineA;
        this.LineB = LineB;
        As = LineA.start;
        Ae = LineA.end;
        Bs = LineB.start;
        Be = LineB.end;
    }

    public IntersectioniFinder() {}

    /**
     * Used to enumerate the results of getOrientation, for clarity and concise refernece
     */
    public enum Orientation {
        COLLINEAR, RIGHT, LEFT
    }

    /**
     * This method will find return the Point at which LineA intersects with LineB, by first checking if the intersect
     * then using point slope formula to determine the value of each line's slope, y-intercept and then using those to
     * to determine the x,y coordinate at which they meet.
     * @return new Point of intersection
     * @throws ParallelException default, not needed
     */
    public static Point findPOI() throws ParallelException{

        if(Bs.x.equals(Be.x)) {                     // it is preferred to pass LineB as the Polygon of the viewfinder
            Am = (Ae.y - As.y) / (Ae.x - As.x);     // this algorithm will check if the line is vertical line first,
            Ab = As.y - (Am * As.x);                // this will subvert an unnecessary condition check.
            y = (Am * Bs.x) + Ab;
            return new Point(Bs.x, y);
        }
        if(As.x.equals(Ae.x)) {                     // check, just in case this line is vertical too
            Bm = (Be.y - Bs.y) / (Be.x - Bs.x);
            Bb = Bs.y - (Bm * Bs.x);
            y = (Bm * As.x) + Bb;
            return new Point(As.x, y);
        }
        Am = (Ae.y - As.y) / (Ae.x - As.x);         // m = (y1 - y2) / (x2 - x1)
        Bm = (Be.y - Bs.y) / (Be.x - Bs.x);         // for each line
        if(Am.equals(Bm)) {
            throw new ParallelException();
        }
        Ab = As.y - (Am * As.x);                    // b = y - (m * x)
        Bb = Bs.y - (Bm * Bs.x);                    // for each line
        x = ((-1 * Ab) + Bb) / (Am - Bm);           // x = (-b1 + b2) / (m1 - m2)
        y = (Am * x) + Ab;                          // y = (m * x) + b
        return new Point(x, y);
    }

    public static PointOfIntersection findPOI(Line LineA, Line LineB) {

        As = LineA.start;
        Ae = LineA.end;
        Bs = LineB.start;
        Be = LineB.end;

        if(Bs.x.equals(Be.x)) {                     // it is preferred to pass LineB as the Polygon of the viewfinder
            Am = (Ae.y - As.y) / (Ae.x - As.x);     // this algorithm will check if the line is vertical line first,
            Ab = As.y - (Am * As.x);                // this will subvert an unnecessary condition check.
            y = (Am * Bs.x) + Ab;
            return new PointOfIntersection(Bs.x, y);
        }
        if(As.x.equals(Ae.x)) {                     // check, just in case this line is vertical too
            Bm = (Be.y - Bs.y) / (Be.x - Bs.x);
            Bb = Bs.y - (Bm * Bs.x);
            y = (Bm * As.x) + Bb;
            return new PointOfIntersection(As.x, y);
        }
        Am = (Ae.y - As.y) / (Ae.x - As.x);         // m = (y1 - y2) / (x2 - x1)
        Bm = (Be.y - Bs.y) / (Be.x - Bs.x);         // for each line
        Ab = As.y - (Am * As.x);                    // b = y - (m * x)
        Bb = Bs.y - (Bm * Bs.x);                    // for each line
        x = ((-1 * Ab) + Bb) / (Am - Bm);           // x = (-b1 + b2) / (m1 - m2)
        y = (Am * x) + Ab;                          // y = (m * x) + b
        return new PointOfIntersection(x, y);
    }

    /**
     * This method will return true if the lines intersect at some point
     * else false
     * @return  True if intersect, else false
     */
    public static boolean checkIntersect() {

        Orientation o1 = getOrientation(As, Ae, Bs);
        Orientation o2 = getOrientation(As, Ae, Be);
        Orientation o3 = getOrientation(Bs, Be, As);
        Orientation o4 = getOrientation(Bs, Be, Ae);

        if (o1 != o2 && o3 != o4) {
            return true;
        }
        return false;
    }

    public static boolean checkIntersect(Line LineA, Line LineB) {

        As = LineA.start;
        Ae = LineA.end;
        Bs = LineB.start;
        Be = LineB.end;

        Orientation o1 = getOrientation(As, Ae, Bs);
        Orientation o2 = getOrientation(As, Ae, Be);
        Orientation o3 = getOrientation(Bs, Be, As);
        Orientation o4 = getOrientation(Bs, Be, Ae);

        if (o1 != o2 && o3 != o4) {
            return true;
        }
        return false;
    }

    public static boolean checkIntersect(Point As, Point Ae, Point Bs, Point Be) {

        Orientation o1 = getOrientation(As, Ae, Bs);
        Orientation o2 = getOrientation(As, Ae, Be);
        Orientation o3 = getOrientation(Bs, Be, As);
        Orientation o4 = getOrientation(Bs, Be, Ae);

        if (o1 != o2 && o3 != o4) {
            return true;
        }
        return false;
    }

    /**
     * this method will return the determined orientation of the lines if they share one point as vertex
     * @param p beginning point of LineA
     * @param q end point of LineA
     * @param r a point of lineB, which may or may not fall within the region of LineA, allowing it to be applicable as a intersecting Line
     * @return  Orientation of teo lines, can be Collinear, Left Turning (ccw) or Right Turning (cw)
     */
    private static Orientation getOrientation(Point p, Point q, Point r) {

        double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

        if (val == 0.0) {
            return Orientation.COLLINEAR;               // then collinear
        }
        if(val > 0) {
            return Orientation.RIGHT;               // then clockwise
        }
        return Orientation.LEFT;                   // else counterclock wise
    }
}

class Point implements Comparable{

    public Double x;
    public Double y;

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        Point p = (Point) o;
        DecimalFormat df = new DecimalFormat("###.#######");      /*Implied that closest degree of precision is
        the practical limit of commercial surveying techniques*/
        if(df.format(p.x).equals(df.format(this.x)) && df.format(p.y).equals(df.format(this.y))) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        Point p = (Point) o;
        if(this.x > p.x || this.y > p.y) {
            return 1;
        }
        if(this.x < p.x || this.y < p.y) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}

class PointOfIntersection extends Point{

    public PointOfIntersection(Double x, Double y) {
       super(x, y);
    }

    public String toString() {
        return super.toString();
    }
}

class Line {

    public Point start;
    public Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return " [" + start.toString() + ", " + end.toString() + "] ";
    }

    @Override
    public boolean equals(Object o) {
        Line l = (Line) o;
        if(this.start.equals(l.start) && this.end.equals(l.end)) {
            return true;
        }
        if(this.start.equals(l.end) && this.end.equals(l.start)) {
            return true;
        }
        return false;
    }
}

class ParallelException extends Exception {

    public ParallelException() {
        super();
    }

    public ParallelException(String message) {
        super(message);
    }

    public ParallelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParallelException(Throwable cause) {
        super(cause);
    }
}

