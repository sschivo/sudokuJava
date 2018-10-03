//TODO:
//zwarte/rode meldingen


//Punten om over na te denken:
//
//Exceptions?
//
//GridLayout, TextArea's met een bepaald aantal rijen en kolommen?
//(ipv zoals nu met de hand afmetingen en posities aangeven)
//

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

public class SudokuApplet extends Applet{

  //breedte en hoogte van de cellen van matrix (in pixels?)
  public static final int BREEDTE = 45;
  public static final int HOOGTE = 45;

  //scheiding tussen de vakken
  public static final int SCHEIDING = 3;

  //een font voor de ingevulde cijfers en een voor de opties
  public static final Font CIJFERFONT = new java.awt.Font("SansSerif", 0, 30);
  public static final Font KANDIDATENFONT = new java.awt.Font("SansSerif", 1, 10);

  //kleuren
  public static final Color BEGINKLEUR = Color.BLACK;
  public static final Color CELKLEUR = Color.WHITE;
  public static final Color CIJFERKLEUR = Color.DARK_GRAY;
  public static final Color LIJNENKLEUR = Color.GRAY;
  public static final Color MARKEERKLEUR = Color.GREEN;
  public static final Color MATCHKLEUR = Color.MAGENTA;
  public static final Color MATCHGETALKLEUR = Color.WHITE;
  public static final Color MELDINGENKLEUR = Color.RED;
  public static final Color KANDIDATENKLEUR = Color.GRAY;
  public static final Color SCANKLEUR = Color.PINK;
  public static final Color SPECIALESCANKLEUR = Color.MAGENTA;
  public static final Color SPECIALESCANGETALKLEUR = Color.WHITE;




  /**
   * De op te lossen Sudokupuzzel (het model).
   */
  private SudokuPuzzel puzzel;

  /**
   * Het visuele diagram waarin de beginwaarden en de rest van de puzzel
   * ingevuld worden. De indices lopen van 0 tot en met 9; het
   * is de bedoeling dat alleen 1..9 gebruikt worden (omdat je de
   * vakjes liefst zo nummert: 11,12,13,...,89,99).
   * (de view)
   */
  private SudokuTextArea[][] matrix;

  //een rare manier om lijnen te tekenen
  private Panel lijnen;




  //panel om de "algemene" knoppen op te zetten
  private Panel algemeenPanel;

  //knop om nieuwe puzzel mee te maken
  private Button nieuwePuzzelKnop;

  //knop om laatste actie mee te herstellen
  private Button herstelKnop;

  //knop om eigen toevoegingen aan huidige puzzel mee te verwijderen
  private Button verwijderEigenToevoegingenKnop;

  //knop om hele matrix leeg te maken
  private Button verwijderAllesKnop;




  //panel om de specifieke "sudokuknoppen" op te zetten
  private Panel sudokuPanel;

  //knop om alle kandidaten te laten tonen/verbergen
  private Button kandidatenKnop;

  private boolean kandidatenTonen = false;

  //knop om te checken of er conflicten zijn
  private Button checkKnop;

  private boolean checkAan;

  //knop om alle rijen/kolommen/vakken waarin een te kiezen cijfer
  //voorkomt te laten arceren
  private Button scanKnop;

  private boolean scanAan;

  //knop om een "match" mee te laten zoeken
  private Button matchKnop;

  private boolean matchAan = false;

  private ArrayList matchList = new ArrayList();

  //knop om een "submatch" mee te laten zoeken
  private Button submatchKnop;

  //knop om een cel mee te kunnen markeren
  private Button markeerKnop;

  private boolean markeerAan = false;
  private boolean celGemarkeerd = false;

  private Point gemarkeerdeCel = new Point();

  //label om de gebruiker meldingen te kunnen geven
  private Label meldingenLabel;




  //een panel om de verschillende regels aan te bieden
  private Panel regels;

  private Label regelsLabel;

  private Panel panel1;
  private Checkbox regel1;
  private Checkbox herhaal1;

  private Panel panel2;
  private Checkbox regel2;
  private Checkbox herhaal2;

  private Panel panel3;
  private Checkbox regel3;
  private Checkbox herhaal3;

  //knop om regel(s) toe te passen
  private Button regelKnop;



//Door JBuilder gegenereerd

  //private boolean isStandalone = false;

  //Get a parameter value (JBuilder)
  //public String getParameter(String key, String def)
  //{
  //  return isStandalone ? System.getProperty(key, def) :
  //    (getParameter(key) != null ? getParameter(key) : def);
  //}

  //Construct the applet; door JBuilder gegenereerd
  public SudokuApplet()
  {
  }




//"Hulp"-methoden
  /**
   * Geeft true terug als ch een van de cijfers 1..9 voorstelt,
   * anders false.
   */
  public boolean isGetal(char ch){
    return ch == '1' ||
        ch == '2' ||
        ch == '3' ||
        ch == '4' ||
        ch == '5' ||
        ch == '6' ||
        ch == '7' ||
        ch == '8' ||
        ch == '9';
  }



  /**
   * Creeert een 9-bij-9 matrix van SudokuTextArea's waarin de op te
   * lossen Sudokupuzzel getoond kan worden en waarin de gebruiker
   * dingen kan intypen. Zet deze matrix netjes op het scherm.
   */
  public void maakMatrix(){

    int x = -BREEDTE + SCHEIDING;
    int y = -HOOGTE + SCHEIDING;

    matrix = new SudokuTextArea[SudokuPuzzel.D][SudokuPuzzel.D];

    //beetje onhandig: in een sudokupuzzel tel je eerst de rijen van boven naar
    //beneden en dan de kolommen van links naar rechts, terwijl je bij de
    //coordinaten in de applet eerst de x geeft (loopt van links naar rechts)
    //en dan de y (van boven naar beneden)
    for (int i = 1; i < matrix.length; i++){
      y = y + HOOGTE + 1;
      for (int j = 1; j < matrix[i].length; j++){
        x = x + BREEDTE + 1;
        //maak een SudokuTextArea zonder scrollbars
        matrix[i][j] = new SudokuTextArea("", 3, 3, TextArea.SCROLLBARS_NONE);
        matrix[i][j].rij = i;
        matrix[i][j].kolom = j;
        matrix[i][j].setBackground(CELKLEUR);
        matrix[i][j].setFont(CIJFERFONT);
        matrix[i][j].setBounds(x, y, BREEDTE, HOOGTE);
        matrix[i][j].addFocusListener(new java.awt.event.FocusListener()
        {//om input van de gebruiker te verwerken

          //de tekst die in de TextArea staat ...
          String start = ""; //...aan het begin van focusGained()
          String eind = "";  //...aan het begin van focusLost()


          public void focusGained(FocusEvent e){

            //sla de coordinaten van de actieve cel op in i en j
            int i = ( (SudokuTextArea) e.getSource()).rij;
            int j = ( (SudokuTextArea) e.getSource()).kolom;

            //sla de huidige tekst van de cel op
            start = matrix[i][j].getText();

            //voeg een TextListener toe die ervoor zorgt dat het font
            //en de kleur passen bij het aantal cijfers dat in de cel staat

            //definitie lokale klasse
            class TekstLuisteraar implements TextListener{
             public void textValueChanged(TextEvent e){
               //sla de coordinaten maar weer op
               int i = ( (SudokuTextArea) e.getSource()).rij;
               int j = ( (SudokuTextArea) e.getSource()).kolom;

               int aantalCijfers = aantalCijfersIn(matrix[i][j].getText());
               if (aantalCijfers <= 1){
                 matrix[i][j].setFont(CIJFERFONT);
                 matrix[i][j].setForeground(CIJFERKLEUR);
               }
               else
                 if (aantalCijfers > 1){
                   matrix[i][j].setFont(KANDIDATENFONT);
                   if (!(matchAan || scanAan))
                     matrix[i][j].setForeground(KANDIDATENKLEUR);
                   else
                     if (matrix[i][j].getBackground().equals(MATCHKLEUR))
                       matrix[i][j].setForeground(MATCHGETALKLEUR);
                     else
                       if (matrix[i][j].getBackground().equals(SPECIALESCANKLEUR))
                         matrix[i][j].setForeground(SPECIALESCANGETALKLEUR);

                 }
              }
            }

            TekstLuisteraar tl = new TekstLuisteraar();
            matrix[i][j].addTextListener(tl);

            //haal foutmelding weg
            //meldingenLabel.setText("");
            //werkt niet goed want om de focus uit een cel te halen moet
            //je in een andere cel klikken en dan zie je de foutmelding
            //die focusLost geeft als er een niet-getal is ingevuld nooit
          }

          public void focusLost(FocusEvent e){

            //sla de coordinaten van de actieve cel op in i en j
            int i = ( (SudokuTextArea) e.getSource()).rij;
            int j = ( (SudokuTextArea) e.getSource()).kolom;

            //verwijder de textListener! Anders geeft-ie cellen waar maar
            //1 kandidaat in staat een groot font...
            TextListener[] tlArray = matrix[i][j].getTextListeners();
            for (int k = 0; k < tlArray.length; k++)
              matrix[i][j].removeTextListener(tlArray[k]);

            //sla de huidige tekst van de cel op (kan veranderd zijn)
            eind = matrix[i][j].getText();

            if (!start.equals(eind)) //gebruiker heeft iets ingevoerd
              if (puzzel != null)
                verwerkInvoer(eind, i, j);
              else{
                meldingenLabel.setForeground(MELDINGENKLEUR);
                meldingenLabel.setText("Nog geen puzzel gegeven!");
              }
          }
        });
        matrix[i][j].addMouseListener(new java.awt.event.MouseAdapter()
        {
          //als markeerAan en er wordt in deze cel met de muis geklikt
          //dan moet deze cel MARKEERKLEUR krijgen
          public void mouseClicked(MouseEvent e){
            if (markeerAan && !celGemarkeerd){
              int i = ((SudokuTextArea)e.getSource()).rij;
              int j = ((SudokuTextArea)e.getSource()).kolom;
              gemarkeerdeCel.x = i;
              gemarkeerdeCel.y = j;
              matrix[i][j].setBackground(MARKEERKLEUR);
              celGemarkeerd = true;
              meldingenLabel.setForeground(MELDINGENKLEUR);
              meldingenLabel.setText("");
            }
         //Hoe zorg je ervoor dat er inderdaad in een of andere cel wordt
         //geklikt??
          }
        });
      if (j % 3 == 0)
        x = x + 3; //scheiding tussen vakken
    }
    if (i % 3 == 0)
      y = y + 3; //scheiding tussen vakken
    x = -BREEDTE + SCHEIDING; //cel [i][1] moet weer vooraan de regel
  }

  //het panel waarop de TextArea's geplaatst gaan worden;
  //dat panel zorgt voor de scheidingslijnen tussen de vakken en cellen
  lijnen = new Panel();
  lijnen.setLayout(null);
  lijnen.setBounds(BREEDTE, HOOGTE,
                   9 * (BREEDTE + 1) + 5 * SCHEIDING,
                   9 * (HOOGTE + 1) + 5 * SCHEIDING);
  lijnen.setBackground(LIJNENKLEUR);
  lijnen.setVisible(true);

  //maak de applet
  this.setLayout(null);
  this.setSize(21 * BREEDTE, 14 * HOOGTE);
  this.add(lijnen, null);
  for (int i = 1; i < matrix.length; i++)
    for (int j = 1; j < matrix[i].length; j++)
      lijnen.add(matrix[i][j], null);
}

  public int aantalCijfersIn(String str){
    int antw = 0;
    for (int k = 0; k < str.length(); k++){
      if (isGetal(str.charAt(k)))
        antw++;
    }
    return antw;
  }


  public void verwerkInvoer(String invoer, int i, int j){

    MijnSet hulpSet = new MijnSet();
    meldingenLabel.setForeground(MELDINGENKLEUR);
    meldingenLabel.setText("");

    //haal spaties en enters eruit
    String hulp = "";
    for (int k = 0; k < invoer.length(); k++)
      if ( (invoer.charAt(k) != ' ') && (invoer.charAt(k) != '\n')){
        hulp = hulp.concat(String.valueOf(invoer.charAt(k)));
      }
    invoer = hulp;

    //check of alleen cijfers 1..9 ingevuld; zo niet: meteen stoppen
    for (int k = 0; k < invoer.length(); k++){
      if (! (isGetal(invoer.charAt(k)))){
        meldingenLabel.setForeground(MELDINGENKLEUR);
        meldingenLabel.setText(
            "Vul alleen cijfers van 1 tot en met 9 in!");
        return;
      }
      else{//stop de ingevulde cijfers in een hulpset
        hulpSet.add(new Integer(
            Integer.parseInt(String.valueOf(invoer.charAt(k)))));
      }
    }

    //verwerk de invoer in het model en in de view
    if (hulpSet.isEmpty()){//gebruiker heeft cel leeggemaakt
      //pas model aan
      puzzel.setOpnieuwTekenen(false);
      puzzel.vulIn(i,j,SudokuPuzzel.LEEG);//GAAT DIT GOED?
      puzzel.maakKandidatenLeeg(i,j);
      //pas view aan EN KOMT HET OOK GOED IN DE VIEW?
      if (kandidatenTonen)
        toonVeranderdeKandidaten();
    }
    else
      if (hulpSet.size() == 1){//gebruiker wil 1 cijfer in deze cel
        //pas model aan
        puzzel.setOpnieuwTekenen(false);
        puzzel.vulIn(i,j,Integer.parseInt(hulpSet.first().toString()));

        //pas view aan
        matrix[i][j].setFont(CIJFERFONT);
        matrix[i][j].setForeground(CIJFERKLEUR);
        matrix[i][j].setText(" " + hulpSet.first());
        if (kandidatenTonen)
          toonVeranderdeKandidaten();
      }
      else{//hulpSet.size() > 1, dus gebruiker wil kandidaten in deze cel
        //pas model aan
        puzzel.setOpnieuwTekenen(false);
        puzzel.maakKandidatenLeeg(i,j);
        Iterator it = hulpSet.iterator();
        while (it.hasNext())
          puzzel.voegKandidaatToe(i,j,
                  Integer.parseInt(it.next().toString()));
        //pas view aan
        toonKandidaten(i,j);
      }

      //maak de matchList leeg, want die moet nu opnieuw berekend worden
      matchList.clear();
      matchAan = false;

      //maak alle cellen weer wit
      //GAAT DIT NU OOK GOED MET SCAN EN MARKEER? NEE!!
      //for (int i = 1; i < matrix.length; i++)
      //  for (int j = 1; j < matrix[i].length; j++)
      //    matrix[i][j].setBackground(CELKLEUR);
  }


  public void maakAlgemeenPanel()
  {
    //het panel waarop de "algemene" knoppen geplaatst gaan worden
    algemeenPanel = new Panel();
    algemeenPanel.setLayout(new GridLayout(2, 2));
    algemeenPanel.setBounds(BREEDTE,
                            9 * (HOOGTE + 1) + 5 * SCHEIDING + 2 * HOOGTE + 10,
                            9 * (BREEDTE + 1) + 5 * SCHEIDING,
                            2 * HOOGTE);
    algemeenPanel.setVisible(true);
    this.add(algemeenPanel, null);

    //voeg een knop toe om een nieuwe puzzel te laten zien
    nieuwePuzzelKnop = new Button("Nieuwe puzzel");
    nieuwePuzzelKnop.setSize(4 * BREEDTE, HOOGTE);
    algemeenPanel.add(nieuwePuzzelKnop, null);
    nieuwePuzzelKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){
        //nieuwePuzzel();
        maakNieuwePuzzelPopup();
        meldingenLabel.setText("");
      }
    });


    //voeg een knop toe om de laatste actie te herstellen
    herstelKnop = new Button("Herstel laatste actie");
    herstelKnop.setSize(4 * BREEDTE, HOOGTE);
    herstelKnop.setEnabled(false); //nog niet in gebruik
    algemeenPanel.add(herstelKnop, null);
    /*
        herstelKnop.addActionListener(new java.awt.event.ActionListener()
        {
          public void actionPerformed(ActionEvent e){
          }
        });
     */

    //voeg een knop toe om eigen toevoegingen aan huidige puzzel
    //te verwijderen
    verwijderEigenToevoegingenKnop =
        new Button("Verwijder eigen toevoegingen");
    verwijderEigenToevoegingenKnop.setSize(4 * BREEDTE, HOOGTE);
    verwijderEigenToevoegingenKnop.setEnabled(false);
    algemeenPanel.add(verwijderEigenToevoegingenKnop, null);
    /*    verwijderEigenToevoegingenKnop.addActionListener(new java.awt.event.ActionListener()
        {
          public void actionPerformed(ActionEvent e){
            //resetApplet(); ??
            puzzel.resetPuzzel();
          }
        });
     */

    //voeg een knop toe om alles te verwijderen
    verwijderAllesKnop =
        new Button("Verwijder alles");
    verwijderAllesKnop.setSize(4 * BREEDTE, HOOGTE);
    verwijderAllesKnop.setEnabled(true);
    algemeenPanel.add(verwijderAllesKnop, null);

    verwijderAllesKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){
        resetApplet();
      }
    });
  }



  public void maakScanPopup(){
    PopupMenu popup = new PopupMenu();
    scanKnop.add(popup);
    for (int k = 1; k < SudokuPuzzel.D; k++)
      popup.add(k + "");
    popup.show(scanKnop,
               scanKnop.getWidth() - BREEDTE, scanKnop.getHeight() / 2);
    //let op: indices in popupmenu van 0..8!!
    for (int itemIndex = 0; itemIndex < SudokuPuzzel.D - 1; itemIndex++){
      popup.getItem(itemIndex).
          addActionListener(new java.awt.event.ActionListener()
      {
        public void actionPerformed(ActionEvent e){
          scanGetal(e);
        }
      });
    }
  }



//HET UITZOEKWERK HIERVAN HOORT NATUURLIJK IN SudokuPuzzel THUIS...
  public void scanGetal(ActionEvent e){
    if (puzzel != null){ //voor alle zekerheid...
      int getal = Integer.parseInt( ( (MenuItem) e.getSource()).getLabel());
      puzzel.setOpnieuwTekenen(false);

      //zet getal op scanKnop
      scanKnop.setLabel("Scan (" + getal + ")");

      //loop heel puzzel.getIngevuld() door op zoek naar getal
      for (int i = 1; i < puzzel.getIngevuld().length; i++){
        for (int j = 1; j < puzzel.getIngevuld()[i].length; j++){
          if (puzzel.getIngevuld()[i][j] == getal){
            //System.out.println("getal " + getal + "ïn cel " + i + j);

            //rij i en kolom j moeten gearceerd worden
            for (int k = 1; k < puzzel.getOpnieuwTekenen()[i].length; k++){
              puzzel.setOpnieuwTekenen(i, k, true); //rij i
              puzzel.setOpnieuwTekenen(k, j, true); //kolom j
            }

            //het vak waarin cel (i,j) ligt moet gearceerd worden
            int startrij = puzzel.bepaalStartVanVak(i);
            int startkolom = puzzel.bepaalStartVanVak(j);
            for (int m = startrij; m < startrij + 3; m++)
              for (int n = startkolom; n < startkolom + 3; n++)
                puzzel.setOpnieuwTekenen(m, n, true);
          }
        }
      }

      //arceer alle velden van puzzel.getOpnieuwTekenen() die op true staan
      for (int i = 1; i < puzzel.getOpnieuwTekenen().length; i++)
        for (int j = 1; j < puzzel.getOpnieuwTekenen()[i].length; j++)
          if (puzzel.getOpnieuwTekenen()[i][j])
            if (nietGemarkeerd(i,j))
              matrix[i][j].setBackground(SCANKLEUR);
            else
              matrix[i][j].setBackground(MARKEERKLEUR);


//DOE DIT LATER EFFICIENTER EN NETTER!!
      //als in een rij maar 1 cel over is die niet ingevuld is of
      //net opnieuw gekleurd is, dan moet die cel een andere kleur krijgen
      for (int i = 1; i < puzzel.getIngevuld().length; i++){
        int teller = 0;
        for (int j = 1; j < puzzel.getIngevuld()[i].length; j++)
          if (!puzzel.isIngevuld(i,j) && !puzzel.getOpnieuwTekenen()[i][j])
            teller++;
        if (teller == 1){
          int k = 1;
          while (puzzel.isIngevuld(i,k) ||
                 puzzel.getOpnieuwTekenen()[i][k])
            k++;
          matrix[i][k].setBackground(SPECIALESCANKLEUR);
          matrix[i][k].setForeground(SPECIALESCANGETALKLEUR);
        }
      }

      //ook zo voor iedere kolom
      for (int j = 1; j < puzzel.getIngevuld().length; j++){
        int teller = 0;
        for (int i = 1; i < puzzel.getIngevuld()[j].length; i++)
          if (!puzzel.isIngevuld(i,j) && !puzzel.getOpnieuwTekenen()[i][j])
            teller++;
        if (teller == 1){
          int k = 1;
          while (puzzel.isIngevuld(k,j) ||
                 puzzel.getOpnieuwTekenen()[k][j])
            k++;
          matrix[k][j].setBackground(SPECIALESCANKLEUR);
          matrix[k][j].setForeground(SPECIALESCANGETALKLEUR);
        }
      }

      //ook zo voor ieder vak
      for (int m = 1; m < puzzel.getIngevuld().length; m+=3)
        for (int n = 1; n < puzzel.getIngevuld()[m].length; n+=3){
          //vak met startcel (m,n)
          int teller = 0;
          for (int i = m; i < m+3; i++)
            for (int j = n; j < n+3; j++)
              if (!puzzel.isIngevuld(i,j) &&
                  !puzzel.getOpnieuwTekenen()[i][j])
                teller++;
          if (teller == 1){
            for (int i = m; i < m+3; i++)
              for (int j = n; j < n+3; j++)
                if (!puzzel.isIngevuld(i,j) &&
                    !puzzel.getOpnieuwTekenen()[i][j]){
                  matrix[i][j].setBackground(SPECIALESCANKLEUR);
                  matrix[i][j].setForeground(SPECIALESCANGETALKLEUR);
                }
          }
        }

    }
    else{
      meldingenLabel.setForeground(MELDINGENKLEUR);
      meldingenLabel.setText("Nog geen puzzel gegeven!");
    }
  }


  public boolean nietGemarkeerd(int i,int j){
    return (i != gemarkeerdeCel.x || j != gemarkeerdeCel.y);
  }


  public void maakSudokuPanel()
  {
    //het panel om de specifieke sudokuknoppen op te zetten
    sudokuPanel = new Panel();
    sudokuPanel.setLayout(new GridLayout(3, 2));
    sudokuPanel.setBounds(12 * BREEDTE, HOOGTE,
                          8 * BREEDTE, 3 * HOOGTE);
    sudokuPanel.setVisible(true);
    this.add(sudokuPanel, null);

    //voeg een knop toe om de kandidaten te tonen of te verbergen
    kandidatenKnop = new Button("Toon alle kandidaten");
    kandidatenKnop.setSize(4 * BREEDTE, HOOGTE);
    kandidatenKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){
        if (! (puzzel == null)){
          if (!kandidatenTonen){ //hij stond op "verbergen"
            toonAlleKandidaten();
            kandidatenTonen = true;
            kandidatenKnop.setLabel("Verberg alle kandidaten");
          }
          else{ //hij stond op "tonen"
            verbergAlleKandidaten();
            kandidatenTonen = false;
            kandidatenKnop.setLabel("Toon alle kandidaten");
          }
        }
        else{
          meldingenLabel.setForeground(MELDINGENKLEUR);
          meldingenLabel.setText("Nog geen puzzel gegeven!");
        }
      }
    });
    sudokuPanel.add(kandidatenKnop, null);

    //voeg een knop toe om te checken op conflicten
    checkKnop = new Button("Check op conflicten");
    checkKnop.setSize(4 * BREEDTE, HOOGTE);
    //checkKnop.setEnabled(false);//nog niet in gebruik
    sudokuPanel.add(checkKnop, null);
    checkKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){
        if (puzzel != null){
          if (!checkAan){ //toon conflicten
            //checkAan = true;
            //checkKnop.setLabel("Zet check uit");
            if (puzzel.checkOpConflicten()){
              checkAan = true;
              checkKnop.setLabel("Zet check uit");
              for (int i = 1; i < puzzel.getOpnieuwTekenen().length; i++)
                for (int j = 1; j < puzzel.getOpnieuwTekenen()[i].length; j++)
                  if (puzzel.getOpnieuwTekenen()[i][j] &&
                      nietGemarkeerd(i,j))
                    matrix[i][j].setBackground(Color.ORANGE);
            }
            else{
              meldingenLabel.setForeground(MELDINGENKLEUR);
              meldingenLabel.setText("Geen conflicten gevonden!");
            }
          }
          else{ //maak alle cellen weer wit, behalve de gemarkeerde
            checkAan = false;
            checkKnop.setLabel("Check op conflicten");
            meldingenLabel.setText("");
            for (int i = 1; i < matrix.length; i++)
              for (int j = 1; j < matrix[i].length; j++)
                if (nietGemarkeerd(i,j))
                  matrix[i][j].setBackground(CELKLEUR);
                else
                  matrix[i][j].setBackground(MARKEERKLEUR);
          }
        }
        else{
          meldingenLabel.setForeground(MELDINGENKLEUR);
          meldingenLabel.setText("Nog geen puzzel gegeven!");
        }
      }
    });

    //voeg een knop toe om de puzzel te "scannen"
    scanKnop = new Button("Scan");
    scanKnop.setSize(4 * BREEDTE, HOOGTE);
    //scanKnop.setEnabled(false);//nog niet in gebruik
    sudokuPanel.add(scanKnop, null);
    scanKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){
        if (puzzel != null){
          if (!scanAan){
            scanAan = true;
            maakScanPopup();
            meldingenLabel.setText("");
          }
          else{
            scanAan = false;
            scanKnop.setLabel("Scan");
            for (int i = 1; i < matrix.length; i++)
              for (int j = 1; j < matrix[i].length; j++)
                if (nietGemarkeerd(i,j))
                  matrix[i][j].setBackground(CELKLEUR);
                else
                  matrix[i][j].setBackground(MARKEERKLEUR);
          }
        }
        else{
          meldingenLabel.setForeground(MELDINGENKLEUR);
          meldingenLabel.setText("Nog geen puzzel gegeven!");
        }
      }
    });

    //voeg een knop toe om een "match" te laten arceren
    matchKnop = new Button("Match");
    matchKnop.setSize(4 * BREEDTE, HOOGTE);
    //matchKnop.setEnabled(false); //nog niet in gebruik
    sudokuPanel.add(matchKnop, null);
    matchKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){

        if (puzzel != null){
          if (!matchAan){
            matchAan = true;
            matchList.clear();
            meldingenLabel.setText("");
            //let op: addAll gebruikt de volgorde die de iterator ook
            //gebruikt (en dus misschien niet je eigen volgorde)
            matchList.addAll(puzzel.zoekMatches());
            if (!matchList.isEmpty())
              toonVolgendeMatch(matchList);
            else{//er zijn geen matches gevonden
              matchAan = false;
              meldingenLabel.setForeground(MELDINGENKLEUR);
              meldingenLabel.setText("Er zijn geen matches gevonden");
              //maak alle cellen weer wit
              for (int i = 1; i < matrix.length; i++)
                for (int j = 1; j < matrix[i].length; j++)
                  geefCelStandaardKleuren(i,j);
                  /*if (nietGemarkeerd(i,j))
                    matrix[i][j].setBackground(CELKLEUR);
                  else
                    matrix[i][j].setBackground(MARKEERKLEUR);*/
            }
          }
          else//matchList is al gevuld
            if (!matchList.isEmpty()){
              //matchAan = false;
              toonVolgendeMatch(matchList);
            }
            else{//er zijn geen matches (meer)
              matchAan = false;
              meldingenLabel.setForeground(MELDINGENKLEUR);
              meldingenLabel.setText("Er zijn geen matches meer");
              //maak alle cellen weer wit
              for (int i = 1; i < matrix.length; i++)
                for (int j = 1; j < matrix[i].length; j++)
                  geefCelStandaardKleuren(i,j);
                  /*if (nietGemarkeerd(i,j))
                    matrix[i][j].setBackground(CELKLEUR);
                  else
                    matrix[i][j].setBackground(MARKEERKLEUR);*/
            }
        }
        else{
          meldingenLabel.setForeground(MELDINGENKLEUR);
          meldingenLabel.setText("Nog geen puzzel gegeven!");
        }
      }
    });

    //voeg een knop toe om een "submatch" te laten arceren
    submatchKnop =
        new Button("Submatch");
    submatchKnop.setSize(4 * BREEDTE, HOOGTE);
    submatchKnop.setEnabled(false); //nog niet in gebruik
    sudokuPanel.add(submatchKnop, null);
    /*
        submatchKnop.addActionListener(new java.awt.event.ActionListener()
        {
          public void actionPerformed(ActionEvent e){
          }
        });
     */
    //voeg een knop toe om een cel te kunnen markeren
    markeerKnop =
        new Button("Markeer");
    markeerKnop.setSize(4 * BREEDTE, HOOGTE);
    //markeerKnop.setEnabled(false); //nog niet in gebruik
    sudokuPanel.add(markeerKnop, null);
    markeerKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){
        if (puzzel != null){
          markeerAan = !markeerAan;
          if (markeerAan){
            markeerKnop.setLabel("Zet markering uit");
            meldingenLabel.setForeground(Color.BLACK);
            meldingenLabel.setText("Klik in de te markeren cel");
            //nu wachten de MouseListeners van de cellen op een klik
            //hoe zorg je dat die klik ook komt?????
          }
          else{
            celGemarkeerd = false;
            gemarkeerdeCel.x = 0;
            gemarkeerdeCel.y = 0;
            markeerKnop.setLabel("Markeer");
            meldingenLabel.setForeground(MELDINGENKLEUR);
            meldingenLabel.setText("");
            //maak ALLE cellen weer wit
            //JE KUNT NU OOK GEMARKEERDECEL.X EN .Y GEBRUIKEN?!
            for (int i = 1; i < matrix.length; i++)
              for (int j = 1; j < matrix[i].length; j++)
                matrix[i][j].setBackground(CELKLEUR);
          }
        }
        else{
          meldingenLabel.setForeground(MELDINGENKLEUR);
          meldingenLabel.setText("Nog geen puzzel gegeven!");
        }
      }
    });
  }


  /**
   * Geeft matrix[i][j] de standaardkleuren, d.w.z. standaard achtergrondkleur
   * (behalve als de cel gemarkeerd is: dan markeerkleur als achtergrond)
   * en de standaard cijfer- of kandidatenkleur.
   */
  public void geefCelStandaardKleuren(int i, int j){
    if (nietGemarkeerd(i,j)){
      if (!puzzel.isIngevuld(i, j))
        matrix[i][j].geefKleuren(CELKLEUR, KANDIDATENKLEUR);
      else
        matrix[i][j].geefKleuren(CELKLEUR, CIJFERKLEUR);
      }
    else{
      if (!puzzel.isIngevuld(i, j))
        matrix[i][j].geefKleuren(MARKEERKLEUR, KANDIDATENKLEUR);
      else
        matrix[i][j].geefKleuren(MARKEERKLEUR, CIJFERKLEUR);
    }
  }

  public void toonVolgendeMatch(ArrayList lijst){
    if (!lijst.isEmpty()){
      //maak eerst alle cellen weer wit, behalve de gemarkeerde
      for (int i = 1; i < matrix.length; i++)
        for (int j = 1; j < matrix[i].length; j++)
          geefCelStandaardKleuren(i,j);
          /*if (nietGemarkeerd(i,j)){
            if (!puzzel.isIngevuld(i, j))
              matrix[i][j].geefKleuren(CELKLEUR, KANDIDATENKLEUR);
            else
              matrix[i][j].geefKleuren(CELKLEUR, CIJFERKLEUR);
          }
          else
            matrix[i][j].setBackground(MARKEERKLEUR);
         */
      //iterator over eerste match in matchList
      Iterator it = ((MijnSet)(lijst.get(0))).iterator();
      while (it.hasNext()){
        Point cel = (Point)(it.next());
        int i = cel.x;
        int j = cel.y;
        matrix[i][j].geefKleuren(MATCHKLEUR,MATCHGETALKLEUR);
        //matrix[i][j].setBackground(MATCHKLEUR);
      }
      //System.out.println("match " + ((MijnSet)(lijst.get(0))).toString());
      lijst.remove(0);
    }
  }

  public void maakMeldingenLabel()
  {
    //voeg een label toe om de gebruiker meldingen te kunnen geven
    meldingenLabel = new Label();
    meldingenLabel.setBounds(3 * BREEDTE, 10 * HOOGTE + 25, 8 * BREEDTE, HOOGTE);
    meldingenLabel.setForeground(MELDINGENKLEUR);
    this.add(meldingenLabel);
  }

  /**
   * Biedt de mogelijkheid om het programma de eerste cel waar (nog) maar
   * 1 kandidaat in staat in te laten vullen, of om deze regel te laten 
   * herhalen tot het niet meer kan.
   */
  public void maakInvulRegel(){
  
    regel1 = new Checkbox("Vul eerste cel die maar 1 kandidaat heeft in");
    regel1.setBounds(12*BREEDTE, 7*HOOGTE, 8*BREEDTE, HOOGTE);
    regel1.setVisible(true);
    this.add(regel1);

    herhaal1 = new Checkbox("Zo vaak mogelijk herhalen");
    herhaal1.setEnabled(false);
    herhaal1.setBounds(12*BREEDTE, 8*HOOGTE - 20, 8*BREEDTE, HOOGTE);
    herhaal1.setVisible(true);
    regel1.addItemListener(new java.awt.event.ItemListener()
    {
      public void itemStateChanged(ItemEvent e){
        if (regel1.getState())
          herhaal1.setEnabled(true);
        else
          herhaal1.setEnabled(false);
      }
    });
    this.add(herhaal1);    
    
        
    regelKnop = new Button("Pas toe");
    regelKnop.setBounds(14*BREEDTE, 9*HOOGTE,4*BREEDTE, HOOGTE);
    regelKnop.setVisible(true);
    this.add(regelKnop, null);
    regelKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){
        boolean keuzeGemaakt = false;

        if (puzzel == null){
          meldingenLabel.setForeground(MELDINGENKLEUR);
          meldingenLabel.setText("Nog geen puzzel gegeven!");
        }
        else{
          meldingenLabel.setText("");
          if (regel1.getState() && !herhaal1.getState()){
            keuzeGemaakt = true;
            pasCelMetEnkeleKandidaatRegelToe(e);
          }
          if (regel1.getState() && herhaal1.getState()){
            keuzeGemaakt = true;
            pasHerhaaldCelMetEnkeleKandidaatRegelToe(e);
          }
          if (!keuzeGemaakt){
              meldingenLabel.setForeground(MELDINGENKLEUR);
              meldingenLabel.setText("Er is geen regel geselecteerd!");
            }
        }
      }
    });
  }

  public void maakRegelsPanel()
  {
    //voeg een panel toe waarop de regels staan die de gebruiker kan selecteren
    //en een knop om ze uit te laten voeren
    regels = new Panel();
    regels.setLayout(new GridLayout(3, 1));
    //regels.setLayout(null);
    regels.setBounds(12 * BREEDTE, 7 * HOOGTE,
                     8 * BREEDTE, 8 * HOOGTE);
    regels.setVisible(true);
    this.add(regels, null);

    //regelsLabel = new Label("Regel:");
    //regels.add(regelsLabel, null);

    //eerste regel
    panel1 = new Panel();
    //panel1.setLayout(new GridLayout(1, 2));
    panel1.setLayout(new GridLayout(2, 1));
    //panel1.setBounds(12*BREEDTE, 7*HOOGTE,
    //                 9*BREEDTE, 2*HOOGTE);
    panel1.setVisible(true);
    regels.add(panel1, null);

    regel1 = new Checkbox("Vul eerste cel die maar 1 kandidaat heeft in");
    panel1.add(regel1, null);

    herhaal1 = new Checkbox("Zo vaak mogelijk herhalen");
    herhaal1.setEnabled(false);
    regel1.addItemListener(new java.awt.event.ItemListener()
    {
      public void itemStateChanged(ItemEvent e){
        if (regel1.getState())
          herhaal1.setEnabled(true);
        else
          herhaal1.setEnabled(false);
      }
    });
    panel1.add(herhaal1, null);

/*    //tweede regel
    panel2 = new Panel();
    panel2.setLayout(new GridLayout(1, 2));
    panel2.setVisible(true);
    regels.add(panel2, null);

//TEKST NOG VERBETEREN
    regel2 = new Checkbox("Zoek \"twee kandidaten voor twee cellen\"");
    panel2.add(regel2, null);

    herhaal2 = new Checkbox("Zo vaak mogelijk herhalen");
    herhaal2.setEnabled(false);
    regel2.addItemListener(new java.awt.event.ItemListener()
    {
      public void itemStateChanged(ItemEvent e){
        if (regel2.getState())
          herhaal2.setEnabled(true);
        else
          herhaal2.setEnabled(false);
      }
    });
    panel2.add(herhaal2, null);
*/
/*    //derde regel
    panel3 = new Panel();
    panel3.setLayout(new GridLayout(1, 2));
    panel3.setVisible(true);
    regels.add(panel3, null);

    regel3 = new Checkbox("Zoek \"drie kandidaten voor drie cellen\"");
    panel3.add(regel3, null);

    herhaal3 = new Checkbox("Zo vaak mogelijk herhalen");
    herhaal3.setEnabled(false);
    regel3.addItemListener(new java.awt.event.ItemListener()
    {
      public void itemStateChanged(ItemEvent e){
        if (regel3.getState())
          herhaal3.setEnabled(true);
        else
          herhaal3.setEnabled(false);
      }
    });
    panel3.add(herhaal3, null);
*/
    //knop om regel(s) toe te passen
    //regelKnop = new Button("Pas regel(s) toe");
    regelKnop = new Button("Pas toe");
    //regelKnop.setBounds(12*BREEDTE, 3*HOOGTE,4*BREEDTE, HOOGTE);
    regelKnop.setVisible(true);
    regels.add(regelKnop, null);
    regelKnop.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e){
        boolean keuzeGemaakt = false;

        if (puzzel == null){
          meldingenLabel.setForeground(MELDINGENKLEUR);
          meldingenLabel.setText("Nog geen puzzel gegeven!");
        }
        else{
          if (regel1.getState() && !herhaal1.getState()){
            keuzeGemaakt = true;
            pasCelMetEnkeleKandidaatRegelToe(e);
          }
          if (regel1.getState() && herhaal1.getState()){
            keuzeGemaakt = true;
            pasHerhaaldCelMetEnkeleKandidaatRegelToe(e);
          }
/*          if (regel2.getState() && !herhaal2.getState()){
            keuzeGemaakt = true;
            pasDubbelTweetalRegelToe(e);
          }
          if (regel2.getState() && herhaal2.getState()){
            keuzeGemaakt = true;
            pasHerhaaldDubbelTweetalRegelToe(e);
          }
*/
/*          if (regel3.getState() && !herhaal3.getState()){
            keuzeGemaakt = true;
            pasDrieKandidatenVoorDrieCellenRegelToe(e);
          }
          if (regel3.getState() && herhaal3.getState()){
            keuzeGemaakt = true;
            pasHerhaaldDrieKandidatenVoorDrieCellenRegelToe(e);
          }
*/          if (!keuzeGemaakt){
              meldingenLabel.setForeground(MELDINGENKLEUR);
              meldingenLabel.setText("Er is geen regel geselecteerd!");
            }
        }
      }
    });
  }

  //Mijn eigen initialisatie van de applet
  public void mijnInit()
  {
    maakMatrix();
    maakAlgemeenPanel();
    maakSudokuPanel();
    maakMeldingenLabel();
    //maakRegelsPanel();
    maakInvulRegel();
  }


  //Reset de applet
  public void resetApplet(){
    for (int i = 1; i < matrix.length; i++)
      for (int j = 1; j < matrix[i].length; j++){
        matrix[i][j].setText("");
        matrix[i][j].setBackground(CELKLEUR);
    }
    kandidatenTonen = false;
    kandidatenKnop.setLabel("Toon alle kandidaten");
    checkAan = false;
    checkKnop.setLabel("Check op conflicten");
    scanAan = false;
    scanKnop.setLabel("Scan");
    matchAan = false;
    //submatch
    markeerAan = false;
    celGemarkeerd = false;
    gemarkeerdeCel.x = 0;
    gemarkeerdeCel.y = 0;
    markeerKnop.setLabel("Markeer");
    regel1.setState(false);
    herhaal1.setState(false);
    //regel2.setState(false);
    //herhaal2.setState(false);
    //regel3.setState(false);
    //herhaal3.setState(false);
    meldingenLabel.setForeground(MELDINGENKLEUR);
    meldingenLabel.setText("");
    puzzel = null; //is dit een goede manier om de oude puzzel te verwijderen?
  }


  //Reset de applet, creeer een nieuwe Sudokupuzzel en toon de gegeven
  //cijfers in de matrix.
  public void nieuwePuzzel(ActionEvent e){
    int item = Integer.parseInt(((MenuItem)e.getSource()).getLabel());
    resetApplet();
    //creeer en toon nieuwe puzzel
    puzzel = new SudokuPuzzel();
    switch (item){
      case 0: puzzel.testVoorbeeld();
              break;
      case 1: puzzel.testVb3();
              break;
      case 2: puzzel.testVb4();
              break;
      case 3: puzzel.testVb5();
              break;
      case 4: puzzel.legePuzzel();
              break;
    }
    toonIngevuld(BEGINKLEUR);
  }

  public void maakNieuwePuzzelPopup(){
    PopupMenu popup = new PopupMenu();
    nieuwePuzzelKnop.add(popup);
    //geef lijstje puzzels op
    //helemaal niet netjes!!!
    for (int k = 0; k < 5; k++)
      popup.add(k + "");
    popup.show(nieuwePuzzelKnop,
               nieuwePuzzelKnop.getWidth() - BREEDTE,
               nieuwePuzzelKnop.getHeight() / 2);
    //let op: indices in popupmenu van 0..8!!
    for (int itemIndex = 0; itemIndex < 5; itemIndex++){
      popup.getItem(itemIndex).
          addActionListener(new java.awt.event.ActionListener()
      {
        public void actionPerformed(ActionEvent e){
          nieuwePuzzel(e);
        }
      });
    }

  }




  //LET OP!
  //Dit is niet echt een regel van Evert, ik doe eerst een heel simpele regel:
  //als er nog maar 1 optie over is in een cel kun je die invullen en
  //de betreffende optie uit de andere cellen in dezelfde rij/kolom/vak
  //halen.
  public void pasCelMetEnkeleKandidaatRegelToe(ActionEvent e){
    if (puzzel != null){
      puzzel.setOpnieuwTekenen(false);
      if (puzzel.vulEersteCelMetEnkeleKandidaatIn()){
        toonIngevuld(CIJFERKLEUR);
        if (kandidatenTonen)
          toonVeranderdeKandidaten();
      }
      else{
        meldingenLabel.setForeground(MELDINGENKLEUR);
        meldingenLabel.setText("Er is geen cel met maar 1 kandidaat gevonden.");
      }
    }
    else{ //ook nog exception?
      meldingenLabel.setForeground(MELDINGENKLEUR);
      meldingenLabel.setText("Nog geen puzzel gegeven!");
    }
  }



  public void pasHerhaaldCelMetEnkeleKandidaatRegelToe(ActionEvent e){
    if (puzzel != null){
      puzzel.setOpnieuwTekenen(false);
      while (puzzel.vulEersteCelMetEnkeleKandidaatIn()){
        //herhaal totdat het niet meer kan
      }
      toonIngevuld(CIJFERKLEUR);
      if (kandidatenTonen)
        toonAlleKandidaten();
    }
    else{ //ook nog exception?
      meldingenLabel.setForeground(MELDINGENKLEUR);
      meldingenLabel.setText("Nog geen puzzel gegeven!");
    }
  }




  public void pasDubbelTweetalRegelToe(ActionEvent e){
    if (puzzel != null){
      puzzel.setOpnieuwTekenen(false);
      if (puzzel.pasKandidatenAanVoorEersteDubbeleTweetal()){
        if (kandidatenTonen)
          toonVeranderdeKandidaten();
      }
      else{
        meldingenLabel.setForeground(MELDINGENKLEUR);
        meldingenLabel.setText("Er is geen dubbel tweetal " +
                               "(met effect) gevonden.");
      }
    }
    else{ //ook nog exception?
      meldingenLabel.setForeground(MELDINGENKLEUR);
      meldingenLabel.setText("Nog geen puzzel gegeven!");
    }
  }


  public void pasHerhaaldDubbelTweetalRegelToe(ActionEvent e){
  }

  public void pasDrieKandidatenVoorDrieCellenRegelToe(ActionEvent e){
    if (puzzel != null){
      puzzel.setOpnieuwTekenen(false);
      if (puzzel.pasKandidatenAan_DrieKandidatenVoorDrieCellen()){
        if (kandidatenTonen)
          toonVeranderdeKandidaten();
      }
      else{
        meldingenLabel.setForeground(MELDINGENKLEUR);
        meldingenLabel.setText("Er is geen drietal in 3 cellen " +
                               "(met effect) gevonden.");
      }
    }
    else{ //ook nog exception?
      meldingenLabel.setForeground(MELDINGENKLEUR);
      meldingenLabel.setText("Nog geen puzzel gegeven!");
    }

  }

  public void pasHerhaaldDrieKandidatenVoorDrieCellenRegelToe(ActionEvent e){
  }

  //Zet de kandidaten van cel (rij,kolom) netjes verdeeld over de cel op het scherm.
  //DAT "NETJES VERDEELD" MOET NOG
  public void toonKandidaten(int rij, int kolom){

    if (!puzzel.isIngevuld(rij, kolom)){
      Iterator h = puzzel.getKandidaten(rij, kolom).iterator();
      matrix[rij][kolom].setText(""); //eerst huidige tekst weghalen
      while (h.hasNext()){
        String waarde = h.next().toString();
        matrix[rij][kolom].setFont(KANDIDATENFONT);
        matrix[rij][kolom].setForeground(KANDIDATENKLEUR);
        matrix[rij][kolom].append(waarde);
      }
    }
  }

  //Zet de kandidatensets die veranderd zijn opnieuw in de matrix (laat de
  //niet veranderde kandidatensets gewoon staan).
  //Welke kandidatensets veranderd (kunnen) zijn wordt afgeleid uit het
  //array opnieuwTekenen.
  public void toonVeranderdeKandidaten(){
    for (int i = 1; i < puzzel.getOpnieuwTekenen().length; i++)
      for (int j = 1; j < puzzel.getOpnieuwTekenen()[i].length; j++)
        if (puzzel.getOpnieuwTekenen()[i][j])
          toonKandidaten(i,j);
  }


  //Zet de huidige kandidaten in alle cellen van de matrix
  public void toonAlleKandidaten(){
    if (puzzel != null)
      for (int i = 1; i < matrix.length; i++)
        for (int j = 1; j < matrix[i].length; j++)
          toonKandidaten(i, j);
    else{
      meldingenLabel.setForeground(MELDINGENKLEUR);
      meldingenLabel.setText("Nog geen puzzel gegeven!");
    }
  }

  //Haalt alle kandidaten van het scherm (laat de ingevulde cijfers wel staan).
  public void verbergAlleKandidaten(){
    if (puzzel != null)
      for (int i = 1; i < matrix.length; i++)
        for (int j = 1; j < matrix[i].length; j++)
          if (!puzzel.isIngevuld(i, j))
            matrix[i][j].setText("");
  }

  //Zet de op dit moment bekende cijfers in de betreffende cellen van
  //de matrix.
  public void toonIngevuld(Color kleur){
    for (int i = 1; i < puzzel.getIngevuld().length; i++)
      for (int j = 1; j < puzzel.getIngevuld()[i].length; j++)
        if (puzzel.isIngevuld(i, j)){
          matrix[i][j].setFont(CIJFERFONT);
          matrix[i][j].setForeground(kleur);
          matrix[i][j].setText(" " + puzzel.getCijfer(i, j));
        }
  }



  //Initialize the applet
  public void init()
  {
    try
    {
      jbInit();
      mijnInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception
  {
  }

  //Get Applet information
  public String getAppletInfo()
  {
    return "SudokuApplet";
  }

  //Get parameter info
  public String[][] getParameterInfo()
  {
    return null;
  }
}
