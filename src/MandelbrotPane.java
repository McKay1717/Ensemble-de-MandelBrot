import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Zone "image" de l'ensemble de MendelBrot
 * @author Nicolas I., Marie B.
 *
 */
public class MandelbrotPane extends JPanel implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7246875999457604140L;
	private ArrayList<ColoredPoint> set;
	private Mandelbrot mb = new Mandelbrot();
	private int MAX = 20;
	private Image image;
	private ToolbarPane tpane;
	private Point p1;
	private Point p2;

	/**
	 * Initialize this pane
	 */
	public MandelbrotPane()
	{
		set = calculate();
		this.addMouseListener(this);
	}
	/**
	 * Permet la comunication entre les objet
	 * @param t La boite � outil
	 */
	public void setTPane(ToolbarPane t)
	{
		tpane = t;
		mb.setTPane(tpane);
	}


	/**
	 * @return Le reel Minimal (X min)
	 */
	public double GetMINRE()
	{
		return mb.GetMINRE();
	}

	/**
	 * @param s Le nouveau Reel Minimum
	 */
	public void SetMINRE( double s)
	{
		mb.SetMINRE(s);
	}


	/**
	 * @return L'imaginaire Minimal (X min)
	 */
	public double GetMINIM()
	{
		return mb.GetMINIM();
	}

	/**
	 * @param s Le nouveau imaginaire Minimum
	 */
	public void SetMINIM( double s)
	{
		mb.SetMINIM(s);
	}

	/**
	 * @return Le reel Maximal (X MAX)
	 */
	public double GetMAXRE()
	{
		return mb.GetMAXRE();
	}

	/**
	 * @param s Le nouveau Reel Maximum
	 */
	public void SetMAXRE( double s)
	{
		mb.SetMAXRE(s);
	}


	/**
	 * @return L'imaginaire Maximal (X MAX)
	 */
	public double GetMAXIM()
	{
		return mb.GetMAXIM();
	}

	/**
	 * @param s Le nouveau imaginaire Maximum
	 */
	public void SetMAXIM( double s)
	{
		mb.SetMAXIM(s);
	}


	/**
	 * 
	 * @return La largeur de l'image en pixels
	 */
	public int getImageWidth()
	{
		return mb.ImageWidth;
	}

	/**
	 * 
	 * @return La hauteur de l'image en pixels
	 */
	public int getImageHeight()
	{
		return mb.ImageHeight;
	}

	/**
	 * R�initialiser la fen�tre par d�faut et re-rendre
	 */
	public void reset()
	{
		mb.reset();
		render();
	}

	/**
	 * Change la taille de l"image
	 * @param w The new width
	 * @param h The new height
	 */
	public void changeSize(int w, int h)
	{
		mb.changeSize(w, h);
		this.setSize(w, h);
		render();
	}

	/**
	 * D�finit le facteur de zoom
	 * @param s le nouveau facteur
	 */
	public void setZoomFactor(int s)
	{
		mb.setZoomFactor(s);
	}

	/**
	 * 
	 * @return Le facteur de zoom utilis�
	 */
	public int getZoomFactor()
	{

		return mb.getZoomFactor();
	}



	/**
	 * Cr�e l'image et la peint dans le volet
	 * Dessine une bordure noire 1px sur le bas du volet
	 */
	public void paint(Graphics g)
	{
		image = createImage();
		g.drawImage(image, 0, 0, null);
		g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
	}

	/**
	 * 
	 * @return Les it�rations max utilis�es pour produire l'ensemble
	 */
	public int getMaxIterations()
	{
		return MAX;
	}

	/**
	 * D�finissez les it�rations max qui seront utilis�s dans la production de l'ensemble
	 * @param max
	 */
	public void setMaxIterations(int max)
	{
		MAX = max;
	}



	/**
	 * Calculer l'ensemble et repeindre
	 */
	public void render()
	{

		set = calculate();
		this.repaint();
	}

	/**
	 * 
	 * @return L'image � l'�cran
	 */
	public Image getImage()
	{
		return image;
	}

	/**
	 * Cr�er le Buffer Image et le retourne sous forme d'image
	 * @return Une image contenant l'ensemble
	 */
	private Image createImage()
	{
		BufferedImage theImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D)theImage.createGraphics();
		for(ColoredPoint p: set)
		{
			g2d.setColor(p.getColor());
			g2d.drawRect(p.x, p.y, 1, 1);
		}
		return theImage;
	}


	/**
	 * Zoom lorsque la souris est cliqu� gauche
	 * Zoom arri�re lorsque le droit de la souris est cliqu�
	 */
	public void mouseClicked(MouseEvent click) {
		if(click.getButton() == MouseEvent.BUTTON1)
		{
			mb.changeView(click.getPoint(), true);
		}
		else if(click.getButton() == MouseEvent.BUTTON3)
		{
			mb.changeView(click.getPoint(), false);
		}
		else
		{
			return;
		}
		set = calculate();
		this.repaint();
	}

	/**
	 * 
	 * @return Une liste de tableaux contenant tous les ColoredPoints � restituer
	 */
	private ArrayList<ColoredPoint> calculate()
	{
		//long start = System.currentTimeMillis();
		ArrayList<ColoredPoint> ret = mb.calculate(MAX);
		//System.out.println("Total iterations: " + (MAX*mb.ImageHeight*mb.ImageWidth) + " in " + (System.currentTimeMillis() - start) + "ms");
		return ret;
	}

	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
