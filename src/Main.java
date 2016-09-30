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
		final int SPEED_FIRE = 5;
		final int SPEED_FIRE_INVADERS = 3;// Vitesse de tir
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
		
		Texture spaceShip = new Texture("./ressources/spaceship.png", new Point(W_WIDTH / 2 - SS_WIDTH / 2, 0), SS_WIDTH, SS_WIDTH);
		f.ajouter(spaceShip);
		
		ArrayList<Texture> invaders = new ArrayList<Texture>();
		int invadersNumber = 2 + (int)(Math.random() * ((5 - 2) + 1));
		
		ArrayList<Tir> tirs = new ArrayList<Tir>();
		ArrayList<Cercle> missilesInvaders = new ArrayList<Cercle>();
		
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
			
			/* Déplacement du vaisseau vers la gauche */
			if (clavier.getGauche()) {
				if (spaceShip.getA().getX() > 0) {
					spaceShip.translater(-5, 0);
				}
			}
			
			/* Déplacement du vaisseau vers la droite */
			if (clavier.getDroite()) {
				if (spaceShip.getB().getX() < W_WIDTH) {
					spaceShip.translater(5, 0);
				}
			}
			
			/* Déplacement du vaisseau vers le bas */
			if (clavier.getBas()) {
				if (spaceShip.getA().getY() > 0) {
					spaceShip.translater(0, -5);
				}
			}
			
			/* Déplacement du vaisseau vers le bas */
			if (clavier.getHaut()) {
				if (spaceShip.getB().getY() < W_HEIGHT) {
					spaceShip.translater(0, 5);
				}
			}
			
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
				ennemi.translater(deplacement, 0);
				
				int fireOrder = 1 + (int)(Math.random() * ((500 - 1) + 1));
				if (fireOrder > 495) {
					if (k % 2 == 0) {
						Point canon = new Point(ennemi.getA().getX() + SS_WIDTH / 2, ennemi.getA().getY());
						Cercle missileEnnemi = new Cercle(Couleur.ROUGE, canon, 3, true);
						missilesInvaders.add(missileEnnemi);
						f.ajouter(missileEnnemi);
					}
				} else if(fireOrder < 2){
					if (k % 2 != 0) {
						Point canon = new Point(ennemi.getA().getX() + SS_WIDTH / 2, ennemi.getA().getY());
						Cercle missileEnnemi = new Cercle(Couleur.ROUGE, canon, 3, true);
						missilesInvaders.add(missileEnnemi);
						f.ajouter(missileEnnemi);
					}
				}
				
				
				
			}
			
			/* Déplacement des missiles ennemis */
			for (int numeroMissileEnnemi=0; numeroMissileEnnemi<missilesInvaders.size(); numeroMissileEnnemi++) {
				Cercle missileEnnemi = missilesInvaders.get(numeroMissileEnnemi);
				missileEnnemi.translater(0, -SPEED_FIRE_INVADERS);
				
				/* Suppression du missile s'il sort de la fenêtre */
				if (missileEnnemi.getO().getY() < 0) {
					f.supprimer(missileEnnemi);
					missilesInvaders.remove(numeroMissileEnnemi);
				}
				
				if (missileEnnemi.intersection(spaceShip)) {
					gameOver = true;
				}
			}
			
			/* Tir du vaisseau */
			if (clavier.getEspace()) {
				Date newDateFire = new Date();
				
				if ((newDateFire.getTime() - lastDateFire.getTime()) > 500){
					Point canon = new Point(spaceShip.getA().getX() + SS_WIDTH / 2, spaceShip.getB().getY());
					Tir tir = new Tir(canon);
					Cercle missile = tir.getMissile();
					
					tirs.add(tir);
					f.ajouter(missile);
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
					if (tir.getMissile().intersection(ennemi)) {
						invaders.remove(k);
						f.supprimer(ennemi);
						
						tirs.remove(numeroTir);
						f.supprimer(tir.getMissile());
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
