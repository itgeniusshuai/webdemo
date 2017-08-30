package com.boot.db;


public class DBContextHolder {

	private static ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();
	
	private final static DataSourceType DB_TYPE_R = DataSourceType.READ;
	public final static DataSourceType DB_TYPE_W =  DataSourceType.WRITE;
	private final static String DB_DATASOURCE_RW = "dataSourceRW";
	private final static String DB_DATASOURCE_R = "dataSourceR";

	public static String getDbType() {
		return DB_TYPE_R == contextHolder.get()?  DB_DATASOURCE_R : DB_DATASOURCE_RW;
	}

	public static void setDbType(DataSourceType type) {
		contextHolder.set(type);
	}

	public static void clearDBType() {
		contextHolder.remove();
	}
}
