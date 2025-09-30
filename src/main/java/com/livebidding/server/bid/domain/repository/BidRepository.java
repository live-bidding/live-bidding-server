package com.livebidding.server.bid.domain.repository;

import com.livebidding.server.bid.domain.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
