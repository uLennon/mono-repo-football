import { Component } from '@angular/core';
import { MatchPlay } from '../../models/MatchPlay';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { TeamService } from '../../services/team.service';
import { ResultMatch } from '../../models/ResultMach';


@Component({
  selector: 'app-match-list',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './match-list.component.html',
  styleUrl: './match-list.component.css'
})
export class MatchListComponent {
  
  
  matchSelected: ResultMatch | null = null;
  historico: ResultMatch[] = [];
  private subscription: Subscription = new Subscription();
  page: number = 0;
  size: number = 5;
  totalPages: number = 0;

  constructor(private teamService: TeamService) {}

  ngOnInit() {

    this.loadDataHistoric();
    this.subscription.add(
      this.teamService.receberPartidas().subscribe(partida => {
        this.addPartidaNoInicio(partida);
      })
    );
  }

  chooseMatch(resultMatch: ResultMatch) {
    this.matchSelected = resultMatch;
  }

  getBadgeClass(winner: string, teamHomeName: string): string {
    if (winner === 'Draw') return 'bg-warning';
    return winner === teamHomeName ? 'bg-success' : 'bg-danger';
  }

  getStatusText(winner: string, teamHomeName: string): string {
    if (winner === 'Empate') return 'Draw';
    return winner === teamHomeName ? 'Win' : 'Lose';
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  loadDataHistoric(): void {
    const nameTeam = localStorage.getItem('nameTeam');

    this.teamService.getHistoricPage(this.page, this.size).subscribe(data => {
      this.historico = data.content;  
      this.totalPages = data.totalPages;  
    });
  }

  changePage(page: number): void {
    this.page = page;
    this.loadDataHistoric();
  }
  
  private addPartidaNoInicio(partida: ResultMatch): void {
    this.historico.unshift(partida);

    const newTotalPages = Math.ceil(this.historico.length / this.size);
  
    if (newTotalPages > this.totalPages && this.page >= newTotalPages) {
      this.page = newTotalPages - 1;  
    }
    this.totalPages = newTotalPages;
  }
  
}
