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
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

	@Override
	public LocalDate convert(MappingContext<String, LocalDate> context) {
		if (context.getSource() == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.STRICT_DATE.getPattern())
				.withResolverStyle(ResolverStyle.STRICT);
		return (context.getSource() == null ? null : LocalDate.parse(context.getSource(), formatter));
	}

}
