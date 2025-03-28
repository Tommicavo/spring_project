package com.project.ecom.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class AppLogger {
	public final Logger log = LogManager.getLogger();
}
