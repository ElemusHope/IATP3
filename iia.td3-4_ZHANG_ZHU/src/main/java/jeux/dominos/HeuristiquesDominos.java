package jeux.dominos;

import iia.jeux.alg.Heuristique;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;


public class HeuristiquesDominos{
/*这个Heuristique hblanc是用来统计白色玩家是如何计算自己当前剩下的摆放棋子位置数与敌方剩下的摆放位置数量的差，以下通过规则表示evaluation的值为多少时算胜利，多少为失败*/
	public static  Heuristique hblanc = new Heuristique(){

		public int eval(PlateauJeu p, Joueur j){
			/*A COMPLETER*/
			PlateauDominos pd=(PlateauDominos) p;
			if(pd.isJoueurBlanc(j)) {
				if(pd.nbCoupsBlanc()==0) {
					return (int) Double.NEGATIVE_INFINITY;
				}
				if(pd.nbCoupsNoir()==0) {
					return (int) Double.POSITIVE_INFINITY;
				}
				int h=pd.nbCoupsBlanc()-pd.nbCoupsNoir();
				return h;
			}	else {//黑玩家
				if(pd.nbCoupsBlanc()==0) {
					return (int) Double.POSITIVE_INFINITY;
				}
				if(pd.nbCoupsNoir()==0) {
					return (int) Double.NEGATIVE_INFINITY;
				}
				int h=pd.nbCoupsBlanc()-pd.nbCoupsNoir();
				return h;
			}
		}
	};
	/*这个Heuristique hbnoir是用来统计白色玩家是如何计算自己当前剩下的摆放棋子位置数与敌方剩下的摆放位置数量的差，以下通过规则表示evaluation的值为多少时算胜利，多少为失败*/
	public static  Heuristique hnoir = new Heuristique(){

		public int eval(PlateauJeu p, Joueur j){
			/*A COMPLETER*/
			PlateauDominos pd=(PlateauDominos) p;
			if(pd.isJoueurNoir(j)) {
				if(pd.nbCoupsNoir()==0) {//黑色出牌，但是黑色牌可以摆放的位置数为0，表示已经挂了
					return (int) Double.NEGATIVE_INFINITY;
				}
				if(pd.nbCoupsBlanc()==0) {//表示已经赢了
					return (int) Double.POSITIVE_INFINITY;
				}
				int h=pd.nbCoupsNoir()-pd.nbCoupsBlanc();
				return h;
			}	else {//白色玩家走
				if(pd.nbCoupsBlanc()==0) {//现在白色走，但是白色摆放为0，证明已经赢了
					return (int) Double.POSITIVE_INFINITY;
				}
				if(pd.nbCoupsNoir()==0) {
					return (int) Double.NEGATIVE_INFINITY;
				}
				int h=pd.nbCoupsNoir()-pd.nbCoupsBlanc();
				return h;
			}
		}
	};

}
