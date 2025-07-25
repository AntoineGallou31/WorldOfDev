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
      sortDirection: 'asc' | 'desc' = 'desc';

      constructor(private postService: PostService) { }

      ngOnInit(): void {
        this.fetchPosts();
      }

      fetchPosts(): void {
        this.postService.getPosts().subscribe({
          next: (data) => {
            this.posts = data;
            this.sortPosts();
          },
          error: (err) => {
            console.error('Erreur lors de la récupération des posts', err);
          }
        });
      }

      // Sort posts by creation date in the specified direction
      sortPosts(): void {
        this.posts.sort((a, b) => {
          const dateA = new Date(a.createdAt).getTime();
          const dateB = new Date(b.createdAt).getTime();
          return this.sortDirection === 'desc' ? dateB - dateA : dateA - dateB;
        });
      }

      // Toggle the sort direction between ascending and descending
      toggleSortDirection(): void {
        this.sortDirection = this.sortDirection === 'desc' ? 'asc' : 'desc';
        this.sortPosts();
      }

      // Get the CSS class for the sort icon based on the current sort direction
      getSortIconClass(): string {
        return this.sortDirection === 'desc' ? 'sort-icon-desc' : 'sort-icon-asc';
      }
    }
