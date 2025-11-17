import { Injectable } from '@angular/core';
import { Team } from '../models/Team';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, of, Subject, tap } from 'rxjs';
import { ResultMatch } from '../models/ResultMach';
import { PageResultMatch } from '../models/PageResulMatch';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class TeamService {

  private api = environment.apiUrl;
  private apiTeam = `${this.api}/teams/find?name=`;
  private apiHistoric = `${this.api}/results/page`;

  private teamSubject: BehaviorSubject<Team | null> = new BehaviorSubject<Team | null>(null);  
  private teamLoaded: boolean = false;  

  constructor(private http: HttpClient) {
    this.iniciarConexaoSSE();
  }



  getTeam():  Observable<Team | null> {
    if (this.teamLoaded) {
      return this.teamSubject.asObservable();
    } else {
        const nameTeam = localStorage.getItem('nameTeam');
        const token = localStorage.getItem('authToken');
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      return this.http.get<Team>(this.apiTeam+nameTeam,{headers}).pipe(
        tap(team => {
          this.teamSubject.next(team);
          this.teamLoaded = true;
        }),
        catchError(error => {
          console.error('Erro na requisição do time:', error);
          return of(null);  
        })
      );
    }
  }


  private matchSubject = new Subject<ResultMatch>();
  private historicoPartidas: ResultMatch[] = []; 

  private iniciarConexaoSSE() {
    const token = localStorage.getItem('authToken');
    const url = `${this.api}/results/conectar?token=${token}`;
    
    const eventSource = new EventSource(url);
    
    
    eventSource.addEventListener('partida-nova', (event: MessageEvent) => {
      try {
        const match: ResultMatch = JSON.parse(event.data);
        this.historicoPartidas.unshift(match);
        this.matchSubject.next(match);
      } catch (error) {
        console.error('Error message:', error);
      }
    });
  }

  receberPartidas(): Observable<ResultMatch> {
    return this.matchSubject.asObservable();
  }

  getHistoricPage(page: number, size: number): Observable<PageResultMatch>{
    const token = localStorage.getItem('authToken');
    const nameTeam = localStorage.getItem("nameTeam");
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams().set('page', `${page}`).set('size', `${size}`).set('nameTeam',`${nameTeam}`);
    return this.http.get<PageResultMatch>(this.apiHistoric, {headers,params});
  }
}