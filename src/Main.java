import java.util.ArrayList;
import java.util.Date;

import MG2D.*;
import MG2D.geometrie.*;

public class Main {

	public static void main(String[] args) {
		
		final int W_WIDTH = 600; // Largeur de la fenêtre
		final int W_HEIGHT = 800; // Hauteur de la fenêtre
		
		final int SS_WIDTH = 70; // Taille du vaisseau
		final int SPEED_MOVE_INVADERS = 2;
		
		boolean gameOver = false;
		
		/* Éléments basique de l'interface */
		Fenetre f = new Fenetre("Shoot'em up", W_WIDTH, W_HEIGHT);
		
		Clavier clavier = new Clavier();
		f.addKeyListener ( clavier );
		
		Souris souris = new Souris(W_HEIGHT);
		f.addMouseListener( souris );
		f.getP().addMouseListener(souris);
		f.getP().addMouseMotionListener(souris);
		
		
		/* Éléments graphiques de l'interface & IA*/
		Texture background = new Texture("./ressources/background.jpg", new Point(0,0), W_WIDTH, W_HEIGHT);
		f.ajouter(background);
		
		Joueur joueur = new Joueur(f);
		f.ajouter(joueur);
		
		ArrayList<Tir> tirs = new ArrayList<Tir>();
		ArrayList<Ennemi> ennemis = new ArrayList<Ennemi>();
		int invadersNumber = 2 + (int)(Math.random() * ((5 - 2) + 1));
		
		int offset = W_WIDTH / 2 + SS_WIDTH;
		int deplacement = SPEED_MOVE_INVADERS;
		
		/* Initialisation des vaisseaux ennemis */
		for (int i = 0; i<invadersNumber; i++) {
			int xPosition = offset - (i*SS_WIDTH + i*10);
			int yPosition;
			if (i % 2 == 0) {
				yPosition = 500;
			} else {
				yPosition = 500 + SS_WIDTH + 20;
			}
			
			Ennemi ennemi = new Ennemi(new Point(xPosition, yPosition));
			ennemis.add(ennemi);
			f.ajouter(ennemis.get(i));
		}
		
		/* Tant qu'il y a des ennemis */
		while (ennemis.size() > 0 && gameOver == false) {
			
			joueur.deplacer(clavier, f);
			
			/* Déplacement des vaisseaux ennemis */
			for (int k=0; k<ennemis.size(); k++) {
				Ennemi ennemi = ennemis.get(k);
				ennemi.deplacer(f);
				
				int fireOrder = 1 + (int)(Math.random() * ((500 - 1) + 1));
				
				if (fireOrder > 495) {
					if (k % 2 == 0) {
						Tir tir = new Tir(ennemi.positionCanon(), Couleur.ROUGE, true);
						tirs.add(tir);
						f.ajouter(tir.getMissile());
					}
				} else if(fireOrder < 2){
					if (k % 2 != 0) {
						Tir tir = new Tir(ennemi.positionCanon(), Couleur.ROUGE, true);
						tirs.add(tir);
						f.ajouter(tir.getMissile());
					}
				}
			}
			
			/* Tir du vaisseau */
			if (clavier.getEspace()) {
				if (joueur.peutTirer()){
					Tir tir = new Tir(joueur.positionCanon());
					tirs.add(tir);
					f.ajouter(tir.getMissile());
				}
			}
			
			/* Déplacement des missiles */
			for (int numeroTir=0; numeroTir<tirs.size(); numeroTir++) {
				Tir tir = tirs.get(numeroTir);
				tir.deplacer();
				
				/* Suppression du missile s'il sort de la fenêtre */
				if (!tir.estVisible(f)) {
					f.supprimer(tir.getMissile());
					tirs.remove(numeroTir);
				}

				/* Gestion des collisions entre missiles et joueurs */
				for (int k=0; k<ennemis.size(); k++) {
					Ennemi ennemi = ennemis.get(k);
					
					/* Élimination d'un ennemi */
					if (tir.getMissile().intersection(ennemi) && !tir.getEstEnnemi()) {
						ennemis.remove(k);
						f.supprimer(ennemi);
						
						tirs.remove(numeroTir);
						f.supprimer(tir.getMissile());
					}
					
					/* Élimination joueur */
					if (tir.getMissile().intersection(joueur) && tir.getEstEnnemi()) {
						gameOver = true;
					}
				}
				
			}
			
			/* Rafraichissement de la fenêtre */
			f.rafraichir();
			
			/* Fix processeur */
			try {
				Thread.sleep (20);
			}
			catch ( Exception e ) {
				System.out.println ( e );
			}
		}
	}

}
