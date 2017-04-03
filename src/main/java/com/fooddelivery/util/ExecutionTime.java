package com.fooddelivery.util;

import java.util.concurrent.TimeUnit;

public class ExecutionTime {
	
	public long startTime;
	public long endTime;
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void end() {
		endTime = System.currentTimeMillis();
	}
	
	public long getExecutionTime() {
		return TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
	}

}
