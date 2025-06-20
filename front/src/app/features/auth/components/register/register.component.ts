import { Component } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {RegisterRequest} from "../../interfaces/register-request";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  public onError = false;

  public form = this.fb.group({
    email: [
      '',
      [
        Validators.required,
        Validators.email
      ]
    ],
    username: [
      '',
      [
        Validators.required,
        Validators.min(3),
        Validators.max(20)
      ]
    ],
    password: [
      '',
      [
        Validators.required,
        Validators.min(3),
        Validators.max(40)
      ]
    ]
  });

  constructor( private authService: AuthService,
               private fb: FormBuilder,
               private router: Router
  ) { }

  public onSubmit(): void {
    const registerRequest = this.form.value as RegisterRequest;
    this.authService.register(registerRequest).subscribe({
        next: (_: void) => this.router.navigate(['auth/login']),
        error: _ => this.onError = true,
      }
    );
  }
}
