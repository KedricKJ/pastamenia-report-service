package com.pastamenia.dto.wrapper;


import com.pastamenia.enums.RestApiResponseStatus;
import lombok.Data;

@Data
public class SingleResponseWrapper<T> extends BaseResponseWrapper {

    public SingleResponseWrapper(){}

    private T content;
    //private HttpStatus status;

    public SingleResponseWrapper(T object, String status) {
        super(RestApiResponseStatus.OK);
        this.content = object;
        this.status = status;
    }

    public SingleResponseWrapper(T object) {
        super(RestApiResponseStatus.OK);
        this.content = object;
    }


}
