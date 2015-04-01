package ai;

import java.util.Comparator;


/**
 * Extremely trivial pair class of one type. To use the comparator
 * the objects L and R must be comparable.
 * 
 * @author Mike Nowicki
 *
 * @param <L> The first pair element type
 * @param <R> The second pair element type
 */
public class Pair<L, R> implements Comparator<Pair<L, R>>{

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
	
	
	/**
	 * Do not know if this works, just threw it together late, need to think it through
	 */
	@Override
	public int compare(Pair<L, R> p1, Pair<L, R> p2) {
		if (p1.getLeft() == p2.getLeft() && p1.getRight() == p2.getRight()) {
			return 1;
		}
		return -1;
	}
	/**
	 * No generic protection, meant for Pair<Byte, Byte> only. Checks if the two pairs are equal
	 * @param otherPair The pair to compare against, must be a pair of Byte's
	 * @return True if they are the same, false otherwise.
	 */
	public boolean equals(Pair<L, R> otherPair) {
		if (this.getLeft() == otherPair.getLeft() && this.getRight() == otherPair.getRight()) {
			return true;
		}
		return false;
	}
}
