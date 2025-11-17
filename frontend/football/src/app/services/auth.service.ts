import { Injectable } from '@angular/core';
import { Route, Router } from '@angular/router';
import { User } from '../auth/models/User';
import { flush } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';

export interface NewUser{
    name: string;
    username: string;
    email: string;
    nameTeam: string;
    password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private api = environment.apiUrl;
  private urlLogin = `${this.api}/auths/login`;
  private urlRegister = `${this.api}/auths/register`;

  constructor(private router: Router, private http: HttpClient) { }

  login(user: User): Observable<boolean> {
    return this.http.post<string>(this.urlLogin, user, { responseType: 'text' as 'json' }).pipe(
      map((token: string) => {

        localStorage.setItem('authToken', token);

      
        const claims = this.extractClaimsFromToken(token);
        localStorage.setItem('userEmail', claims.email);
        localStorage.setItem('username', claims.username);
        localStorage.setItem('nameTeam', claims.nameTeam);

        localStorage.setItem('isLoggedIn', 'true');

        return true;
      }),
      catchError(err => {
        console.error("Erro de login:", err);
        return of(false);
      })
    );
  }

  
  private extractClaimsFromToken(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      const parsedPayload = JSON.parse(decoded);

      return {
        username: parsedPayload.username,
        email: parsedPayload.email,
        nameTeam: parsedPayload.nameTeam
      };

    } catch (err) {
      console.error("Error decoder JWT:", err);
      return {};
    }
  }


  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }


  public isLoggedIn(): boolean {
    const token = localStorage.getItem('authToken');

    if (!token) return false;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));

      return payload.exp * 1000 > Date.now();
    } catch (err) {
      return false;
    }
  }


  getCurrentUser(): string | null {
    return localStorage.getItem('userEmail');
  }


  register(newUser: NewUser) {
    return this.http.post<any>(this.urlRegister, newUser);
  }
}
