package com.pastamenia.dto.wrapper;

import com.pastamenia.enums.RestApiResponseStatus;
import lombok.Data;

import java.util.List;

@Data
public class  ListResponseWrapper<T> extends BaseResponseWrapper {

    private List<T> content;

    public ListResponseWrapper(List<T> content) {
        super(RestApiResponseStatus.OK);
        this.content = content;
    }

    public List<T> getContent() {
        return this.content;
    }

    public void setContent(final List<T> content) {
        this.content = content;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ListResponseWrapper)) {
            return false;
        } else {
            ListResponseWrapper<?> other = (ListResponseWrapper)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$content = this.getContent();
                Object other$content = other.getContent();
                if (this$content == null) {
                    if (other$content != null) {
                        return false;
                    }
                } else if (!this$content.equals(other$content)) {
                    return false;
                }

                return true;
            }
        }
    }

    /*protected boolean canEqual(final Object other) {
        return other instanceof ListResponseWrapper;
    }

    public int hashCode() {
        int PRIME = true;
        int result = 1;
        Object $content = this.getContent();
        int result = result * 59 + ($content == null ? 43 : $content.hashCode());
        return result;
    }*/


}
