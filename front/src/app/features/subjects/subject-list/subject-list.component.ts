import { Component, OnInit } from '@angular/core';
  import { SubjectService } from '../services/subject.service';
import {Subject} from "../../../core/interfaces/subject";

  @Component({
    selector: 'app-subject-list',
    templateUrl: './subject-list.component.html',
    styleUrls: ['./subject-list.component.scss']
  })
  export class SubjectListComponent implements OnInit {

    subjects: Subject[] = [];
    loading = false;
    error: string | null = null;

    constructor(private subjectService: SubjectService) { }

    ngOnInit(): void {
      this.loadSubjects();
    }

    loadSubjects(): void {
      this.loading = true;
      this.error = null;

      this.subjectService.getAllSubjects().subscribe({
        next: (data) => {
          this.subjects = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Erreur lors du chargement des sujets', err);
          this.error = 'Impossible de charger les sujets.';
          this.loading = false;
        }
      });
    }

    onSubscribe(subject: Subject): void {
      if (!subject.subscribed) {
        this.subjectService.subscribeToSubject(subject.id).subscribe({
          next: () => {
            subject.subscribed = true;
          },
          error: (err) => {
            console.error('Erreur lors de l\'abonnement', err);
          }
        });
      }
    }
  }
