import java.util.*;
import java.awt.Point;

public class PointComparator implements Comparator{


  public int compare(Object a, Object b)
  {
    if (a instanceof Point && b instanceof Point)
    {
      Point pa = (Point) a;
      Point pb = (Point) b;
      if (pa.x < pb.x)
        return -1;
      else if (pa.x > pb.x)
        return 1;
      else if (pa.x == pb.x)
      {
        if (pa.y < pb.y)
          return -1;
        else if (pa.y>pb.y)
          return 1;
        else return 0; //pa.x == pb.x en pa.y==pb.y)
      }
    }
    return -1;// alleen als a en of b geen point is, komt in theorie niet voor-hoop ik
  }

  public boolean equals(Object a)
  {
    return compare(this, a)==0;
  }

  public static void main(String[] args) {
    PointComparator pointComp = new PointComparator();
    TreeSet set = new TreeSet(pointComp);
    set.add(new Point(3,0));
    set.add(new Point(3,2));
    set.add(new Point(2,0));
    set.add(new Point(3,4));
    System.out.println("set : "+ set);


  }
}