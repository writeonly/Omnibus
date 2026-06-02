export interface RegisterFormData {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export function validateRegistration(form: RegisterFormData): string | null {
  if (form.username.trim().length < 3) {
    return "Username must contain at least 3 characters";
  }

  if (!form.email.includes("@")) {
    return "Enter a valid email address";
  }

  if (form.password.length < 8) {
    return "Password must contain at least 8 characters";
  }

  return null;
}

