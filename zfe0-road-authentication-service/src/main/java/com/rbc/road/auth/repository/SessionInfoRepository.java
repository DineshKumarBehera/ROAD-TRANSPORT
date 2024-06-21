package com.rbc.road.auth.repository;

import com.rbc.road.auth.entity.SessionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionInfoRepository extends JpaRepository<SessionInfo, String> {
}
