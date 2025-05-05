package it.epicode.bw.finale.clienti;

import org.springframework.data.jpa.domain.Specification;

public class ClienteSpecification {
    public static Specification<Cliente> filterBy(ClienteFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (filter.getFatturatoAnnuale() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("fatturatoAnnuale"), filter.getFatturatoAnnuale()));
            }

            if (filter.getDataInserimento() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("dataInserimento"), filter.getDataInserimento()));
            }

            if (filter.getDataUltimoContatto() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("dataUltimoContatto"), filter.getDataUltimoContatto()));
            }

            if (filter.getNomeParziale() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("ragioneSociale"), "%" + filter.getNomeParziale() + "%"));
            }
            return predicate;
        };
    }
}
