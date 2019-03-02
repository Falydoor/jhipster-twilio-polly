/* tslint:disable max-line-length */
import axios from 'axios';
import { format } from 'date-fns';

import * as config from '@/shared/config/config';
import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import VoiceCallService from '@/entities/voice-call/voice-call.service';
import { VoiceCall, Voice } from '@/shared/model/voice-call.model';

const mockedAxios: any = axios;
jest.mock('axios', () => ({
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  delete: jest.fn()
}));

describe('Service Tests', () => {
  describe('VoiceCall Service', () => {
    let service: VoiceCallService;
    let elemDefault;
    let currentDate: Date;
    beforeEach(() => {
      service = new VoiceCallService();
      currentDate = new Date();

      elemDefault = new VoiceCall(0, 'AAAAAAA', 'AAAAAAA', Voice.JOANNA, 'AAAAAAA', currentDate);
    });

    describe('Service methods', async () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            date: format(currentDate, DATE_TIME_FORMAT)
          },
          elemDefault
        );
        mockedAxios.get.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should create a VoiceCall', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: format(currentDate, DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            date: currentDate
          },
          returnedFromService
        );

        mockedAxios.post.mockReturnValue(Promise.resolve({ data: returnedFromService }));
        service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should update a VoiceCall', async () => {
        const returnedFromService = Object.assign(
          {
            number: 'BBBBBB',
            message: 'BBBBBB',
            voice: 'BBBBBB',
            twiml: 'BBBBBB',
            date: format(currentDate, DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate
          },
          returnedFromService
        );
        mockedAxios.put.mockReturnValue(Promise.resolve({ data: returnedFromService }));

        service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should return a list of VoiceCall', async () => {
        const returnedFromService = Object.assign(
          {
            number: 'BBBBBB',
            message: 'BBBBBB',
            voice: 'BBBBBB',
            twiml: 'BBBBBB',
            date: format(currentDate, DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            date: currentDate
          },
          returnedFromService
        );
        mockedAxios.get.mockReturnValue(Promise.resolve([returnedFromService]));
        service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should delete a VoiceCall', async () => {
        mockedAxios.delete.mockReturnValue(Promise.resolve({ ok: true }));
        service.delete(123).then(res => {
          expect(res.ok);
        });
      });
    });
  });
});
