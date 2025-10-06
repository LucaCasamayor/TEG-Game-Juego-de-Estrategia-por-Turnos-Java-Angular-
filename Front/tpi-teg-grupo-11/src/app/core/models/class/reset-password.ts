export class ResetPassword {
  username: string;
  code: string;
  newPassword: string;

  constructor(username: string, code: string, newPassword: string) {
    this.username = username;
    this.code = code;
    this.newPassword = newPassword;
  }
}
