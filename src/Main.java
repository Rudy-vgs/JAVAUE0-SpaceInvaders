import javax.swing.*;
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
		
		Date lastDateFire = new Date();
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
		
		ArrayList<Texture> invaders = new ArrayList<Texture>();
		int invadersNumber = 2 + (int)(Math.random() * ((5 - 2) + 1));
		
		ArrayList<Tir> tirs = new ArrayList<Tir>();
		
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
			
			Texture invader = new Texture("./ressources/spaceship_enemy.png", new Point(xPosition, yPosition), SS_WIDTH, SS_WIDTH);
			invaders.add(invader);
			f.ajouter(invaders.get(i));
		}
		
		/* Tant qu'il y a des ennemis */
		while (invaders.size() > 0 && gameOver == false) {
			
			joueur.deplacer(clavier, f);
			
			/* Déplacement des vaisseaux ennemis */
			Texture dernier = invaders.get(0);
			Texture premier = invaders.get(invaders.size()-1);
			
			if(premier.getA().getX() < 0) {
				deplacement = SPEED_MOVE_INVADERS;
			}
			
			if(dernier.getB().getX() > W_WIDTH) {
				deplacement = -(SPEED_MOVE_INVADERS);
			}
			
			
			for (int k=0; k<invaders.size(); k++) {
				Texture ennemi = invaders.get(k);
				Point canon = new Point(ennemi.getA().getX() + SS_WIDTH / 2, ennemi.getA().getY());
				ennemi.translater(deplacement, 0);
				
				int fireOrder = 1 + (int)(Math.random() * ((500 - 1) + 1));
				
				if (fireOrder > 495) {
					if (k % 2 == 0) {
						Tir tir = new Tir(canon, Couleur.ROUGE, true);
						tirs.add(tir);
						f.ajouter(tir.getMissile());
					}
				} else if(fireOrder < 2){
					if (k % 2 != 0) {
						Tir tir = new Tir(canon, Couleur.ROUGE, true);
						tirs.add(tir);
						f.ajouter(tir.getMissile());
					}
				}
			}
			
			/* Tir du vaisseau */
			if (clavier.getEspace()) {
				Date newDateFire = new Date();
				
				if (joueur.peutTirer()){
					Tir tir = new Tir(joueur.positionCanon());
					
					tirs.add(tir);
					f.ajouter(tir.getMissile());
					lastDateFire = newDateFire;
				}
			}
			
			/* Déplacement des missiles */
			for (int numeroTir=0; numeroTir<tirs.size(); numeroTir++) {
				Tir tir = tirs.get(numeroTir);
				tir.deplacer();
				
				/* Suppression du missile s'il sort de la fenêtre */
				if (!tir.estVisible(W_HEIGHT)) {
					f.supprimer(tir.getMissile());
					tirs.remove(numeroTir);
				}

				/* Gestion des collisions entre missiles et joueurs */
				for (int k=0; k<invaders.size(); k++) {
					Texture ennemi = invaders.get(k);
					
					/* Élimination d'un ennemi */
					if (tir.getMissile().intersection(ennemi) && !tir.getEstEnnemi()) {
						invaders.remove(k);
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
