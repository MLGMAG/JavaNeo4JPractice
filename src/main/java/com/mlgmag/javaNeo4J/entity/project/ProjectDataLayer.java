package com.mlgmag.javaNeo4J.entity.project;

import java.util.Set;

public interface ProjectDataLayer {
    String getTitle();

    Set<ProjectEmployer> getEmployers();

    interface ProjectEmployer {
        String getName();
    }

}
