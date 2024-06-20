package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Address;
import org.springframework.data.repository.Repository;

public interface AddressRepository extends Repository<Address, Long> {
    Address save(Address address);
}