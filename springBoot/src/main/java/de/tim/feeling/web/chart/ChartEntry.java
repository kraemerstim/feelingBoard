package de.tim.feeling.web.chart;

public class ChartEntry {
	private Integer year;
	private Integer month;
	private Integer week;
	private Integer day;
	private Double feeling;
	
	public ChartEntry(Integer aYear, Integer aMonth, Integer aWeek, Integer aDay, Double aFeeling){
		setFeeling(aFeeling);
		setYear(aYear);
		setMonth(aMonth);
		setWeek(aWeek);
		setDay(aDay);
	}
	
	public ChartEntry(Integer aYear, Integer aMonth, Integer aWeek, Integer aDay){
		this(aYear, aMonth, aWeek, aDay, 0.0);
	}

	public String getString(ChartSorting sorting)
	{
		String resultString = "";
		switch (sorting) {
		case DAY:
			resultString = getYear().toString() + '-' + String.format("%02d", getMonth()) + '-' +  String.format("%02d", getDay());
			break;
		case WEEK:
			resultString = getYear().toString() + '-' + String.format("%02d", getWeek());
			break;
		case MONTH:
			resultString = getYear().toString() + '-' + String.format("%02d", getMonth());
			break;
		default:
			break;
		}
		return resultString;
	}
	
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Double getFeeling() {
		return feeling;
	}
	public void setFeeling(Double feeling) {
		this.feeling = feeling;
	}
    
	
}
