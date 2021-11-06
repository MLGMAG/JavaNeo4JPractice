package com.mlgmag.javaNeo4J.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistanceInfo {
    private String target;
    int distance;
}
