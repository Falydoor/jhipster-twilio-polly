package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VoiceCall;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VoiceCall entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoiceCallRepository extends JpaRepository<VoiceCall, Long> {

}
