import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {RegisterRequest} from "../../interfaces/register-request";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {markFormGroupTouched, passwordComplexityValidator} from "../../../../shared/utils/my-utils";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit  {

  public errorMessage = '';
  postForm!: FormGroup;

  constructor( private authService: AuthService,
               private fb: FormBuilder,
               private router: Router
  ) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.postForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.min(3), Validators.max(20)]],
      password: ['', [Validators.required, Validators.min(8), Validators.max(40), passwordComplexityValidator.bind(this)]]
    });
  }

  onSubmit(): void {
    if (this.postForm.invalid) {
      markFormGroupTouched(this.postForm);
      return;
    }

    const registerRequest = this.postForm.value as RegisterRequest;
    this.authService.register(registerRequest).subscribe({
        next: () => this.router.navigate(['/feed']),
        error: (error) => {
          if (error.error && error.error.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = "Une erreur est survenue. Veuillez réessayer.";
          }
        }
      });
  }
}
