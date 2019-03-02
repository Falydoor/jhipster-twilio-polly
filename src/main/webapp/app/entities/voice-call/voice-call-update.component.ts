import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minLength, maxLength } from 'vuelidate/lib/validators';
import format from 'date-fns/format';
import parse from 'date-fns/parse';
import { DATE_TIME_LONG_FORMAT, INSTANT_FORMAT, ZONED_DATE_TIME_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';
import { IVoiceCall, VoiceCall } from '@/shared/model/voice-call.model';
import VoiceCallService from './voice-call.service';

const validations: any = {
  voiceCall: {
    number: {
      required
    },
    message: {
      required
    },
    voice: {
      required
    },
    twiml: {},
    date: {}
  }
};

@Component({
  validations
})
export default class VoiceCallUpdate extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('voiceCallService') private voiceCallService: () => VoiceCallService;
  public voiceCall: IVoiceCall = new VoiceCall();
  public isSaving = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voiceCallId) {
        vm.retrieveVoiceCall(to.params.voiceCallId);
      }
    });
  }

  public save(): void {
    this.isSaving = true;
    if (this.voiceCall.id) {
      this.voiceCallService()
        .update(this.voiceCall)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A VoiceCall is updated with identifier ' + param.id;
          this.alertService().showAlert(message, 'info');
        });
    } else {
      this.voiceCallService()
        .create(this.voiceCall)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A VoiceCall is created with identifier ' + param.id;
          this.alertService().showAlert(message, 'success');
        });
    }
  }

  public convertDateTimeFromServer(date: Date): string {
    if (date) {
      return format(date, DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.voiceCall[field] = parse(event.target.value, DATE_TIME_LONG_FORMAT, new Date());
    } else {
      this.voiceCall[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.voiceCall[field] = parse(event.target.value, DATE_TIME_LONG_FORMAT, new Date());
    } else {
      this.voiceCall[field] = null;
    }
  }

  public retrieveVoiceCall(voiceCallId): void {
    this.voiceCallService()
      .find(voiceCallId)
      .then(res => {
        this.voiceCall = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
