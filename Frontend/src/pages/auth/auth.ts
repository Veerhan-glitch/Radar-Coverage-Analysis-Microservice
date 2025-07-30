import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router'; // Import for routing support

@Component({
  selector: 'app-auth',
  standalone: true, // Allows the component to be used independently
  imports: [FormsModule, RouterModule, CommonModule], // Import necessary modules
  templateUrl: './auth.html',
  styleUrls: ['./auth.css']
})
export class Auth implements OnInit {
  // Form fields
  users: string[] = [];       // (Unused if user list isn't fetched)
  name = '';                  // Bound to username input
  password = '';              // Bound to password input
  errorMsg = '';              // Error message shown on form

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Initialization logic here if needed
    // this.users fetch is now redundant if you're using free-text input
  }

  // Called on login form submission
  login(): void {
    this.errorMsg = ''; // Clear previous error

    // Validate input fields
    if (!this.name || !this.password) {
      this.errorMsg = 'Please select a username and enter password.';
      return;
    }

    // Send POST request to backend login endpoint
    fetch('http://127.0.0.1:8085/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: this.name, password: this.password })
    })
    .then(res => res.json().then(body => ({ status: res.status, body }))) // Combine status + body
    .then(({ status, body }) => {
      if (status === 200) {
        const user = body.user;
        localStorage.setItem('currentUser', JSON.stringify(user)); // Save user in localStorage

        // Navigate to dashboard after successful login
        this.router.navigate(['/dashboard']);
      } else if (status === 401) {
        this.errorMsg = 'Invalid credentials'; // Wrong username/password
      } else {
        this.errorMsg = body.error || 'Server error. Try again.';
      }
    })
    .catch(() => {
      this.errorMsg = 'Network error. Try again later.'; // Server unreachable or failed fetch
    });
  }
}
