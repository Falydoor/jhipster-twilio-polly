package com.mycompany.myapp.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Voice;

/**
 * A VoiceCall.
 */
@Entity
@Table(name = "voice_call")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VoiceCall implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "voice", nullable = false)
    private Voice voice;

    @Column(name = "twiml")
    private String twiml;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public VoiceCall number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public VoiceCall message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Voice getVoice() {
        return voice;
    }

    public VoiceCall voice(Voice voice) {
        this.voice = voice;
        return this;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public String getTwiml() {
        return twiml;
    }

    public VoiceCall twiml(String twiml) {
        this.twiml = twiml;
        return this;
    }

    public void setTwiml(String twiml) {
        this.twiml = twiml;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public VoiceCall date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VoiceCall voiceCall = (VoiceCall) o;
        if (voiceCall.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), voiceCall.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VoiceCall{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", message='" + getMessage() + "'" +
            ", voice='" + getVoice() + "'" +
            ", twiml='" + getTwiml() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
