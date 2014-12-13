import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Calcule De l'Ensemble de Mendelbrot
 * @author Nicolas I., Marie B.
 *
 */
public class Mandelbrot
{
	public int ImageHeight = 600,ImageWidth = 600;

	//La valeur minimale et maximale réelle
	private double MINRE = -2.0;
	private double MAXRE = 1.0;
	//Les valeurs minimum et maximum imaginaires
	private double MINIM = -1.2;
	private double MAXIM = MINIM+(MAXRE-MINRE)*ImageHeight/ImageWidth;
	//la distance entre x et les coordonnées y adjacentes
	protected double RE_FACTOR = (MAXRE-MINRE)/(ImageWidth-1);
	protected double IM_FACTOR = (MAXIM-MINIM)/(ImageHeight - 1);
	private int zoom;
	private double PasX;
	private double PasY;
	private double zx;
	private double zy;
	private double cX ;
	private double cY; 
	private double tmp;
	private int iter;

	private ToolbarPane tpane;



	/**
	 *Les valeurs par défaut
	 */
	public Mandelbrot()
	{
		zoom = 2;
	}



	/**
	 * Réinitialiser les valeurs par défaut
	 */
	public void reset()
	{
		MINRE = -2.0;
		MAXRE = 1.0;
		MINIM = -1.2;
		refactor();
	}

	/**
	 * Réglez la largeur et la hauteur de l'ensemble
	 *@param W La nouvelle largeur
	 *@param H la nouvelle hauteur
	 */
	public void changeSize(int w, int h)
	{
		ImageHeight = h;
		ImageWidth = w;
		refactor();
	}

	/**
	 *
	 * @param s Le nouveau facteur de zoom
	 */
	public void setZoomFactor(int s)
	{
		zoom = s;
	}

	/**
	 * 
	 * @return Le facteur de zoom
	 */
	public int getZoomFactor()
	{
		return zoom;
	}

	
	/**
	 * @return Le reel Minimal (X min)
	 */
	public double GetMINRE()
	{
		return MINRE;
	}

	/**
	 * @param s Le nouveau Reel Minimum
	 */
	public void SetMINRE( double s)
	{
		MINRE = s;
	}
	

	/**
	 * @return L'imaginaire Minimal (X min)
	 */
	public double GetMINIM()
	{
		return MINIM;
	}

	/**
	 * @param s Le nouveau imaginaire Minimum
	 */
	public void SetMINIM( double s)
	{
		MINIM = s;
	}

	/**
	 * @return Le reel Maximal (X MAX)
	 */
	public double GetMAXRE()
	{
		return MAXRE;
	}

	/**
	 * @param s Le nouveau Reel Maximum
	 */
	public void SetMAXRE( double s)
	{
		MAXRE = s;
	}
	

	/**
	 * @return L'imaginaire Maximal (X MAX)
	 */
	public double GetMAXIM()
	{
		return MAXIM;
	}

	/**
	 * @param s Le nouveau imaginaire Maximum
	 */
	public void SetMAXIM( double s)
	{
		MAXIM = s;
	}

	/**
	 * Permet la comunication entre les objet
	 * @param t La boite à outil
	 */
	public void setTPane(ToolbarPane t)
	{
		tpane = t;
	}
	/**
	 * Gestion du Zoom
	 *@param ClickPoint L'emplacement qui a été cliqué
	 *@param ZoomIn Si ce est vrai, zoom
	 */
	public void changeView(Point clickPoint, boolean zoomIn)
	{
		double c_im = MAXIM - (clickPoint.y) * IM_FACTOR;
		double c_re = MINRE + clickPoint.x * RE_FACTOR;	
		double rediff, imdiff;
		if(zoomIn)
		{
			rediff = (MAXRE - MINRE)/zoom;
			imdiff = (MAXIM - MINIM)/zoom ;
		}
		else
		{
			rediff = (MAXRE - MINRE)*zoom;
			imdiff = (MAXIM - MINIM)*zoom;
		}
		MINRE= c_re - (rediff / 2);
		MAXRE = c_re + (rediff / 2);
		MINIM = c_im - imdiff/2;
		refactor();

	}



	/**
	 * Définit les facteurs et les valeurs de MAXIM
	 */
	private void refactor()
	{
		RE_FACTOR = (MAXRE-MINRE)/(ImageWidth-1);
		MAXIM = MINIM+(MAXRE-MINRE)*ImageHeight/ImageWidth;
		IM_FACTOR = (MAXIM-MINIM)/(ImageHeight - 1);
		tpane.update();
	}

	/**
	 * Algorithme de Calcul et de colorisation
	 * @author Nicolas I.
	 * @author Mr. Cury
	 * @param MaxIterations  Les itérations maximales avant de déterminer si un point est dans l'ensemble
	 * @return ArrayList 
	 */
	public ArrayList<ColoredPoint> calculate(int MaxIterations)
	{
		ArrayList<ColoredPoint> ret = new ArrayList<ColoredPoint>();
		PasX = (MAXRE - MINRE)/(ImageHeight);
		PasY = (MINIM - MAXIM)/(ImageHeight);
		for (int y = 0; y < ImageHeight; y++) {
			for (int x = 0; x < ImageWidth; x++) {
				zx = zy = 0;
				cX = MINRE + PasX*x;
				cY = MINIM - PasY*y;
				iter = MaxIterations;
				while (zx * zx + zy * zy < 4 && iter > 0) {
					tmp = zx * zx - zy * zy + cX; 
					zy = 2.0 * zx * zy + cY;
					zx = tmp;
					iter--;

				}
				while (iter > 27) iter = iter%27;
				Color color = null;
				if(iter == 0)  color = new Color( 0x00 | 0x00<<8 | 0x00<<16);
				if(iter == 1) color = new Color(0x00 | 0x00<<8 | 0x80<<16);
				if(iter == 2) color = new Color(0x00 | 0x00<<8 | 0xFF<<16);
				if(iter == 3) color = new Color(0x00 | 0x80<<8 | 0x00<<16);
				if(iter == 4) color = new Color(0x00 | 0x80<<8 | 0x80<<16);
				if(iter == 5) color = new Color(0x00 | 0x80<<8 | 0xFF<<16);
				if(iter == 6) color = new Color(0x00 | 0xFF<<8 | 0x00<<16);
				if(iter == 7) color = new Color(0x00 | 0xFF<<8 | 0x80<<16);
				if(iter == 8) color = new Color(0x00 | 0xFF<<8 | 0xFF<<16);
				if(iter == 9) color = new Color(0x80 | 0x00<<8 | 0x00<<16);
				if(iter == 10) color = new Color(0x80 | 0x00<<8 | 0x80<<16);
				if(iter == 11) color = new Color(0x80 | 0x00<<8 | 0xFF<<16);
				if(iter == 12) color = new Color(0x80 | 0x80<<8 | 0x00<<16);
				if(iter == 13) color = new Color(0x80 | 0x80<<8 | 0x80<<16);
				if(iter == 14) color = new Color(0x80 | 0x80<<8 | 0xFF<<16);
				if(iter == 15) color = new Color(0x80 | 0xFF<<8 | 0x00<<16);
				if(iter == 16) color = new Color(0x80 | 0xFF<<8 | 0x80<<16);
				if(iter == 17) color = new Color(0x80 | 0xFF<<8 | 0xFF<<16);
				if(iter == 18) color = new Color(0xFF | 0x00<<8 | 0x00<<16);
				if(iter == 19) color = new Color(0xFF | 0x00<<8 | 0x80<<16);
				if(iter == 20) color = new Color(0xFF | 0x00<<8 | 0xFF<<16);
				if(iter == 21) color = new Color(0xFF | 0x80<<8 | 0x00<<16);
				if(iter == 22) color = new Color(0xFF | 0x80<<8 | 0x80<<16);
				if(iter == 23) color = new Color(0xFF | 0x80<<8 | 0xFF<<16);
				if(iter == 24) color = new Color(0xFF | 0xFF<<8 | 0x00<<16);
				if(iter == 25) color = new Color(0xFF | 0xFF<<8 | 0x80<<16);
				if(iter == 26) color = new Color(0xFF | 0xFF<<8 | 0xFF<<16);
				ret.add(new ColoredPoint(x, y,	color));
			}
		}
		return ret;
	}
}
