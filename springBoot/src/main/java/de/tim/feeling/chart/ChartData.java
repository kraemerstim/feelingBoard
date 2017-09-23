package de.tim.feeling.chart;

import java.util.HashSet;
import java.util.Set;

/* data: {
	"labels":[1,2,3,4,5,6,7,8,9,10,11,12],
    "datasets": [{
        label: '# of Votes',
        data: [{x: 1, y: 12}, {x: 2, y: 19}, {x: 3, y: 3}, {x: 5, y: 5}, {x: 6, y: 2}, {x: 12, y: 3}],
        borderWidth: 1
    }]
},
*/

public class ChartData {
	private Set<String> labels;
	private Set<DataSet> datasets;
	
	public Set<String> getLabels() {
		return labels;
	}

	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}

	public Set<DataSet> getDatasets() {
		return datasets;
	}

	public void setDatasets(Set<DataSet> datasets) {
		this.datasets = datasets;
	}

	public ChartData()
	{
		labels = new HashSet<String>();
		datasets = new HashSet<DataSet>();
	}
	
	public void initWithFakeData()
	{
		labels.add("1");
		labels.add("2");
		labels.add("3");
		labels.add("4");
		labels.add("5");
		
		DataSet testSet = new DataSet();
		testSet.initWithFakeData();
		datasets.add(testSet);
	}
}
