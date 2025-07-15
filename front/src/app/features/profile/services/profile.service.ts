import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from "../../../core/interfaces/user";
import {environment} from "../../../../environments/environment";
import {Subject} from "../../../core/interfaces/subject";

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private apiUrl = `${environment.apiUrl}`;

  constructor(
    private http: HttpClient,
  ) { }

  getUserProfile(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/user/me`);
  }

  updateUserProfile(id : number, user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/user/${id}`, user);
  }
}
