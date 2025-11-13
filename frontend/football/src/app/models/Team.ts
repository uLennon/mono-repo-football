import { Preset } from "./Preset";

export interface Team {
    id: number;
    name: string;
    presets: Preset[];
}