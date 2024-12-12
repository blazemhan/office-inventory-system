package com.blazemhan.gptoffice.repository;

import com.blazemhan.gptoffice.entity.SupplyItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {

    boolean existsByName(String name);
}