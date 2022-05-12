package it.palex.attendanceManagement.library.rest.dtos;

public class PairDTO<K,T> implements DTO{

    private static final long serialVersionUID = 1L;

    private K key;
    private T value;

    public PairDTO(K key, T value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
