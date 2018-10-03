import java.util.*;

//Deze klasse is een idee van Jurriaan, ik weet nog niet
//precies waarom hij handig is.
//In de SudokuPuzzel wil ik sets van kandidaten gebruiken;
//omdat de klasse Set uit het Java Collections Framework een
//interface is is MijnSet een subclass van TreeSet (TreeSet
//heeft namelijk een ordening op de elementen van de set, lijkt
//me wel handig voor deze puzzel).
public class MijnSet extends TreeSet{

  public MijnSet(){
    super();//WAT DOET DIT????????
  }

  /**
   * Een constructor om de set een comparator te geven waarmee elementen
   * die geen natuurlijke ordening hebben met elkaar vergeleken kunnen
   * worden (om ze op volgorde in de set te kunnen zetten).
   */
  public MijnSet(Comparator c){
    super(c);
  }
  
  /**
   * Geeft true terug als deze set minstens 1 element uit andereSet
   * bevat, anders false.
   */
  public boolean containsSome(MijnSet andereSet){
    boolean antw = false;
    
    Iterator it = andereSet.iterator();
    while (it.hasNext() && !antw){
      antw = antw || this.contains(it.next());
    }
    
    return antw;
  }
  
  /**
   * Geeft een set terug die alle subsets van grootte size van deze set 
   * bevat.
   * We gaan ervanuit dat we met geordende sets van ints te maken hebben 
   * (MijnSets zijn eigenlijk TreeSets, dus hebben een ordening), en dat 
   * kleinere sets altijd voor grotere sets komen.
   */ 
/*  public MijnSet allSubsets(int grootte){
    MijnSetComparator mijnSetComp = new MijnSetComparator();
    MijnSet antw = new MijnSet(mijnSetComp);//MijnSet van MijnSets van ints
    MijnSet hulp = new MijnSet(mijnSetComp);//om een kopie van antw in te
      //zetten, om een ConcurrentModificationException te omzeilen
    int klaar = -1; //de subsets t/m grootte klaar zijn gedaan
    
    if (this.size() >= grootte){
      antw.add(new MijnSet());//eerst alleen de lege set
      klaar++;//grootte 0 is klaar
      
      while (klaar < grootte){
        System.out.println("klaar = "+klaar+" en grootte is "+grootte);
        //maak kopie van antw
        hulp.clear();
        hulp.addAll(antw);
        System.out.println("hulp is "+hulp.toString());
        //itereer over kopie van antw, want je wilt antw zelf veranderen
        Iterator antwIt = hulp.iterator();
        while (antwIt.hasNext()){
          int startsetsize = 0;
          MijnSet startset = new MijnSet();
          //al gegenereerde subsets van grootte < klaar overslaan,
          //te beginnen met de lege subset
          MijnSet hulpset = (MijnSet)(antwIt.next());          
          while (startsetsize < klaar){
//            MijnSet hulpset = (MijnSet)(antwIt.next());
            startset.clear();
            startset.addAll(hulpset);
            System.out.println("startset = "+startset.toString());
            startsetsize = startset.size();
          }
          System.out.println("hier");
          //startset is de eerste subset in antw van grootte klaar;
          //daar moeten we nu op alle mogelijke manieren 1 nieuw element
          //van "this" aan toevoegen
          Iterator e = this.iterator();
          MijnSet subset = new MijnSet();           
          while (e.hasNext()){
            subset.clear();
            subset.addAll(startset);
            int volgendElement = Integer.parseInt(e.next().toString());
            if (!subset.contains(new Integer(volgendElement)))
              subset.add(new Integer(volgendElement));
            antw.add(subset);
          }      
        }  
        klaar++;
      }
      //}
    }
    else
      System.out.println("Fout in MijnSet.allSubsets(int size): "+
                "subsets kunnen niet groter zijn dan hun superset.");
    
    return antw;
  }
*/

  /** 
   * Geeft een set terug die alle subsets van deze set bevat.
   */  
/*  public MijnSet allSubsets(){
  
    MijnSet antw = new MijnSet();
    
    for (int i = 0; i <= this.size(); i++)
      antw.add(this.allSubsets(i));

    return antw;
  }
  
*/

  public static void main (String[] args){
/*  System.out.println("start");
    MijnSet set1 = new MijnSet();
  System.out.println("set1");
    set1.add(new Integer(1));
    set1.add(new Integer(2));
    set1.add(new Integer(3));
    
  System.out.println("voor set2");
    MijnSet set2 = new MijnSet();
  System.out.println("set2");
    set2.add(new Integer(4));
    set2.add(new Integer(2));
    set2.add(new Integer(5));
    
    if (set1.containsSome(set2))
      System.out.println("set1 door set2 niet-leeg");
    else
      System.out.println("set1 en set2 disjunct");
*/
  
/*  MijnSet setje = new MijnSet();
  setje.add(new Integer(1));
  setje.add(new Integer(2));
  setje.add(new Integer(3));
  setje.add(new Integer(4));
  
  MijnSetComparator mijnSetComp = new MijnSetComparator();
  MijnSet antw = new MijnSet(mijnSetComp);//MijnSet van MijnSets van ints
  antw.addAll(setje.allSubsets(0));
  System.out.println("grootte 0: "+antw.toString());
  System.out.println("");
  
  antw.clear();
  antw.addAll(setje.allSubsets(1));
  System.out.println("grootte 1: "+antw.toString());
  System.out.println("");
  
  antw.clear();
  antw.addAll(setje.allSubsets(2));
  System.out.println("grootte 2: "+antw.toString());
  System.out.println("");
  
  antw.clear();
  antw.addAll(setje.allSubsets(3));
  System.out.println("grootte 3: "+antw.toString());
  System.out.println("");
  
  antw.clear();
  System.out.println("grootte 4: "+antw.toString());
  
  System.out.println("");    
*/  }
  
}


