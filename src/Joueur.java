import MG2D.*;
import MG2D.geometrie.*;
import java.util.Date;

public class Joueur extends Texture{
	
	private final static String SPRITE = "./ressources/spaceship.png";
	private final static int TAILLE = 70;
	private final static int VITESSE_DEFAUT = 5;
	private final static int ANTI_SPAM = 500;

	private int vie;
	private Date derniereAction;
	
	/* CONSTRUCTEURS */
	public Joueur(Fenetre f) {
		super(SPRITE, new Point(f.getWidth()/2 - TAILLE/2, 0), TAILLE, TAILLE);
		this.vie = 1;
		this.derniereAction = new Date();
	}
	
	public Joueur(Fenetre f, int vie) {
		super(SPRITE, new Point(f.getWidth()/2 - TAILLE/2, 0), TAILLE, TAILLE);
		this.vie = vie;
		this.derniereAction = new Date();
	}
	
	/* LE JOUEUR PEUT-IL TIRER ? (anti-spam) */
	public boolean peutTirer() {
		Date nouvelleAction = new Date();
		
		if((nouvelleAction.getTime() - derniereAction.getTime()) > ANTI_SPAM) {
			derniereAction = nouvelleAction;
			return true;
		}
		return false;
	}
	
	/* GESTION DU DEPLACEMENT DU JOUEUR DANS LA FENETRE */
	public void deplacer(Clavier clavier, Fenetre f) {
		/* Déplacement du vaisseau vers la gauche */
		if (clavier.getGauche()) {
			if (this.getA().getX() > 0) {
				this.translater(-VITESSE_DEFAUT, 0);
			}
		}
		
		/* Déplacement du vaisseau vers la droite */
		if (clavier.getDroite()) {
			if (this.getB().getX() < f.getWidth()) {
				this.translater(VITESSE_DEFAUT, 0);
			}
		}
		
		/* Déplacement du vaisseau vers le bas */
		if (clavier.getBas()) {
			if (this.getA().getY() > 0) {
				this.translater(0, -VITESSE_DEFAUT);
			}
		}
		
		/* Déplacement du vaisseau vers le haut */
		if (clavier.getHaut()) {
			if (this.getB().getY() < f.getHeight()) {
				this.translater(0, VITESSE_DEFAUT);
			}
		}
	}
	
	/* RÉCUPÉRATION DE LA POSITION DU CANON DU JOUEUR */
	public Point positionCanon() {
		return new Point(this.getA().getX() + TAILLE/2, this.getB().getY());
	}
	
	/* GETTERS & SETTERS */
	public int getVie() {
		return vie;
	}

	public void setVie(int vie) {
		this.vie = vie;
	}
	
	public Date getDerniereAction() {
		return derniereAction;
	}

	public void setDerniereAction(Date derniereAction) {
		this.derniereAction = derniereAction;
	}

	/* TO STRING */
	@Override
	public String toString() {
		return "Joueur [vie=" + vie + "]";
	}
	
}
