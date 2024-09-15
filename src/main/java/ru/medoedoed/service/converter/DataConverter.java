package ru.medoedoed.service.converter;

public interface DataConverter<JpaT, DataT> {
    JpaT DataToJpa(DataT data);
    DataT JpaToData(JpaT jpa);
}
