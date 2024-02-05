package com.example.demo.models.imiconnect;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destination {
    @JsonProperty("msisdn")
    List<String> msisdn;
    @JsonProperty("correlationid")
    private String correlationid;

}
