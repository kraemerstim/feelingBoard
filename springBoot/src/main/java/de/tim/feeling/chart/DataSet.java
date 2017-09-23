package de.tim.feeling.chart;

import java.util.HashSet;
import java.util.Set;

/* data: {
"datasets": [{
    label: '# of Votes',
    data: [{x: 1, y: 12}, {x: 2, y: 19}, {x: 3, y: 3}, {x: 5, y: 5}, {x: 6, y: 2}, {x: 12, y: 3}],
    borderWidth: 1
}]
},
*/
public class DataSet {
	private String label;
	private Set<Integer> data;
	
	public DataSet()
	{
		data = new HashSet<Integer>();
	}
	
	public void initWithFakeData()
	{
		label = "Fake Data";
		
		data.add(13);
		data.add(22);
		data.add(3);
		data.add(9);
		data.add(11);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Set<Integer> getData() {
		return data;
	}

	public void setData(Set<Integer> data) {
		this.data = data;
	}
}
