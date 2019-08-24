package com.mosaiker.heanservice.repository;

import com.mosaiker.heanservice.entity.Contribution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionRepository extends CrudRepository<Contribution, Long> {
    List<Contribution> findAllByDateAfter(Long date);

    List<Contribution> findAllByHId(String hId);

    Contribution findByCId(Long cId);

    List<Contribution> findAllByUId(Long uId);

}
