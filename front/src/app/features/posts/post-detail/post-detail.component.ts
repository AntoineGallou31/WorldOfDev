import { Component, OnInit } from '@angular/core';
        import { ActivatedRoute } from "@angular/router";
        import { Post } from "../../../core/interfaces/post";
        import { PostService } from "../services/post.service";
        import { finalize } from "rxjs";
        import { FormBuilder, FormGroup, Validators } from '@angular/forms';

        @Component({
          selector: 'app-subject-detail',
          templateUrl: './post-detail.component.html',
          styleUrls: ['./post-detail.component.scss']
        })
        export class PostDetailComponent implements OnInit {

          postId!: number;
          post?: Post;
          loading = false;
          error: string | null = null;
          commentForm!: FormGroup;
          submittingComment = false;

          constructor(
            private route: ActivatedRoute,
            private postService: PostService,
            private fb: FormBuilder
          ) { }

          ngOnInit(): void {
            this.postId = Number(this.route.snapshot.params['id']);
            this.initCommentForm();
            this.loadPost();
          }

          initCommentForm(): void {
            this.commentForm = this.fb.group({
              content: ['', [Validators.required, Validators.minLength(2)]]
            });
          }

          loadPost(): void {
            this.loading = true;
            this.error = null;

            this.postService.getPostById(this.postId)
              .pipe(
                finalize(() => this.loading = false)
              )
              .subscribe({
                next: (data) => {
                  this.post = data;
                },
                error: (err) => {
                  console.error('Erreur lors du chargement du post', err);
                  this.error = 'Impossible de charger le post.';
                }
              });
          }

          submitComment(): void {
            if (this.commentForm.invalid || !this.post) {
              return;
            }

            const commentContent = this.commentForm.get('content')?.value;
            this.submittingComment = true;

            this.postService.addComment(this.postId, { content: commentContent })
              .pipe(
                finalize(() => this.submittingComment = false)
              )
              .subscribe({
                next: (response) => {
                  this.loadPost();
                  this.commentForm.reset();
                },
                error: (err) => {
                  console.error('Erreur lors de l\'ajout du commentaire', err);
                }
              });
          }
        }
