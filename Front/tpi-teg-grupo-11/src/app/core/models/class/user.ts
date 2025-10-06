export interface User {
  id?: number;
  username: string;
  password: string;
  imgUrl?: string;
  active?: boolean;
  creationDate?: Date;
}
