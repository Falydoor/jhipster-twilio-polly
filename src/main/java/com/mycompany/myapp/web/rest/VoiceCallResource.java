package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.VoiceCall;
import com.mycompany.myapp.repository.VoiceCallRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.twilio.Twilio;
import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Pause;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
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

    private static final String BUCKET = "MY_BUCKET";

    private static final String TWILIO_NUMBER = "NUMBER";

    private final VoiceCallRepository voiceCallRepository;

    private final S3Client s3;

    public VoiceCallResource(VoiceCallRepository voiceCallRepository) {
        this.voiceCallRepository = voiceCallRepository;
        // Initialize Twilio
        Twilio.init("", "");
        // Create S3 client using default credential profile
        s3 = S3Client.builder().region(Region.US_EAST_1).credentialsProvider(ProfileCredentialsProvider.create()).build();
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

        // Generate TwiML
        Say say = new Say.Builder(voiceCall.getMessage()).voice(Say.Voice.valueOf("POLLY_" + voiceCall.getVoice().name())).build();
        VoiceResponse twiml = new VoiceResponse.Builder()
            .say(say)
            .pause(new Pause.Builder().length(1).build())
            .build();

        // Update VoiceCall
        voiceCall.twiml(twiml.toXml())
            .date(ZonedDateTime.now());

        VoiceCall result = voiceCallRepository.save(voiceCall);

        // Save TwiML to S3 with public read
        PutObjectRequest putRequest = PutObjectRequest.builder()
            .bucket(BUCKET)
            .key(result.getId() + ".xml")
            .contentType("text/xml")
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build();
        s3.putObject(putRequest, software.amazon.awssdk.core.sync.RequestBody.fromString(result.getTwiml()));

        // Make call using Twilio
        String twimlUri = "https://s3.amazonaws.com/" + putRequest.bucket() + "/" + putRequest.key();
        Call call = Call.creator(new PhoneNumber(voiceCall.getNumber()), new PhoneNumber(TWILIO_NUMBER), new URI(twimlUri))
            .setMethod(HttpMethod.GET)
            .create();
        log.debug("CALL SID : {}", call.getSid());

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
