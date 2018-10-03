//Punten om over na te denken:
//
//Er kunnen nu alleen CIJFERS ingevuld worden,
//en alleen maar 1 t/m 9. Dus niet echt te veralgemeniseren...
//
//Javadoc commentaar!?
//
//Exceptions?
//

import java.util.*;
import java.awt.*;

/**
 * Een sudokupuzzel bestaat uit een diagram van 9 bij 9 cellen
 * dat verdeeld is in 3 (niet-overlappende) vakken van 3 bij 3
 * cellen. In iedere rij, in iedere kolom en in ieder vak moeten
 * de cijfers 1..9 precies 1 keer ingevuld worden. In een aantal
 * cellen is al een cijfer gegeven.
 */
public class SudokuPuzzel{

  /**
   * De dimensie(s) van het diagram; eigenlijk maar 9 cellen nodig. Het is
   * dan ook de bedoeling dat alleen de indices 1..9 gebruikt worden.
   */
  public static final int D = 10;

  /**
   * De waarde die aangeeft dat een cel nog niet ingevuld is.
   */
  public static final int LEEG = 0;

  /**
   * Het diagram met daarin alle bekende cellen ingevuld; als het
   * cijfer in een cel nog niet bekend is staat er LEEG.
   * Gebruik weer alleen de indices 1..9.
   */
  private int[][] ingevuld = new int[D][D];

  /**
   * Voor ieder van de (D-1)*(D-1) cellen wordt een set bijgehouden met
   * daarin de overgebleven kandidaten voor die cel. Het streven is om
   * voor iedere set een singleton over te houden.
   * Gebruik weer alleen de indices 1..9.
   */
  private MijnSet[][] kandidaten = new MijnSet[D][D];

  /**
   * Voor iedere cel een boolean die aangeeft of de cel opnieuw getekend moet
   * worden (omdat er een cijfer is ingevuld, of de kandidatenset is aangepast,
   * of hij moet een andere kleur krijgen).
   * Dit array is vooral nodig om de applet te laten weten welke kandidatensets
   * veranderd zijn, zodat niet steeds *alle* kandidatensets opnieuw hoeven te
   * worden getekend. Gebruik weer alleen de indices 1..9.
   */
  private boolean[][] opnieuwTekenen = new boolean[D][D];




//Methoden die (informatie over) een attribuut (of een deel van een
//attribuut) teruggeven, of een attribuut een waarde geven.


  public int[][] getIngevuld(){
    return ingevuld;
  }


  /**
   * Geeft true als de cel met aanduiding (rij,kolom) is ingevuld,
   * false anders.
   */
  public boolean isIngevuld(int rij, int kolom){
    return (ingevuld[rij][kolom] != LEEG);
  }


  /** 
   * Geeft true terug als getal is ingevuld in rij rij, anders false.
   */
  public boolean isIngevuldInRij(int rij, int getal){
    boolean antw = false;
    
    for (int j = 1; j < ingevuld[rij].length; j++)
      antw = antw || (ingevuld[rij][j] == getal);
    
    return antw;
  }


  /**
   * Geeft true terug als getal is ingevuld in kolom kolom, anders false.
   */
  public boolean isIngevuldInKolom(int kolom, int getal){
    boolean antw = false;
    
    for (int i = 1; i < ingevuld.length; i++)
      antw = antw || (ingevuld[i][kolom] == getal);
    
    return antw;
  }

  /**
   * Geeft true terug als getal is ingevuld in vak met startcel 
   * (startrij, startkolom), anders false.
   */
  public boolean isIngevuldInVak(int startrij, int startkolom, int getal){
    boolean antw = false;
    
    for (int m = startrij; m < startrij + 3; m++)
      for (int n = startkolom; n < startkolom + 3; n++)
        antw = antw || (ingevuld[m][n] == getal);
    
    return antw;
  }


  /**
   * Geeft het cijfer dat in de cel met aanduiding (rij,kolom) is ingevuld.
   */
  public int getCijfer(int rij, int kolom){
    return ingevuld[rij][kolom];
  }

  /**
   * Geeft de set met kandidaten voor de cel met aanduiding (rij,kolom)
   */
  public MijnSet getKandidaten(int rij, int kolom){
    return kandidaten[rij][kolom];
  }

  /**
   * Voegt kandidaat toe aan de set met kandidaten voor de cel met aanduiding
   * (rij, kolom)
   */
  public void voegKandidaatToe(int rij, int kolom, int kandidaat){
    kandidaten[rij][kolom].add(new Integer(kandidaat));
    setOpnieuwTekenen(rij, kolom, true);
  }

  /**
   * Maakt de kandidatenset van cel (rij, kolom) leeg
   */
  public void maakKandidatenLeeg(int rij, int kolom){
    kandidaten[rij][kolom].clear();
  }


  public boolean[][] getOpnieuwTekenen(){
      return opnieuwTekenen;
  }

  /**
   * Geeft alle elementen van opnieuwTekenen de waarde b.
   */
  public void setOpnieuwTekenen(boolean b){
    for (int i = 1; i < opnieuwTekenen.length; i++)
      for (int j = 1; j < opnieuwTekenen[i].length; j++)
        opnieuwTekenen[i][j] = b;
  }

  /**
   * Geeft het element op positie (rij,kolom) in opnieuwTekenen de waarde b.
   */
  public void setOpnieuwTekenen(int rij, int kolom, boolean b){
    opnieuwTekenen[rij][kolom] = b;
  }



//Constructor

  /**
   * Bij een nieuwe puzzel worden alle kandidatensets op {1,..,9} gezet,
   * en voorlopig wordt in ieder veld van "ingevuld" LEEG gezet, behalve in
   * velden met een index 0: die krijgen waarde -1.
   * Voor iedere cel wordt de waarde van opnieuwTekenen op false gezet.
   */
  public SudokuPuzzel(){
    for (int i = 1; i < kandidaten.length; i++)
      for (int j = 1; j < kandidaten[i].length; j++){
        ingevuld[i][j] = LEEG;
        kandidaten[i][j] = new MijnSet();
        for (int k = 1; k < D; k++)
          kandidaten[i][j].add(new Integer(k));
          //N.B. eigenlijk levert add een boolean op, maar die
          //wordt nu genegeerd.
        setOpnieuwTekenen(i,j,false);
      }
    //geef cellen met een index 0 de waarde -1
    for (int i = 1; i < ingevuld.length; i++){
      ingevuld[i][0] = -1;
      ingevuld[0][i] = -1;
    }
  }






//"Hulp"-methoden

  public void resetPuzzel(){
    //moet alle door de gebruiker ingevoerde veranderingen verwijderen
    //Doe je dat door:
    //- deze puzzel weg te gooien en hem opnieuw "op te starten"?
    //  Dat moet dan vanuit SudokuApplet gebeuren (want daar wordt-ie
    //  ook gecreeerd), en je moet dan weten met *welke* puzzel je bezig
    //  was.
    //- alleen de door de gebruiker toegevoegde dingen te verwijderen?
    //  Dan moet je dus bijhouden wat je beginwaarden waren (in ingevuld
    //  bv.)
  }



  /**
   * Geeft het aantal nog niet ingevulde cellen in rij i.
   */
  public int aantalCellenVrijInRij(int i){
    int teller = 0;
    for (int j = 1; j < ingevuld[i].length; j++){
      if (!isIngevuld(i,j))
        teller++;
    }
    return teller;
  }


  /**
   * Geeft het aantal nog niet ingevulde cellen in kolom j.
   */
  public int aantalCellenVrijInKolom(int j){
    int teller = 0;
    for (int i = 1; i < ingevuld.length; i++){
      if (!isIngevuld(i,j))
        teller++;
    }
    return teller;
  }


  /**
   * Geeft het aantal nog niet ingevulde cellen in vak met linksboven
   * de cel (startrij, startkolom).
   */
  public int aantalCellenVrijInVak(int startrij, int startkolom){
    int teller = 0;
    for (int m = startrij; m < startrij + 3; m++)
      for (int n = startkolom; n < startkolom + 3; n++){
        if (!isIngevuld(m,n))
          teller++;
      }
    return teller;
  }


  /**
   * Geeft een array kopie terug waarin kopie[k] dezelfde inhoud als
   * cel (i,k) bevat (d.w.z. LEEG of een cijfer in 1..9).
   */
  public int[] kopieerCijfersVanRij(int i){

    int[] kopie = new int[D];

    for (int k = 1; k < kopie.length; k++){
      kopie[k] = ingevuld[i][k];
    }
    return kopie;
  }


  /**
   * Geeft een array kopie terug waarin kopie[k] de kandidatenset
   * van cel (i,k) bevat.
   */
  public MijnSet[] kopieerKandidatenVanRij(int i){

    MijnSet[] kopie = new MijnSet[D];

    for (int k = 1; k < kopie.length; k++){
      kopie[k] = new MijnSet();
      kopie[k].addAll(kandidaten[i][k]);
    }
    return kopie;
  }


  /**
   * Kopieert de kandidatensets uit het array kopie terug in rij i.
   * Als de kopie anders blijkt te zijn dan het origineel wordt de
   * bijbehorende entry van opnieuwTekenen op true gezet en wordt true
   * teruggegeven (anders false).
   */
  public boolean kopieerKandidatenVanRijTerug(int i, MijnSet[] kopie){
    boolean antw = false;
    for (int j = 1; j < kandidaten[i].length; j++)
      if (!kandidaten[i][j].equals(kopie[j])){
        kandidaten[i][j].retainAll(kopie[j]);
        opnieuwTekenen[i][j] = true;
        antw = true;
    }
    return antw;
  }


  /**
   * Geeft een array kopie terug waarin kopie[k] dezelfde inhoud als
   * cel (k,j) bevat (d.w.z. LEEG of een cijfer in 1..9).
   */
  public int[] kopieerCijfersVanKolom(int j){

    int[] kopie = new int[D];

    for (int k = 1; k < kopie.length; k++){
      kopie[k] = ingevuld[k][j];
    }
    return kopie;
  }


  /**
   * Geeft een array kopie terug waarin kopie[k] de kandidatenset
   * van cel (k,j) bevat.
   */
  public MijnSet[] kopieerKandidatenVanKolom(int j){

    MijnSet[] kopie = new MijnSet[D];

    for (int k = 1; k < kopie.length; k++){
      kopie[k] = new MijnSet();
      kopie[k].addAll(kandidaten[k][j]);
    }
    return kopie;
  }


  /**
   * Kopieert de kandidatensets uit het array kopie terug in kolom j.
   * Als de kopie anders blijkt te zijn dan het origineel wordt de
   * bijbehorende entry van opnieuwTekenen op true gezet en wordt true
   * teruggegeven (anders false).
   */
  public boolean kopieerKandidatenVanKolomTerug(int j, MijnSet[] kopie){
    boolean antw = false;
    for (int i = 1; i < kandidaten.length; i++)
      if (!kandidaten[i][j].equals(kopie[i])){
        kandidaten[i][j].retainAll(kopie[i]);
        opnieuwTekenen[i][j] = true;
        antw = true;
      }
    return antw;
  }


  /**
   * Geeft een array kopie terug dat de inhoud (d.w.z. LEEG of een cijfer
   * in 1..9) van de cellen van het vak met linksboven de cel (i,j) bevat.
   * De kopie wordt gemaakt door van links naar rechts en van boven naar
   * beneden door het vak te lopen.
   */
  public int[] kopieerCijfersVanVak(int i, int j){

    int[] kopie = new int[D];

    int k = 1;
    for (int m = i; m < i + 3; m++)
      for (int n = j; n < j + 3; n++){
        kopie[k] = ingevuld[m][n];
        k++;
      }
    return kopie;
  }


  /**
   * Geeft een array kopie terug dat de kandidatensets van het vak
   * met linksboven de cel (i,j) bevat. De kopie wordt gemaakt door
   * van links naar rechts en van boven naar beneden door het vak
   * te lopen.
   */
  public MijnSet[] kopieerKandidatenVanVak(int i, int j){

    MijnSet[] kopie = new MijnSet[D];

    int k = 1;
    for (int m = i; m < i + 3; m++)
      for (int n = j; n < j + 3; n++){
        kopie[k] = new MijnSet();
        kopie[k].addAll(kandidaten[m][n]);
        k++;
      }
    return kopie;
  }


  /**
   * Kopieert de kandidatensets uit het array kopie terug in het vak
   * met linksboven cel (i,j) (van links naar rechts en van boven naar
   * beneden door het vak lopend).
   * Als de kopie anders blijkt te zijn dan het origineel wordt de
   * bijbehorende entry van opnieuwTekenen op true gezet en wordt true
   * teruggegeven (anders false).
   */
  public boolean kopieerKandidatenVanVakTerug(int i, int j, MijnSet[] kopie){
    boolean antw = false;
    int k = 1;
    for (int m = i; m < i + 3; m++)
      for (int n = j; n < j + 3; n++){
        if (!kandidaten[m][n].equals(kopie[k])){
          kandidaten[m][n].retainAll(kopie[k]);
          opnieuwTekenen[m][n] = true;
          antw = true;
        }
        k++;
      }
    return antw;
  }


  /**
   * Geeft alle elementen van array a de waarde waarde.
   */
  public void reset(int[] a, int waarde){
    for (int i = 0; i < a.length; i++)
      a[i] = waarde;
  }


  /**
   * De index van de bovenste rij resp. meest linkse kolom van het vak
   * waarin cel (a,?) resp. (?,a) ligt wordt berekend m.b.v. de functie
   * f(a) = a - ((a-1) % 3), waarvoor geldt:
   *     a     1  2  3  4  5  6  7  8  9
   *     f(a)  1  1  1  4  4  4  7  7  7
   */
  public int bepaalStartVanVak(int a){
    return a - ((a - 1) % 3);
  }





//Methoden die cijfers invullen.


  /**
   * Vult het cijfer k in in de cel in rij i en kolom j; past ook de
   * kandidatensets van alle cellen in de rij, de kolom en het vak waarin
   * cel (i,j) ligt aan. De ingevulde cel krijgt als kandidatenset een
   * singleton met het cijfer k erin. Zet ook opnieuwTekenen op true voor de
   * cellen waarin iets veranderd is.
   */
  public void vulIn(int i, int j, int k){
    if (i > 0 && i < ingevuld.length && j > 0 && j < ingevuld[i].length){
      //if (kandidaten[i][j].contains(new Integer(k))){//werkt niet als je LEEG
      //invult!!!
      if (k != LEEG){
        //vul cijfer in
        ingevuld[i][j] = k;
        opnieuwTekenen[i][j] = true;

        //haal cijfer uit kandidatensets in zelfde rij
        for (int n = 1; n < kandidaten[i].length; n++){
          kandidaten[i][n].remove(new Integer(k));
          //N.B. eigenlijk levert remove een boolean op, maar die
          //wordt nu genegeerd.
          opnieuwTekenen[i][n] = true;
        }

        //haal cijfer uit kandidatensets in zelfde kolom
        for (int n = 1; n < kandidaten.length; n++){
          kandidaten[n][j].remove(new Integer(k));
          opnieuwTekenen[n][j] = true;
        }

        //haal cijfer uit kandidatensets in zelfde vak
        int startrij = bepaalStartVanVak(i);
        int startkolom = bepaalStartVanVak(j);
        for (int m = startrij; m < startrij + 3; m++)
          for (int n = startkolom; n < startkolom + 3; n++){
            kandidaten[m][n].remove(new Integer(k));
            opnieuwTekenen[m][n] = true;
          }

        //pas kandidatenset van cel (i,j) aan
        kandidaten[i][j].clear();
        kandidaten[i][j].add(new Integer(k));
      }
      else{ //cel is leeg (moet leeg worden)

        if (isIngevuld(i,j)){//cel was ingevuld
          ingevuld[i][j] = LEEG;
          opnieuwTekenen[i][j] = true;
   //       System.out.println("vulIn("+i+","+j+",LEEG)");

          //pas kandidaatsets voor rij i en kolom j aan (behalve cel (i,j)
          for (int p = 1; p < kandidaten.length; p++){
            if ((p != j) && !isIngevuld(i,p)){//cel (i,j) niet
              kandidaten[i][p] = berekenKandidatenVoorCel(i,p);
              opnieuwTekenen[i][p] = true;
            }
            if ((p != i) && !isIngevuld(p,j)){//cel (i,j) niet
              kandidaten[p][j] = berekenKandidatenVoorCel(p,j);
              opnieuwTekenen[p][j] = true;
            }
          }

          //pas kandidaatsets voor het vak aan
          int startrij = bepaalStartVanVak(i);
          int startkolom = bepaalStartVanVak(j);
          for (int m = startrij; m < startrij + 3; m++)
            for (int n = startkolom; n < startkolom + 3; n++)
              if (((m != i) || (n != j)) && !isIngevuld(m,n)){
                kandidaten[m][n] = berekenKandidatenVoorCel(m,n);
                opnieuwTekenen[m][n] = true;
              }


        }
      }
      //als er een kandidatenset in stond:
      //niets doen?
    }
    //}
  }

  /**
   * Berekent welke cijfers nog in cel (i,j) ingevuld kunnen worden,
   * op basis van de al ingevulde cijfers in rij i, kolom j en het vak
   * waarin cel (i,j) ligt. Geeft een set terug die precies die
   * cijfers bevat.
   */
  public MijnSet berekenKandidatenVoorCel(int i, int j){

    //System.out.println("");
    //System.out.println("berekenKandidatenVoorCel("+i+","+j+")");
    //System.out.println("oude kandidaten: "+kandidaten[i][j].toString());

    MijnSet set = new MijnSet();

    //kopieer rij i en tel hoe vaak ieder cijfer voorkomt
    int[] kopie = kopieerCijfersVanRij(i);
    int[] aantalVoorkomensTotaal = telAantalVoorkomens(kopie);



   //   System.out.println("kopie van rij "+i+" is ");
   //   for (int k = 0; k<kopie.length; k++)
   //     System.out.print(kopie[k]+" ");
   //   System.out.println("");
      //System.out.println("na rij: aantalVoorkomensTotaal is ");
      //for (int k = 0; k<kopie.length; k++)
      //  System.out.print(aantalVoorkomensTotaal[k]+" ");
      //System.out.println("");


    //doe het ook voor kolom j en tel aantallen bij die van rij i op;
    //er wordt een cel dubbel geteld, maar dat geeft niet want het gaat
    //alleen om "0?" of "meer dan 0?"
    kopie = kopieerCijfersVanKolom(j);
    int[] hulp = telAantalVoorkomens(kopie);
    for (int k = 0; k < hulp.length; k++)
      aantalVoorkomensTotaal[k] += hulp[k];


      //System.out.println("na kolom: aantalVoorkomensTotaal is ");
      //for (int k = 0; k<kopie.length; k++)
      //  System.out.print(aantalVoorkomensTotaal[k]+" ");
      //System.out.println("");



    //doe dat ook voor het vak waar cel (i,j) in ligt; er wordt weer
    //dubbel geteld maar dat geeft weer niet
    int m = bepaalStartVanVak(i);
    int n = bepaalStartVanVak(j);
    kopie = kopieerCijfersVanVak(m,n);
    hulp = telAantalVoorkomens(kopie);
    for (int k = 0; k < hulp.length; k++)
      aantalVoorkomensTotaal[k] += hulp[k];


      //System.out.println("na vak: aantalVoorkomensTotaal is ");
      //for (int k = 0; k<kopie.length; k++)
      //  System.out.print(aantalVoorkomensTotaal[k]+" ");
      //System.out.println("");


    //voeg kandidaat aan set toe als die kandidaat niet voorkomt
    //in rij i, kolom j en het vak waar cel (i,j) in zit
    set.clear();
    for (int k = 1; k < aantalVoorkomensTotaal.length; k++)
      if (aantalVoorkomensTotaal[k] == 0)
        set.add(new Integer(k));

    //System.out.println("nieuwe kandidaten: "+set.toString());
    return set;
  }

  /**
   * Een voorbeeld om te testen
   */
  public void testVoorbeeld(){
	//puzzel van tante Jo en mama (aug 2011)
	vulIn(1,3,5); vulIn(1,4,3);
    vulIn(2,1,8); vulIn(2,8,2);
    vulIn(3,2,7); vulIn(3,5,1); vulIn(3,7,5);
    vulIn(4,1,4); vulIn(4,6,5); vulIn(4,7,3);
    vulIn(5,2,1); vulIn(5,5,7); vulIn(5,9,6);
    vulIn(6,3,3); vulIn(6,4,2); vulIn(6,8,8);
    vulIn(7,2,6); vulIn(7,4,5); vulIn(7,9,9);
    vulIn(8,3,4); vulIn(8,8,3);
    vulIn(9,6,9); vulIn(9,7,7);
	//onderstaand het originele testvoorbeeld  
    /*vulIn(1,3,2); vulIn(1,4,7); vulIn(1,5,6);
    vulIn(2,2,6); vulIn(2,5,4); vulIn(2,7,9);
    vulIn(3,2,1); vulIn(3,4,9);
    vulIn(4,3,8); vulIn(4,6,9); vulIn(4,7,4); vulIn(4,8,2);
    vulIn(5,1,5); vulIn(5,2,9); vulIn(5,8,3); vulIn(5,9,6);
    vulIn(6,2,4); vulIn(6,3,3); vulIn(6,4,6); vulIn(6,7,8);
    vulIn(7,6,2); vulIn(7,8,8);
    vulIn(8,3,6); vulIn(8,5,1); vulIn(8,8,4);
    vulIn(9,5,3); vulIn(9,6,6); vulIn(9,7,2);*/
  }


  /**
   * Nog een voorbeeld, om Check te testen.
   * DUS BEVAT CONFLICTEN!!
   */
  public void testVb2(){
    vulIn(1,3,2); vulIn(1,4,7); vulIn(1,5,6);
    vulIn(2,1,2); vulIn(2,2,6); vulIn(2,5,4); vulIn(2,7,9);
    vulIn(3,2,1); vulIn(3,4,9); vulIn(3,5,1);
    vulIn(4,3,8); vulIn(4,6,9); vulIn(4,7,4); vulIn(4,8,2);
    vulIn(5,1,5); vulIn(5,2,9); vulIn(5,5,4); vulIn(5,8,3); vulIn(5,9,6);
    vulIn(6,2,4); vulIn(6,3,3); vulIn(6,4,6); vulIn(6,7,8);
    vulIn(7,6,2); vulIn(7,8,8);
    vulIn(8,3,6); vulIn(8,5,1); vulIn(8,8,4);
    vulIn(9,5,3); vulIn(9,6,6); vulIn(9,7,2);

  }

  /**
   * Een moeilijker voorbeeld?
   */
   public void testVb3(){
     vulIn(1,1,6); vulIn(1,6,7); vulIn(1,7,9);
     vulIn(2,1,4); vulIn(2,5,1);
     vulIn(3,4,6); vulIn(3,5,4); vulIn(3,7,7);
     vulIn(4,3,1); vulIn(4,4,9); vulIn(4,5,6); vulIn(4,8,5);
     vulIn(5,3,2); vulIn(5,6,1);
     vulIn(6,1,7); vulIn(6,4,4); vulIn(6,7,8);
     vulIn(7,1,3); vulIn(7,4,7);
     vulIn(8,2,5); vulIn(8,5,9); vulIn(8,9,2);
     vulIn(9,3,9); vulIn(9,7,6);
   }


  /**
   * En nog een...
   */
   public void testVb4(){
     vulIn(1,1,1); vulIn(1,4,6);
     vulIn(2,1,9); vulIn(2,5,5); vulIn(2,6,1);
     vulIn(3,2,5);
     vulIn(4,1,2); vulIn(4,4,4); vulIn(4,8,5);
     vulIn(5,3,9); vulIn(5,6,7); vulIn(5,8,8); vulIn(5,9,4);
     vulIn(6,3,7); vulIn(6,7,9);
     vulIn(7,4,5); vulIn(7,5,6); vulIn(7,8,9); vulIn(7,9,7);
     vulIn(8,2,6); vulIn(8,4,8); vulIn(8,7,3);
     vulIn(9,4,1); vulIn(9,6,3); vulIn(9,9,2);
   }


  /**
   * Weer een...
   */
  public void testVb5(){
    vulIn(1,1,2); vulIn(1,2,1); vulIn(1,6,5); vulIn(1,9,9);
    vulIn(2,5,8); vulIn(2,7,2); vulIn(2,9,5);
    vulIn(3,4,3); vulIn(3,6,2); vulIn(3,9,4);
    vulIn(4,5,9); vulIn(4,8,8);
    vulIn(5,1,7); vulIn(5,3,3); vulIn(5,4,4);
    vulIn(6,2,6); vulIn(6,4,7);
    vulIn(7,3,4);
    vulIn(8,1,8); vulIn(8,2,9); vulIn(8,3,1); vulIn(8,5,3); vulIn(8,9,2);
    vulIn(9,6,9);
  }

  /**
   * En een lege puzzel...
   */
  public void legePuzzel(){

  }

  /**
   * Telt in a[i] het aantal keer dat het cijfer i (in 0..9; 0 betekent
   * "nog niet ingevuld" in kopie is ingevuld.
   */
  public int[] telAantalVoorkomens(int[] kopie){

    int[] a = new int[D];
    reset(a,0);

    //tel voor ieder cijfer in 0..9 hoe vaak het is ingevuld
    for (int k = 1; k < kopie.length; k++)
      a[kopie[k]]++;

    return a;
  }

  
  /**
   * Telt het aantal verschillende cellen in kopie (daar staan kopieen
   * van de kandidaatsets van een rij/kolom/vak in) waarin minstens 1
   * van de elementen van set voorkomt, en geeft dit aantal terug.
   */
  public int telAantalCellenVoorDezeKandidaten(MijnSet set, MijnSet[] kopie){
    int antw = 0;
    
    for (int k = 1; k < kopie.length; k++)
      if (kopie[k].containsSome(set))
        antw++;
    
    return antw;
  }


//Methode die uitzoekt wat de button Check moet doen

  public boolean checkOpConflicten(){

    boolean antw = false;

    setOpnieuwTekenen(false);

    //loop de rijen af
    for (int i = 1; i < ingevuld.length; i++){
      int[] kopie = kopieerCijfersVanRij(i);
      int[] aantalVoorkomens = telAantalVoorkomens(kopie);

      for (int getal = 1; getal < aantalVoorkomens.length; getal++)
        if (aantalVoorkomens[getal] > 1){//2 keer getal in rij i
          for (int j = 1; j < opnieuwTekenen[i].length; j++){
            opnieuwTekenen[i][j] = true; //rij i moet gearceerd worden
            antw = true;
          }
        }
    }

    //loop de kolommen af
    for (int j = 1; j < ingevuld[1].length; j++){
      int[] kopie = kopieerCijfersVanKolom(j);
      int[] aantalVoorkomens = telAantalVoorkomens(kopie);

     for (int getal = 1; getal < aantalVoorkomens.length; getal++)
        if (aantalVoorkomens[getal] > 1){//2 keer getal in kolom j
          for (int i = 1; i < opnieuwTekenen.length; i++){
            opnieuwTekenen[i][j] = true; //kolom j moet gearceerd worden
            antw = true;
          }
        }
    }

    //loop de vakken af
    for (int m = 1; m < ingevuld.length; m += 3)
      for (int n = 1; n < ingevuld[m].length; n += 3){
        int[] kopie = kopieerCijfersVanVak(m,n);
        int[] aantalVoorkomens = telAantalVoorkomens(kopie);
/*
      System.out.println("kopie is ");
      for (int k = 0; k<kopie.length; k++)
        System.out.println("k is " + k + " en kopie[k] is " +kopie[k]);
      System.out.println("aantalVoorkomens is ");
      for (int k = 0; k<kopie.length; k++)
        System.out.println("k is "+k+" en aV[k] is " + aantalVoorkomens[k]);
*/
        for (int getal = 1; getal < aantalVoorkomens.length; getal++)
          if (aantalVoorkomens[getal] > 1){//2 keer getal in dit vak
            for (int i = m; i < m + 3; i++)
              for (int j = n; j < n + 3; j++){
                opnieuwTekenen[i][j] = true;
                antw = true;
            }
        }
      }
    return antw;
  }





//Methoden die uitzoeken wat de button Match moet doen.

  /**
   * Zoekt voor n = 1..8 achtereenvolgens alle voorkomens van
   * "er zijn n cellen in een rij/kolom/vak waarin in totaal maar
   * n verschillende kandidaten staan" (dan kunnen die n kandidaten uit
   * de overige kandidaatsets in die rij/kolom/vak verwijderd worden),
   * en alle voorkomens van "er zijn n verschillende kandidaten die
   * maar in n verschillende cellen mogen" (dan kunnen de overige kandidaten
   * uit die n cellen verwijderd worden).
   * Stopt deze voorkomens, gerepresenteerd door MijnSets van Points, in de
   * gevonden volgorde in een ArrayList en levert deze ArrayList op.
   */
  public ArrayList zoekMatches(){

    ArrayList lijst = new ArrayList();

    //let op: addAll gebruikt de volgorde die de iterator ook
    //gebruikt (en dus misschien niet je eigen volgorde)

    //lijst.addAll(zoek1CelVoor1Kandidaat());//doet Scan al
    lijst.addAll(zoek1KandidaatVoor1Cel());
    lijst.addAll(zoek2CellenVoor2Kandidaten());
    lijst.addAll(zoek2KandidatenVoor2Cellen());
    lijst.addAll(zoek3KandidatenVoor3Cellen());
    for (int n = 3; n < D-1; n++)
      lijst.addAll(zoekNCellenVoorNKandidaten(n));
    for (int n = 3; n < D-1; n++)
      lijst.addAll(zoekNKandidatenVoorNCellen(n));

    return lijst;

  }

  /**
   * Zoekt in iedere rij, kolom en vak naar een kandidaat die maar
   * in 1 cel voorkomt. Stopt alle gevonden voorkomens in antw.
   */
  public ArrayList zoek1CelVoor1Kandidaat(){
    ArrayList antw = new ArrayList(0);
    return antw;
  }

  /**
   * Zoekt cellen waar maar 1 kandidaat in staat en stopt deze in antw.
   */
  public ArrayList zoek1KandidaatVoor1Cel(){

    ArrayList antw = new ArrayList(0);//initialCapacity = 0, want anders
    //blijven er misschien <= 10 lege elementen achteraan hangen??

    for (int i = 1; i < ingevuld.length; i++)
      for (int j = 1; j < ingevuld[i].length; j++){
        if (!isIngevuld(i,j) && kandidaten[i][j].size() == 1){
          Point cel =  new Point(i,j);
          MijnSet set = new MijnSet();
          set.add(cel);
          //System.out.println("cel is "+cel.toString());
          //System.out.println("set is "+set.toString());
          antw.add(set);
        }
      }
    //System.out.println("antw is "+antw.toString());
    return antw;
  }


  /** 
   * Geeft een lijst terug met daarin alle mogelijke n-tallen kandidaten,
   * voor 2 <= n <= 9, die voor deze rij/kolom/vak (waarvan een kopie van 
   * de cijfers en de kandidaten meegegeven moet worden) gemaakt kunnen
   * worden.
   */
  public ArrayList maakAlleMogelijkeNtallenKandidaten(
             int n, int[] cijferKopie, MijnSet[] kandidatenKopie){

    ArrayList antw = new ArrayList(0);                 

//kun je niet beter in MijnSet een methode maken die alle subsets
//van grootte n van een bepaalde set genereert?

    return antw;
  }                     


  /**
   * Zoekt een tweetal kandidaten dat maar in twee cellen in een 
   * rij/kolom/vak voorkomt (en dus niet in andere cellen in die 
   * rij/kolom/vak). In deze twee cellen mogen wel andere 
   * kandidaten voorkomen (die dan verwijderd kunnen worden).
   * Stopt de gevonden celcoordinaten in antw.
   */
  public ArrayList zoek2CellenVoor2Kandidaten(){
    
    ArrayList antw = new ArrayList(0);//ArrayList van MijnSets van Points
    PointComparator pointComp = new PointComparator();
    
    //loop de rijen af
    for (int i = 1; i < ingevuld.length; i++){
      
      //maak eerst alle mogelijke tweetallen kandidaten 
      ArrayList tweeKandidatenLijst = new ArrayList(0);
      for (int a = 1; a < D - 1; a++)
        for (int b = a + 1; b < D; b++)
          //ingevulde cijfers doen niet mee
          if (!isIngevuldInRij(i,a) && !isIngevuldInRij(i,b)){
            MijnSet tweeSet = new MijnSet();
            tweeSet.add(new Integer(a));
            tweeSet.add(new Integer(b));
            tweeKandidatenLijst.add(tweeSet);          
          }
         
      //tel dan voor ieder tweetal over hoeveel cellen ze verspreid staan
      //als dat 2 is: match gevonden
      //als <2: foutje...
      //als >2: helpt niet
      Iterator tK = tweeKandidatenLijst.iterator(); 
      while (tK.hasNext()){
        //Iterator tS = ((MijnSet)(tK.next())).iterator();
        //while (tS.hasNext()){
        MijnSet tweetal = (MijnSet)(tK.next());
        MijnSet[] kopie = kopieerKandidatenVanRij(i);
        int aantal = telAantalCellenVoorDezeKandidaten(tweetal, kopie);
        if (aantal == 2){//tweetal in maar twee cellen gevonden
//JE MOET OOK NOG TESTEN OF ER NOG IETS IN EEN VAN DE TWEE CELLEN STAAT!!        
          MijnSet set = new MijnSet(pointComp);
          //zoek bewuste cellen maar weer op...
          for (int j = 1; j < kandidaten[i].length; j++)
            if (kandidaten[i][j].containsSome(tweetal)){
              //deze cel hoort erbij
              Point cel = new Point();
              cel.x = i;
              cel.y = j;
              set.add(cel);
            }
          if (!antw.contains(set))
            antw.add(set);
        }
      }
    }
  
    
    //ook zo voor iedere kolom
    for (int j = 1; j < ingevuld[1].length; j++){
      
      //maak eerst alle mogelijke tweetallen kandidaten 
      ArrayList tweeKandidatenLijst = new ArrayList(0);
      for (int a = 1; a < D - 1; a++)
        for (int b = a + 1; b < D; b++)
          //ingevulde cijfers doen niet mee
          if (!isIngevuldInKolom(j,a) && !isIngevuldInKolom(j,b)){
            MijnSet tweeSet = new MijnSet();
            tweeSet.add(new Integer(a));
            tweeSet.add(new Integer(b));
            tweeKandidatenLijst.add(tweeSet);          
          }
        
      //tel dan voor ieder tweetal over hoeveel cellen ze verspreid staan
      //als dat 2 is: match gevonden
      //als <2: foutje...
      //als >2: helpt niet
      Iterator tK = tweeKandidatenLijst.iterator(); 
      while (tK.hasNext()){
        //Iterator tS = ((MijnSet)(tK.next())).iterator();
        //while (tS.hasNext()){
        MijnSet tweetal = (MijnSet)(tK.next());
       MijnSet[] kopie = kopieerKandidatenVanKolom(j);
        int aantal = telAantalCellenVoorDezeKandidaten(tweetal, kopie);
       if (aantal == 2){//tweetal in maar twee cellen gevonden
//JE MOET OOK NOG TESTEN OF ER NOG IETS IN EEN VAN DE TWEE CELLEN STAAT!!        
          MijnSet set = new MijnSet(pointComp);
          //zoek bewuste cellen maar weer op...
          for (int i = 1; i < kandidaten.length; i++)
            if (kandidaten[i][j].containsSome(tweetal)){
              //deze cel hoort erbij
              Point cel = new Point();
              cel.x = i;
              cel.y = j;
              set.add(cel);
            }
          if (!antw.contains(set))
            antw.add(set);
        }
      }
    }

    
    
    //ook zo voor ieder vak
    for (int m = 1; m < ingevuld.length; m+=3)
      for (int n = 1; n < ingevuld[m].length; n+=3){
      
        //maak eerst alle mogelijke tweetallen kandidaten 
        ArrayList tweeKandidatenLijst = new ArrayList(0);
        for (int a = 1; a < D - 1; a++)
          for (int b = a + 1; b < D; b++)
            //ingevulde cijfers doen niet mee
            if (!isIngevuldInVak(m,n,a) && !isIngevuldInVak(m,n,b)){
              MijnSet tweeSet = new MijnSet();
              tweeSet.add(new Integer(a));
              tweeSet.add(new Integer(b));
              tweeKandidatenLijst.add(tweeSet);          
            }

        //tel dan voor ieder tweetal over hoeveel cellen ze verspreid staan
        //als dat 2 is: match gevonden
        //als <2: foutje...
        //als >2: helpt niet
        Iterator tK = tweeKandidatenLijst.iterator(); 
        while (tK.hasNext()){
          //Iterator tS = ((MijnSet)(tK.next())).iterator();
          //while (tS.hasNext()){
          MijnSet tweetal = (MijnSet)(tK.next());
          MijnSet[] kopie = kopieerKandidatenVanVak(m,n);
          int aantal = telAantalCellenVoorDezeKandidaten(tweetal, kopie);
          if (aantal == 2){//tweetal in maar twee cellen gevonden
  //JE MOET OOK NOG TESTEN OF ER NOG IETS IN EEN VAN DE TWEE CELLEN STAAT!!        
            MijnSet set = new MijnSet(pointComp);
            //zoek bewuste cellen maar weer op...
            for (int i = m; i < m+3; i++)
              for (int j = n; j < n+3; j++)
                if (kandidaten[i][j].containsSome(tweetal)){
                  //deze cel hoort erbij
                  Point cel = new Point();
                  cel.x = i;
                  cel.y = j;
                  set.add(cel);
                }
            if (!antw.contains(set))
              antw.add(set);
          }
        }
    }
     
    return antw;
  }

  /**
   * Zoekt in kopie naar dubbele tweetallen (d.w.z. twee gelijke
   * doubletons).
   * Geeft een ArrayList van MijnSets van ints terug, waarin
   * de indices (in kopie) van de twee cellen die een dubbel tweetal bevatten
   * steeds samen in een set zitten.
   */
  public ArrayList geefDubbeleTweetallen(MijnSet[] kopie){

  ArrayList antw = new ArrayList(0);

  for (int i = 1; i < kopie.length - 1; i++)
    for (int j = i + 1; j < kopie.length; j++){
      if (kopie[i].size() == 2 && kopie[j].size() == 2){
        if (kopie[i].equals(kopie[j])){//dubbel tweetal gevonden
          //kijk of er iets aan te passen valt in de andere cellen
          for (int k = 1; k < kopie.length; k++)
            if (k != i && k != j){
              if (kopie[k].remove(kopie[i].first()) ||
                  kopie[k].remove(kopie[i].last())){//het tweetal heeft effect
                MijnSet tweeCellen = new MijnSet();
                tweeCellen.add(new Integer(i));
                tweeCellen.add(new Integer(j));
                antw.add(tweeCellen);//voeg set {i,j} toe aan antw
                return antw;//anders kan dit tweetal vaker toegevoegd worden
              }
            }
        }
      }
    }
  return antw;
  }


  public ArrayList zoek2KandidatenVoor2Cellen(){

    ArrayList antw = new ArrayList(0);
    ArrayList hulp = new ArrayList(0);
    PointComparator pointComp = new PointComparator();

    //loop de rijen af
    for (int i = 1; i < kandidaten.length; i++)
      if (aantalCellenVrijInRij(i) >= 3){
        MijnSet[] kopie = kopieerKandidatenVanRij(i);
        hulp = geefDubbeleTweetallen(kopie);
        Iterator it = hulp.iterator();
        while (it.hasNext()){
          MijnSet hulpset = new MijnSet();//set van ints
          hulpset.addAll((MijnSet)(it.next()));
          MijnSet set = new MijnSet(pointComp);//set van Points
          Iterator h = hulpset.iterator();
          while (h.hasNext())
            set.add(new Point(i, Integer.parseInt(h.next().toString())));
          if (!antw.contains(set))
            antw.add(set);
        }
      }

    //loop de kolommen af
    for (int j = 1; j < kandidaten[1].length; j++)
      if (aantalCellenVrijInKolom(j) >= 3){
        MijnSet[] kopie = kopieerKandidatenVanKolom(j);
        hulp = geefDubbeleTweetallen(kopie);
        Iterator it = hulp.iterator();
        while (it.hasNext()){
          MijnSet hulpset = new MijnSet();//set van ints
          hulpset.addAll((MijnSet)(it.next()));
          MijnSet set = new MijnSet(pointComp);//set van Points
          Iterator h = hulpset.iterator();
          while (h.hasNext())
            set.add(new Point(Integer.parseInt(h.next().toString()),j));
          if (!antw.contains(set))
            antw.add(set);
        }
      }

    //loop de vakken af
    for (int m = 1; m < kandidaten.length; m += 3)
      for (int n = 1; n < kandidaten[m].length; n += 3)
        if (aantalCellenVrijInVak(m,n) >= 3){
          MijnSet[] kopie = kopieerKandidatenVanVak(m,n);
          hulp = geefDubbeleTweetallen(kopie);
          Iterator it = hulp.iterator();
          while (it.hasNext()){
            MijnSet hulpset = new MijnSet();//set van ints
            hulpset.addAll((MijnSet)(it.next()));
            MijnSet set = new MijnSet(pointComp);//set van Points
            Iterator h = hulpset.iterator();
            while (h.hasNext()){
              int index = Integer.parseInt(h.next().toString());
              //converteer de index uit kopie naar de coordinaten van een cel
              //en voeg die coordinaten als een Point toe aan set
              set.add(new Point(m + ((index-1)/3), n + ((index-1)%3)));
            }
            if (!antw.contains(set))
              antw.add(set);
          }
        }
    return antw;
  }

  public ArrayList zoek3CellenVoor3Kandidaten(){
    ArrayList antw = new ArrayList(0);
    return antw;
  }


  public ArrayList geefDrietalInDrieCellen(MijnSet[] kopie){

    ArrayList antw = new ArrayList(0);
    MijnSet vereniging = new MijnSet();//set van ints

//System.out.println("start zoekDrietal");
    for (int i = 1; i < kopie.length - 2; i++)
      for (int j = i + 1; j < kopie.length - 1; j++)
        for (int m = j + 1; m < kopie.length; m++){
          if (kopie[i].size() > 1 && kopie[j].size() > 1 && kopie[m].size() > 1){
            //construeer de vereniging van de drie kandidatensets
            vereniging.clear();
            vereniging.addAll(kopie[i]);
            vereniging.addAll(kopie[j]);
            vereniging.addAll(kopie[m]);
            //System.out.println("vereniging van "+i+j+m+" is " + vereniging.toString());
            if (vereniging.size() == 3){
              //"enige 3 kandidaten voor 3 cellen"
              //kijk of er iets aan te passen valt in de andere cellen
              for (int k = 1; k < kopie.length; k++)
                if (k != i && k != j && k != m){
                  Iterator h = vereniging.iterator();
                  //loop door kandidaten in vereniging
                  while (h.hasNext()){
                    if (kopie[k].contains(h.next())){
                      MijnSet drieCellen = new MijnSet();
                      drieCellen.add(new Integer(i));
                      drieCellen.add(new Integer(j));
                      drieCellen.add(new Integer(m));
                      antw.add(drieCellen);//voeg set {i,j,m} toe aan antw
                      return antw;//anders kan dit tweetal vaker toegevoegd worden
                    }
                  }
                }
            }
          }
        }
   return antw;
  }

  public ArrayList zoek3KandidatenVoor3Cellen(){

    ArrayList antw = new ArrayList(0);
    ArrayList hulp = new ArrayList(0);
    PointComparator pointComp = new PointComparator();

    //loop de rijen af
    for (int i = 1; i < kandidaten.length; i++)
      if (aantalCellenVrijInRij(i) >= 4){
        MijnSet[] kopie = kopieerKandidatenVanRij(i);
        hulp = geefDrietalInDrieCellen(kopie);
        Iterator it = hulp.iterator();
        while (it.hasNext()){
          MijnSet hulpset = new MijnSet();//set van ints
          hulpset.addAll((MijnSet)(it.next()));
          MijnSet set = new MijnSet(pointComp);//set van Points
          Iterator h = hulpset.iterator();
          while (h.hasNext())
            set.add(new Point(i, Integer.parseInt(h.next().toString())));
          if (!antw.contains(set))
            antw.add(set);
        }
      }

    //loop de kolommen af
    for (int j = 1; j < kandidaten[1].length; j++)
      if (aantalCellenVrijInKolom(j) >= 4){
        MijnSet[] kopie = kopieerKandidatenVanKolom(j);
        hulp = geefDrietalInDrieCellen(kopie);
        Iterator it = hulp.iterator();
        while (it.hasNext()){
          MijnSet hulpset = new MijnSet();//set van ints
          hulpset.addAll((MijnSet)(it.next()));
          MijnSet set = new MijnSet(pointComp);//set van Points
          Iterator h = hulpset.iterator();
          while (h.hasNext())
            set.add(new Point(Integer.parseInt(h.next().toString()),j));
          if (!antw.contains(set))
            antw.add(set);
        }
      }

    //loop de vakken af
    for (int m = 1; m < kandidaten.length; m += 3)
      for (int n = 1; n < kandidaten[m].length; n += 3)
        if (aantalCellenVrijInVak(m,n) >= 4){
          MijnSet[] kopie = kopieerKandidatenVanVak(m,n);
          hulp = geefDrietalInDrieCellen(kopie);
          Iterator it = hulp.iterator();
          while (it.hasNext()){
            MijnSet hulpset = new MijnSet();//set van ints
            hulpset.addAll((MijnSet)(it.next()));
            MijnSet set = new MijnSet(pointComp);//set van Points
            Iterator h = hulpset.iterator();
            while (h.hasNext()){
              int index = Integer.parseInt(h.next().toString());
              //converteer de index uit kopie naar de coordinaten van een cel
              //en voeg die coordinaten als een Point toe aan set
              set.add(new Point(m + ((index-1)/3), n + ((index-1)%3)));
            }
            if (!antw.contains(set))
              antw.add(set);
          }
        }

    return antw;
  }

  public ArrayList zoekNCellenVoorNKandidaten(int n){
    ArrayList antw = new ArrayList(0);
    return antw;
  }
  public ArrayList zoekNKandidatenVoorNCellen(int n){
    ArrayList antw = new ArrayList(0);
    return antw;
  }





//Methoden voor de oplosautomaat


  /**
   * Zoekt de eerste cel (van links naar rechts en van boven naar beneden
   * gerekend) waarin maar 1 kandidaat mogelijk is, vult deze in (de
   * kandidatensets in de betrokken rij, kolom en vak worden ook
   * aangepast door vulIn(), en het array opnieuwTekenen ook) en geeft
   * true terug.
   * Geeft false terug als er niet zo'n cel is.
   */
  public boolean vulEersteCelMetEnkeleKandidaatIn(){
    for (int i = 1; i < ingevuld.length; i++)
        for (int j = 1; j < ingevuld[i].length; j++){
          if (!isIngevuld(i,j) && kandidaten[i][j].size() == 1){
            vulIn(i,j,
                  Integer.parseInt(kandidaten[i][j].first().toString()));
                  //KAN DIT ECHT NIET KORTER???
            return true;//1 cel tegelijk invullen!
          }
        }
    return false;
  }


  /**
   * Zoekt in kopie naar een dubbel tweetal (d.w.z. twee gelijke
   * doubletons) en wijzigt (in kopie) de kandidatensets van andere cellen.
   * Geeft true terug als er inderdaad iets verandert, anders false.
   */
  public boolean zoekDubbelTweetal(MijnSet[] kopie){

  boolean ietsVeranderd = false;

  for (int i = 1; i < kopie.length - 1; i++)
    for (int j = i + 1; j < kopie.length; j++){
      if (kopie[i].size() == 2 && kopie[j].size() == 2){
        if (kopie[i].equals(kopie[j])){//dubbel tweetal gevonden
          //kijk of er iets aan te passen valt in de andere cellen
          for (int k = 1; k < kopie.length; k++)
            if (k != i && k != j){//je checkt niet of hij al ingevuld is!?
              if (kopie[k].remove(kopie[i].first())){
                ietsVeranderd = true;
              }
              if (kopie[k].remove(kopie[i].last())){
                ietsVeranderd = true;//misschien dubbelop, misschien niet!
              }
            }
        }
      }
    }
  return ietsVeranderd;
  }


  /**
   * Zoekt achtereenvolgens in iedere rij, kolom en vak waarin nog
   * minstens 3 cellen vrij zijn naar een dubbel tweetal en past de
   * kandidatensets die hierdoor beinvloed worden aan. Het array
   * opnieuwTekenen wordt indien nodig aangepast door kopieerKandidatenVanRijTerug(),
   * kopieerKandidatenVanKolomTerug() of kopieerKandidatenVanVakTerug().
   * Geeft true terug zodra het eerste dubbele tweetal waardoor er iets
   * verandert is gevonden, false als er niet zo'n dubbel tweetal is.
   */
  public boolean pasKandidatenAanVoorEersteDubbeleTweetal(){

    //loop de rijen af
    for (int i = 1; i < kandidaten.length; i++)
      if (aantalCellenVrijInRij(i) >= 3){
        MijnSet[] kopie = kopieerKandidatenVanRij(i);
        if (zoekDubbelTweetal(kopie)){
          kopieerKandidatenVanRijTerug(i, kopie);
          return true;
        }
      }

    //loop de kolommen af
    for (int j = 1; j < kandidaten[1].length; j++)
      if (aantalCellenVrijInKolom(j) >= 3){
        MijnSet[] kopie = kopieerKandidatenVanKolom(j);
        if (zoekDubbelTweetal(kopie)){
          kopieerKandidatenVanKolomTerug(j, kopie);
          return true;
        }
      }

    //loop de vakken af
    for (int m = 1; m < kandidaten.length; m += 3)
      for (int n = 1; n < kandidaten[m].length; n += 3)
        if (aantalCellenVrijInVak(m,n) >= 3){
          MijnSet[] kopie = kopieerKandidatenVanVak(m,n);
          if (zoekDubbelTweetal(kopie)){
            kopieerKandidatenVanVakTerug(m,n,kopie);
            return true;
          }
        }

    return false;
  }



  public boolean zoekDrietalInDrieCellen(MijnSet[] kopie){

    MijnSet vereniging = new MijnSet();
    boolean ietsVeranderd = false;
//System.out.println("start zoekDrietal");
    for (int i = 1; i < kopie.length - 2; i++)
      for (int j = i + 1; j < kopie.length - 1; j++)
        for (int m = j + 1; m < kopie.length; m++){
          if (kopie[i].size() > 1 && kopie[j].size() > 1 && kopie[m].size() > 1){
            //construeer de vereniging van de drie kandidatensets
            vereniging.clear();
            vereniging.addAll(kopie[i]);
            vereniging.addAll(kopie[j]);
            vereniging.addAll(kopie[m]);
            //System.out.println("vereniging van "+i+j+m+" is " + vereniging.toString());
            if (vereniging.size() == 3){
              //"enige 3 kandidaten voor 3 cellen"
              //kijk of er iets aan te passen valt in de andere cellen
              for (int k = 1; k < kopie.length; k++)
                if (k != i && k != j && k != m){
                  Iterator h = kopie[k].iterator();
                  while (h.hasNext()){//??je gebruikt h.next() niet!!
                    if (kopie[k].removeAll(vereniging))
                      ietsVeranderd = true;
                  }
                }
            }
          }
        }
    return ietsVeranderd;
  }


  public boolean pasKandidatenAan_DrieKandidatenVoorDrieCellen(){

    //loop de rijen af
    for (int i = 1; i < kandidaten.length; i++)
      if (aantalCellenVrijInRij(i) >= 4){
        //System.out.println("rij "+i);
        MijnSet[] kopie = kopieerKandidatenVanRij(i);
        if (zoekDrietalInDrieCellen(kopie)){
          kopieerKandidatenVanRijTerug(i, kopie);
          return true;
        }
      }

    //loop de kolommen af
    for (int j = 1; j < kandidaten[1].length; j++)
      if (aantalCellenVrijInKolom(j) >= 4){
        //System.out.println("kolom "+j);
        MijnSet[] kopie = kopieerKandidatenVanKolom(j);
        if (zoekDrietalInDrieCellen(kopie)){
          kopieerKandidatenVanKolomTerug(j, kopie);
          return true;
        }
      }

    //loop de vakken af
    for (int m = 1; m < kandidaten.length; m += 3)
      for (int n = 1; n < kandidaten[m].length; n += 3)
        if (aantalCellenVrijInVak(m,n) >= 4){
          //System.out.println("vak met startcel ("+m+","+n+")");
          MijnSet[] kopie = kopieerKandidatenVanVak(m,n);
          if (zoekDrietalInDrieCellen(kopie)){
            kopieerKandidatenVanVakTerug(m,n,kopie);
            return true;
          }
        }

    return false;

  }

}//einde SudokuPuzzel

/*
  //Maakt een kopie van de kandidatensets in een rij, kolom of vak, in een
  //array met indices 0..9 (waarvan weer alleen 1..9 gebruikt worden). De
  //parameters i en j geven samen aan om welke rij, kolom of vak het gaat:
  //i != 0 en j = 0 betekent rij i,
  //i = 0 en j != 0 betekent kolom j, en
  //i != 0 en j != 0 betekent het vak dat linksboven cel (i,j) heeft.
  public MijnSet[] maakKopie(int i, int j){

    MijnSet[] antw = null;//om klachten van de compiler te voorkomen...

    if (i != 0 && j == 0) //het gaat om rij i
      antw = kopieerKandidatenVanRij(i);
    else
      if (i == 0 && j != 0) //het gaat om kolom j
        antw = kopieerKandidatenVanKolom(j);
      else
        if (i != 0 && j != 0) // het gaat om vak met linksboven cel (i,j)
          antw = kopieerKandidatenVanVak(i,j);
        else //Exception natuurlijk...
          System.out.println("Fout in SudokuPuzzel.maakKopie(int,int)");
    return antw;
  }
*/



  /*
    //Zet de tot nu toe ingevulde cellen op het scherm.
    public void toonPuzzel(){
      for (int i = 1; i < ingevuld.length; i++){
        /*System.out.println(ingevuld[i][1] + "  " +
                           ingevuld[i][2] + "  " +
                           ingevuld[i][3] + "   " +  //vakscheiding
                           ingevuld[i][4] + "  " +
                           ingevuld[i][5] + "  " +
                           ingevuld[i][6] + "   " +  //vakscheiding
                           ingevuld[i][7] + "  " +
                           ingevuld[i][8] + "  " +
                           ingevuld[i][9]);*/
  /* //nodig omdat die commentaarsoort niet genest mag zijn?

        String bla1,bla2,bla3,bla4,bla5,bla6,bla7,bla8,bla9;
        if (ingevuld[i][1] != 0) bla1 = ingevuld[i][1] + ""; else bla1 = ".";
        if (ingevuld[i][2] != 0) bla2 = ingevuld[i][2] + ""; else bla2 = ".";
        if (ingevuld[i][3] != 0) bla3 = ingevuld[i][3] + ""; else bla3 = ".";
        if (ingevuld[i][4] != 0) bla4 = ingevuld[i][4] + ""; else bla4 = ".";
        if (ingevuld[i][5] != 0) bla5 = ingevuld[i][5] + ""; else bla5 = ".";
        if (ingevuld[i][6] != 0) bla6 = ingevuld[i][6] + ""; else bla6 = ".";
        if (ingevuld[i][7] != 0) bla7 = ingevuld[i][7] + ""; else bla7 = ".";
        if (ingevuld[i][8] != 0) bla8 = ingevuld[i][8] + ""; else bla8 = ".";
        if (ingevuld[i][9] != 0) bla9 = ingevuld[i][9] + ""; else bla9 = ".";
        System.out.println(bla1 + "  " +
                           bla2 + "  " +
                           bla3 + "   " +  //vakscheiding
                           bla4 + "  " +
                           bla5 + "  " +
                           bla6 + "   " +  //vakscheiding
                           bla7 + "  " +
                           bla8 + "  " +
                           bla9);

        if (i % 3 == 0) //vakscheiding
          System.out.println("");
      }
      for (int i = 1; i < kandidaten.length; i++)
        for (int j = 1; j < kandidaten[i].length; j++){
          System.out.println(i+""+j+" "+kandidaten[i][j]);
        }
      System.out.println("");
    }
  */



/*
  //Wordt in Main aangeroepen na de creatie van een nieuwe SudokuPuzzel,
  //zodat niet al het werk in de constructor moet gebeuren (het echte werk
  //kan nu in start gedaan worden).
  public void start(){
    toonPuzzel();
  }
*/
