package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map.Entry;

public class TreeSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable {
    private static final Object PRESENT = null;
    private static final long serialVersionUID = -2479143000061671589L;
    private transient NavigableMap<E, Object> m;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: java.util.TreeSet.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: java.util.TreeSet.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 5 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TreeSet.<clinit>():void");
    }

    TreeSet(NavigableMap<E, Object> m) {
        this.m = m;
    }

    public TreeSet() {
        this(new TreeMap());
    }

    public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap((Comparator) comparator));
    }

    public TreeSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    public TreeSet(SortedSet<E> s) {
        this(s.comparator());
        addAll(s);
    }

    public Iterator<E> iterator() {
        return this.m.navigableKeySet().iterator();
    }

    public Iterator<E> descendingIterator() {
        return this.m.descendingKeySet().iterator();
    }

    public NavigableSet<E> descendingSet() {
        return new TreeSet(this.m.descendingMap());
    }

    public int size() {
        return this.m.size();
    }

    public boolean isEmpty() {
        return this.m.isEmpty();
    }

    public boolean contains(Object o) {
        return this.m.containsKey(o);
    }

    public boolean add(E e) {
        return this.m.put(e, PRESENT) == null;
    }

    public boolean remove(Object o) {
        return this.m.remove(o) == PRESENT;
    }

    public void clear() {
        this.m.clear();
    }

    public boolean addAll(Collection<? extends E> c) {
        if (this.m.size() == 0 && c.size() > 0 && (c instanceof SortedSet) && (this.m instanceof TreeMap)) {
            SortedSet<? extends E> set = (SortedSet) c;
            TreeMap<E, Object> map = this.m;
            Comparator<?> cc = set.comparator();
            Comparator<? super E> mc = map.comparator();
            if (cc == mc || (cc != null && cc.equals(mc))) {
                map.addAllForTreeSet(set, PRESENT);
                return true;
            }
        }
        return super.addAll(c);
    }

    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return new TreeSet(this.m.subMap(fromElement, fromInclusive, toElement, toInclusive));
    }

    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return new TreeSet(this.m.headMap(toElement, inclusive));
    }

    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return new TreeSet(this.m.tailMap(fromElement, inclusive));
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    public Comparator<? super E> comparator() {
        return this.m.comparator();
    }

    public E first() {
        return this.m.firstKey();
    }

    public E last() {
        return this.m.lastKey();
    }

    public E lower(E e) {
        return this.m.lowerKey(e);
    }

    public E floor(E e) {
        return this.m.floorKey(e);
    }

    public E ceiling(E e) {
        return this.m.ceilingKey(e);
    }

    public E higher(E e) {
        return this.m.higherKey(e);
    }

    public E pollFirst() {
        Entry<E, ?> e = this.m.pollFirstEntry();
        if (e == null) {
            return null;
        }
        return e.getKey();
    }

    public E pollLast() {
        Entry<E, ?> e = this.m.pollLastEntry();
        if (e == null) {
            return null;
        }
        return e.getKey();
    }

    public Object clone() {
        try {
            TreeSet<E> clone = (TreeSet) super.clone();
            clone.m = new TreeMap(this.m);
            return clone;
        } catch (Throwable e) {
            throw new InternalError(e);
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(this.m.comparator());
        s.writeInt(this.m.size());
        for (E e : this.m.keySet()) {
            s.writeObject(e);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        TreeMap<E, Object> tm = new TreeMap((Comparator) s.readObject());
        this.m = tm;
        tm.readTreeSet(s.readInt(), s, PRESENT);
    }

    public Spliterator<E> spliterator() {
        return TreeMap.keySpliteratorFor(this.m);
    }
}
