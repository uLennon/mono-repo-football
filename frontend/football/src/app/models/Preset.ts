import { Player } from "./Player";

export interface Preset {
    presetId: number;
    presetFormation: string;
    presetStrategy: string;
    presetPlayers: Player[];
  }