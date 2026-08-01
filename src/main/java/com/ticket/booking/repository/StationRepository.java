package com.ticket.booking.repository;

import com.ticket.booking.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    
    Optional<Station> findByCode(String code);
    
    List<Station> findByCity(String city);
    
    List<Station> findByState(String state);
    
    List<Station> findByNameContainingIgnoreCase(String name);
    List<Station> findByCityIn(List<String> cities);
}