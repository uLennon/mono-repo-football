import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { TeamManagerComponent } from './components/team-manager/team-manager.component';
import { AuthGuardService } from './services/auth-guard.service';
import { AppComponent } from './app.component';
import { MatchListComponent } from './components/match-list/match-list.component';
import { RegisterComponent } from './auth/register/register.component';

export const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'team', component: TeamManagerComponent, canActivate:[AuthGuardService]},
    {path: 'historic', component: MatchListComponent, canActivate: [AuthGuardService]},
    { path: 'register', component: RegisterComponent },
    {path: '**', redirectTo: '/login'}
];

@NgModule({
    imports:[RouterModule.forRoot(routes)],
    exports: [RouterModule]

})
export class appRoutingModule{}