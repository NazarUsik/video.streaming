package com.test.task.stats.data.converter;

import com.test.task.stats.data.entity.Action;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ActionConverter implements AttributeConverter<Action, String> {
    @Override
    public String convertToDatabaseColumn(Action action) {
        if (action == null) {
            return null;
        }

        return action.getAction();
    }

    @Override
    public Action convertToEntityAttribute(String actionStr) {
        return Action.fromString(actionStr);
    }
}
