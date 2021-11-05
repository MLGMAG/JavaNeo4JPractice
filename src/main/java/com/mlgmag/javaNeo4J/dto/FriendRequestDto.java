package com.mlgmag.javaNeo4J.dto;

import lombok.Data;

import java.util.List;

@Data
public class FriendRequestDto {
    private List<FriendPairDto> friendPairs;
}
