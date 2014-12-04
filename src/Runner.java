import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.*;

import observer.Observable;
 
/**
 * Crée la fenêtre principale et l'exécute
 * @author Nicolas I.
 *
 */
public class Runner extends JFrame implements Observable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3574552434097247454L;
	public static final int ImageWidth = 600, ImageHeight = 600;
	private MandelbrotPane mPane;
	protected ToolbarPane tPane;
	private ToolboxDialog tbd;
	
	/**
	 * Initialiser la trame
	 * @throws IOException
	 */
	public Runner() throws IOException
	{
		super("Mandelbrot");

		this.setLayout(null);
		this.setSize(ImageWidth, ImageHeight+210);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mPane = new MandelbrotPane();
		mPane.setSize(ImageWidth, ImageHeight);
		mPane.setLocation(0, 0);
		
		tPane = new ToolbarPane(ImageWidth, 220, mPane, this, true);
		tPane.setLocation(0, mPane.getHeight()+5);
		
		tbd=  new ToolboxDialog(this, false);
		tbd.setSize(tPane.getWidth(), tPane.getHeight());
		
		this.add(tPane);
		this.add(mPane);
		this.repaint();
	}
	
	/**
	 * Classe principale de coureur
	 * @param args Unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		Runner win = new Runner();
		win.setResizable(true);
		win.setVisible(true);
	}

	/**
	 * Contrôle ce qui arrive quand la boîte à outils est (dé) / Attaché
	 */
	public void update()
	{
		boolean docked = tPane.getDocked();
		//si Attaché, déconnectez et créer le toolboxdialog
		if(docked)
		{
			tPane.setLocation(0, 0);
			tPane.setDocked(false);
			tbd.add(tPane);
			this.remove(tPane);
			this.setSize(mPane.getImageWidth(), mPane.getImageHeight()+this.getInsets().top+this.getInsets().bottom);
			this.setPreferredSize(this.getSize());
			tbd.setVisible(true);
		}
		//si non Attaché, ancrer et masquer le toolboxdialog
		else
		{
			tPane.setLocation(0, mPane.getHeight()+5);
			tPane.setDocked(true);
			this.add(tPane);
			this.setSize(mPane.getImageWidth(), mPane.getImageHeight()+210);
			this.setPreferredSize(this.getSize());
			tbd.setVisible(false);
		}
	}	
	
	
	/**
	 * Maintient le ToolbarPane lorsque l'utilisateur la détache
	 * @author Nicolas I.
	 *
	 */
	class ToolboxDialog extends JDialog implements WindowListener
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3100931086078740000L;
		Observable obs;
		public ToolboxDialog(JFrame owner, boolean modal)
		{
			super(owner, modal);
			obs = (Observable)owner;
			this.setLayout(null);
			this.repaint();
			this.addWindowListener(this);
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		}
		
		public void windowActivated(WindowEvent arg0) {	}

		public void windowClosed(WindowEvent arg0) {}

		// Informer l'observateur parent que cette fenêtre va étre fermer
		public void windowClosing(WindowEvent arg0) {
			obs.update();
		}

		public void windowDeactivated(WindowEvent arg0) {}

		public void windowDeiconified(WindowEvent arg0) {}

		public void windowIconified(WindowEvent arg0) {}

		public void windowOpened(WindowEvent arg0) {}
	}
}


