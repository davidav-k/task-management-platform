package com.example.user_service.enumeration.convertor;

import com.example.user_service.enumeration.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;
/**
 * Converter for mapping the Authority enum to a database column and vice versa.
 *
 * <p>This converter is applied automatically to all JPA entity fields of type
 * {@code Authority} due to the {@code @Converter(autoApply = true)} annotation.
 * It converts the enum value to a string for storage in the database and converts
 * the string back to an enum when retrieving it from the database.</p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Converter(autoApply = true)} - Marks this class as a JPA attribute converter and enables automatic application of the converter for all Authority fields.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 *     @Convert(converter = RoleConvertor.class)
 *     private Authority role;
 * </pre>
 */
@Converter(autoApply = true)
public class RoleConvertor implements AttributeConverter<Authority, String> {

    /**
     * Converts the Authority enum to its corresponding string value for storage in the database.
     *
     * @param authority the Authority enum to convert
     * @return the string value associated with the Authority enum, or null if the authority is null
     */
    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if (authority == null) {
            return null;
        }
        return authority.getValue();
    }

    /**
     * Converts the string value from the database back to the corresponding Authority enum.
     *
     * @param code the string value stored in the database
     * @return the corresponding Authority enum for the given code
     * @throws IllegalArgumentException if no matching Authority enum is found for the code
     */
    @Override
    public Authority convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(Authority.values())
                .filter(authority -> authority.getValue().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

