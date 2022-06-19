package com.pastamenia.modelmapper.converter;

/*import com.sunshinelaundry.cleaner.service.CryptoService;*/
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class LongToStringConverter implements Converter<Long,String> {

    /*@Autowired
    private CryptoService cryptoService;*/

    private static final String ATTR_ID = "id";

    @Override
    public String convert(MappingContext<Long, String> context) {
        if (context.getSource() != null) {
            String propertyName = context.getMapping().getLastDestinationProperty().getName();
            /*if(ATTR_ID.equalsIgnoreCase(propertyName)) {
                return cryptoService.encryptEntityId((Long)context.getSource());
            }*/
            return ((Long)context.getSource()).toString();
        }
        return null;
    }
}
