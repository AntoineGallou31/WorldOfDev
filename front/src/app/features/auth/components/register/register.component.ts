import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {RegisterRequest} from "../../interfaces/register-request";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

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
      password: ['', [Validators.required, Validators.min(8), Validators.max(40), this.passwordComplexityValidator.bind(this)]]
    });
  }

  onSubmit(): void {
    if (this.postForm.invalid) {
      this.markFormGroupTouched(this.postForm);
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

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      control?.markAsTouched();
    });
  }

  private passwordComplexityValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value || '';
    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumber = /\d/.test(value);
    const hasSpecialChar = /[^A-Za-z0-9]/.test(value);
    const isValidLength = value.length >= 8;

    const valid = hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && isValidLength;
    return valid ? null : { passwordComplexity: true };
  }
}
