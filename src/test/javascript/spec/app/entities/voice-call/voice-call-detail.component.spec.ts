/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import axios from 'axios';

import * as config from '@/shared/config/config';
import VoiceCallDetailComponent from '@/entities/voice-call/voice-call-details.vue';
import VoiceCallClass from '@/entities/voice-call/voice-call-details.component';
import VoiceCallService from '@/entities/voice-call/voice-call.service';

const localVue = createLocalVue();
const mockedAxios: any = axios;

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

jest.mock('axios', () => ({
  get: jest.fn()
}));

describe('Component Tests', () => {
  describe('VoiceCall Management Detail Component', () => {
    let wrapper: Wrapper<VoiceCallClass>;
    let comp: VoiceCallClass;

    beforeEach(() => {
      mockedAxios.get.mockReset();
      mockedAxios.get.mockReturnValue(Promise.resolve({}));

      wrapper = shallowMount<VoiceCallClass>(VoiceCallDetailComponent, {
        store,
        localVue,
        provide: { voiceCallService: () => new VoiceCallService() }
      });
      comp = wrapper.vm;
    });

    describe('OnInit', async () => {
      it('Should call load all on init', async () => {
        // GIVEN
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: { id: 123 } }));

        // WHEN
        comp.retrieveVoiceCall(123);
        await comp.$nextTick();

        // THEN
        expect(comp.voiceCall).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
