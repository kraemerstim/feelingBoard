package de.tim.feeling.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartData<T, L> {
	private List<T> labels;
	private List<DataSet<T, L>> datasets;
	
	public List<DataSet<T, L>> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DataSet<T, L>> datasets) {
		this.datasets = datasets;
	}

	public ChartData()
	{
		datasets = new ArrayList<DataSet<T, L>>();
		labels = new ArrayList<T>();
	}
	
	public void addDataSet(DataSet<T, L> dataSet)
	{
		this.datasets.add(dataSet);
	}


	public List<T> getLabels() {
		return labels;
	}

	public void setLabels(List<T> labels) {
		this.labels = labels;
	}
}
