export interface LoginUserRequest {
  username: string;
  password: string;
}

export interface LoginUserResponse {
  userId: string;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  token: string;
  status: string;
}

const baseUrl = "/auth";

async function postJson<TResponse, TPayload>(
  path: string,
  payload: TPayload,
): Promise<TResponse> {
  const response = await fetch(`${baseUrl}${path}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`);
  }

  return response.json() as Promise<TResponse>;
}

export const authClient = {
  loginUser(payload: LoginUserRequest): Promise<LoginUserResponse> {
    return postJson("/login", payload);
  },
};
