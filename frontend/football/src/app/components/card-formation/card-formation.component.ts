import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PresetPageComponent } from "../preset-page/preset-page.component";
import { PresetService } from '../../services/preset.service';
import { FormationService } from '../../services/formation.service';
import { Subscription } from 'rxjs';
import { Preset } from '../../models/Preset';
import { Player } from '../../models/Player';
import { PossibleMatchService } from '../../services/possible-match.service';
import { Match } from '../../models/Match';
import { MatchDTO } from '../../models/MatchDTO';



@Component({
  selector: 'app-card-formation',
  standalone: true,
  imports: [FormsModule, CommonModule, PresetPageComponent],
  templateUrl: './card-formation.component.html',
  styleUrl: './card-formation.component.css'
})

export class CardFormationComponent implements OnInit{
    @Output() formationSelected = new EventEmitter<string>();
    presets: Preset[] = [];
    players: Player[] = [];
    time: number = 1; 
   
    
    selectedFormation: string = ' ';
    selectedStrategy: string = ' ';
    currentPresetId: number = 1;
    availableFormations: string[] = [];
    private formationSubscription: Subscription = Subscription.EMPTY;

    constructor(private presetService: PresetService, private formationService :FormationService,private possibleMatchService : PossibleMatchService){}

    ngOnInit(): void {
      this.loadCurrentPreset();
      this.presetService.loadTeamPresets().subscribe({
        next: (presets) => {
          this.presets = presets;
          this.availableFormations = presets.map(p => p.presetFormation);
          this.selectedFormation = presets[0].presetFormation;
          this.selectedStrategy = presets[0].presetStrategy;
          this.updateDataTeam();
        },
        error: (err) => console.error('Error load presets: ', err)
      });
      
      this.formationSubscription = this.formationService.currentFormation.subscribe(
        (formation) => {
          this.updateFormationOptions(formation);
        }
      );
      this.formationService.players$.subscribe(players => {
        this.players = players;
      });
      
      this.updateDataTeam();
    }

  
    savePreset() {
      this.presetService.updatePreset(this.players,this.time, this.currentPresetId, this.selectedStrategy, this.selectedFormation).subscribe({
        next: () => console.log('Preset update ok'),
        error: (err) => console.error('Error preset update:', err)
      });
    }

    selectStrategy(strategy: string): void {
      this.selectedStrategy = strategy;
      this.updateDataTeam();
    }


    getPresetsArray(): Preset[] {
      return Array.from(this.presets.values());
    }

    ngOnDestroy(){
      if (this.formationSubscription) {
        this.formationSubscription.unsubscribe();
      }
    }

    private updateFormationOptions(dynamicFormation: string) {
      if (!dynamicFormation || dynamicFormation === '0-0-0') {
      
        return;
      }
      
    
      // 1. Filtrar formações antigas
      const formationsSemAtual = this.availableFormations.filter(f => 
        !f.includes('(Atual)')
      );
    
      // 2. Verificar se já existe (sem adicionar duplicata)
      const jaExiste = formationsSemAtual.some(f => f === dynamicFormation);
      
      if (!jaExiste) {
    
        this.availableFormations = [`${dynamicFormation} (Atual)`, ...formationsSemAtual];
        

        this.selectedFormation = `${dynamicFormation} (Atual)`;
        
        this.formationSelected.emit(dynamicFormation);
      } else {
        
        this.selectedFormation = dynamicFormation;
      }
    }
  
    async loadCurrentPreset() {
      const preset = this.presetService.getCurrentPreset();
      if (preset) {
        this.selectedFormation = (await preset).presetFormation;
        this.selectedStrategy = (await preset).presetStrategy;
      }
    }
  
    selectFormation(formation: string) {
      const cleanFormation = formation.replace(' (Atual)', '');
      this.selectedFormation = formation;
      this.formationSelected.emit(cleanFormation);
    }

    async onPresetChange() {
      let preset = (await this.presetService.getPreset(this.presetService.getCurrentPresetid()));
      this.currentPresetId = this.presetService.getCurrentPresetid();
      const presetFormation = preset.presetFormation;
      this.selectFormation(presetFormation);
      this.loadCurrentPreset();
    }


    confirmPlay() {
      let match: Match = this.buildMatchAletorio();
      console.log(match);
      this.possibleMatchService.createMatch(match).subscribe({
        next: (response) => {
          console.log('Match creat sucess:', response);
        },
        error: (error) => {
          console.error('Erro created match:', error);
        }
      });

    }

    buildMatch(): Match {
      
      const currentDate = new Date();
      const nameTeam = localStorage.getItem('nameTeam');
      if (nameTeam === null) {
        throw new Error('Erro "nameTeam" not found');
      }
      let match: Match = {
        matchDate: currentDate,
        teamId: this.time,
        name: nameTeam,
        presetFormation: this.selectedFormation,
        presetStrategy: this.selectedStrategy
      };
      return match;
    }

    buildMatchAletorio(): Match {
      const currentDate = new Date();
    
      const teams = [
        { id: 1, name: 'Flamengo' },
        { id: 9, name: 'Vasco' },
        { id: 3, name: 'Fluminense' },
        { id: 4, name: 'Cruzeiro' },
        { id: 5, name: 'São Paulo' },
        { id: 6, name: 'Palmeiras' },
        { id: 7, name: 'Corinthians' },
        { id: 8, name: 'Santos' }
      ];
    
    
      const formations = ['4-4-2', '3-5-2', '4-2-4', '3-4-3', '4-3-3', '5-3-2', '3-6-1', '3-4-4'];
      const strategies = ['Offensive', 'Balanced', 'Defensive'];
      const randomTeam = teams[Math.floor(Math.random() * teams.length)];
      const randomFormation = formations[Math.floor(Math.random() * formations.length)];
      const randomStrategy = strategies[Math.floor(Math.random() * strategies.length)];
    
  
      let match: Match = {
        matchDate: currentDate,
        teamId: randomTeam.id,
        name: randomTeam.name,
        presetFormation: randomFormation,
        presetStrategy: randomStrategy
      };
    
      return match;
    }

    updateDataTeam(){
      const nameTeam = localStorage.getItem('nameTeam');
      if (nameTeam === null) {
        throw new Error('Erro "nameTeam" not found');
      }
      const teamHome: MatchDTO = {
        teamId: this.time,
        name: nameTeam,
        presetStrategy: this.selectedStrategy,
        presetFormation: this.selectedFormation
      };

      this.formationService.setDataTeam(teamHome);
    }
}
