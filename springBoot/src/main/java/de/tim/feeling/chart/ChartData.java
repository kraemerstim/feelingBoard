package de.tim.feeling.chart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChartData<T> {
	private List<T> labels;
	private List<DataSet<T>> datasets;
	
	public List<T> getLabels() {
		return labels;
	}

	public void setLabels(List<T> labels) {
		this.labels = labels;
	}

	public List<DataSet<T>> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DataSet<T>> datasets) {
		this.datasets = datasets;
	}

	public ChartData()
	{
		labels = new ArrayList<T>();
		datasets = new ArrayList<DataSet<T>>();
	}
	
	public void addDataSet(DataSet<T> dataSet)
	{
		this.datasets.add(dataSet);
	}
	
	public void initWithFakeData(Collection<T> fakeData, Collection<T> fakeData_x, Collection<Integer> fakeData_y)
	{
		labels.addAll(fakeData);
		
		DataSet<T> testSet = new DataSet<T>();
		testSet.initWithFakeData(fakeData_x, fakeData_y);
		datasets.add(testSet);
	}
}
