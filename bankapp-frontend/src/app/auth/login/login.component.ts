import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';  
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService,
              private router: Router) {}

  login() {
    this.authService.login(this.username, this.password)
      .subscribe({
        next: () => {
          const role = this.authService.getRole();

          if (role === 'MANAGER') {
            this.router.navigate(['/manager']);
          } else if (role === 'CLERK') {
            this.router.navigate(['/clerk']);
          }
        },
        error: err => {
          this.errorMessage = 'Invalid username or password';
        }
      });
  }
}
