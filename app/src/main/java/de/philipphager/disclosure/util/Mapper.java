package de.philipphager.disclosure.util;

public interface Mapper<K, V> {
  V map(K from);
}
