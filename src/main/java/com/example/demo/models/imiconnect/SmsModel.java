package com.example.demo.models.imiconnect;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SmsModel {
    private String deliverychannel;
    @JsonProperty("channels")
    private Channels channels;
    @JsonProperty("destination")
    List<Destination> destination;
}
