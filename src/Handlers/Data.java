package Handlers;

public class Data {

	private String name;
	
	// types of data
	private String dataString;
	private int dataInt;
	private double dataDouble;
	
	// data type indexes
	private int type;
	public static final int STRING = 0;
	public static final int INT = 1;
	public static final int DOUBLE = 2;
	
	// static references if variable is not available
	public static final String T_STRING = "string";
	public static final int T_INT = 1;
	public static final double T_DOUBLE = 0.5;
	
	// 3 types of data, 3 creation functions
	// String data
	public Data(String n, String info) {
		type = STRING;
		name = n;
		dataString = info;
	}
	// Int data
	public Data(String n, int info) {
		type = INT;
		name = n;
		dataInt = info;
	}
	// Double data
	public Data(String n, double info) {
		type = DOUBLE;
		name = n;
		dataDouble = info;
	}
	
	//getters:
	public int getType() { return type; }
	public String getName() { return name; }
	// getData must be given the type of variable to return
	public String getData(String t) { return dataString; }
	public int getData(int t) { return dataInt; }
	public double getData(double t) { return dataDouble; }
	
	// Set data
	public void setData(String info) { dataString = info; }
	public void setData(int info) { dataInt = info; }
	public void setData(double info) { dataDouble = info; }
	
}
