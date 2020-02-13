/**
 * 
 */

package iia.jeux.alg;

import java.util.ArrayList;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import jeux.dominos.HeuristiquesDominos;
import jeux.dominos.PlateauDominos;

public class Minimax implements AlgoJeu {

	/** La profondeur de recherche par défaut
	 */
	private final static int PROFMAXDEFAUT = 4;


	// -------------------------------------------
	// Attributs
	// -------------------------------------------

	/**  La profondeur de recherche utilisée pour l'algorithme
	 */
	private int profMax = PROFMAXDEFAUT;

	/**  L'heuristique utilisée par l'algorithme
	 */
	private Heuristique h;

	/** Le joueur Min
	 *  (l'adversaire) */
	private Joueur joueurMin;

	/** Le joueur Max
	 * (celui dont l'algorithme de recherche adopte le point de vue) */
	private Joueur joueurMax;

	/**  Le nombre de noeuds développé par l'algorithme
	 * (intéressant pour se faire une idée du nombre de noeuds développés) */
	private int nbnoeuds;

	/** Le nombre de feuilles évaluées par l'algorithme
	 */
	private int nbfeuilles;


	// -------------------------------------------
	// Constructeurs
	// -------------------------------------------
	public Minimax(Heuristique h, Joueur joueurMax, Joueur joueurMin) {
		this(h,joueurMax,joueurMin,PROFMAXDEFAUT);
	}
	//外部给定JoueurMax与JoueurMin以及最大的深度
	public Minimax(Heuristique h, Joueur joueurMax, Joueur joueurMin, int profMaxi) {
		this.h = h;
		this.joueurMin = joueurMin;
		this.joueurMax = joueurMax;
		profMax = profMaxi;
		//		System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
	}

	// -------------------------------------------
	// Méthodes de l'interface AlgoJeu
	// -------------------------------------------
	public CoupJeu meilleurCoup(PlateauJeu p) {
		/* A vous de compléter le corps de ce fichier */
		//我认为这个最好的coup应该是得到的heuristique的值最大的对于玩家1joueurMax来说\
		PlateauJeu p_change = p.copy();

		ArrayList<CoupJeu> listsCoups=p.coupsPossibles(joueurMax);
		int choix = 0;
		int h_max =(int) Double.NEGATIVE_INFINITY;;
		
		for(int i =0;i<listsCoups.size();i++) {
			PlateauJeu p_tmp = p_change.copy();
			p_tmp.joue(joueurMax, listsCoups.get(i));
			int h_tmp=meilleurCoup(p_tmp,1);
			if(h_max<h_tmp) {
				h_max=h_tmp;
				choix=i;
			}
		}
		return listsCoups.get(choix);

	}
	
	private int meilleurCoup(PlateauJeu p,int profNow) {
		int res=0;
		PlateauJeu p_change = p;
		if(profNow==profMax) {
			if(profNow%2==1) {
				//ami
				return h.eval(p, joueurMax);
			}else {
				//ennemi
				return h.eval(p, joueurMin);
			}
		}else {
			if(profNow%2==0) {
				//Ami
				ArrayList<CoupJeu> listsCoups=p.coupsPossibles(joueurMax);
				int h_max =(int) Double.NEGATIVE_INFINITY;;
				for(int i =0;i<listsCoups.size();i++) {
					PlateauJeu p_tmp = p_change.copy();
					p_tmp.joue(joueurMax, listsCoups.get(i));
					int h_tmp=meilleurCoup(p_tmp,profNow++);
					if(h_max<h_tmp) {
						h_max=h_tmp;
					}
				}
				res=h_max;
			}else {
				//ennemi
				ArrayList<CoupJeu> listsCoups=p.coupsPossibles(joueurMin);
				int h_min =(int) Double.POSITIVE_INFINITY;;
				for(int i =0;i<listsCoups.size();i++) {
					PlateauJeu p_tmp = p_change.copy();
					p_tmp.joue(joueurMin, listsCoups.get(i));
					int h_tmp=meilleurCoup(p_tmp,profNow+1);
					if(h_min>h_tmp) {
						h_min=h_tmp;
					}
					
				}
				res=h_min;
			}
		}
		return res;
	}

	// -------------------------------------------
	// Méthodes publiques
	// -------------------------------------------
	public String toString() {
		return "MiniMax(ProfMax="+profMax+")";
	}
	// -------------------------------------------
	// Méthodes internes
	// -------------------------------------------

	//A vous de jouer pour implanter Minimax
	public int GetnbFeuilles() {
		return nbfeuilles;
	}
	public int GetnbNoeuds() {
		return this.nbnoeuds;
	}
	/*我认为统计当前树有多少叶子应该需要对树里的每一个作为Noeud interne的节点进行一次统计（统计的是树的下一层会有多少种可能（敌人或者自己有几种摆法））*/
	/*i:profondeur,当前深度是多少？i从0开始，白牌还是黑牌开始要做个判断
	 * p:当前的正在处理的棋盘*/
	private PlateauDominos Pdebut=new PlateauDominos();//新建一个空的棋盘
	/**@param joueur1: le joueur qui commence
	 * @param p: le plateau actuel
	 * @return combien de noeuds developpes(noeuds internes)
	 * */
	public int compterNoeudsDeveloppes(int i,Joueur joueur1,PlateauDominos p) {//现在的玩家是Max or Min
		PlateauDominos copy_p=(PlateauDominos)p.copy();
		if(i>=0) {
			if(i<profMax) {//还没有到叶节点处的最大深度
				ArrayList<CoupJeu> listsCoups=p.coupsPossibles(joueur1);//这里的joueur1是个形参需要之后引入
				int taille=listsCoups.size();
				if(taille>0) {//证明这个棋盘下这个joueur有位置可以放置牌
					this.nbnoeuds=1;//计数器，  因为现在的棋盘作为一个节点有子节点，因此属于noueuds developes,数量+1
					for(CoupJeu cj:listsCoups) {
						p.joue(joueur1, cj);
						PlateauDominos curr_p=new PlateauDominos(p.getDamier());
						Joueur j2=this.OppositeJoueur(joueur1);
						this.nbnoeuds+=this.compterNoeudsDeveloppes(i+1, j2, curr_p);
						p=new PlateauDominos(copy_p.getDamier());
					}
					return nbnoeuds;
				}else {
					return 0;
				}
			}else {//到达了最大的深度，叶节点返回0
				return 0;
			}
		}else {
			return 0;
		}
	}
	/*i: Prof 当前的深度
	 *joueur1   当前玩家 
	 *p 当前的棋盘状态*/
	public int NbFeuilles(int i,Joueur joueur1,PlateauDominos p) {
		PlateauDominos copy_p=new PlateauDominos(p.getDamier());
		if(i>=0) {//i是符合要求才可以执行函数
			if(i<profMax) {//表示还没到达最大深度，也就是叶子节点开始计算的位置
				ArrayList<CoupJeu> listsCoups=p.coupsPossibles(joueur1);//这里的joueur1是个形参，需要之后实例化引入
				int taille=listsCoups.size();//一共可能的所有coups总数
				if(taille>0) {//表示有子节点，那么当前节点不是叶子节点
					this.nbfeuilles=0;
					for(CoupJeu cj:listsCoups) {
						p.joue(joueur1, cj);
						PlateauDominos curr_p=new PlateauDominos(p.getDamier());
						Joueur j2=this.OppositeJoueur(joueur1);
						this.nbfeuilles+=this.NbFeuilles(i+1, j2, curr_p);
						p=new PlateauDominos(copy_p.getDamier());
					}
					return nbfeuilles;
				}else {//表示无子节点，当前节点就是叶子节点
					return 1;
				}
			}else {//到达叶子节点位置
				return 1;
			}
		}
		return 1;
	}

	public Joueur OppositeJoueur(Joueur j1) {
		if(j1==joueurMax) {
			return joueurMin;
		}else {
			return joueurMax;
		}
	}

}
