// src/app/components/user-list/user-list.component.ts
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  roles: string[] = [];

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.getAllUsers().subscribe(
      data => this.users = data,
      error => console.error('Error al obtener usuarios:', error)
    );

    this.userService.getRoles().subscribe(
      data => this.roles = data,
      error => console.error('Error al obtener roles:', error)
    );
  }

  assignRole(userId: number, event: Event) {
    const selectElement = event.target as HTMLSelectElement;
  const role = selectElement.value;
    this.userService.assignRoleToUser(userId, role).subscribe(
      response => console.log('Rol asignado:', response),
      error => console.error('Error al asignar rol:', error)
    );
  }
}