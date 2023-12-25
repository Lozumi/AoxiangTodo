package sys;

import shared.Pomodoro;
import shared.PomodoroRecord;
import shared.ToDoWorkItem;

import java.util.*;

public class PomodoroRecordCollection implements List<PomodoroRecord> {
    ArrayList<PomodoroRecord> pomodoroRecords;

    /**
     * @return
     */
    @Override
    public int size() {
        return pomodoroRecords.size();
    }

    /**
     * @return
     */
    @Override
    public boolean isEmpty() {
        return pomodoroRecords.isEmpty();
    }

    /**
     * @param o element whose presence in this list is to be tested
     * @return
     */
    @Override
    public boolean contains(Object o) {
        return pomodoroRecords.contains(o);
    }

    /**
     * @return
     */
    @Override
    public Iterator iterator() {
        return pomodoroRecords.iterator();
    }

    /**
     * @return
     */
    @Override
    public Object[] toArray() {
        return pomodoroRecords.toArray();
    }

    /**
     * @param pomodoroRecord element whose presence in this collection is to be ensured
     * @return
     */
    @Override
    public boolean add(PomodoroRecord pomodoroRecord) {
        return false;
    }


    /**
     * @param o element to be removed from this list, if present
     * @return
     */
    @Override
    public boolean remove(Object o) {
        return pomodoroRecords.remove(o);
    }

    /**
     * @param c collection containing elements to be added to this collection
     * @return
     */
    @Override
    public boolean addAll(Collection c) {
        return pomodoroRecords.addAll(c);
    }

    /**
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c     collection containing elements to be added to this list
     * @return
     */
    @Override
    public boolean addAll(int index, Collection c) {
        return pomodoroRecords.addAll(index, c);
    }

    /**
     *
     */
    @Override
    public void clear() {
        pomodoroRecords.clear();
    }

    /**
     * @param index index of the element to return
     * @return
     */
    @Override
    public PomodoroRecord get(int index) {
        return pomodoroRecords.get(index);
    }

    /**
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return
     */
    @Override
    public PomodoroRecord set(int index, PomodoroRecord element) {
        return pomodoroRecords.set(index,element);
    }

    /**
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, PomodoroRecord element) {
        pomodoroRecords.add(index,element);
    }

    /**
     * @param index the index of the element to be removed
     * @return
     */
    @Override
    public PomodoroRecord remove(int index) {
        return pomodoroRecords.remove(index);
    }


    /**
     * @param o element to search for
     * @return
     */
    @Override
    public int indexOf(Object o) {
        return pomodoroRecords.indexOf(o);
    }

    /**
     * @param o element to search for
     * @return
     */
    @Override
    public int lastIndexOf(Object o) {
        return pomodoroRecords.lastIndexOf(o);
    }

    /**
     * @return
     */
    @Override
    public ListIterator listIterator() {
        return pomodoroRecords.listIterator();
    }

    /**
     * @param index index of the first element to be returned from the
     *              list iterator (by a call to {@link ListIterator#next next})
     * @return
     */
    @Override
    public ListIterator listIterator(int index) {
        return pomodoroRecords.listIterator(index);
    }

    /**
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     * @return
     */
    @Override
    public List subList(int fromIndex, int toIndex) {
        return pomodoroRecords.subList(fromIndex,toIndex);
    }

    /**
     * @param c collection containing elements to be retained in this list
     * @return
     */
    @Override
    public boolean retainAll(Collection c) {
        return pomodoroRecords.retainAll(c);
    }

    /**
     * @param c collection containing elements to be removed from this list
     * @return
     */
    @Override
    public boolean removeAll(Collection c) {
        pomodoroRecords.remove(c);
    }

    /**
     * @param c collection to be checked for containment in this list
     * @return
     */
    @Override
    public boolean containsAll(Collection c) {
        return contains(c);
    }

    /**
     * @param a the array into which the elements of this list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return
     */
    @Override
    public Object[] toArray(Object[] a) {
        return pomodoroRecords.toArray();
    }

    public int getAvailableID(){
        if(isEmpty()){
            return 0;
        }
        return this.stream().max(Comparator.comparingInt(PomodoroRecord::getInnerId)).get().getInnerId()+1;
    }
}
