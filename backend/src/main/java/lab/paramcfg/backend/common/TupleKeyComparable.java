package lab.paramcfg.backend.common;

public class TupleKeyComparable<X extends Comparable<? super X>, Y>
        implements Comparable<TupleKeyComparable<X, Y>>
{
    public X x;
    public Y y;

    public TupleKeyComparable(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Implements lexicographic order
     */
    public int compareTo(TupleKeyComparable<X, Y> other) {
        return this.x.compareTo(other.x);
    }
}