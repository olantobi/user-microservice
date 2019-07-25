package com.liferon.usermgt.service;

public interface SequencerService {
	long getNextId(String name);
	long getLastId(String name);
	void updateId(String name, long count);
}
