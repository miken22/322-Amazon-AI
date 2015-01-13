package ai.singleplayer;


/**
 * Extremely trivial pair class of one type
 * 
 * @author Mike Nowicki
 *
 * @param <L> Type that is being paired
 */
public class Pair<L, R> {

	L l;
	R r;
	
	public Pair(L l, R r){
		this.l = l;
		this.r = r;
	}
	/**
	 * Left pair, for this it is the x value
	 * @return X-coordinate
	 */
	public L getLeft(){
		return l;
	}

	/**
	 * Right pair, for this it is the y value
	 * @return Y-coordinate
	 */
	public R getRight(){
		return r;
	}
}
