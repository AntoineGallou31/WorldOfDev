import {Comment} from "./comment";

export interface Post {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  userId: number;
  userUsername: string;
  subjectName: string;
  comments: Comment[];
}
