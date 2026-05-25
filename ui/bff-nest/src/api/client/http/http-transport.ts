import { Injectable } from '@nestjs/common';

@Injectable()
export class HttpTransport {
  async post<TResponse>(
    baseUrl: string,
    path: string,
    body?: unknown,
    headers?: Record<string, string>
  ): Promise<TResponse> {
    const url = `${baseUrl}${path}`;

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
        ...(headers ?? {}),
      },
      body: body ? JSON.stringify(body) : undefined,
    });

    if (!response.ok) {
      const text = await response.text();

      throw new Error(
        `HTTP ${response.status} ${response.statusText}: ${text}`
      );
    }

    if (response.status === 204) {
      return undefined as TResponse;
    }

    return response.json() as Promise<TResponse>;
  }

  async get<TResponse>(
    baseUrl: string,
    path: string,
    headers?: Record<string, string>
  ): Promise<TResponse> {
    const url = `${baseUrl}${path}`;

    const response = await fetch(url, {
      method: 'GET',
      headers,
    });

    if (!response.ok) {
      const text = await response.text();

      throw new Error(
        `HTTP ${response.status} ${response.statusText}: ${text}`
      );
    }

    return response.json() as Promise<TResponse>;
  }
}
