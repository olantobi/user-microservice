package com.liferon.usermgt.repository;

import com.liferon.usermgt.model.Sequencer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequencerRepository extends JpaRepository<Sequencer, String>{
	Sequencer getByName(String name);	
}
