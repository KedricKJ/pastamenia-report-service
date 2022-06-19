package com.pastamenia.modelmapper.converter;

import com.pastamenia.enums.DateTimePattern;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(MappingContext<String, LocalDateTime> context) {
        if (context.getSource() == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.STRICT_DATE_TIME_WITHOUT_ZONE.getPattern())
                .withResolverStyle(ResolverStyle.STRICT);
        return (context.getSource() == null ? null : LocalDateTime.parse(context.getSource(), formatter));
    }
}
