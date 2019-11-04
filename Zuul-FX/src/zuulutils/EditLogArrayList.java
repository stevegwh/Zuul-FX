package zuulutils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Implementation of ArrayList that logs any update of the ArrayList to the
 * field 'lastEdit'
 * 
 * @author Steve
 *
 * @param <E>
 */
public class EditLogArrayList<E> implements List<E> {
	private long lastEdit;
	List<E> list;

	private void updateLastEdit() {
		lastEdit = Instant.now().getEpochSecond();
	}

	/**
	 * @return the time this array was last edited in unix time.
	 */
	public long getLastEdit() {
		return lastEdit;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		updateLastEdit();
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		updateLastEdit();
		return list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		updateLastEdit();
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		updateLastEdit();
		list.clear();
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public E set(int index, E element) {
		updateLastEdit();
		return list.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		updateLastEdit();
		list.add(index, element);

	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public boolean add(E e) {
		updateLastEdit();
		return list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		updateLastEdit();
		return list.remove(o);
	}

	@Override
	public E remove(int index) {
		updateLastEdit();
		return list.remove(index);
	}

	public EditLogArrayList() {
		list = new ArrayList<E>();
	}

}
