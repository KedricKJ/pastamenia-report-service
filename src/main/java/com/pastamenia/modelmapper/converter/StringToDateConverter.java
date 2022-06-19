package com.pastamenia.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class StringToDateConverter implements Converter<String, Date> {


    @Override
    public Date convert(MappingContext<String, Date> context) {

        if (context.getSource() == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Colombo"));
        try {
            return formatter.parse(context.getSource());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
