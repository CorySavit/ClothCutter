/*
 * Cory Savit
 * cas241@pitt.edu
 * CS1541
 * Project2: ClothCutter
 */
import java.awt.* ;
import javax.swing.* ;


public class PaintPanel extends JPanel {

  public Color BACKGROUND = Color.blue;
  public Color CUTS = Color.red;
  public Color GARMENTLINE = Color.black;
  public Color GARMENT = Color.yellow;

  public int width, height, pixels;


  public PaintPanel(int w, int h, int p) {
	this.pixels = p;
	this.width = w * p ;
    this.height = h * p;
    setPreferredSize(new Dimension(w*p,h*p));
  }

  public void drawGarment(Garment c){
	  Graphics g = getGraphics();
	  g.setColor(GARMENTLINE);
	  g.drawRect(c.x * pixels, c.y* pixels, c.width* pixels, c.height * pixels);
	  g.setColor(GARMENT);
	  g.fillRect(c.x * pixels, c.y* pixels, c.width* pixels, c.height * pixels);

  }
  public void drawCut(Cut c){
	Graphics g = getGraphics();
	g.setColor(CUTS);
	g.drawLine(c.x1 * pixels, c.y1 * pixels, c.x2 * pixels, c.y2 * pixels);
  }
  


  public void paintComponent(Graphics g) {
    super.paintComponent(g) ;
    g.setColor(BACKGROUND) ;
    g.fillRect(0,0,width,height) ;
  }

}
