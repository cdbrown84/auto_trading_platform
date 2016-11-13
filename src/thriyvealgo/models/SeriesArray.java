package thriyvealgo.models;

import java.util.AbstractList;
import java.util.List;

public class SeriesArray<T> extends AbstractList<T>{
	
	 private final T[] a;

	    SeriesArray(T[] array) {
	        a = array;
	    }

	    public T get(int index) {
	        return a[index];
	    }

	    public T set(int index, T element) {
	        T oldValue = a[index];
	        a[index] = element;
	        return oldValue;
	    }

	    public int size() {
	        return a.length;
	    }

}

