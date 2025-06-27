export interface Comment {
  id: number;
  content: string;
  authorUsername: string;
  createdAt: string;
}

export interface CommentRequest {
  content: string;
}
