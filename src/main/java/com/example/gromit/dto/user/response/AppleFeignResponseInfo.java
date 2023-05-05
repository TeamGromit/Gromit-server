package com.example.gromit.dto.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppleFeignResponseInfo {

    @JsonProperty("keys")
    private List<Key> keys;

    @Getter
    public static class Key {

        @JsonProperty("kty")
        private String kty;
        @JsonProperty("kid")
        private String kid;
        @JsonProperty("use")
        private String use;
        @JsonProperty("alg")
        private String alg;
        @JsonProperty("n")
        private String n;
        @JsonProperty("e")
        private String e;
    }
}
