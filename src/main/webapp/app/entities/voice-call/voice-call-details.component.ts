import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVoiceCall } from '@/shared/model/voice-call.model';
import VoiceCallService from './voice-call.service';

@Component
export default class VoiceCallDetails extends Vue {
  @Inject('voiceCallService') private voiceCallService: () => VoiceCallService;
  public voiceCall: IVoiceCall = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voiceCallId) {
        vm.retrieveVoiceCall(to.params.voiceCallId);
      }
    });
  }

  public retrieveVoiceCall(voiceCallId) {
    this.voiceCallService()
      .find(voiceCallId)
      .then(res => {
        this.voiceCall = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
