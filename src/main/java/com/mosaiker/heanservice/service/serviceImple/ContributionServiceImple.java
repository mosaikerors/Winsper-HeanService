package com.mosaiker.heanservice.service.serviceImple;

import com.mosaiker.heanservice.entity.Contribution;
import com.mosaiker.heanservice.repository.ContributionRepository;
import com.mosaiker.heanservice.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContributionServiceImple implements ContributionService {
    @Autowired
    private ContributionRepository contributionRepository;

    @Override
    public List<Contribution> findContributionsByDate(Long date) {
        return contributionRepository.findAllByDate(date);
    }

    @Override
    public List<Contribution> findContributionsByHId(String hId) {
        return contributionRepository.findAllByHId(hId);
    }

    @Override
    public Contribution findContributionByCId(Long cId) {
        return contributionRepository.findByCId(cId);
    }

    @Override
    public void addNewContribution(String hId) {
        Contribution contribution = new Contribution(hId);
        contributionRepository.save(contribution);
    }
}
