package lab.paramcfg.backend.common;

/*
 * Key为可比较的Tuple元组。
 * 用于近邻查询的Top K元素排序。
 */
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