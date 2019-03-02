<template>
    <div>
        <h2 id="page-heading">
            <span id="voice-call-heading">Voice Calls</span>
            <router-link :to="{name: 'VoiceCallCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-voice-call">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span >
                    Create new VoiceCall
                </span>
            </router-link>
        </h2>
        <b-alert :show="dismissCountDown"
            dismissible
            :variant="alertType"
            @dismissed="dismissCountDown=0"
            @dismiss-count-down="countDownChanged">
            {{alertMessage}}
        </b-alert>
        <br/>
        <div class="table-responsive" v-if="voiceCalls">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><span>ID</span></th>
                    <th><span>Number</span></th>
                    <th><span>Message</span></th>
                    <th><span>Voice</span></th>
                    <th><span>Twiml</span></th>
                    <th><span>Date</span></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="voiceCall in voiceCalls"
                    :key="voiceCall.id">
                    <td>
                        <router-link :to="{name: 'VoiceCallView', params: {voiceCallId: voiceCall.id}}">{{voiceCall.id}}</router-link>
                    </td>
                    <td>{{voiceCall.number}}</td>
                    <td>{{voiceCall.message}}</td>
                    <td>{{voiceCall.voice}}</td>
                    <td>{{voiceCall.twiml}}</td>
                    <td>{{voiceCall.date | formatDate}}</td>
                    <td class="text-right">
                        <div class="btn-group flex-btn-group-container">
                            <router-link :to="{name: 'VoiceCallView', params: {voiceCallId: voiceCall.id}}" tag="button" class="btn btn-info btn-sm">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline">View</span>
                            </router-link>
                            <router-link :to="{name: 'VoiceCallEdit', params: {voiceCallId: voiceCall.id}}"  tag="button" class="btn btn-primary btn-sm">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(voiceCall)"
                                   class="btn btn-danger btn-sm"
                                   v-b-modal.removeEntity>
                                <font-awesome-icon icon="times"></font-awesome-icon>
                                <span class="d-none d-md-inline">Delete</span>
                            </b-button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <b-modal ref="removeEntity" id="removeEntity" >
            <span slot="modal-title"><span id="jhipsterApp.voiceCall.delete.question">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-voiceCall-heading" >Are you sure you want to delete this Voice Call?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-voiceCall" v-on:click="removeVoiceCall()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./voice-call.component.ts">
</script>
