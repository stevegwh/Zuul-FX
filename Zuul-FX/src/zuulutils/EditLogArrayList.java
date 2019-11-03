package zuulutils;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Extension of ArrayList that logs any update of the ArrayList to the field
 * 'lastEdit'
 * 
 * @author Steve
 *
 * @param <E>
 */
public class EditLogArrayList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long lastEdit;

	@Override
	public boolean add(E e) {
		lastEdit = Instant.now().getEpochSecond();
		return super.add(e);
	}

	@Override
	public E remove(int index) {
		lastEdit = Instant.now().getEpochSecond();
		return super.remove(index);
	}

	/**
	 * @return the time this array was last edited in unix time.
	 */
	public long getLastEdit() {
		return lastEdit;
	}

}
