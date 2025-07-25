import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AuthGuard} from "./core/guards/auth.guard";
import {LayoutComponent} from "./layout/layout.component";
import {FeedComponent} from "./features/posts/feed/feed.component";
import {ProfileComponent} from "./features/profile/profile.component";
import {SubjectListComponent} from "./features/subjects/subject-list/subject-list.component";
import {PostDetailComponent} from "./features/posts/post-detail/post-detail.component";
import {PostCreateComponent} from "./features/posts/post-create/post-create.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'authPage',
    pathMatch: 'full'
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'feed', component: FeedComponent },
      { path: 'subjects', component: SubjectListComponent },
      { path: 'posts/:id', component: PostDetailComponent },
      { path: 'profile', component: ProfileComponent },
      { path: 'create', component: PostCreateComponent}
    ]
  },
  {
    path: '',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
