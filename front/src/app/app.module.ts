import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {JwtInterceptor} from "./core/interceptors/jwt.interceptor";
import { ProfileComponent } from './features/profile/profile.component';
import { LayoutComponent } from './layout/layout.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import {NgOptimizedImage} from "@angular/common";
import { FeedComponent } from './features/posts/feed/feed.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {SubjectListComponent} from "./features/subjects/subject-list/subject-list.component";
import {PostDetailComponent} from "./features/posts/post-detail/post-detail.component";
import { PostCreateComponent } from './features/posts/post-create/post-create.component';

@NgModule({
  declarations: [AppComponent, NotFoundComponent, ProfileComponent, LayoutComponent, NavbarComponent, FeedComponent, SubjectListComponent, PostDetailComponent, PostCreateComponent],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatButtonModule,
        HttpClientModule,
        NgOptimizedImage,
        FormsModule,
        ReactiveFormsModule
    ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }],
  bootstrap: [AppComponent],
})
export class AppModule {}
