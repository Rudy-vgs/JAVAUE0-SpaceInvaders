import java.util.ArrayList;

import MG2D.*;
import MG2D.geometrie.*;

public class Jeu {
	private final static int W_WIDTH = 600; // Largeur de la fenêtre
	private final static int W_HEIGHT = 800; // Hauteur de la fenêtre
	
	private Fenetre f;
	private Clavier clavier;
	private Souris souris;
	private boolean gameOver = false;
	
	private Joueur joueur;
	private ArrayList<Tir> tirs;
	private ArrayList<Ennemi> ennemis;
	
	/* JEU */
	public Jeu(){
		initialisationJeu();
		configPeripherique();
		
		// Tant qu'il y a des ennemis
		while (ennemis.size() > 0 && gameOver == false) {
			
			joueur.deplacer(clavier, f);
			
			/* Déplacement des vaisseaux ennemis et génération des tirs ennemis */
			for (int k=0; k<ennemis.size(); k++) {
				Ennemi ennemi = ennemis.get(k);
				ennemi.deplacer(f);
				
				int ordreFaireFeu = 1 + (int)(Math.random() * ((500 - 1) + 1));
				if (ordreFaireFeu > 495) {
					if (k % 2 == 0) {
						Tir tir = new Tir(ennemi.positionCanon(), Couleur.ROUGE, true);
						tirs.add(tir);
						f.ajouter(tir.getMissile());
					}
				} else if(ordreFaireFeu < 2){
					if (k % 2 != 0) {
						Tir tir = new Tir(ennemi.positionCanon(), Couleur.ROUGE, true);
						tirs.add(tir);
						f.ajouter(tir.getMissile());
					}
				}
			}
			
			/* Tir du joueur */
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
	
	/* INITIALISATION DU JEU */
	private void initialisationJeu() {
		f = new Fenetre("Shoot'em up", W_WIDTH, W_HEIGHT);
		
		// Mise en place du fond du jeu
		Texture background = new Texture("./ressources/background.jpg", new Point(0,0), W_WIDTH, W_HEIGHT);
		f.ajouter(background);
		
		// Ajout du joueur
		joueur = new Joueur(f);
		f.ajouter(joueur);
		
		// Initialisation des tirs et des ennemis
		tirs = new ArrayList<Tir>();
		ennemis = new ArrayList<Ennemi>();
		
		int nombreEnnemis = 2 + (int)(Math.random() * ((5 - 2) + 1));
		int offset = W_WIDTH / 2 + Ennemi.getTaille();
		
		// Placement des vaisseaux ennemis 
		for (int i = 0; i<nombreEnnemis; i++) {
			int xPosition = offset - (i*Ennemi.getTaille() + i*10);
			int yPosition;
			if (i % 2 == 0) {
				yPosition = 500;
			} else {
				yPosition = 500 + Ennemi.getTaille() + 20;
			}
			
			Ennemi ennemi = new Ennemi(new Point(xPosition, yPosition));
			ennemis.add(ennemi);
			f.ajouter(ennemis.get(i));
		}
	}
	
	/* CONFIGURATION DES PERIPHERIQUES */
	private void configPeripherique() {
		clavier = new Clavier();
		souris = new Souris(W_HEIGHT);
		
		f.addKeyListener ( clavier );
		f.addMouseListener( souris );
		f.getP().addMouseListener(souris);
		f.getP().addMouseMotionListener(souris);
	}

	/* GETTERS & SETTERS */
	public Fenetre getF() {
		return f;
	}

	public void setF(Fenetre f) {
		this.f = f;
	}

	public Clavier getClavier() {
		return clavier;
	}

	public void setClavier(Clavier clavier) {
		this.clavier = clavier;
	}

	public Souris getSouris() {
		return souris;
	}

	public void setSouris(Souris souris) {
		this.souris = souris;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public ArrayList<Tir> getTirs() {
		return tirs;
	}

	public void setTirs(ArrayList<Tir> tirs) {
		this.tirs = tirs;
	}

	public ArrayList<Ennemi> getEnnemis() {
		return ennemis;
	}

	public void setEnnemis(ArrayList<Ennemi> ennemis) {
		this.ennemis = ennemis;
	}
	
	/* TO STRING */
	@Override
	public String toString() {
		return "Ennemis restants: " + this.ennemis.size();
	}
	
}
