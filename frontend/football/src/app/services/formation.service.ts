import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Player } from '../models/Player';
import { TeamService } from './team.service';
import { MatchDTO } from '../models/MatchDTO';

@Injectable({
  providedIn: 'root'
})
export class FormationService {

  formation: string | undefined;
  private formationSource = new BehaviorSubject<string>("");
  private playerSource = new BehaviorSubject<Player[]>([]);
  players$ = this.playerSource.asObservable();

  private dataSubject = new BehaviorSubject<MatchDTO>({
    teamId: 0,
    name: '',
    presetStrategy: '',
    presetFormation: ''
  });

  constructor(private teamService: TeamService) {}


  ngOnit(){
    this.teamService.getTeam().subscribe(
      {next:(teamResponse => 
        {this.formation = teamResponse?.presets[0].presetFormation;
          let playersPresets = teamResponse?.presets[0].presetPlayers;
              if(playersPresets){
                this.setPlayers(playersPresets)
              }
          })});
  }
  

  setDataTeam(newData: MatchDTO) {
    this.dataSubject.next(newData);  
  }

  getDataTeam() {
    return this.dataSubject.asObservable(); 
  }
 
  setPlayers(players: Player[]){
    this.playerSource.next(players);
  }
  

  public currentFormation = this.formationSource.asObservable();

  updateFormation(formation: string) {
    this.formationSource.next(formation);
  }

  getCurrentFormation(): string {
    return this.formationSource.value;
  }
}
