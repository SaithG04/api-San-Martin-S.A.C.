// src/app/components/profile/profile.component.ts
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  user: User = { username: '', email: '', password: '' };

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.getUserProfile().subscribe(
      data => this.user = data,
      error => console.error('Error al obtener perfil:', error)
    );
  }

  updateProfile() {
    this.userService.updateUserProfile(this.user).subscribe(
      response => console.log('Perfil actualizado:', response),
      error => console.error('Error al actualizar perfil:', error)
    );
  }
}