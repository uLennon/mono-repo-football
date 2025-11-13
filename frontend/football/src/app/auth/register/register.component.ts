import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule,RouterModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  newUser = {
    name: '',
    username: '',
    email: '',
    nameTeam: '',
    password: ''
  };

  confirmPassword = '';

  errorMessage = '';
  successMessage = '';
  loading = false;

  constructor(private auth: AuthService, private router: Router) {}

  get passwordMismatch(): boolean {
    return this.confirmPassword.length > 0 &&
           this.newUser.password !== this.confirmPassword;
  }

  onRegisterSubmit() {

    this.errorMessage = '';
    this.successMessage = '';

    if (this.passwordMismatch) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    if (!this.areAllFieldsFilled()) {
      this.errorMessage = 'Please fill in all required fields.';
      return;
    }

    this.loading = true;

    this.auth.register(this.newUser).subscribe({
      next: (response) => {
        this.loading = false;

        this.successMessage = 'Account created successfully!';
        
        // Navegar após sucesso
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 800);
      },

      error: (err) => {
        this.loading = false;

        this.errorMessage = err?.error?.message 
          ? err.error.message 
          : 'Failed to create account. Please try again.';
      }
    });
  }

  
    private areAllFieldsFilled(): boolean {
      return !!(
        this.newUser.name &&
        this.newUser.username &&
        this.newUser.email &&
        this.newUser.nameTeam &&
        this.newUser.password
      );
  }
}
