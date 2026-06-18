package com.cakesandsnacks.repository;

import com.cakesandsnacks.entity.Address;
import com.cakesandsnacks.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Address Repository
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByUserId(Long userId);
    List<Address> findByUserIdAndAddressType(Long userId, AddressType addressType);
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
}
