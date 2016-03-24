import javax.swing.*;
import java.util.*;

/**
 * Created by robertseedorf on 3/11/16.
 */
public class Driver {

    private static Polygon subject;
    private static Polygon viewport;
    private static Polygon clipped;

    public static void main(String[] Args) {

        //create subject
        Point A = new Point(3.0, 1.0);
        Point B = new Point(1.0, 3.5);
        Point C = new Point(0.0, 0.0);
        Point D = new Point(0.0, -3.0);
        Point E = new Point(1.0, 0.0);
        subject = new Polygon(Arrays.asList(A, B, C, D, E));

        //create viewport, square
        Point F = new Point(2.0, 3.0);
        Point G = new Point(-1.0, 3.0);
        Point H = new Point(-1.0, -1.0);
        Point I = new Point(2.0, -1.0);
        viewport = new Polygon(Arrays.asList(F, G, H, I));

        clipped = findClipped();
        renderWindow();
        test5();
    }

    private static void renderWindow() {
        JFrame frame = new JFrame(" Clipping Testing ");
        frame.setSize(500,500);

        Canvas canvas = new Canvas(viewport.getSides(), subject.getSides(),
                clipped.getSides(), frame.getWidth(), frame.getHeight());
        frame.getContentPane().add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        System.out.println("Success");
    }

    private static void test5() {

        IntersectioniFinder IF = new IntersectioniFinder();

        ArrayList<Point> P = new ArrayList<Point>();
        ArrayList<Point> Q = new ArrayList<Point>();
        Stack<Point> Ie = new Stack<Point>();
        ArrayList<Line> subjectLines = new ArrayList<Line>(subject.getSides());
        ArrayList<Line> viewPortLines = new ArrayList<Line>(viewport.getSides());

         /*
        Construct P
         */
        Collections.reverse(subjectLines);          // need to do this to get Ie in correct order
        for(Line subjectLine: subjectLines) {
            P.add(subjectLine.end);
            int crossCount = 0;
            for(Line viewPortLine: viewPortLines) {
                Point poi = null;
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    //print(subjectLine + " " + viewPortLine + " : " + IF.findPOI(subjectLine, viewPortLine));
                    poi = IF.findPOI(subjectLine, viewPortLine);
                    P.add(poi);
                    Ie.push(poi);        // build Ie
                    crossCount++;
                }
                if(crossCount > 1) {
                    Point closer = IF.closestPoint(subjectLine.end, poi, Ie.peek());
                    if (closer.equals(poi)) {
                        Point temp = Ie.pop();
                        Ie.push(closer);
                        Ie.push(temp);
                    } else ;           // they are already in the correct order
                }
                crossCount = 0;
            }
        }

        /*
        Construct Q using new algorithm;
         */
        Point corner;                       // Point representing cardinal corners of viewPort polygon
        ArrayList<Point> storage ;          // temporary storage of only matching poi's

        // Top right corner first
        corner = viewPortLines.get(0).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.y.equals(p.y)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Collections.reverse(storage);
        Q.addAll(storage);

        // top left corner next
        corner = viewPortLines.get(1).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.x.equals(p.x)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Collections.reverse(storage);
        Q.addAll(storage);

        // bottom left corner next
        corner = viewPortLines.get(2).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.y.equals(p.y)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Q.addAll(storage);

        // bottom right corner last
        corner = viewPortLines.get(3).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.x.equals(p.x)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Q.addAll(storage);

        Collections.reverse(P);
        print(P);
        print(Q);
        print(Ie);

        Stack<Point> result = new Stack<Point>();

        Point reserve, end, location;
        reserve = Ie.peek();
        location = reserve;
        int index;
        boolean flag = true;
        while(flag) {
            Ie.pop();
            end = Ie.peek();
            index = P.indexOf(location);
            while(!location.equals(end)) {
                result.push(location);
                index++;
                index = index % P.size();
                location = P.get(index);
            }
            Ie.pop();
            try {
                end = Ie.peek();
            }
            catch(EmptyStackException e) {
                end = reserve;
                flag = false;
            }
            finally {
                index = Q.indexOf(location);
                while (!location.equals(end)) {
                    result.push(location);
                    index++;
                    index = index % Q.size();
                    location = Q.get(index);
                }
            }
        }


        print(result);
    }

    private static Polygon findClipped() {

        IntersectioniFinder IF = new IntersectioniFinder();

        ArrayList<Point> P = new ArrayList<Point>();        //Collection of points lying on perimeter of subject
        ArrayList<Point> Q = new ArrayList<Point>();        //Collection of points lying on perimeter of viewport
        Stack<Point> Ie = new Stack<Point>();       //Collection of all points of intersection

        ArrayList<Line> subjectLines = new ArrayList<Line>(subject.getSides());
        ArrayList<Line> viewPortLines = new ArrayList<Line>(viewport.getSides());

        // Construct P from subject, and viewport
        for(Line subjectLine: subjectLines) {
            P.add(subjectLine.start);
            for(Line viewPortLine: viewPortLines) {
                if(IF.checkIntersect(subjectLine, viewPortLine)) {
                    Point poi = IF.findPOI(subjectLine, viewPortLine);
                    P.add(poi);
                    Ie.push(poi);        // build Ie in the mean while
                }
            }
        }
        Collections.reverse(Ie);

        /*
        Construct Q using new algorithm;
         */
        Point corner;                       // Point representing cardinal corners of viewPort polygon
        ArrayList<Point> storage ;          // temporary storage of only matching poi's

        // Top right corner first
        corner = viewPortLines.get(0).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.y.equals(p.y)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Collections.reverse(storage);
        Q.addAll(storage);

        // top left corner next
        corner = viewPortLines.get(1).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.x.equals(p.x)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Collections.reverse(storage);
        Q.addAll(storage);

        // bottom left corner next
        corner = viewPortLines.get(2).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.y.equals(p.y)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Q.addAll(storage);

        // bottom right corner last
        corner = viewPortLines.get(3).start;
        storage = new ArrayList<Point>();
        Q.add(corner);
        for(Point p: Ie) {
            if(corner.x.equals(p.x)) {
                storage.add(p);
            }
        }
        Collections.sort(storage);
        Q.addAll(storage);

        print(P);
        print(Q);
        print(Ie);

        //If no intersecting region is found then return polygon of zero dimension
        if(Ie.isEmpty()) {
            return new Polygon();
        }

        Stack<Point> result = new Stack<Point>();

        Point reserve, end, location;
        reserve = Ie.peek();
        location = reserve;
        int index;
        boolean flag = true;
        while(flag) {
            Ie.pop();
            end = Ie.peek();
            index = P.indexOf(location);
            while(!location.equals(end)) {
                result.push(location);
                index++;
                index = index % P.size();
                location = P.get(index);
            }
            Ie.pop();
            try {
                end = Ie.peek();
            }
            catch(EmptyStackException e) {
                end = reserve;
                flag = false;
            }
            finally {
                index = Q.indexOf(location);
                while (!location.equals(end)) {
                    result.push(location);
                    index++;
                    index = index % Q.size();
                    location = Q.get(index);
                }
            }
        }

        print(result);
        print("---------------");

        return new Polygon(result);
    }


    private static void print(Object o) {
        System.out.println(o.toString());
    }
}

class CustomComparator implements Comparator<Point> {
    @Override
    public int compare(Point p1, Point p2) {
        return p1.compareTo(p2);
    }
}





