import java.util.*;
//import java.awt.Point;

/**
 * Comparator voor MijnSets van ints.
 */
public class MijnSetComparator implements Comparator{


  public int compare(Object a, Object b)
  {
    if (a instanceof MijnSet && b instanceof MijnSet)
    {
      MijnSet seta = (MijnSet) a;
      MijnSet setb = (MijnSet) b;
      if (seta.size() < setb.size())
        return -1;
      else if (seta.size() > setb.size())
        return 1;
      else if (seta.size() == setb.size()){
        //beide sets zijn MijnSets van ints, dus TreeSets en dus geordend
        Iterator ha = seta.iterator();
        Iterator hb = setb.iterator();
        while (ha.hasNext() && hb.hasNext()){
          int elta = Integer.parseInt(ha.next().toString());
          int eltb = Integer.parseInt(hb.next().toString());
          if (elta < eltb)
            return -1;
          else if (elta > eltb)
            return 1; 
          //als elta == eltb: door naar volgende element
        }
        //nog niet gereturnd, dus seta == setb
        return 0;
      }     
    }
    return -1;// alleen als a en of b geen MijnSet is, komt in theorie niet voor-hoop ik
  }

  public boolean equals(Object a)
  {
    return compare(this, a)==0;
  }

  public static void main(String[] args) {
    MijnSetComparator mijnSetComp = new MijnSetComparator();
    MijnSet set = new MijnSet(mijnSetComp);//MijnSet van MijnSets van ints
    MijnSet s1 = new MijnSet();
    s1.add(new Integer(3));
    set.add(s1);
    System.out.println("na set1 "+set.toString());
    MijnSet s2 =  new MijnSet();
    s2.add(new Integer(2));
    s2.add(new Integer(3));
    set.add(s2);
    System.out.println("na set2 "+set.toString());
    MijnSet s3 = new MijnSet();
    set.add(s3);
    System.out.println("na set3 "+set.toString());
    MijnSet s4 = new MijnSet();
    s4.add(new Integer(1));
    s4.add(new Integer(2));
    s4.add(new Integer(5));
    set.add(s4);
    System.out.println("na set4 "+set.toString());
  }
}
