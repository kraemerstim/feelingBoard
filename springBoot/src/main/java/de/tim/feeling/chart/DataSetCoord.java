package de.tim.feeling.chart;

public class DataSetCoord<T> {
	private T x;
	private Integer y;
	
	public DataSetCoord(T x, Integer y)
	{
		this.x = x;
		this.y = y;
	}
	
	public T getX() {
		return x;
	}
	public void setX(T x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
}
