package com.farmapp.helper;

import com.farmapp.service.id.IdSequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class IdGenerator {

    private final IdSequenceService idSequenceService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");

    private String generate(String prefix, String entityName) {
        int index = idSequenceService.getNextIndex(entityName);
        String date = LocalDate.now().format(DATE_FORMAT);
        return String.format("%s-%s-%03d", prefix,date, index);
//        return String.format("%s-%s", prefix,date);
    }

    public String generateDepositId() {
        return generate("D", "deposit");
    }

    public String generateCloseId() {
        return generate("C", "close");
    }

    public String generateSellId() {
        return generate("S", "sell");
    }
}
