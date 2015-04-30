package com.excilys.model.beans;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author excilys
 * This class contains methods to convert Timestamp objects into 
 *   LocalDateTime objects and vice-versa, 
 *   for ORM compatibility purpose.
 */
@Converter(autoApply = true)
public class LocalDateTimeToTimestampConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime entityAttribute) {
		if (entityAttribute == null) {
			return null;
		}
		return Timestamp.valueOf(entityAttribute);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp column) {
		if (column == null) {
			return null;
		}
		return column.toLocalDateTime();
	}

}