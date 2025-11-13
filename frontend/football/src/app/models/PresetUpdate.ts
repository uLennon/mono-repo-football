import { Player } from "./Player";

export interface PresetUpdate{
    presetId: number;
    teamId: number;
    strategy: string;
    formation: string;
    players: Player[];
  
}