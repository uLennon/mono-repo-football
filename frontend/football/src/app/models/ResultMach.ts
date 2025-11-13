import { Match } from "./Match";


export interface ResultMatch{
  id: string;
  teams: Match[];
  matchDate: string;
  description: string;
  result: {
    scoreteamHome: number;
    scoreteamAway: number;
    winner: string;
  };
}