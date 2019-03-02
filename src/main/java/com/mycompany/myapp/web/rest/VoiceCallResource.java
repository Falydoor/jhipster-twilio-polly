package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.domain.VoiceCall;
import com.mycompany.myapp.repository.VoiceCallRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing VoiceCall.
 */
@RestController
@RequestMapping("/api")
public class VoiceCallResource {

    private final Logger log = LoggerFactory.getLogger(VoiceCallResource.class);

    private static final String ENTITY_NAME = "voiceCall";

    private final VoiceCallRepository voiceCallRepository;

    public VoiceCallResource(VoiceCallRepository voiceCallRepository) {
        this.voiceCallRepository = voiceCallRepository;
    }

    /**
     * POST  /voice-calls : Create a new voiceCall.
     *
     * @param voiceCall the voiceCall to create
     * @return the ResponseEntity with status 201 (Created) and with body the new voiceCall, or with status 400 (Bad Request) if the voiceCall has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/voice-calls")
    public ResponseEntity<VoiceCall> createVoiceCall(@Valid @RequestBody VoiceCall voiceCall) throws URISyntaxException {
        log.debug("REST request to save VoiceCall : {}", voiceCall);
        if (voiceCall.getId() != null) {
            throw new BadRequestAlertException("A new voiceCall cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoiceCall result = voiceCallRepository.save(voiceCall);
        return ResponseEntity.created(new URI("/api/voice-calls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /voice-calls : Updates an existing voiceCall.
     *
     * @param voiceCall the voiceCall to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated voiceCall,
     * or with status 400 (Bad Request) if the voiceCall is not valid,
     * or with status 500 (Internal Server Error) if the voiceCall couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/voice-calls")
    public ResponseEntity<VoiceCall> updateVoiceCall(@Valid @RequestBody VoiceCall voiceCall) throws URISyntaxException {
        log.debug("REST request to update VoiceCall : {}", voiceCall);
        if (voiceCall.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VoiceCall result = voiceCallRepository.save(voiceCall);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, voiceCall.getId().toString()))
            .body(result);
    }

    /**
     * GET  /voice-calls : get all the voiceCalls.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of voiceCalls in body
     */
    @GetMapping("/voice-calls")
    public List<VoiceCall> getAllVoiceCalls() {
        log.debug("REST request to get all VoiceCalls");
        return voiceCallRepository.findAll();
    }

    /**
     * GET  /voice-calls/:id : get the "id" voiceCall.
     *
     * @param id the id of the voiceCall to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the voiceCall, or with status 404 (Not Found)
     */
    @GetMapping("/voice-calls/{id}")
    public ResponseEntity<VoiceCall> getVoiceCall(@PathVariable Long id) {
        log.debug("REST request to get VoiceCall : {}", id);
        Optional<VoiceCall> voiceCall = voiceCallRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(voiceCall);
    }

    /**
     * DELETE  /voice-calls/:id : delete the "id" voiceCall.
     *
     * @param id the id of the voiceCall to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/voice-calls/{id}")
    public ResponseEntity<Void> deleteVoiceCall(@PathVariable Long id) {
        log.debug("REST request to delete VoiceCall : {}", id);
        voiceCallRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
