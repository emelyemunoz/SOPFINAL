import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClienteService } from '../cliente.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  login = {
    correo: '',
    contrasena: ''
  };

  constructor(
    private clienteService: ClienteService,
    private router: Router
  ) {}

 iniciarSesion() {

  const datosLogin = {
    correoElectronico: this.login.correo,
    password: this.login.contrasena
  };

  this.clienteService.login(datosLogin).subscribe({

  next: (respuesta: any) => {

  console.log('Inicio de sesión correcto:', respuesta);

  localStorage.setItem('usuario', JSON.stringify(respuesta));

  if (respuesta.rol === 'ADMIN') {
    this.router.navigate(['/admin/producto']);
  } else {
    this.router.navigate(['/catalogo']);
  }
},

    error: (error: any) => {

      console.error('Error al iniciar sesión:', error);

      alert('Correo o contraseña incorrectos');

    }

  });

}

  }
