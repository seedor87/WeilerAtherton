import java.util.*;

/**
 * Created by robertseedorf on 3/11/16.
 */
public class Polygon {

    private ArrayList<Line> sides;

    public Polygon(ArrayList<Line> sides) {
        this.sides = new ArrayList(sides);
    }

    public Polygon(List<Point> points) {
        this.sides = new ArrayList<Line>();

        for(int i = 0; i < points.size()-1; i++) {
            addLine(new Line(points.get(i), points.get(i+1)));
        }
        addLine(new Line(points.get(points.size()-1), points.get(0)));
    }

    public Polygon() {
        this.sides = new ArrayList<Line>();
    }

    public boolean addLine(Line line) {
        sides.add(line);
        return true;
    }

    public boolean addPoint(Point start, Point end) {
        sides.add(new Line(start, end));
        return true;
    }

    public ArrayList getSides() {
        return this.sides;
    }

    public String toString() {
        String returnString =" {";
        for(Line l: sides) {
            returnString += l.toString();
        }
        return returnString + "} ";
    }
}
