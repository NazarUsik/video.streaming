package com.test.task.videostream.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CriteriaPredicateBuilderHelper {
    public Predicate equal(CriteriaBuilder criteriaBuilder, Root<?> root, String field, String value) {
        return criteriaBuilder.equal(
                criteriaBuilder.upper(root.get(field)),
                StringUtils.isBlank(value) ? "" : value.toUpperCase());
    }


    public Predicate betweenOrEqual(CriteriaBuilder criteriaBuilder, Root<?> root, String field,
                                    Integer greaterThan, Integer lessThan, Integer equalTo) {
        List<Predicate> predicates = new ArrayList<>();

        if (greaterThan != null) {
            predicates.add(criteriaBuilder.greaterThan(root.get(field), greaterThan));
        }
        if (lessThan != null) {
            predicates.add(criteriaBuilder.lessThan(root.get(field), lessThan));
        }
        if (equalTo != null) {
            predicates.add(criteriaBuilder.equal(root.get(field), equalTo));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public Predicate like(CriteriaBuilder criteriaBuilder, Root<?> root, String field, String value) {
        return criteriaBuilder.like(
                criteriaBuilder.upper(root.get(field)),
                StringUtils.isBlank(value) ? "" : "%" + value.toUpperCase() + "%");
    }
}
