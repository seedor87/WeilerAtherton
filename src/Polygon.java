import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

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

        Stack<Point> temp = new Stack<Point>();
        for(Point p: points) {
            temp.push(p);
        }

        Point pointZero = temp.peek();
        while(!temp.empty()) {
            Point reserve = temp.pop();
            try {
                addPoint(reserve, temp.peek());
            }
            catch (EmptyStackException e) {
                addPoint(reserve, pointZero);
            }
        }
    }
    
    public Polygon(Stack<Point> points) {
        this.sides = new ArrayList<Line>();

        Point pointZero = points.peek();
        while(!points.empty()) {
            Point reserve = points.pop();
            try {
                addPoint(reserve, points.peek());
            }
            catch (EmptyStackException e) {
                addPoint(reserve, pointZero);
            }
        }
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
