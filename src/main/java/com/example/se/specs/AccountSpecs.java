package com.example.se.specs;

import com.example.se.model.Accounts;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class AccountSpecs {
    /**
     * To add predicates for account filtering.
     * Also performs order by based on account name.
     * @param accountId
     * @return
     */
    public Specification<Accounts> getAccountSpec (Long accountId){
    return (root, query, criteriaBuilder) -> {

        List<Predicate> predicates = new ArrayList<>();
        query.distinct(true);
        if(Objects.nonNull(accountId))
            predicates.add(criteriaBuilder.equal(root.get("accountId"), accountId));
        query.orderBy((criteriaBuilder.asc(root.get("name"))));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
}
}
