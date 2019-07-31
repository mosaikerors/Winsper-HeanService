package com.mosaiker.heanservice.service;

import com.mosaiker.heanservice.entity.Contribution;

import java.util.List;

public interface ContributionService {
    List<Contribution> findContributionsByDate(Long date);

    List<Contribution> findContributionsByHId(String hId);

    Contribution findContributionByCId(Long cId);

    void addNewContribution(String hId);
}
