
export interface MatchPlay {
  id: number;
  teamHome: string;
  teamOut: string;
  result: string;
  status: 'Win' | 'Lose' | 'Draw';
  formationHome: string;
  formationOut: string;
  details: string;
  data: Date;
  }