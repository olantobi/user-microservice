package com.liferon.usermgt.service.impl;

import com.liferon.usermgt.repository.SequencerRepository;
import com.liferon.usermgt.model.Sequencer;
import com.liferon.usermgt.service.SequencerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SequencerServiceImpl implements SequencerService {

	@Autowired
	private SequencerRepository sequencerRepository;
	
	@Transactional
	@Override
	public long getNextId(String name) {	
		Sequencer sequencer = sequencerRepository.findById(name).orElse(null);
		if (sequencer == null) {
			sequencer = sequencerRepository.save(new Sequencer(name));
		}
		long nextId = sequencer.increment();
		sequencerRepository.saveAndFlush(sequencer);		
		return nextId;
	}

	@Override
	public long getLastId(String name) {
		Sequencer sequencer = sequencerRepository.findById(name).orElse(null);
		if (sequencer == null) {
			sequencer = sequencerRepository.save(new Sequencer(name));
		}
		return sequencer.getCount();
	}

	@Override
	public void updateId(String name, long count) {
		Sequencer sequencer = sequencerRepository.findById(name).orElse(null);
		if (sequencer == null) {
			sequencer = sequencerRepository.save(new Sequencer(name));
		}
		sequencer.setCount(count);
		
		sequencerRepository.save(sequencer);
	}

}
