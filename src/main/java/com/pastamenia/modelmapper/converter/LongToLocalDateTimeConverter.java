package com.pastamenia.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Nuwan
 */
@Component
public class LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
    @Override
    public LocalDateTime convert(MappingContext<Long, LocalDateTime> context) {
        if (context.getSource() == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(context.getSource()), ZoneId.systemDefault());
    }
}
