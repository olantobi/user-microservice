package com.liferon.usermgt;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class DatabaseResetter implements ApplicationListener<ContextRefreshedEvent>{	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {		               
		
	}
}	
