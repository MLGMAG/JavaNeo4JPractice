package com.mlgmag.javaNeo4J.utils;

import com.mlgmag.javaNeo4J.model.DistanceInfo;

import java.util.Comparator;

public final class DistanceUtils {

    private DistanceUtils() {
    }

    public static final Comparator<DistanceInfo> DISTANCE_INFO_COMPARATOR = Comparator.comparingInt(DistanceInfo::getDistance).reversed();

}
