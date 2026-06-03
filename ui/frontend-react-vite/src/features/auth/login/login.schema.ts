export interface LoginFormData {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export function validateLogin(form: LoginFormData): string | null {
  if (form.username.trim().length < 3) {
    return "Username must contain at least 3 characters";
  }

  if (form.password.length < 8) {
    return "Password must contain at least 8 characters";
  }

  return null;
}

