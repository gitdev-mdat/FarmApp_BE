package com.farmapp.service.id;

import com.farmapp.model.IdSequence;
import com.farmapp.repository.IdSequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdSequenceService {

    private final IdSequenceRepository idSequenceRepository;

    @Autowired
    public IdSequenceService(IdSequenceRepository idSequenceRepository) {
        this.idSequenceRepository = idSequenceRepository;
    }

    @Transactional
    public int getNextIndex(String entityName) {
        IdSequence sequence = idSequenceRepository.findById(entityName)
                .orElseGet(() -> {
                    IdSequence newSeq = new IdSequence();
                    newSeq.setEntity(entityName);
                    newSeq.setLastIndex(0);
                    return newSeq;
                });

        int nextIndex = sequence.getLastIndex() + 1;
        sequence.setLastIndex(nextIndex);
        idSequenceRepository.save(sequence);
        return nextIndex;
    }

    // Optional: Reset hoặc get hiện tại
    public int getCurrentIndex(String entityName) {
        return idSequenceRepository.findById(entityName)
                .map(IdSequence::getLastIndex)
                .orElse(0);
    }

    public void resetIndex(String entityName) {
        idSequenceRepository.findById(entityName).ifPresent(seq -> {
            seq.setLastIndex(0);
            idSequenceRepository.save(seq);
        });
    }
}
