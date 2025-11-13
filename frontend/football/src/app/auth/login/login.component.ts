import { Component } from '@angular/core';
import { User } from '../models/User';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  user: User = { email: '', password: '' };
  errorMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.errorMessage = '';
    this.loading = true; 

    this.authService.login(this.user).subscribe((success: boolean) => {
      this.loading = false;

      if (success) {
        this.router.navigate(['/team']);
      } else {
        this.errorMessage = 'Email or password incorrect';
      }
    });
  }

}
