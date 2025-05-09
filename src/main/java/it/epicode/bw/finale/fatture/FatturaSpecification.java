package it.epicode.bw.finale.fatture;

import org.springframework.data.jpa.domain.Specification;

public class FatturaSpecification {
    public static Specification<Fattura> filterBy(FatturaFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (filter.getIdCliente() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("cliente").get("id"), filter.getIdCliente()));
            }

            if (filter.getData() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("data"), filter.getData()));
            }

            if (filter.getAnno() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("data")), filter.getAnno().getYear()));
            }

            if (filter.getImportoDa() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("importo"), filter.getImportoDa()));
            }

            if (filter.getImportoA() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("importo"), filter.getImportoA()));
            }

            return predicate;
        };
    }
}
