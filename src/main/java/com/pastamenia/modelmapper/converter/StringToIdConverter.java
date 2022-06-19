package com.pastamenia.modelmapper.converter;

/*@Component
public class StringToIdConverter implements Converter<String,String> {


    *//*@Autowired
    private CryptoService cryptoService;*//*

    private static final String ATTR_ID = "id";


    @Override
    public String convert(MappingContext<String, String> mappingContext) {

        if(!StringUtils.isEmpty(mappingContext.getSource())) {

            if (mappingContext.getMapping() == null) {
                return mappingContext.getSource();
                //return cryptoService.decryptEntityId((String) mappingContext.getSource());
            }

            String propertyName = mappingContext.getMapping().getLastDestinationProperty().getName();

            System.out.println("destination property name {}"+propertyName);

            *//*if(propertyName.equalsIgnoreCase(ATTR_ID)) {
                return cryptoService.decryptEntityId((String)mappingContext.getSource());
            }*//*
            return mappingContext.getSource() == null ? null : mappingContext.getSource();

        }
        return null;
    }


}*/
