<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="jhipsterApp.voiceCall.home.createOrEditLabel">Create or edit a VoiceCall</h2>
                <div>
                    <div class="form-group" v-if="voiceCall.id">
                        <label for="id">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="voiceCall.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="voice-call-number">Number</label>
                        <input type="text" class="form-control" name="number" id="voice-call-number"
                            :class="{'valid': !$v.voiceCall.number.$invalid, 'invalid': $v.voiceCall.number.$invalid }" v-model="$v.voiceCall.number.$model"  required/>
                        <div v-if="$v.voiceCall.number.$anyDirty && $v.voiceCall.number.$invalid">
                            <small class="form-text text-danger" v-if="!$v.voiceCall.number.required">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="voice-call-message">Message</label>
                        <input type="text" class="form-control" name="message" id="voice-call-message"
                            :class="{'valid': !$v.voiceCall.message.$invalid, 'invalid': $v.voiceCall.message.$invalid }" v-model="$v.voiceCall.message.$model"  required/>
                        <div v-if="$v.voiceCall.message.$anyDirty && $v.voiceCall.message.$invalid">
                            <small class="form-text text-danger" v-if="!$v.voiceCall.message.required">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="voice-call-voice">Voice</label>
                        <select class="form-control" name="voice" :class="{'valid': !$v.voiceCall.voice.$invalid, 'invalid': $v.voiceCall.voice.$invalid }" v-model="$v.voiceCall.voice.$model" id="voice-call-voice"  required>
                            <option value="JOANNA" >JOANNA</option>
                            <option value="MATTHEW" >MATTHEW</option>
                            <option value="CELINE" >CELINE</option>
                            <option value="MATHIEU" >MATHIEU</option>
                        </select>
                        <div v-if="$v.voiceCall.voice.$anyDirty && $v.voiceCall.voice.$invalid">
                            <small class="form-text text-danger" v-if="!$v.voiceCall.voice.required">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="voice-call-twiml">Twiml</label>
                        <input type="text" class="form-control" name="twiml" id="voice-call-twiml"
                            :class="{'valid': !$v.voiceCall.twiml.$invalid, 'invalid': $v.voiceCall.twiml.$invalid }" v-model="$v.voiceCall.twiml.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" for="voice-call-date">Date</label>
                        <div class="d-flex">
                            <input id="voice-call-date" type="datetime-local" class="form-control" name="date" :class="{'valid': !$v.voiceCall.date.$invalid, 'invalid': $v.voiceCall.date.$invalid }"
                            
                            :value="convertDateTimeFromServer($v.voiceCall.date.$model)"
                            @change="updateZonedDateTimeField('date', $event)"/>
                        </div>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.voiceCall.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./voice-call-update.component.ts">
</script>
