package com.mlgmag.javaNeo4J.entity.employer;

import java.util.Set;

public interface EmployerDataLayer {
    String getName();

    int getAge();

    Set<Friend> getFriends();

    interface Friend {
        String getName();
        String getAge();
    }
}
