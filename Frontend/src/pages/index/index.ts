import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-index',           // Component selector used in templates
  standalone: true,                // Indicates this component doesn't belong to an NgModule
  template: '',                    // Empty template (no UI content here)
  imports: [],                     // No module imports required here
  styleUrls: ['./index.css']       // CSS file for styling (if used)
})
export class Index {
  constructor(private router: Router) {
    // Redirect to /auth immediately upon component initialization
    this.router.navigate(['/auth']);
  }
}
