package com.higherli.asyn_syn_demo;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class AppLogger {
	private static Logger LOG = null;

	static {
		DOMConfigurator.configure("log4j.xml");//加载log4j.xml文件
		LOG = Logger.getLogger("AppLogger");
	}

	public static void error(Object msg) {
		LOG.warn("<>" + msg, null);
	}

	public static void error(Object msg, Throwable t) {
		LOG.warn("<>" + msg, t);
	}

	public static void info(Object msg) {
		LOG.info("<>" + msg);
	}
	
}
