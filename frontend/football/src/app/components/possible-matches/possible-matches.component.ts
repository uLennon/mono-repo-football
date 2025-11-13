import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PossibleMatchService } from '../../services/possible-match.service';
import { FormationService } from '../../services/formation.service';
import { PlayMatch } from '../../models/PlayMatch';


export interface MatchDTO{
  teamId: number;
  name: string;
  id: string;
  presetStrategy: string;
  presetFormation: string;
}

export interface MatchD{
  teamIdVerso?: string;
  teamId: number;
  name: string;
  presetStrategy: string;
  presetFormation: string;
}


@Component({
  selector: 'app-possible-matches',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './possible-matches.component.html',
  styleUrl: './possible-matches.component.css'
})

export class PossibleMatchesComponent implements OnInit{
  
  availableMatches: MatchDTO [] = [];
  page = 0;
  size = 6;
  totalPages = 0;
  
  homeTeam: MatchD | null = null;

  selectedMatch : MatchDTO | null = null;

  constructor(private possibleMatch:PossibleMatchService, private formationService: FormationService){}

  ngOnInit(): void {
   this.loadMatches();

   this.formationService.getDataTeam().subscribe(data =>{
      this.homeTeam = data;
   });

  }

  loadMatches(): void {
    this.possibleMatch.listPaginated(this.page, this.size).subscribe(data => {
      this.availableMatches = data.content; 
      this.totalPages = data.totalPages;
    });
  }

  openDuelModal(match: MatchDTO): void {
    this.selectedMatch = match;
    // Show Bootstrap modal
    const modal = new (window as any).bootstrap.Modal(document.getElementById('duelModal'));
    modal.show();
  }

  confirmDuel(): void {
    if (this.selectedMatch) {
      const matchPlay: PlayMatch = {
        name: '',
        presetFormation: '',
        presetStrategy: '',
        teamId: 0,
        teamIdVerso: '' 
      };
      
      this.formationService.getDataTeam().subscribe(data =>{
        matchPlay.name = data.name,
        matchPlay.presetFormation = data.presetFormation,
        matchPlay.presetStrategy = data.presetStrategy
        matchPlay.teamId = data.teamId;
        if (this.selectedMatch) {
          matchPlay.teamIdVerso = this.selectedMatch.id;
        }
      })
      
      this.possibleMatch.playMatch(matchPlay).subscribe();
    
      const modal = (window as any).bootstrap.Modal.getInstance(document.getElementById('duelModal'));
      modal.hide();
      
      this.selectedMatch = null;
    }
  }


  nextPage(): void {
    this.page++;
    this.loadMatches();
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadMatches();
    }
  }
}
