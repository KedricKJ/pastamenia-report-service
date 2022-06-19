package com.pastamenia.modelmapper.converter;

import com.pastamenia.enums.DateTimePattern;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * 
 * @author Nuwan
 *
 */
@Component
public class LocalDateToStringConverter implements Converter<LocalDate, String> {

	@Override
	public String convert(MappingContext<LocalDate, String> context) {
		if (context.getSource() != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.STRICT_DATE.getPattern())
					.withResolverStyle(ResolverStyle.STRICT);
			return formatter.format(context.getSource());
		} else {
			return null;
		}

	}

}
