import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:3001/api/auth';

  constructor(private http: HttpClient) {}

  login(credentials: { identifier: string; password: string }) {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, credentials)
      .pipe(tap(response => {
        localStorage.setItem('token', response.token);
      }));
  }

  logout() {
    localStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  register(registerRequest: { email: string; username: string; password: string }) {
    return this.http.post<void>(`${this.apiUrl}/register`, registerRequest);
  }
}
