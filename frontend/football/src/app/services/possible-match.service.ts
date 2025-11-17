import { Injectable } from '@angular/core';
import { Match } from '../models/Match';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayMatch } from '../models/PlayMatch';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class PossibleMatchService {
  private api = environment.apiUrl;

  private urlMatch = `${this.api}/matchs`;
  private urlMatchPlay = `${this.api}/matchs/play`;

  constructor(private http: HttpClient) { }


  createMatch(match: Match): Observable<void> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    console.log(match);
    return this.http.post<void>(this.urlMatch,match,{headers});
  }

  listPaginated(page: number, size: number): Observable<any> {
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `${this.urlMatch}/page?page=${page}&size=${size}`;
    return this.http.get<any>(url,{headers});
  }

  deleteMatch(id: string){
    return this.http.delete<void>(`${this.urlMatch}/${id}`);
  }
  
  playMatch(matchPlay: PlayMatch){
    const token = localStorage.getItem('authToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    console.log(matchPlay);
    return this.http.post<void>(this.urlMatchPlay, matchPlay,{headers});
  }
}
