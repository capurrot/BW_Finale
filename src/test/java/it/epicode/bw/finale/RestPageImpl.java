package it.epicode.bw.finale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties(value = { "pageable", "sort" }, ignoreUnknown = true) // <- evita l'errore
public class RestPageImpl<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPageImpl(
            @JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") long totalElements
    ) {
        super(content, Pageable.ofSize(size).withPage(number), totalElements);
    }

    public RestPageImpl(List<T> content) {
        super(content);
    }

    public RestPageImpl() {
        super(List.of());
    }
}
