import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Observable, map } from "rxjs";
import {Post} from "../../../core/interfaces/post";
import {environment} from "../../../../environments/environment";
import {PostsResponse} from "../../../core/interfaces/posts-response";
import {Subject} from "../../../core/interfaces/subject";

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  getPosts(): Observable<Post[]> {
    return this.http.get<PostsResponse>(`${this.apiUrl}/posts/list`)
      .pipe(map(response => response.posts));
  }

  getPostById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/posts/${id}`);
  }

  getAllSubjectsList(): Observable<Subject[]> {
    return this.http.get<Subject[]>(`${this.apiUrl}/subjects/list`);
  }

  createPost(postData: any) {
    return this.http.post<any>(`${this.apiUrl}/posts/create`, postData);
  }

  addComment(postId: number, commentData: { content: string }) {
    return this.http.post<any>(`${this.apiUrl}/posts/${postId}/comment`, commentData);
  }
}
