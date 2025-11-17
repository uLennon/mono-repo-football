import { Component, EventEmitter, Output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(private authService: AuthService, private router: Router){}
  logout(): void {
    this.authService.logout();
  }

  @Output() navigation = new EventEmitter<string>();

  navegarPara(tipo: string) {
    this.navigation.emit(tipo);
  }

}
