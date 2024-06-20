package com.jungle.Tabbit.domain.nfc.repository;

import com.jungle.Tabbit.domain.nfc.entity.Nfc;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface NfcRepository extends Repository<Nfc, String> {
    Optional<Nfc> findByNfcId(String nfcId);
}
