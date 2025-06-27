import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-subject-list',
  templateUrl: './subject-list.component.html',
  styleUrls: ['./subject-list.component.scss']
})
export class SubjectListComponent implements OnInit {

  subjects = [
    {
      id : 1,
      name: "Titre du thème",
      description: "Description: lorem ipsum is simply dummy text of the printing and typesetting industry...",
      subscribed: false
    },
    {
      id : 2,
      name: "Titre du thème",
      description: "Description: lorem ipsum is simply dummy text of the printing and typesetting industry...",
      subscribed: true
    }
  ];

  constructor() { }

  ngOnInit(): void {
  }

  onSubscribe(subject: any) {
    if (!subject.subscribed) {
      // Appel à ton service ici
      subject.subscribed = true;
    }
  }

}
