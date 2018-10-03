import java.awt.*;
/**
 *Een TextArea met de mogelijkheid de coordinaten van de cel die
 *hij representeert erbij op te slaan.
 */
public class SudokuTextArea extends TextArea{
  public int rij;
  public int kolom;
  public TextArea cel;

  public SudokuTextArea(){
    super();
  }

  public SudokuTextArea(String text, int rows, int columns, int scrollbars){
    super(text, rows, columns, scrollbars);
  }

  public void geefKleuren(Color background, Color foreground){
    this.setBackground(background);
    this.setForeground(foreground);
  }
}
