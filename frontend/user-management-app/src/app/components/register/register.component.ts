// src/app/components/register/register.component.ts
import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  user: User = { username: '', email: '', password: '' };

  constructor(private userService: UserService) {}

  register() {
    this.userService.registerUser(this.user).subscribe(
      response => console.log('Usuario registrado:', response),
      error => console.error('Error al registrar usuario:', error)
    );
  }
}