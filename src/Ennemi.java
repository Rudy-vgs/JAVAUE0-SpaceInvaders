import MG2D.*;
import MG2D.geometrie.*;

public class Ennemi extends Texture{

	private final static String SPRITE = "./ressources/spaceship_enemy.png";
	private final static int TAILLE = 70;
	private final static int VITESSE_DEFAUT = 2;
	
	private boolean seDeplaceVersDroite = true;
	private int sens = VITESSE_DEFAUT;
	
	/* CONSTRUCTEURS */
	public Ennemi(Point origine) {
		super(SPRITE, origine, TAILLE, TAILLE);
	}
	
	/* RÉCUPÉRATION DE LA POSITION DU CANON DE L'ENNEMI */
	public Point positionCanon() {
		return new Point(this.getA().getX() + TAILLE/2, this.getB().getY());
	}
	
	/* DEPLACEMENT */
	public void deplacer(Fenetre f) {
		if(this.getB().getX() > f.getWidth() && seDeplaceVersDroite) {
			this.setSens(-VITESSE_DEFAUT);
			this.setSeDeplaceVersDroite(false);
		}
		
		if(this.getA().getX() < 0 && !seDeplaceVersDroite) {
			this.setSens(VITESSE_DEFAUT);
			this.setSeDeplaceVersDroite(true);
		}
		
		this.translater(this.sens, 0);
	}
	
	/* GETTERS & SETTERS */
	public boolean getSeDeplaceVersDroite() {
		return seDeplaceVersDroite;
	}

	public void setSeDeplaceVersDroite(boolean seDeplaceVersDroite) {
		this.seDeplaceVersDroite = seDeplaceVersDroite;
	}

	public int getSens() {
		return sens;
	}

	public void setSens(int sens) {
		this.sens = sens;
	}
	
	public static int getTaille() {
		return TAILLE;
	}

	/* TO STRING */
	@Override
	public String toString() {
		return "Ennemi [seDeplaceVersDroite=" + seDeplaceVersDroite + ", sens=" + sens + "]";
	}
}
