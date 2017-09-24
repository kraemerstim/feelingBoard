package de.tim.feeling.chart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DataSet<T> {
	private String label;
	private List<DataSetCoord<T>> data;
	private String backgroundColor;
	private String borderColor;
	private boolean fill;

	public DataSet()
	{
		data = new ArrayList<DataSetCoord<T>>();
		fill = false;
	}
	
	public void addNewDataSetCoords(T x, Integer y)
	{
		data.add(new DataSetCoord<T>(x, y));
	}
	
	public void initWithFakeData(Collection<T> x_data, Collection<Integer> y_data)
	{
		label = "Fake Data";
		
		Iterator<T> it1 = x_data.iterator();
		Iterator<Integer> it2 = y_data.iterator();

		while (it1.hasNext() && it2.hasNext()) {
			addNewDataSetCoords(it1.next(), it2.next());
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<DataSetCoord<T>> getData() {
		return data;
	}

	public void setData(List<DataSetCoord<T>> data) {
		this.data = data;
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
}
