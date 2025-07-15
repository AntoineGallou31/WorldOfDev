import { Component, OnInit } from '@angular/core';
import {User} from "../../core/interfaces/user";
import {Subject} from "../../core/interfaces/subject";
import {ProfileService} from "./services/profile.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SubjectService} from "../subjects/services/subject.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  subjects: Subject[] = [];
  user: User | null = null;
  submitting = false;
  errorMessage = '';
  profileForm!: FormGroup;

  constructor(
    private profileService : ProfileService,
    private subjectService: SubjectService,
    private fb: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.getUserProfile();
    this.getSubjectSubscribed();
  }

  initProfileForm(): void {
    this.profileForm = this.fb.group({
      username: [this.user?.username || '', [Validators.required, Validators.minLength(3)]],
      email: [this.user?.email || '', [Validators.required, Validators.email]],
      password: ['', [Validators.minLength(6)]]
    });
  }

  getUserProfile(): void {
    this.profileService.getUserProfile().subscribe({
      next: (user) => {
        this.user = user;
        this.initProfileForm();
        console.log('User profile loaded:', this.user);
      },
      error: (err) => {
        console.error('Error loading user profile:', err);
      }
    })
  }

  getSubjectSubscribed(): void {
    this.subjectService.getAllSubjects().subscribe({
      next: (subjects: Subject[]) => {
        this.subjects = subjects.filter(subject => subject.subscribed === true);
      },
      error: (err: any) => {
        console.error('Error loading subjects:', err);
      }
    })
  }

  onSubmit(): void {
    if (this.profileForm.invalid) {
      this.markFormGroupTouched(this.profileForm);
      return;
    }

    this.submitting = true;

    if (this.user) {
      this.profileService.updateUserProfile(this.user.id, this.profileForm.value).subscribe({
        next: (response: any) => {
          console.log('Profil mise à jour avec succès', response);
          this.submitting = false;
        },
        error: (err: any) => {
          console.error('Erreur lors de la mise à jour', err);
          this.errorMessage = 'Une erreur est survenue lors de la mise à jour du profil.';
          this.submitting = false;
        }
      });
    }
  }

  // Utilitaire pour marquer tous les champs comme touchés (pour afficher les erreurs)
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(field => {
      const control = formGroup.get(field);
      control?.markAsTouched();
    });
  }

  unsubscribe(subject: Subject) {
    this.subjectService.unsubscribeFromSubject(subject.id).subscribe({
      next: () => {
        this.subjects = this.subjects.filter(s => s.id !== subject.id);
      },
      error: (err) => {
        console.error('Erreur lors de l\'abonnement', err);
      }
    })
  }
}
