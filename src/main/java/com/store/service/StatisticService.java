package com.store.service;

public interface StatisticService {
	String [][] getTotalPriceMonth( String month, String year);

	String [][] getProductTotal(String day, String month, String year);
}
