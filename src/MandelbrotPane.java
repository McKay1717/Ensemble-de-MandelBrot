import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Zone "image" de l'ensemble de MendelBrot
 * @author Nicolas I.
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

	/**
	 * Initialize this pane
	 */
	public MandelbrotPane()
	{
		set = calculate();
		this.addMouseListener(this);
	}

	/**
	 * 
	 * @return Quel mode de Math.exp est utilisé (true est exp approximative rapide, faux est lente exp exacte)
	 */
	public boolean getExpMode()
	{
		return mb.getExpMode();
	}

	/**
	 * 
	 * @param b Si b est vrai, alors l'agorithme va utiliser une version rapide de Math.exp
	 */
	public void useFastExp(boolean b)
	{
		mb.useFastExp(b);
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
	 * Réinitialiser la fenêtre par défaut et re-rendre
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
	 * Définit le facteur de zoom
	 * @param s le nouveau facteur
	 */
	public void setZoomFactor(int s)
	{
		mb.setZoomFactor(s);
	}

	/**
	 * 
	 * @return Le facteur de zoom utilisé
	 */
	public int getZoomFactor()
	{
		return mb.getZoomFactor();
	}

	/**
	 * Crée l'image et la peint dans le volet
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
	 * @return Les itérations max utilisées pour produire l'ensemble
	 */
	public int getMaxIterations()
	{
		return MAX;
	}

	/**
	 * Définissez les itérations max qui seront utilisés dans la production de l'ensemble
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
	 * @return L'image à l'écran
	 */
	public Image getImage()
	{
		return image;
	}

	/**
	 * Créer le Buffer Image et le retourne sous forme d'image
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
	 * Zoom lorsque la souris est cliqué gauche
	 * Zoom arrière lorsque le droit de la souris est cliqué
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
	 * @return Une liste de tableaux contenant tous les ColoredPoints à restituer
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
