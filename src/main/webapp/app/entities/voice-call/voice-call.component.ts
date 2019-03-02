import { mixins } from 'vue-class-component';
import { Component, Inject, Vue } from 'vue-property-decorator';
import { IVoiceCall } from '@/shared/model/voice-call.model';
import AlertService from '@/shared/alert/alert.service';

import VoiceCallService from './voice-call.service';

@Component
export default class VoiceCall extends Vue {
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('voiceCallService') private voiceCallService: () => VoiceCallService;
  private removeId: string = null;
  public voiceCalls: IVoiceCall[] = [];

  public dismissCountDown: number = this.$store.getters.dismissCountDown;
  public dismissSecs: number = this.$store.getters.dismissSecs;
  public alertType: string = this.$store.getters.alertType;
  public alertMessage: any = this.$store.getters.alertMessage;

  public getAlertFromStore() {
    this.dismissCountDown = this.$store.getters.dismissCountDown;
    this.dismissSecs = this.$store.getters.dismissSecs;
    this.alertType = this.$store.getters.alertType;
    this.alertMessage = this.$store.getters.alertMessage;
  }

  public countDownChanged(dismissCountDown: number) {
    this.alertService().countDownChanged(dismissCountDown);
    this.getAlertFromStore();
  }

  public mounted(): void {
    this.retrieveAllVoiceCalls();
  }

  public clear(): void {
    this.retrieveAllVoiceCalls();
  }

  public retrieveAllVoiceCalls(): void {
    this.voiceCallService()
      .retrieve()
      .then(res => {
        this.voiceCalls = res.data;
      });
  }

  public prepareRemove(instance): void {
    this.removeId = instance.id;
  }

  public removeVoiceCall(): void {
    this.voiceCallService()
      .delete(this.removeId)
      .then(() => {
        const message = 'A VoiceCall is deleted with identifier ' + this.removeId;
        this.alertService().showAlert(message, 'danger');
        this.getAlertFromStore();

        this.removeId = null;
        this.retrieveAllVoiceCalls();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
