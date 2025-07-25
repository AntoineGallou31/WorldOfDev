import { Component, OnInit } from '@angular/core';
import {User} from "../../core/interfaces/user";
import {Subject} from "../../core/interfaces/subject";
import {ProfileService} from "./services/profile.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {markFormGroupTouched, passwordComplexityValidator} from "../../shared/utils/my-utils";
import {SubjectService} from "../subjects/services/subject.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  subjects: Subject[] = [];
  user: User | null = null;
  errorMessage = '';
  postForm!: FormGroup;

  constructor(
    private profileService : ProfileService,
    private subjectService: SubjectService,
    private fb: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.getUserProfile();
    this.getSubjectSubscribed();
  }

  initForm(): void {
    this.postForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.min(3), Validators.max(20)]],
      password: ['', [Validators.required, Validators.min(8), Validators.max(40), passwordComplexityValidator.bind(this)]]
    });
  }

  getUserProfile(): void {
    this.profileService.getUserProfile().subscribe({
      next: (user) => {
        this.user = user;
        this.postForm.patchValue({
          email: user.email,
          username: user.username,
          password: ''
        });
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
    if (this.postForm.invalid) {
        markFormGroupTouched(this.postForm);
      return;
    }

    if (this.user) {
      this.profileService.updateUserProfile(this.user.id, this.postForm.value).subscribe({
        next: (response: any) => {
          console.log('Profil mise à jour avec succès', response);
        },
        error: (err: any) => {
          console.error('Erreur lors de la mise à jour', err);
          this.errorMessage = 'Une erreur est survenue lors de la mise à jour du profil.';
        }
      });
    }
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
