import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.*;

import observer.Observable;

/**
 * Le ToolBarPane détient la totalité des options pour manipuler le MandelbrotPane y compris:
 * Changer la taille d'image
 * Modifier le nombre d'itérations max
 * Changer le schéma de couleurs
 * Changer le facteur de zoom
 * Enregistrer l'image actuelle
 * Remise à l'affichage par défaut
 * POPOUT la barre d'outils
 * Re-rendu de l'image
 * Sélection entre une mise en œuvre approximatives et exacte de Math.exp ()
 *
 *@author Nicolas I., Marie B.
 *
 */
public class ToolbarPane extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1413862779872127736L;
	private JButton render, save, resize, reset, popout;
	private JLabel maxLabel, colorLabel, zoomLabel, dimensionLabel, reel1Label, img1Label;
	private JFormattedTextField maxField, zoomField, wField, hField, reel1Field, img1Field;
	private MandelbrotPane mPane;
	private JFrame parent;
	private Observable obs;
	private boolean docked;
	private JLabel reel2Label;
	private JFormattedTextField reel2Field;
	private JLabel img2Label;
	private JFormattedTextField img2Field;
	private DecimalFormatSymbols dfs;
	private DecimalFormat dFormat;



	/**
	 * Initialise tout et ajoute à la Pane (Taille Indefini pour le momant)
	 * Utilise le positionnement absolu
	 *@param W La largeur de ce volet
	 *@param H La hauteur de ce volet
	 *@param Mpane Le MandelbrotPane qui sera mis à jour
	 *@param Parent Le parent 
	 *@param docked Attaché ou non,
	 */
	public ToolbarPane(int w, int h, MandelbrotPane mPane, JFrame parent, boolean docked)
	{
		this.docked = docked;
		this.setSize(w, h);
		this.setLayout(null);
		this.mPane = mPane;
		this.parent = parent;
		obs = (Observable)parent;

		render = new JButton("Re Calculer");
		render.addActionListener(this);

		save = new JButton("Sauvegarder");
		save.addActionListener(this);

		reset = new JButton("Reinitialiser");
		reset.addActionListener(this);

		popout = new JButton("^");
		popout.addActionListener(this);

		maxLabel = new JLabel("Max Iterations:");

		colorLabel = new JLabel("Modificateur de couleur:");

		NumberFormat format = NumberFormat.getNumberInstance();

		format.setGroupingUsed(false);
		maxField = new JFormattedTextField(format);
		maxField.setValue(mPane.getMaxIterations());


		zoomLabel = new JLabel("Ratio de Zoom:");

		zoomField = new JFormattedTextField(format);
		zoomField.setValue(mPane.getZoomFactor());

		dimensionLabel = new JLabel("Largeur - Hauteur");

		wField = new JFormattedTextField(format);
		wField.setValue(mPane.getImageWidth());

		hField = new JFormattedTextField(format);
		hField.setValue(mPane.getImageHeight());

		resize = new JButton("Redimensioner");
		resize.addActionListener(this);


		dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.'); //separator for the decimals
		dfs.setGroupingSeparator(','); //separator for the thousands
		dFormat = new DecimalFormat ("#0.##", dfs);
		
		reel1Label = new JLabel("Réel 1:");

		reel1Field = new JFormattedTextField(dfs);
		reel1Field.setValue(mPane.GetMINRE());

		img1Label = new JLabel("Imaginaire 1:");

		img1Field = new JFormattedTextField(dfs);
		img1Field.setValue(mPane.GetMINIM());

		reel2Label = new JLabel("Réel 2:");

		reel2Field = new JFormattedTextField(dfs);
		reel2Field.setValue(mPane.GetMAXRE());

		img2Label = new JLabel("Imaginaire 2:");

		img2Field = new JFormattedTextField(dfs);
		img2Field.setValue(mPane.GetMAXIM());

		initialize();

		this.add(popout);
		this.add(render);
		this.add(save);
		this.add(maxLabel);
		this.add(maxField);

		this.add(zoomLabel);
		this.add(zoomField);
		this.add(dimensionLabel);
		this.add(wField);
		this.add(hField);
		this.add(resize);
		this.add(reset);
		this.add(reel1Field);
		this.add(reel1Label);
		this.add(img1Field);
		this.add(img1Label);
		this.add(reel2Field);
		this.add(reel2Label);
		this.add(img2Field);
		this.add(img2Label);

	}

	/**
	 * 
	 * @return Attaché ou non
	 */
	public boolean getDocked()
	{
		return docked;
	}

	/**
	 * Detache ou Attache
	 * @param d 
	 */
	public void setDocked(boolean d)
	{
		docked = d;
	}

	/**
	 * Gère toutes les actions des boutons
	 */
	public void actionPerformed(ActionEvent event) {
		//Rendu avec toutes les modifications appropriées
		if(event.getSource() == render)
		{
			mPane.setMaxIterations(Math.abs(Integer.parseInt(maxField.getText())));
			mPane.setZoomFactor(Math.abs(Integer.parseInt(zoomField.getText())));
			mPane.SetMINRE(Double.parseDouble(reel1Field.getText()));
			mPane.SetMINIM(Double.parseDouble(img1Field.getText()));
			mPane.SetMAXRE(Double.parseDouble(reel2Field.getText()));
			mPane.SetMAXIM(Double.parseDouble(img2Field.getText()));

			mPane.render();
		}
		//Sauvegardez l'image
		else if(event.getSource() == save)
		{
			try {
				String name = "output";
				File f;
				for(int i = 0; ; i++)
				{
					f = new File(name + i +".png");
					if(!f.exists())
						break;
				}
				ImageIO.write((RenderedImage) mPane.getImage(), "png", f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if(event.getSource() == resize)
		{
			mPane.changeSize(Math.abs(Integer.parseInt(wField.getText())), Math.abs(Integer.parseInt(hField.getText())));	
			if(docked)
			{
				parent.setSize(mPane.getImageWidth(), mPane.getImageHeight()+210);
				this.setLocation(0, mPane.getHeight()+5);
				initialize();
			}
			else
			{
				parent.setSize(mPane.getImageWidth(), mPane.getImageHeight()+parent.getInsets().top+parent.getInsets().bottom);
			}
		}

		// réinitialiser la vue
		else if(event.getSource() == reset)
		{
			mPane.reset();
			reel1Field.setValue(mPane.GetMINRE());
			img1Field.setValue(mPane.GetMINIM());
			reel2Field.setValue(mPane.GetMAXRE());
			img2Field.setValue(mPane.GetMAXIM());
		}
		// détacher ce volet
		else if(event.getSource() == popout)
		{
			obs.update();
		}
	}
	/**
	 * Définit la taille et l'emplacement de tous les composants
	 */
	private void initialize()
	{			
		popout.setSize(60, 25);
		popout.setLocation(getWidth() - popout.getWidth() - 3, 75);

		maxLabel.setSize(125, 25);
		maxLabel.setLocation(0, 5);

		colorLabel.setSize(125, 25);
		colorLabel.setLocation(0, maxLabel.getY() + 35);

		maxField.setSize(125, 25);
		maxField.setLocation(maxLabel.getX() + maxLabel.getWidth(), maxLabel.getY());	



		zoomLabel.setSize(125, 25);
		zoomLabel.setLocation(0, colorLabel.getY());

		zoomField.setSize(125, 25);
		zoomField.setLocation(zoomLabel.getX()+zoomLabel.getWidth(), zoomLabel.getY());


		dimensionLabel.setSize(125, 25);
		dimensionLabel.setLocation(0, zoomLabel.getY() + 35);

		wField.setSize(80, 25);
		wField.setLocation(dimensionLabel.getX() + dimensionLabel.getWidth(), dimensionLabel.getY());

		hField.setSize(80, 25);
		hField.setLocation(wField.getX() + wField.getWidth(), wField.getY());

		resize.setSize(120, 25);
		resize.setLocation(hField.getX() + hField.getWidth(), hField.getY());

		render.setSize(125, 25);
		render.setLocation(0, dimensionLabel.getY() + 35);

		save.setSize(125, 25);
		save.setLocation(render.getX() + save.getWidth(), render.getY());

		reset.setSize(125, 25);
		reset.setLocation(save.getX()+reset.getWidth(), save.getY());

		reel1Label.setSize(70, 25);
		reel1Label.setLocation(maxLabel.getWidth() + maxField.getWidth() +35 , maxLabel.getY());

		reel1Field.setSize(70, 25);
		reel1Field.setLocation(reel1Label.getX() +45 , reel1Label.getY());

		img1Label.setSize(75, 25);
		img1Label.setLocation(zoomLabel.getWidth() + zoomField.getWidth() +35 , zoomLabel.getY());

		img1Field.setSize(60, 25);
		img1Field.setLocation(img1Label.getX()+ img1Label.getWidth()  , img1Label.getY());

		reel2Label.setSize(70, 25);
		reel2Label.setLocation(reel1Label.getWidth() + reel1Field.getWidth() + zoomLabel.getWidth() + zoomField.getWidth() +45, maxLabel.getY());

		reel2Field.setSize(60, 25);
		reel2Field.setLocation(reel2Label.getX() +45 , reel2Label.getY());

		img2Label.setSize(75, 25);
		img2Label.setLocation(img1Label.getWidth() + img1Field.getWidth() + zoomLabel.getWidth() + zoomField.getWidth() +45 , zoomLabel.getY());

		img2Field.setSize(60, 25);
		img2Field.setLocation(img2Label.getX()+ img2Label.getWidth() /*-55*/ , img2Label.getY());


	}
	
	/**
	 * Met à jour les valeur des champ imaginaire et reel
	 */
	protected void update()
	{
		reel1Field.setValue(mPane.GetMINRE());
		img1Field.setValue(mPane.GetMINIM());
		reel2Field.setValue(mPane.GetMAXRE());
		img2Field.setValue(mPane.GetMAXIM());
	}

}
