package com.wenyiguai.commons.mybaits;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by tree on 6/5/17.
 */
@Getter
@Setter
public class Paging<T> implements Serializable {

    Long total;
    List<T> data;

    public Paging() {
    }

    public Paging(Long total, List<T> data) {
        this.total = total;
        this.data = data;
    }

    public Boolean isEmpty() {
        return Objects.equals(0L, this.total) || this.data == null || this.data.isEmpty();
    }

    public static<T> Paging<T> empty(Class<T> clazz) {
        List<T> emptyList = Collections.emptyList();
        return new Paging<T>(0L, emptyList);
    }

    public static<T> Paging<T> empty() {
        return new Paging<T>(0L, Collections.<T>emptyList());
    }

}
