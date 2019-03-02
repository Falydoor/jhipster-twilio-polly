export const enum Voice {
  JOANNA = 'JOANNA',
  MATTHEW = 'MATTHEW',
  CELINE = 'CELINE',
  MATHIEU = 'MATHIEU'
}

export interface IVoiceCall {
  id?: number;
  number?: string;
  message?: string;
  voice?: Voice;
  twiml?: string;
  date?: Date;
}

export class VoiceCall implements IVoiceCall {
  constructor(
    public id?: number,
    public number?: string,
    public message?: string,
    public voice?: Voice,
    public twiml?: string,
    public date?: Date
  ) {}
}
