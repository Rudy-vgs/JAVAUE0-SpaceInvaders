import MG2D.*;
import MG2D.geometrie.*;

public class Tir {
	
	private final static int VITESSE_DEFAUT = 5;
	
	private Cercle missile;
	private int vitesse;
	
	/* CONSTRUCTEURS */
	public Tir(Point origine, Couleur couleur) {
		this.missile = new Cercle(couleur, origine, 3, true);
		this.vitesse = VITESSE_DEFAUT;
	}
	
	public Tir(Point origine, Couleur couleur, int vitesse) {
		this.missile = new Cercle(couleur, origine, 3, true);
		this.vitesse = vitesse;
	}
	
	/* DEPLACEMENT DU TIR */
	public void deplacer() {
		this.missile.translater(0, this.vitesse);
	}
	
	/* LE TIR EST IL ENCORE DANS LA FENÊTRE DE JEU */
	public boolean estVisible(int hauteurFenetre) {
		int y = this.missile.getO().getY();
		if (y > hauteurFenetre || y < 0) {
			return false;
		} 
		
		return true;
	}
	
	/* GETTERS & SETTERS */	
	public Cercle getMissile() {
		return missile;
	}

	public void setMissile(Cercle missile) {
		this.missile = missile;
	}

	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}
	
	/* TO STRING */
	@Override
	public String toString() {
		return "Tir [missile=" + missile + ", vitesse=" + vitesse + "]";
	}
}
