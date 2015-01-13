package ai;


/**
 * Extremely trivial pair class of one type
 * 
 * @author Mike Nowicki
 *
 * @param <L> Type that is being paired
 */
public class Pair<L> {

	L l;
	L r;
	
	public Pair(L l1, L l2){
		this.l = l1;
		this.r = l2;
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
	public L getRight(){
		return r;
	}
}
