import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  username = '';
  password = '';
  message = '';
  isSuccess = false;
  isError = false;

  constructor(private http: HttpClient) {}

  register(): void {
    this.message = '';
    this.isSuccess = this.isError = false;

    this.http.post<{ message?: string }>(
        'http://127.0.0.1:8085/api/auth/register',
        { name: this.username, password: this.password },
        { observe: 'response' }
      )
      .subscribe({
        next: res => {
          if (res.status === 200) {
            this.message = res.body?.message || 'User registered successfully';
            this.isSuccess = true;
            this.username = '';
            this.password = '';
          }
        },
        error: err => {
          if (err.status === 409) {
            this.message = err.error?.['error'] || 'Username already exists';
          } else {
            this.message = 'Registration failed. Try again.';
          }
          this.isError = true;
        }
      });
  }
}
