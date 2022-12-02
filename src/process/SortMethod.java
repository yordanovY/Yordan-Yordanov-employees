package process;

import java.util.Comparator;

import objects.WorkPairs;

public class SortMethod implements Comparator<WorkPairs> {
	/**
	 * Used to sort working pairs by days of collaboration
	 * DATE			USER				NOTE:
	 * 2022.12.01	Yordan Yordanov		Created
	 */
	public int compare(WorkPairs a, WorkPairs b)
    {
        return (int) (b.getDaysTogether() - a.getDaysTogether());
    }
}
