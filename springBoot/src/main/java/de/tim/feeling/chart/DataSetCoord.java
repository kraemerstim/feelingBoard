package de.tim.feeling.chart;

public class DataSetCoord<T, L> {
	private T x;
	private L y;
	
	public DataSetCoord(T x, L y)
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
	public L getY() {
		return y;
	}
	public void setY(L y) {
		this.y = y;
	}
}
