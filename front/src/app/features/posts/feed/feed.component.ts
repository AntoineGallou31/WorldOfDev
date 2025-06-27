import { Component, OnInit } from '@angular/core';
    import { Post } from "../../../core/interfaces/post";
    import { PostService } from "../services/post.service";

    @Component({
      selector: 'app-feed',
      templateUrl: './feed.component.html',
      styleUrls: ['./feed.component.scss']
    })
    export class FeedComponent implements OnInit {
      posts: Post[] = [];
      loading = false;
      error = false;
      sortDirection: 'asc' | 'desc' = 'desc'; // Par défaut tri décroissant (plus récent en premier)

      constructor(private postService: PostService) { }

      ngOnInit(): void {
        this.fetchPosts();
      }

      fetchPosts(): void {
        this.loading = true;
        this.error = false;

        this.postService.getPosts().subscribe({
          next: (data) => {
            this.posts = data;
            this.sortPosts();
            this.loading = false;
          },
          error: (err) => {
            console.error('Erreur lors de la récupération des posts', err);
            this.error = true;
            this.loading = false;
          }
        });
      }

      toggleSortDirection(): void {
        this.sortDirection = this.sortDirection === 'desc' ? 'asc' : 'desc';
        this.sortPosts();
      }

      sortPosts(): void {
        this.posts.sort((a, b) => {
          const dateA = new Date(a.createdAt).getTime();
          const dateB = new Date(b.createdAt).getTime();
          return this.sortDirection === 'desc' ? dateB - dateA : dateA - dateB;
        });
      }

      getSortIconClass(): string {
        return this.sortDirection === 'desc' ? 'sort-icon-desc' : 'sort-icon-asc';
      }
    }
