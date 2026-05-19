import { Controller, Get, Query, Redirect, Req, UseGuards } from '@nestjs/common';
import { AuthService } from './auth.service';
import { AuthGuard } from '@nestjs/passport';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Get('config')
  config() {
    return this.authService.getClientConfig();
  }

  @Get('login')
  @Redirect()
  login(@Query('redirectUri') redirectUri?: string) {
    return { url: this.authService.buildLoginUrl(normalizeRedirectUri(redirectUri)) };
  }

  @Get('register')
  @Redirect()
  register(@Query('redirectUri') redirectUri?: string) {
    return { url: this.authService.buildRegistrationUrl(normalizeRedirectUri(redirectUri)) };
  }

  @Get('logout')
  @Redirect()
  logout(@Query('redirectUri') redirectUri?: string) {
    return { url: this.authService.buildLogoutUrl(normalizeRedirectUri(redirectUri)) };
  }

  @UseGuards(AuthGuard('jwt'))
  @Get('me')
  me(@Req() req: any) {
    return this.authService.getMe(req.user);
  }
}

function normalizeRedirectUri(redirectUri?: string): string {
  const fallback = process.env.FRONTEND_PUBLIC_URL ?? 'http://localhost:4300';
  const allowedOrigins = (process.env.FRONTEND_ALLOWED_REDIRECT_ORIGINS ?? fallback)
    .split(',')
    .map((origin) => origin.trim())
    .filter(Boolean);

  if (!redirectUri) {
    return fallback;
  }

  return allowedOrigins.some((origin) => redirectUri === origin || redirectUri.startsWith(`${origin}/`))
    ? redirectUri
    : fallback;
}
