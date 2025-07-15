import { Injectable } from '@angular/core';
import {map, Observable} from "rxjs";
import {Subject} from "../../../core/interfaces/subject";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SubjectService {

  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  getAllSubjects(): Observable<Subject[]> {
    return this.http.get<{subjects: Subject[]}>(`${this.apiUrl}/subjects/list`).pipe(
      map(response => response.subjects || [])
    );
  }

  subscribeToSubject(subjectId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/subjects/${subjectId}/subscribe`, {});
  }

  unsubscribeFromSubject(subjectId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/subjects/${subjectId}/unsubscribe`, {});
  }
}
