package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.RegistrationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationInfoRepository extends JpaRepository<RegistrationInfo, String> {
}
