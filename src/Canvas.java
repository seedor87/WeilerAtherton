import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by robertseedorf on 3/11/16.
 */
public class Canvas extends JPanel{

    private static ArrayList<Line> viewport;
    private static ArrayList<Line> subject;
    private static ArrayList<Line> clipped;
    private static int HEIGHT, WIDTH, SCALE;

    public Canvas(ArrayList<Line> viewport, ArrayList<Line> subject, ArrayList<Line> clipped, int height, int width) {
        this.viewport = new ArrayList(viewport);
        this.subject = new ArrayList(subject);
        this.clipped = new ArrayList(clipped);
        this.HEIGHT = height;
        this.WIDTH = width;
        SCALE = (HEIGHT / 60) * (WIDTH / 60);
    }

    public void paint(Graphics g) {
        Graphics2D g2;
        g2 = (Graphics2D) g;
        g2.translate((WIDTH / 2), (HEIGHT / 2));

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        g2.drawLine(0, HEIGHT, 0, -HEIGHT);
        g2.drawLine(WIDTH, 0, -WIDTH, 0);

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.BLUE);
        for(Line l: subject) {
            Double temp;
            temp = (l.start.x * SCALE);
            int Ax = temp.intValue();
            temp = (l.start.y * SCALE * -1);
            int Ay = temp.intValue();
            temp = (l.end.x * SCALE);
            int Bx = temp.intValue();
            temp = (l.end.y * SCALE  * -1);
            int By = temp.intValue();
            g2.drawLine(Ax, Ay, Bx, By);
        }

        g2.setColor(Color.RED);
        for(Line l: viewport) {
            Double temp;
            temp = (l.start.x * SCALE);
            int Ax = temp.intValue();
            temp = (l.start.y * SCALE * -1);
            int Ay = temp.intValue();
            temp = (l.end.x * SCALE);
            int Bx = temp.intValue();
            temp = (l.end.y * SCALE * -1);
            int By = temp.intValue();
            g2.drawLine(Ax, Ay, Bx, By);
        }

        g2.setColor(Color.GREEN);
        for(Line l: clipped) {
            Double temp;
            temp = (l.start.x * SCALE);
            int Ax = temp.intValue();
            temp = (l.start.y * SCALE * -1);
            int Ay = temp.intValue();
            temp = (l.end.x * SCALE);
            int Bx = temp.intValue();
            temp = (l.end.y * SCALE * -1);
            int By = temp.intValue();
            g2.drawLine(Ax, Ay, Bx, By);
        }
    }
}
