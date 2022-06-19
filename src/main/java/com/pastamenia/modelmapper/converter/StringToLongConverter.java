package com.pastamenia.modelmapper.converter;

/*import com.sunshinelaundry.cleaner.service.CryptoService;*/

/*@Component
public class StringToLongConverter implements Converter<String,Long> {

    *//*@Autowired
    private CryptoService cryptoService;*//*

    private static final String ATTR_ID = "id";


    @Override
    public Long convert(MappingContext<String, Long> mappingContext) {
        if(!StringUtils.isEmpty(mappingContext.getSource())) {
            *//*if (mappingContext.getMapping() == null) {
                return cryptoService.decryptEntityId((String) mappingContext.getSource());
            }*//*

            String propertyName = mappingContext.getMapping().getLastDestinationProperty().getName();

            *//*if(propertyName.equalsIgnoreCase(ATTR_ID)) {
                return cryptoService.decryptEntityId((String)mappingContext.getSource());
            }*//*
            return mappingContext.getSource() == null ? null : Long.valueOf((String) mappingContext.getSource());

        }
        return null;
    }
}*/
