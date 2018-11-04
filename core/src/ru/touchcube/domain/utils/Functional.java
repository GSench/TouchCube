package ru.touchcube.domain.utils;

/**
 * Created by grish on 04.11.2018.
 */

public interface Functional<T,V> {
    public V get(T ... params);
}
