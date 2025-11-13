import { Component, OnInit } from '@angular/core';
import { Player } from '../../models/Player';
import { TeamService } from '../../services/team.service';
import { HeaderComponent } from "../header/header.component";
import { FieldComponent } from "../field/field.component";
import { CardFormationComponent } from "../card-formation/card-formation.component";
import { MatchListComponent } from "../match-list/match-list.component";
import { CommonModule } from '@angular/common';
import { PresetService } from '../../services/preset.service';
import { PossibleMatchesComponent } from "../possible-matches/possible-matches.component";
import { ProfileComponent } from "../profile/profile.component";
import { firstValueFrom } from 'rxjs';
import { Team } from '../../models/Team';
import { FormationService } from '../../services/formation.service';

@Component({
  selector: 'app-team-manager',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FieldComponent, CardFormationComponent, MatchListComponent, PossibleMatchesComponent, ProfileComponent],
  templateUrl: './team-manager.component.html',
  styleUrl: './team-manager.component.css'
})
export class TeamManagerComponent implements OnInit{
    mostrarHistorico: boolean = false;
    mostraMatches: boolean = false;
    mostrarFormation: boolean = true;
    mostraProfile: boolean = false;

    currentDynamicFormation: string = '';

    team: Team | null = null;
    players: Player[] = [];
    currentFormation: string = '';
    currentStrategy: string = '';

    constructor(private teamService: TeamService, private presetService: PresetService, private formationService: FormationService){}

    async loadTeam(formation: string): Promise<void> {
      if (!this.team || !this.team.presets) {
        return console.error("Team null");
      }
    
      let formationPreset = this.team.presets.find(p => p.presetFormation === formation);
    
      if (formationPreset) {
        this.currentFormation = formationPreset.presetFormation;
        this.currentStrategy = formationPreset.presetStrategy;
        this.players = formationPreset.presetPlayers;
      } 
    }

  
    async ngOnInit(): Promise<void> {
      try {
        this.team = await firstValueFrom(this.teamService.getTeam());

        if (!this.team || !this.team.presets || this.team.presets.length === 0) {
          return;
        }
  
        const firstPreset = this.team.presets[0]; 
      
        if (firstPreset && firstPreset.presetPlayers) {
          this.players = firstPreset.presetPlayers; 
        }
      } catch (error) {
        console.error('Error load team:', error);
      }
    }
  
  
    onFormationChangedFromField(formation: string) {
      this.currentDynamicFormation = formation;
    }

    onFormationChange(formation: string){
      this.loadTeam(formation);
    }



    alternarView(mostrarHist: boolean) {
      this.mostrarHistorico = mostrarHist;
    }
  
    onHeaderNavigation(tipo: string) {
      if (tipo === 'historic') {
        this.mostrarHistorico = true;
        this.mostraMatches = false;
        this.mostrarFormation = false;
        this.mostraProfile = false;
        
      } else if (tipo === 'formation') {
        this.mostrarFormation = true;
        this.mostrarHistorico = false;
        this.mostraMatches = false;
        this.mostraProfile = false;
        
      } else if (tipo === 'matches'){
        this.mostraMatches = true;
        this.mostrarHistorico = false;
        this.mostrarFormation = false;
        this.mostraProfile = false;


      } else if (tipo === 'profile'){
        this.mostraProfile = true;
        this.mostraMatches = false;
        this.mostrarHistorico = false;
        this.mostrarFormation = false;
       
      }
  
      
    }

}
