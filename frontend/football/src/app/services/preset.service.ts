import { Injectable } from '@angular/core';
import { Preset } from '../models/Preset';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, firstValueFrom, map, Observable, of, tap, throwError } from 'rxjs';
import { Player } from '../models/Player';
import { PresetUpdate } from '../models/PresetUpdate';
import { FormationService } from './formation.service';
import { environment } from '../../environments/environment';



@Injectable({
  providedIn: 'root'
})
export class PresetService {
  private api = environment.apiUrl;
  private presets: Preset[] = [];
  private currentPresetid: number = 1; 
  private apiPreset = `${this.api}/teams/find-preset?id=1`;
  private apiPresetUpdate = `${this.api}/api/teams/preset`;
  

  constructor(private http: HttpClient,private formationService: FormationService) {}


  updatePreset(players: Player[], teamId: number, presetId: number, strategy: string, formation: string): Observable<void>{
    let upPreset: PresetUpdate = {
      players: players,      
      presetId: presetId,   
      teamId: teamId,
      formation: formation,       
      strategy: strategy
    };
    return this.http.put<void>(this.apiPresetUpdate, upPreset);
  }

  loadTeamPresets(): Observable<Preset[]> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<Preset[]>(this.apiPreset, {headers}).pipe(
      tap((data) => {
        this.presets = data; 
      }),catchError((err) => {
        console.error('Error HTTP |', err);
        return of([]); 
      })
    );
  }

  
  getAllPresets(): Preset[] {
    return this.presets;
  }

  getCurrentPresetid(): number {
    return this.currentPresetid;
  }

  setCurrentPresetId(presetId: number) {
    if (presetId >= 1 && presetId <= 3) {
      this.currentPresetid = presetId;
    }
  }

  async getPreset(presetId: number): Promise<Preset>  {
    const presets = await firstValueFrom(this.loadTeamPresets());
    let preset = this.presets.find(p => p.presetId === presetId);
    if(preset){
      this.formationService.setPlayers(preset.presetPlayers);
    }
  
    if (!preset) {
      throw new Error(`Preset ID ${presetId} not found`);
    }
    return preset;
  }

  async getCurrentPreset(): Promise<Preset> {
    return await this.getPreset(this.currentPresetid);
  }

  async savePreset(presetId: number, formation: string, strategy: string): Promise<void> {
    const preset = await this.getPreset(presetId); 
    if (preset) {
      preset.presetFormation = formation;
      preset.presetStrategy = strategy;
    }
  }
  
  saveCurrentPreset(formation: string, strategy: string) {
    this.savePreset(this.currentPresetid, formation, strategy);
  }

}
