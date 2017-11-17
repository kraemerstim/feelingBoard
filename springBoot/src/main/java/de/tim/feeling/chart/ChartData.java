package de.tim.feeling.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartData<T, L> {
	private List<T> labels;
	private List<L> yLabels;
	private List<DataSet<T, L>> datasets;
	
	public List<L> getyLabels() {
		return yLabels;
	}

	public void setyLabels(List<L> yLabels) {
		this.yLabels = yLabels;
	}

	public List<DataSet<T, L>> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DataSet<T, L>> datasets) {
		this.datasets = datasets;
	}

	public ChartData()
	{
		yLabels = new ArrayList<L>();
		datasets = new ArrayList<DataSet<T, L>>();
		labels = new ArrayList<T>();
	}
	
	public void addDataSet(DataSet<T, L> dataSet)
	{
		this.datasets.add(dataSet);
	}
	
	public void addLabel(L label){
		this.yLabels.add(label);
	}

	public List<T> getLabels() {
		return labels;
	}

	public void setLabels(List<T> labels) {
		this.labels = labels;
	}
}
