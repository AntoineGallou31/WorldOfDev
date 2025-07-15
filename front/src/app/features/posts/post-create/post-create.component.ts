import { Component, OnInit } from '@angular/core';
  import { FormBuilder, FormGroup, Validators } from '@angular/forms';
  import { PostService } from "../services/post.service";
  import { Subject } from "../../../core/interfaces/subject";
  import { Router } from '@angular/router';

  @Component({
    selector: 'app-post-create',
    templateUrl: './post-create.component.html',
    styleUrls: ['./post-create.component.scss']
  })
  export class PostCreateComponent implements OnInit {
    subjects: Subject[] = [];
    postForm!: FormGroup;
    submitting = false;
    errorMessage = '';

    constructor(
      private postService: PostService,
      private fb: FormBuilder,
      private router: Router
    ) { }

    ngOnInit(): void {
      this.initForm();
      this.loadSubjects();
    }

    initForm(): void {
      this.postForm = this.fb.group({
        subjectId: ['', Validators.required],
        title: ['', [Validators.required, Validators.minLength(3)]],
        content: ['', [Validators.required, Validators.minLength(10)]]
      });
    }

    loadSubjects(): void {
      this.postService.getAllSubjectsList().subscribe({
        next: (response: any) => {
          if (response && response.subjects) {
            this.subjects = response.subjects;
          }
        },
        error: (err) => {
          console.error('Erreur lors du chargement des sujets', err);
        }
      });
    }

    onSubmit(): void {
      if (this.postForm.invalid) {
          this.markFormGroupTouched(this.postForm);
        return;
      }

      this.submitting = true;

      this.postService.createPost(this.postForm.value).subscribe({
        next: (response) => {
          console.log('Article créé avec succès', response);
          this.router.navigate(['/feed']);
        },
        error: (err) => {
          console.error('Erreur lors de la création de l\'article', err);
          this.errorMessage = 'Une erreur est survenue lors de la création de l\'article.';
          this.submitting = false;
        }
      });
    }

    // Utilitaire pour marquer tous les champs comme touchés (pour afficher les erreurs)
    private markFormGroupTouched(formGroup: FormGroup) {
      Object.keys(formGroup.controls).forEach(field => {
        const control = formGroup.get(field);
        control?.markAsTouched();
      });
    }
  }
