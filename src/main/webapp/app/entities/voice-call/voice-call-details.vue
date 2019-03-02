<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="voiceCall">
                <h2><span>VoiceCall</span> {{voiceCall.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span>Number</span>
                    </dt>
                    <dd>
                        <span>{{voiceCall.number}}</span>
                    </dd>
                    <dt>
                        <span>Message</span>
                    </dt>
                    <dd>
                        <span>{{voiceCall.message}}</span>
                    </dd>
                    <dt>
                        <span>Voice</span>
                    </dt>
                    <dd>
                        <span>{{voiceCall.voice}}</span>
                    </dd>
                    <dt>
                        <span>Twiml</span>
                    </dt>
                    <dd>
                        <span>{{voiceCall.twiml}}</span>
                    </dd>
                    <dt>
                        <span>Date</span>
                    </dt>
                    <dd>
                        <span>{{voiceCall.date | formatDate}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
                </button>
                <router-link v-if="voiceCall.id" :to="{name: 'VoiceCallEdit', params: {voiceCallId: voiceCall.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./voice-call-details.component.ts">
</script>
