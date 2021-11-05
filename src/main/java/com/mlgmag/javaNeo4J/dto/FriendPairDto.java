package com.mlgmag.javaNeo4J.dto;

import com.mlgmag.javaNeo4J.entity.employer.Employer;
import lombok.Data;

@Data
public class FriendPairDto {
    private Employer friend1;
    private Employer friend2;
}
