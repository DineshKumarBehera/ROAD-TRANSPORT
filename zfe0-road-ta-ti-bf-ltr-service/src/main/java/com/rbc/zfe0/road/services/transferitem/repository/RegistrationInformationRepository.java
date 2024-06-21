package com.rbc.zfe0.road.services.transferitem.repository;

import com.rbc.zfe0.road.services.transferitem.entity.RegistrationInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationInformationRepository extends JpaRepository<RegistrationInformation, String> {

    RegistrationInformation findByRegistrationKey(String registrationName);

    Optional<RegistrationInformation> findByTransactionId(String transactionId);

    List<RegistrationInformation> findByTelephoneOrFax(String telephone, String fax);

}

