package de.tim.feeling.chart;

import java.util.ArrayList;
import java.util.List;

public class DataSet<T, L> {
	private String label;
	private List<DataSetCoord<T, L>> data;
	private String backgroundColor;
	private String borderColor;
	private boolean fill;

	public DataSet()
	{
		data = new ArrayList<DataSetCoord<T, L>>();
		fill = false;
	}
	
	public void addNewDataSetCoords(T x, L y)
	{
		data.add(new DataSetCoord<T, L>(x, y));
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<DataSetCoord<T, L>> getData() {
		return data;
	}

	public void setData(List<DataSetCoord<T, L>> data) {
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
