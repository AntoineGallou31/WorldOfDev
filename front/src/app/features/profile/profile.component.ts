import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  user = {
    username: 'JohnDoe',
    email: 'john.doe@example.com',
    password: 'password123' // In a real app, you wouldn't pre-fill password like this
  };

  subscriptions = [
    {
      id: 1,
      title: 'Titre du thème 1',
      description: 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard...'
    },
    {
      id: 2,
      title: 'Titre du thème 2',
      description: 'Description: lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard...'
    }
    // Add more subscriptions as needed
  ];

  constructor() { }

  ngOnInit(): void {
  }

  saveProfile() {
    // Logic to save user profile data
    console.log('Saving profile:', this.user);
    alert('Profil sauvegardé !');
    // You would typically call a service here to update the backend
  }

  unsubscribe(subscriptionId: number) {
    // Logic to unsubscribe from a service
    console.log('Unsubscribing from:', subscriptionId);
    // In a real application, you would make an API call to unsubscribe
    this.subscriptions = this.subscriptions.filter(sub => sub.id !== subscriptionId);
    alert(`Vous êtes désabonné du thème ${subscriptionId} !`);
  }

}
